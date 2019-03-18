package org.whirlplatform.server.metadata.container;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.jci.ReloadingClassLoader;
import org.apache.commons.jci.stores.MemoryResourceStore;
import org.apache.commons.lang.StringUtils;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.server.compiler.CompilationData;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.evolution.EvolutionException;
import org.whirlplatform.server.evolution.EvolutionManager;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.MetadataModifiedHandler;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;
import org.whirlplatform.server.metadata.store.WatchableStore;
import org.whirlplatform.server.utils.ApplicationReference;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.exception.JclException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Контейнер хранит текущие рабочие экземпляры приложения, которые на данный
 * момент выполняются. При подгрузке приложения инициализирует зависимые
 * библиотеки.
 */
@Singleton
public class DefaultMetadataContainer implements MetadataContainer {

    private static Logger _log = LoggerFactory.getLogger(DefaultMetadataContainer.class);

    private static final Version NULL_VERSION = Version.create(0, 0, Integer.MAX_VALUE);

    private MetadataStore metadataStore;

    private static final int DEFAULT_CACHE_TIMEOUT_SECONDS = 10;
    private static final int LAST_ACCESS_TIMEOUT_MINUTES = 60;
    private Timer reloadTimer = new Timer(true);
    private int cacheTimeout;
    private Table<String, Version, AtomicReference<ApplicationReference>> cache = HashBasedTable.create();
    private Table<String, Version, Date> lastAccessTime = HashBasedTable.create();

    private ExecutorService executorService;
    private EvolutionManager evolutionManager;

    @Inject
    public DefaultMetadataContainer(Configuration configuration, MetadataStore metadataStore,
                                    EvolutionManager evolutionManager) {
        this.metadataStore = metadataStore;
        Integer timeout = configuration.<Integer>lookup("Whirl/cachetimeout");
        this.cacheTimeout = (timeout == null) ? DEFAULT_CACHE_TIMEOUT_SECONDS : timeout;
        this.evolutionManager = evolutionManager;
        initTimerReload();
    }

    /**
     * Инициализация процесса обновления метаданных загружаемых приложений.
     */
    private void initTimerReload() {
        if (!(metadataStore instanceof WatchableStore)) {
            reloadTimer.schedule(reloadTask(), cacheTimeoutMillis());
        }
    }

    class ApplicationLoadCallable implements Callable<Void> {
        private String code;
        private Version version;

        public ApplicationLoadCallable(String code, Version version) {
            super();
            this.code = code;
            this.version = version;
        }

