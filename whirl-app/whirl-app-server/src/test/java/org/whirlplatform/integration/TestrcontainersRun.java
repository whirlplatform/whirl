package org.whirlplatform.integration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.*;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestrcontainersRun {

    private final String pathNameWar =  "target/whirl-app-server-0.3.0-SNAPSHOT.war";
    private final String littleWebAppPath = "target/LittleWebApp.war";
    private final String pathNameIndex = "src/test/resources/index.html";
    private final String urlWhirlDemo = "whirl-demo.jelastic.regruhosting.ru/app";

    MountableFile warFile = MountableFile.forHostPath(
            Paths.get(littleWebAppPath), 0777);

    Network net = Network.newNetwork();
    @Rule
    public GenericContainer<?> tomcat = new GenericContainer<>(
            DockerImageName.parse("tomcat:9-jdk8"))
            .withNetwork(net)
            .withNetworkAliases("tomcat")
            .withExposedPorts(8080)
            .withCopyToContainer(warFile, "/usr/local/tomcat/webapps/ROOT.war")
//            .withStartupCheckStrategy(new OneShotStartupCheckStrategy().withTimeout(Duration.ofSeconds(6)))
            .waitingFor(Wait.forLogMessage(".* Server startup .*\\s", 1))
//            .withCommand("--deploy \\usr\\local\\tomcat\\webapps\\ROOT.war ----contextRoot /")
            ;

    DockerImageName POSTGRES_TEST_IMAGE = DockerImageName.parse("postgres:10");
    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_TEST_IMAGE)
//            .withDatabaseName("whirl")
//            .withUsername("whirl")
//            .withPassword("password")
            .withNetwork(net)
            .withNetworkAliases("postgres")
            .withExposedPorts(5432)
            .withInitScript("init_postgres.sql")
            ;

    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withNetwork(net)
            .withNetworkAliases("chrome")
            .withCapabilities(new ChromeOptions())
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING, Paths.get("C:\\Users\\Nastia\\Documents").toFile())
            ;

    private Integer localPort= 8090;
    private Integer tomcatPort;
    private String tomcatHost;

//    @Before
//    public void setUp() {
//
//    }


    ArrayList<String> portBindings = new ArrayList<>();

    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {

//        postgres.start();
//        tomcat.start();

        tomcat.addExposedPort(8090);

        portBindings.add(String.format("%s:%d/%s", "localhost", 8080, InternetProtocol.TCP));
        tomcat.setPortBindings(portBindings);


        tomcatHost = tomcat.getHost();
        // = tomcat.getFirstMappedPort();
        tomcatPort = tomcat.getMappedPort(8080);

        System.out.println(Paths.get(pathNameWar).toAbsolutePath());
        final String rootUrl = String.format("http://%s:%d/", tomcatHost, tomcatPort);
        System.out.println(rootUrl);

        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://tomcat:8080/");

        try {
            String heading = driver.getTitle();
            while (heading.isEmpty()) {
                Thread.sleep(1000);
                heading = driver.getTitle();
            }
            System.out.println(heading);

            assertEquals("LittleApp", heading);

            Thread.sleep(300000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//       assertTrue(false);
    }
}
