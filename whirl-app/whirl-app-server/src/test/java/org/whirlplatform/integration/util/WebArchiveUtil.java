package org.whirlplatform.integration.util;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.filter.ExcludePaths;
import org.whirlplatform.server.metadata.store.simple.SimpleMetadataStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WebArchiveUtil {
    public static final String INTEGRATION_PACKAGE = "org.whirlplatform.integration";
    public static final String RESOURCES = "src/test/resources";

    public static WebArchive createTestWar(final String whirlProjectVersion) {
        WebArchive testWar = ShrinkWrap.create(WebArchive.class).addAsManifestResource("arquillian.xml");
        testWar.addClasses(SimpleMetadataStore.class);
        addApplicationsToWebArchive(testWar);
        testWar.addPackages(true, INTEGRATION_PACKAGE);
        testWar.addAsManifestResource("context.xml").setWebXML("web.xml");
        testWar.merge(getWhirlApplicationWar(whirlProjectVersion));
        return testWar;
    }

    public static WebArchive getWhirlApplicationWar(final String projectVersion) {
        final String warFileName = String.format("target/whirl-app-server-%s.war", projectVersion);
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(warFileName))
                .filter(new ExcludePaths("/WEB-INF/web.xml"));
    }

    private static void addApplicationsToWebArchive(final WebArchive war) {
        Map<File, String> apps = applicationFiles();
        for (Map.Entry<File, String> app : apps.entrySet()) {
            war.addAsResource(app.getKey(), app.getValue());
        }
        System.out.printf("Found and added %d application file(s)%n", apps.size());
    }

    private static Map<File, String> applicationFiles() {
        Map<File, String> result = new HashMap<>();
        String packPath = SimpleMetadataStore.class.getPackage().toString().replace("package ", "").replace(".", "/");
        Path appsRoot = Paths.get(RESOURCES).resolve(packPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(appsRoot)) {
            for (Path file : stream) {
                if (file.toString().endsWith(".xml")) {
                    result.put(file.toFile(), String.format("%s/%s", packPath, file.getFileName()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error getting application files");
            e.printStackTrace();
        }
        return result;
    }
}