        @Override
        public Void call() {
            try {
                ApplicationElement app = metadataStore.loadApplication(code, version);
                ApplicationReference appRef = new ApplicationReference(app, initCompilationData(app));
                cache.get(code, assureNotNull(version)).set(appRef);
            } catch (Exception e) {
                _log.warn(String.format("Application '%s [%s]' reload problem: ", code, version), e);
            }
            return null;
        }
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (this) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(2);
                }
            }
        }
        return executorService;
    }

    private TimerTask reloadTask() {
        return new TimerTask() {
            @Override
            public void run() {
                // загрузка приложений будет идти параллельно
                ExecutorService executor = getExecutorService();
                List<ApplicationLoadCallable> calls = new ArrayList<>();

                for (Cell<String, Version, AtomicReference<ApplicationReference>> c : cache.cellSet()) {
                    Version version = c.getColumnKey() == NULL_VERSION ? null : c.getColumnKey();

                    // если никто не запускал послденее время приложение, то не
                    // вытаскиваем
                    if (new Date(System.currentTimeMillis() - lastAccessTimeoutMillis())
                            .after(lastAccessTime.get(c.getRowKey(), c.getColumnKey()))) {
                        continue;
                    }

                    ApplicationLoadCallable callable = new ApplicationLoadCallable(c.getRowKey(), version);
                    calls.add(callable);
                }

                try {
                    List<Future<Void>> result = executor.invokeAll(calls);
                    for (Future<Void> f : result) {
                        f.get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    _log.warn("Application reload interrupted", e);
                }

                reloadTimer.schedule(reloadTask(), cacheTimeoutMillis());
            }

        };
    }

    private long cacheTimeoutMillis() {
        return cacheTimeout * 1000;
    }

    private long lastAccessTimeoutMillis() {
        return LAST_ACCESS_TIMEOUT_MINUTES * 60 * 1000;
    }

    private Version assureNotNull(Version version) {
        return version == null ? NULL_VERSION : version;
    }

    private void putCache(String code, Version version, AtomicReference<ApplicationReference> reference) {
        cache.put(code, assureNotNull(version), reference);
    }

    private AtomicReference<ApplicationReference> getCache(String code, Version version) {
        return cache.get(code, assureNotNull(version));
    }

    private void initApplicationReloadHandler(final String code, final Version version) {
        if (metadataStore instanceof WatchableStore) {
            _log.info(String.format("Add the modification handler for the %s[%s]", code, version));
            WatchableStore watchableStore = (WatchableStore) metadataStore;
            watchableStore.addModifiedHandler(code, version, new MetadataModifiedHandler() {
                @Override
                public void loaded(ApplicationElement app) {
                    try {
                        reloadApplication(app, code, version);
                    } catch (EvolutionException e) {
                        _log.error(e.getMessage(), e);
                    }
                }
            });
        } else {
            _log.info("Current MetadataStore is not WatchableStore");
        }
    }

    private AtomicReference<ApplicationReference> initialLoadApplication(String code, Version version) throws MetadataStoreException, EvolutionException {
        ApplicationElement application = metadataStore.loadApplication(code, version);
        CompilationData compilation = initCompilationData(application);
        AtomicReference<ApplicationReference> result = new AtomicReference<>(new ApplicationReference(application, compilation));
        applyDatabaseEvolutions(application, code, version);
        putCache(code, version, result);
        return result;
    }

    private void reloadApplication(ApplicationElement app, String code, Version version) throws EvolutionException {
        _log.info(String.format("Reloading the application %s[%s], id=%s", app.getCode(), app.getVersion(),
                app.getId()));
        ApplicationReference reference = new ApplicationReference(app, initCompilationData(app));
        _log.info(String.format("Updating the application cache: code=%s, version=%s", code,
                version));
        applyDatabaseEvolutions(app, code, version);
        getCache(code, version).set(reference);
    }

    private void applyDatabaseEvolutions(ApplicationElement application, String code, Version version) throws EvolutionException {
        if (metadataStore.getLastVersion(code).compareTo(version) == 0) {
            for (DataSourceElement dataSource : application.getDataSources()) {
                if (dataSource.getEvolution() == null) {
                    continue;
                }
                evolutionManager.applyApplicationEvolution(dataSource.getAlias(), dataSource.getEvolution().getInputStreamProvider().path());
            }
        }
    }

    @Override
    public AtomicReference<ApplicationReference> getApplication(String code, Version originalVersion)
            throws ContainerException {
        try {
            Version version = originalVersion;
            if (version == null) {
                version = metadataStore.getLastVersion(code);
            }
            final String strVersion = (version == null) ? "null" : version.toString();
            _log.info(String.format("The last version of '%s' is [%s]", code, strVersion));
            AtomicReference<ApplicationReference> result = getCache(code, version);
            if (result == null) {
                _log.info(String.format("The application %s[%s] was not cached", code, strVersion));
                synchronized (cache) {
                    result = getCache(code, version);
                    if (result == null) {
                        initialLoadApplication(code, version);
                        initApplicationReloadHandler(code, version);
                    }
                }
            } else {
                _log.info(String.format("The application %s[%s] was loaded from the cache", code, strVersion));
            }
            lastAccessTime.put(code, assureNotNull(version), new Date());
            return result;
        } catch (MetadataStoreException | EvolutionException e) {
            final String message = String.format("Cannot load the application '%s' from container", code);
            _log.error(message, e);
            throw new ContainerException(message, e);
        }
    }

    private CompilationData initCompilationData(ApplicationElement application) {
        JarClassLoader applicationClassLoader = new JarClassLoader();
        initApplicationClassLoader(applicationClassLoader, application);
        ReloadingClassLoader mainClassLoader = new ReloadingClassLoader(applicationClassLoader);

        MemoryResourceStore store = new MemoryResourceStore();
        mainClassLoader.addResourceStore(store);

        CompilationData data = new CompilationData(store, mainClassLoader, applicationClassLoader);
        return data;
    }

    @SuppressWarnings("deprecation")
    private void initApplicationClassLoader(JarClassLoader applicationClassLoader, ApplicationElement application) {
        applicationClassLoader.getParentLoader().setEnabled(true);
        applicationClassLoader.getSystemLoader().setEnabled(true);
        applicationClassLoader.getLocalLoader().setOrder(0);
        applicationClassLoader.getParentLoader().setOrder(1);
        applicationClassLoader.getSystemLoader().setOrder(2);
        /*
         * TODO В светлом будущем надо избежать использования f.getPath()
         *
         * По идее мы не должны знать в какой папке находится jar-файл. Для
         * этого надо использовать f.getInputStream(). Однако библиотека
         * jcl-core не полностью поддерживает работу с потоками, возвращая
         * ошибку. Возможности обхода следуюшие:
         *
         * 1. Написать свой ClassLoader для jar-файлов, но такой опыт уже был и
         * оказался негативным. По малопонятным причинам работал такой класс
         * неустойчиво и непредсказуемо падал.
         *
         * 2. Реализовать собственный протокол для ClassLoader. Однако, в этом
         * случае потребуется создание реализаций URLStreamHandlerFactory,
         * URLStreamHandler. Работа достаточно объемная и, также, с
         * непредсказуемым результатом в смысле надежности.
         *
         * Таким образом пока оставлена загрузка jar-файла пользователя через
         * файл, а не через поток: applicationClassLoader.add(f.getPath())
         */
        for (FileElement f : application.getJavaFiles()) {
            // (InputStream is = (InputStream) f.getInputStream()
            try {
                // applicationClassLoader.add(is);
                if (StringUtils.isEmpty(f.getPath())) {
                    final String WARN = "The path of Jar file '%s' of application '%s' is empty";
                    _log.warn(String.format(WARN, f.getFileName(), application.getCode()));
                } else {
                    applicationClassLoader.add(f.getPath());
                }
            } catch (JclException e) {
                final String WARN = "The Jar file '%s' of application '%s' not loaded";
                _log.warn(String.format(WARN, f.getFileName(), application.getCode()), e);
            }
        }
    }
}
