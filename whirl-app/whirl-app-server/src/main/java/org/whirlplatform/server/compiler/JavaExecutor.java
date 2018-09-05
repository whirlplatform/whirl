package org.whirlplatform.server.compiler;

import com.google.common.primitives.Primitives;
import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.apache.commons.jci.compilers.JavaCompilerSettings;
import org.apache.commons.jci.problems.CompilationProblem;
import org.apache.commons.jci.readers.MemoryResourceReader;
import org.whirlplatform.java.*;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.impl.ErrorMessage;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.utils.ContextUtil;
import org.whirlplatform.server.utils.PathUtils;
import org.xeustechnologies.jcl.JarClassLoader;

import javax.servlet.ServletContext;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaExecutor {

    private static final Logger _log = LoggerFactory.getLogger(JavaExecutor.class);

    private static final String COMPILER_SOURCE_ENCODING = "UTF-8";

    private static final Pattern ifacePattern = Pattern.compile("(?<=implements[\\s])(([\\s]+)?[\\w]+)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern packPattern = Pattern.compile("(?<=package[\\s+])([\\w\\.]+)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern classPattern = Pattern.compile("(?<=class[\\s+])([\\w]+)", Pattern.CASE_INSENSITIVE);

    private Connector connector;

    private ConnectionProvider connectionProvider;

    private ApplicationUser user;

    private String source;

    // TODO переделать на PlatformContextProvider
    private ServletContext srvContext;

    public JavaExecutor(Connector connector, ConnectionProvider connectionProvider, ApplicationUser user, String source,
            ServletContext srvContext) {
        if (source == null || source.isEmpty()) {
            throw new CustomException("No Java source for event.");
        }
        this.connector = connector;
        this.connectionProvider = connectionProvider;
        this.user = user;
        this.source = source;
        this.srvContext = srvContext;
    }

	public EventResult execute(Collection<DataValue> parameters) {
		try {
			Set<Connection> connections = new HashSet<Connection>();
			try {
				Matcher m = ifacePattern.matcher(source);
				if (!m.find()) {
					throw new CustomException("Java class should implement Function interface");
				}

				m = packPattern.matcher(source);
				if (!m.find()) {
					throw new CustomException("Wrong class package name");
				}
				String packageName = m.group().trim();

				m = classPattern.matcher(source);
				if (!m.find()) {
					throw new CustomException("Wrong class name");
				}
				String className = m.group().trim();

				Class<Function> clazz = getClass(packageName, className, source);

				Function function = null;
				// восстанавливаем функцию
				if (clazz.isAnnotationPresent(Inject.class)) {
					Inject i = clazz.getAnnotation(Inject.class);
					String name;
					if (i.name() != null && !i.name().isEmpty()) {
						name = i.name();
					} else {
						name = clazz.getName();
					}
					if (i.singleton() && user.hasJavaObject(name) && clazz.isInstance(user.loadJavaObject(name))) {
						function = (Function) user.loadJavaObject(name);
					}
				}
				if (function == null) {
					function = clazz.newInstance();
				}

				FunctionContext context = new JavaFunctionContext(user);
				function.setContext(context);

				// пробегаем по полям и восстанавливаем значения
				for (Field f : clazz.getDeclaredFields()) {
					// данные
					if (f.isAnnotationPresent(Inject.class)) {
						Inject i = f.getAnnotation(Inject.class);
						String name;
						if (i.name() != null && !i.name().isEmpty()) {
							name = i.name();
						} else {
							name = f.getName();
						}
						if (user.hasJavaObject(name)) {
							Object o = user.loadJavaObject(name);
							if (Primitives.wrap(f.getType()).isInstance(o)) {
								f.setAccessible(true);
								f.set(function, user.loadJavaObject(name));
							}
						}
					}

					// коннект
					if (f.isAnnotationPresent(DataSource.class)) {
						if (f.getType().isAssignableFrom(Connection.class)) {
							DataSource annotation = f.getAnnotation(DataSource.class);
							Connection connection = connectionProvider.getConnection(annotation.alias, user);
							connections.add(connection);
							f.setAccessible(true);
							f.set(function, connection);
						}
					} else if (f.isAnnotationPresent(Manager.class)) {
						if (f.getType().isAssignableFrom(ReportManager.class)) {
							f.setAccessible(true);
							try {
								Constructor<ReportManager> c = ReportManager.class.getDeclaredConstructor(
										ApplicationUser.class, Connector.class);
								c.setAccessible(true);
								ReportManager manager = c.newInstance(user, connector);
								f.set(function, manager);
							} catch (NoSuchMethodException | InvocationTargetException e) {
								_log.error("Error creating ReportManager", e);
								throw new CustomException("Error creating ReportManager");
							}

						} else if (f.getType().isAssignableFrom(FileManager.class)) {
							f.setAccessible(true);
							try {
								// Определение директории приложения
								String srvPath = srvContext.getContextPath().startsWith("/") ? srvContext
										.getContextPath().substring(1) : srvContext.getContextPath();
								if (srvPath == null || srvPath.isEmpty()) {
									srvPath = "ROOT";
								}
								srvPath.replace("/", "-");

								String rootFolderPath = PathUtils.getApplicationFilePath(ContextUtil
                                                .lookup("Whirl/work-path"), srvPath, user.getApplication().getCode(),
										"data", null);

								Constructor<FileManager> c = FileManager.class.getDeclaredConstructor(File.class);
								c.setAccessible(true);
								FileManager manager = c.newInstance(new File(rootFolderPath));
								f.set(function, manager);
							} catch (NoSuchMethodException | InvocationTargetException e) {
								_log.error("Error creating ReportManager", e);
								throw new CustomException("Error creating ReportManager");
							}
						}
					}
				}

				// параметры для функции
				List<DataValue> funcParams = new ArrayList<DataValue>();
				funcParams.addAll(parameters);

                org.whirlplatform.java.EventResult funcResult = null;
				try {
					final Function fFunction = function;
					final List<DataValue> fParams = funcParams;
                    funcResult = executeCallable(new Callable<org.whirlplatform.java.EventResult>() {
                        public org.whirlplatform.java.EventResult call() {
							return fFunction.execute(fParams);
						}
					});
				} catch (Throwable e) {
					_log.error(new ErrorMessage(user, e.getMessage()), e);
					throw new CustomException(e.getMessage());
				}
				EventResult result = funcResult.asInternal();

				// сохраняем значения полей
				for (Field f : clazz.getDeclaredFields()) {
					if (f.isAnnotationPresent(Inject.class)) {
						Inject i = f.getAnnotation(Inject.class);
						String name;
						if (i.name() != null && !i.name().isEmpty()) {
							name = i.name();
						} else {
							name = f.getName();
						}
						f.setAccessible(true);
						Object o = f.get(function);
						if (o != null) {
							user.saveJavaObject(name, o);
						} else if (user.hasJavaObject(name)) {
							user.removeJavaObject(name);
						}
					}

					// коннект
					if (f.isAnnotationPresent(DataSource.class)) {
						if (f.getType().isAssignableFrom(Connection.class)) {
							f.setAccessible(true);
							Connection connection = (Connection) f.get(function);
							DBConnection.closeResources(connection);
							f.set(function, null);
						}
					}
				}

				// сохраняем функцию
				if (clazz.isAnnotationPresent(Inject.class)) {
					Inject i = clazz.getAnnotation(Inject.class);
					String name;
					if (i.name() != null && !i.name().isEmpty()) {
						name = i.name();
					} else {
						name = clazz.getName();
					}
					user.saveJavaObject(name, function);
				}

				return result;
			} finally {
				for (Connection c : connections) {
					DBConnection.closeResources(c);
				}
			}
		} catch (ClassNotFoundException | ClassCastException | IllegalArgumentException
				| IllegalAccessException | SecurityException | InstantiationException | ConnectException e) {
			_log.error(new ErrorMessage(user, e.getMessage()), e);
			throw new CustomException(e.getMessage());
		} catch (Throwable e) {
			_log.error(new ErrorMessage(user, e.getMessage()), e);
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * Выполнение java события в новом потоке.
	 * Ждет окончания выполнения и возврата результата
	 */
	public <T> T executeCallable(Callable<T> callable) throws Throwable {
		try {
			ThreadFactory f = new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setContextClassLoader(user.getCompilationData().getMainClassLoader());
					return t;
            }
			};
			return Executors.newSingleThreadExecutor(f).submit(callable).get();
		} catch (ExecutionException e) {
			throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getClass(String packageName, String className, String source)
            throws ClassNotFoundException {
        CompilationData data = user.getCompilationData();
        String fullName = getClassName(packageName, className);
        ClassLoader classLoader = data.getMainClassLoader();

        if (!data.sourceChanged(fullName, source)) {
            try {
                return (Class<T>) classLoader.loadClass(fullName);
            } catch (ClassNotFoundException e) {
            }
        }

        JavaCompilerSettings settings = new JavaCompilerSettings();
        settings.setDeprecations(false);
        settings.setWarnings(false);
        settings.setSourceVersion(getJavaVersion());
        settings.setTargetVersion(getJavaVersion());
        settings.setSourceEncoding(COMPILER_SOURCE_ENCODING);

        JavaCompiler compiler = new JavaCompilerFactory().createCompiler("eclipse");

        MemoryResourceReader reader = new MemoryResourceReader();

        String pathName = fullName.replace(".", "/") + ".java";
        reader.add(pathName, source.getBytes(StandardCharsets.UTF_8));
        CompilationResult result = compiler.compile(new String[] { pathName }, reader, data.getResourceStore(),
                classLoader, settings);
        if (result.getErrors().length > 0) {
            StringBuilder message = new StringBuilder();
            for (CompilationProblem p : result.getErrors()) {
                message.append("start: " + p.getStartLine() + " " + p.getStartColumn() + " end: " + p.getEndLine() + " "
                        + p.getEndColumn() + " - " + p.getMessage() + "\n");
            }
            String msg = message.toString();
            _log.warn("Method compilation problem" + "\n" + msg);
            throw new CustomException(msg);
        }
        Class<T> clazz = (Class<T>) data.getMainClassLoader().loadClass(fullName);
        data.cacheSource(fullName, source);
        return clazz;
    }

    private String getJavaVersion() {
        String version = System.getProperty("java.version");
        String[] v = version.split("\\.");
        return v[0] + "." + v[1];
    }

    private void refreshApplicationClassLoader(JarClassLoader applicationClassLoader, ApplicationUser user) {
        // не реализовано
    }

    private String getClassName(String packageName, String className) {
        String result = "";
        if (packageName != null && !packageName.isEmpty()) {
            result = packageName + ".";
        }
        return result + className;
    }

}
