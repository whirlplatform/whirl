package org.whirlplatform.server.metadata.store.file;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.metadata.store.MetadataStoreException;
import org.whirlplatform.test.TestHelper;

public class FileSystemMetadataStoreTest {

    private static String WORK_PATH = "whirl";
    private static String APPLICATION_CODE = "test-application";
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private ApplicationUser user;
    private FileSystem fs;
    private Path applicationPath;
    private Path tagPath;
    private Path branchPath;
    private Path version100Path;
    private Path version102Path;
    private Path version220Path;
    private Path version231Path;
    private Path branch1Path;

    @Before
    public void before() throws IOException {
        user = new ApplicationUser();
        user.setId("1");
        user.setLogin("test_user");

        WORK_PATH = tempFolder.newFolder(WORK_PATH).getAbsolutePath();

//        fs = Jimfs.newFileSystem(com.google.common.jimfs.Configuration.unix());
        fs = FileSystems.getDefault();
        applicationPath = fs.getPath(WORK_PATH,
            FileSystemMetadataStore.APPLICATIONS_PATH, APPLICATION_CODE);
        tagPath = applicationPath.resolve(FileSystemMetadataStore.TAG_PATH);
        branchPath = applicationPath
            .resolve(FileSystemMetadataStore.BRANCH_PATH);
        Files.createDirectories(applicationPath);
        Files.createDirectories(tagPath);
        Files.createDirectories(branchPath);

        version100Path = tagPath.resolve("1.0.0");
        Files.createDirectories(version100Path);
        version102Path = tagPath.resolve("1.0.2");
        Files.createDirectories(version102Path);
        version220Path = tagPath.resolve("2.2.0");
        Files.createDirectories(version220Path);
        version231Path = tagPath.resolve("2.3.1");
        Files.createDirectories(version231Path);

        branch1Path = branchPath.resolve("branch1");
        Files.createDirectories(branch1Path);
    }

    @Test
    public void test() throws MetadataStoreException {
        FileSystemMetadataStore store = new FileSystemMetadataStore(
            new Configuration() {

                @Override
                public <T> Map<String, T> lookupAll(String path,
                                                    Class<T> cls) {
                    return null;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T> T lookup(String name) {
                    if ("Whirl/cachetimeout".equals(name)) {
                        return (T) new Integer(10);
                    }
                    return (T) WORK_PATH;
                }
            }, fs);

        List<Version> allVersions = new ArrayList<>();

        // проверяем сохранение
        Assert.assertFalse(Files.exists(version100Path
            .resolve(FileSystemMetadataStore.APPLICATION_FILE)));

        ApplicationElement application = TestHelper.emptyApplication();
        application.setCode(APPLICATION_CODE);
        application.setName("Version 1.0.0");
        store.saveApplication(application, Version.parseVersion("1.0.0"), user);
        allVersions.add(Version.parseVersion("1.0.0"));

        Assert.assertTrue("Application not saved", Files.exists(version100Path
            .resolve(FileSystemMetadataStore.APPLICATION_FILE)));

        // проверяем загрузку
        ApplicationElement loadedApplication = store.loadApplication(
            application.getCode(), Version.parseVersion("1.0.0"));
        Assert.assertEquals("Application not loaded", application.getCode(),
            loadedApplication.getCode());

        // проверяем загрузку последней версии
        application.setName("Version 1.0.2");
        store.saveApplication(application, Version.parseVersion("1.0.2"), user);
        allVersions.add(Version.parseVersion("1.0.2"));

        application.setName("Version 2.2.0");
        store.saveApplication(application, Version.parseVersion("2.2.0"), user);
        allVersions.add(Version.parseVersion("2.2.0"));

        application.setName("Version 2.3.1");
        store.saveApplication(application, Version.parseVersion("2.3.1"), user);
        allVersions.add(Version.parseVersion("2.3.1"));

        loadedApplication = store.loadApplication(application.getCode(), null);
        Assert.assertEquals("Not last version loaded", application.getName(),
            loadedApplication.getName());

        // проверяем сохранение и загрузку ветки(branch)
        application.setName("Branch 1");
        store.saveApplication(application, Version.create("branch1"), user);
        Assert.assertTrue("Branch 1 save problem",
            Files.exists(branch1Path.resolve(FileSystemMetadataStore.APPLICATION_FILE)));
        allVersions.add(Version.create("branch1"));

        loadedApplication = store.loadApplication(application.getCode(), Version.create("branch1"));
        Assert.assertEquals("Branch 1 load problem", application.getName(),
            loadedApplication.getName());

        // проверяем получение списка со всеми версиями
        List<ApplicationStoreData> list = store.all();
        Assert.assertEquals("Size of saved version and size of actual version not equal",
            allVersions.size(), list.size());

        for (ApplicationStoreData d : list) {
            Assert.assertTrue("Version not found", allVersions.contains(d.getVersion()));
        }
    }
}

