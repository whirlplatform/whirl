package org.whirlplatform.server.metadata.store.file;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.Version.VersionFormatException;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.DatabaseEvolution;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.version.VersionUtil;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.metadata.store.AbstractMetadataStore;
import org.whirlplatform.server.metadata.store.ApplicationFilesUtil;
import org.whirlplatform.server.metadata.store.MetadataModifiedHandler;
import org.whirlplatform.server.metadata.store.MetadataStoreException;
import org.whirlplatform.server.metadata.store.WatchableStore;

@Singleton
@Named("FileSystemMetadataStore")
public class FileSystemMetadataStore extends AbstractMetadataStore
        implements Runnable, WatchableStore {
    protected static final String APPLICATIONS_PATH = "applications";
    protected static final String TAG_PATH = "tag";
    protected static final String BRANCH_PATH = "branch";
    protected static final String APPLICATION_FILE = "application.wrl";
    protected static final String EVOLUTION_PATH = "evolutions";
    protected static final String EVOLUTION_ROOT = "root.xml";
    private static final Logger _log = LoggerFactory.getLogger(FileSystemMetadataStore.class);
    protected FileSystem fileSystem;
    protected Table<String, Version, Set<MetadataModifiedHandler>> modifiedHandlers =
            HashBasedTable.create();
    private String workPath;
    private WatchService watchService;
    private Map<WatchKey, String> watchedCodes = new HashMap<>();
    private Map<WatchKey, Version> watchedVersions = new HashMap<>();

    @Inject
    public FileSystemMetadataStore(Configuration configuration, FileSystem fileSystem) {
        this.workPath = configuration.lookup("Whirl/work-path");
        this.fileSystem = fileSystem;
        initWatchService(fileSystem);
    }

    public static void loadDatabaseEvolutions(final Path applicationPath,
                                              final ApplicationElement application) {
        for (DataSourceElement dataSource : application.getDataSources()) {
            Path path = applicationPath.resolve(EVOLUTION_PATH).resolve(dataSource.getAlias());
            Path root = path.resolve(EVOLUTION_ROOT);
            if (Files.notExists(path) || !Files.isDirectory(path) || Files.notExists(root)) {
                continue;
            }
            dataSource.setEvolution(new DatabaseEvolution(new FileElement.InputStreamProvider() {

                @Override
                public Object get() throws IOException {
                    return Files.newInputStream(root);
                }

                @Override
                public String path() {
                    return root.toAbsolutePath().toString();
                }
            }));
        }
    }

    private void initWatchService(FileSystem fileSystem) {
        try {
            this.watchService = fileSystem.newWatchService();
            Thread watchThread = new Thread(this, "FileWatchService");
            watchThread.start();
        } catch (UnsupportedOperationException e) {
            _log.error("WatchService: is not supporeted by this FileSystem", e);
        } catch (IOException e) {
            _log.error("WatchService: initialization error", e);
        }
    }

    private ApplicationElement loadApplicationFromPath(Path applicationPath,
                                                       boolean ignoreReferences)
            throws MetadataStoreException, IOException {
        final String xml = readApplicationXmlFile(applicationPath);
        if (xml == null) {
            String message =
                    String.format("The application xml file not found in %", applicationPath);
            _log.error(message);
            throw new MetadataStoreException(message);
        }
        ApplicationElement result = deserialize(xml, ignoreReferences);
        return result;
    }

    /**
     * Загружает содержимое xml файла приложения в строку
     *
     * @param applicationPath - путь к версии приложения
     * @return содержимое xml файла приложения
     * @throws IOException
     */
    protected String readApplicationXmlFile(final Path applicationPath) throws IOException {
        Path appFile = applicationPath.resolve(APPLICATION_FILE);
        if (!Files.exists(appFile)) {
            return null;
        }
        String result = ApplicationFilesUtil.readTextFile(appFile);
        return result;
    }

    private Path basePath() {
        return fileSystem.getPath(workPath);
    }

    @Override
    protected Path resolveApplicationPath(final String appCode, final Version appVersion)
            throws IOException, MetadataStoreException {
        Path base = basePath();
        if (!Files.exists(base)) {
            throw new MetadataStoreException("Base path doesn't exists: " + base.toString());
        }
        Path tag = base.resolve(APPLICATIONS_PATH).resolve(appCode).resolve(TAG_PATH);
        Path branch = base.resolve(APPLICATIONS_PATH).resolve(appCode).resolve(BRANCH_PATH);
        Path applicationPath;
        if (appVersion == null) {
            if (!Files.exists(tag)) {
                return null;
            }
            // достаем последнюю доступную версию если не указано
            List<Version> versions = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(tag)) {
                for (Path p : stream) {
                    try {
                        versions.add(Version.parseVersion(p.getFileName().toString()));
                    } catch (VersionFormatException e) {
                        _log.warn("Can't parse version from directory: " + p.toString(), e);
                    }
                }
            }
            if (versions.isEmpty()) {
                final String message =
                        String.format("Versions not found for the application: '%s'", appCode);
                _log.error(message);
                throw new MetadataStoreException(message);
            }
            Collections.sort(versions, Version.NATURAL_ORDER);
            applicationPath = tag.resolve(versions.get(versions.size() - 1).toString());
        } else {
            if (appVersion.isBranch()) {
                // достаем последнюю доступную версию если не указано
                applicationPath = branch.resolve(appVersion.getBranch());
            } else {
                applicationPath = tag.resolve(appVersion.toString());
            }
        }
        return applicationPath;
    }

    @Override
    public ApplicationElement loadApplication(String code, Version version,
                                              boolean ignoreReferences)
            throws MetadataStoreException {
        final String stringVersion = (version != null) ? version.toString() : "null";
        try {
            _log.info(String.format("Loading the application %s[%s]", code, stringVersion));
            Path applicationPath = resolveApplicationPath(code, version);

            // загружаем xml
            ApplicationElement application =
                    loadApplicationFromPath(applicationPath, ignoreReferences);
            // загружаем файлы
            ApplicationFilesUtil.loadApplicationFiles(applicationPath, application);

            loadDatabaseEvolutions(applicationPath, application);

            return application;
        } catch (IOException e) {
            final String message =
                    String.format("Error loading the application %s[%s]", code, stringVersion);
            _log.error(message, e);
            throw new MetadataStoreException(message, e);
        }
    }

    @Override
    public void saveApplication(ApplicationElement application, Version version,
                                ApplicationUser user)
            throws MetadataStoreException {
        try {
            final String appCode = application.getCode();
            if (version == null) {
                //TODO сделать нормальное сообщение о том что приложение не доступно
                throw new MetadataStoreException(
                        String.format("The version of '%s' should be provided", appCode));
            }
            final Path applicationPath = createApplicatonPath(appCode, version);
            writeApplicationXmlFile(applicationPath, serialize(application));
            saveApplicationFiles(applicationPath, application);
        } catch (IOException e) {
            throw new MetadataStoreException(e);
        }
    }

    private Path createApplicatonPath(final String appCode, final Version version)
            throws IOException {
        final Path appsRoot = basePath().resolve(APPLICATIONS_PATH);
        final String versionFolder = version.isBranch() ? BRANCH_PATH : TAG_PATH;
        final Path result =
                appsRoot.resolve(appCode).resolve(versionFolder).resolve(version.toString());
        if (!Files.exists(result)) {
            Files.createDirectories(result);
        }
        return result;
    }

    /**
     * Сохраняет приложение в xml формате в файл
     *
     * @param applicationPath - путь к версии приложения
     * @param xml             - xml приложения
     * @throws IOException
     */
    protected void writeApplicationXmlFile(final Path applicationPath, final String xml)
            throws IOException {
        Path appFile = applicationPath.resolve(APPLICATION_FILE);
        if (!Files.exists(appFile)) {
            Files.createFile(appFile);
        }
        _log.info(String.format("Saving application xml to %s", applicationPath));
        ApplicationFilesUtil.writeTextFile(appFile, xml);
        _log.info(String.format("Application xml saved to %s", applicationPath));
    }

    protected void saveApplicationFiles(Path applicationPath, ApplicationElement application) {
        Map<FileElement, Exception> errors =
                ApplicationFilesUtil.saveApplicationFiles(applicationPath, application);
        final String NOT_SAVED = "Application file %s not saved to path %s";
        for (Entry<FileElement, Exception> e : errors.entrySet()) {
            _log.warn(
                    String.format(NOT_SAVED, e.getKey().getFileName(), applicationPath.toString()),
                    e.getValue());
        }
    }

    @Override
    public void run() {
        if (watchService == null) {
            return;
        }
        _log.info("WatchService: started");
        while (true) {
            WatchKey key = null;
            try {
                key = watchService.take();
                if (!key.isValid()) {
                    cancelInvalidKey(key);
                    continue;
                }
                final String code = getCodeByKey(key);
                final Version notNullVersion = getVersionByKey(key);
                if (code == null || notNullVersion == null) {
                    Path unregistered = (Path) key.watchable();
                    _log.warn(String.format("WatchService: Unregistered WatchKey for the path:'%s'",
                            unregistered));
                    continue;
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    processWatchKeyEvent(event, code, notNullVersion);
                }
            } catch (InterruptedException | MetadataStoreException e) {
                _log.warn("Application reload error", e);
            } finally {
                if (key != null) {
                    key.reset();
                }
            }
        }
    }

    private void cancelInvalidKey(WatchKey key) {
        Path invalidPath = (Path) key.watchable();
        _log.warn(String.format("WatchService: the folder '%s' is not valid now.", invalidPath));
        watchedCodes.remove(key);
        watchedVersions.remove(key);
        key.cancel();
    }

    private void processWatchKeyEvent(WatchEvent<?> event, String code, Version notNullVersion)
            throws MetadataStoreException {
        if (StandardWatchEventKinds.OVERFLOW == event.kind()) {
            _log.warn("OVERFLOW detected");
            return;
        }
        Set<MetadataModifiedHandler> handlers = modifiedHandlers.get(code, notNullVersion);
        if (handlers == null) {
            final String NO_HANDLERS =
                    "WatchService: Not found handlers for the application %s[%s]";
            _log.warn(String.format(NO_HANDLERS, code, notNullVersion));
            return;
        }
        String context = event.context().toString();
        if (APPLICATION_FILE.equals(context) || context.endsWith("jar")) {
            final String MODIFY =
                    "WatchService: The application %s[%s] context '%s' has been modified";
            _log.info(String.format(MODIFY, code, notNullVersion, context));
            _log.info(String.format("WatchService: %d handler(s) registered", handlers.size()));
            ApplicationElement application = loadApplication(code, originalVersion(notNullVersion));
            for (MetadataModifiedHandler h : handlers) {
                h.loaded(application);
            }
        } else if (!"java".equals(context)) {
            _log.info(String.format("WatchService: the context '%s' was deliberately ignored",
                    context));
        }
    }

    @Override
    public void addModifiedHandler(String code, Version version, MetadataModifiedHandler handler) {
        if (watchService == null) {
            _log.warn(
                    "WatchService: Unable to register the handler due to the service is not available");
            return;
        }
        if (code == null || handler == null) {
            _log.warn("WatchService: Unable to register the handler due to illegal parameters");
            return;
        }
        synchronized (modifiedHandlers) {
            Set<MetadataModifiedHandler> set = getModifiedHandlers(code, version);
            try {
                Path appPath = resolveApplicationPath(code, version);
                startChangeTracking(appPath, code, version);
                Path javaPath = appPath.resolve("java");
                startChangeTracking(javaPath, code, version);
                set.add(handler);
            } catch (IOException | MetadataStoreException e) {
                _log.warn(String.format(
                        "WatchService: the tracking not started for the application '%s[%s]' ",
                        code,
                        version), e);
            }
        }
    }

    protected Set<MetadataModifiedHandler> getModifiedHandlers(final String code,
                                                               final Version version) {
        Version notNullVersion = assureNotNull(version);
        Set<MetadataModifiedHandler> result = modifiedHandlers.get(code, notNullVersion);
        if (result == null) {
            result = new HashSet<>();
            modifiedHandlers.put(code, notNullVersion, result);
        }
        return result;
    }

    private void startChangeTracking(final Path path, final String code, final Version version)
            throws IOException {
        if (Files.exists(path) && Files.isDirectory(path)) {
            WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            registerCodeVersionKey(key, code, version);
            _log.info(String.format("WatchService: The change tracking started for '%s'", path));
        }
    }

    private void registerCodeVersionKey(final WatchKey key, final String code,
                                        final Version version) {
        Version notNullVersion = assureNotNull(version);
        watchedCodes.put(key, code);
        watchedVersions.put(key, notNullVersion);
    }

    private String getCodeByKey(final WatchKey key) {
        return watchedCodes.get(key);
    }

    private Version getVersionByKey(final WatchKey key) {
        return watchedVersions.get(key);
    }

    protected Version assureNotNull(Version version) {
        return VersionUtil.ensureNotNull(version);
    }

    protected Version originalVersion(Version version) {
        return VersionUtil.originalVersion(version);
    }

    @Override
    public List<ApplicationStoreData> all() {
        List<ApplicationStoreData> result = new ArrayList<>();
        Path base = basePath();
        Path applications = base.resolve(APPLICATIONS_PATH);

        // проверка наличия директории
        if (Files.notExists(applications)) {
            try {
                Files.createDirectories(applications);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create the \"applications\" directory");
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(applications)) {
            for (Path code : stream) {
                Path tags = code.resolve(TAG_PATH);
                Path branches = code.resolve(BRANCH_PATH);
                if (Files.exists(tags)) {
                    try (DirectoryStream<Path> tagsStream = Files.newDirectoryStream(tags)) {
                        for (Path t : tagsStream) {
                            if (Files.exists(t.resolve(APPLICATION_FILE))) {
                                result.add(createApplicationStoreData(t, false));
                            }
                        }
                    } catch (IOException e) {
                        _log.error("Can not iterate over tags: " + applications.toString(), e);
                    }

                }
                if (Files.exists(branches)) {
                    try (DirectoryStream<Path> branchesStream = Files.newDirectoryStream(
                            branches)) {
                        for (Path b : branchesStream) {
                            if (Files.exists(b.resolve(APPLICATION_FILE))) {
                                result.add(createApplicationStoreData(b, true));
                            }
                        }
                    } catch (IOException e) {
                        _log.error("Can not iterate over tags: " + applications.toString(), e);
                    }
                }
            }
        } catch (IOException e) {
            _log.error("Can not iterate over applications: " + applications.toString(), e);
        }
        return result;
    }

    private ApplicationStoreData createApplicationStoreData(Path applicationVersionPath,
                                                            boolean branch)
            throws IOException {
        Path appFile = applicationVersionPath.resolve(APPLICATION_FILE);
        String appFileContent = new String(Files.readAllBytes(appFile), StandardCharsets.UTF_8);
        ApplicationStoreData data = assembleApplicationStoreData(appFileContent);
        data.setId(applicationVersionPath.toString());
        String versionString = applicationVersionPath.getFileName().toString();
        data.setVersion(VersionUtil.createVersion(versionString));
        long timestamp = appFile.toFile().lastModified();
        data.setModified(timestamp);
        return data;
    }

    @Override
    public Version getLastVersion(final String appCode) {
        Path applicationPath;
        try {
            applicationPath = resolveApplicationPath(appCode, null);
            if (applicationPath == null || Files.notExists(applicationPath) ||
                    !Files.isDirectory(applicationPath)) {
                return null;
            }
            Version result = Version.parseVersion(applicationPath.getFileName().toString());
            return result;
        } catch (IOException | MetadataStoreException e) {
            return null;
        }
    }

}
