package org.whirlplatform.integration;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class TestrcontainersRun {

    //private final String pathNameWar =  "target/whirl-app-server-0.3.0-SNAPSHOT/index.html";
    //private final String pathNameWar =  "target/whirl-app-server-0.3.0-SNAPSHOT.war";
    //private final String pathNameWar = "target/index.html";
    private final String pathNameWar = "src/test/resources/index.html";
    MountableFile warFile = MountableFile.forHostPath(
            Paths.get(pathNameWar), 0777);

    Network net = Network.newNetwork();

    @Rule
    public GenericContainer<?> tomcat = new GenericContainer<>(
            DockerImageName.parse("tomcat:9-jdk8"))
            .withNetwork(net)
            .withNetworkAliases("tomcat")
            .withExposedPorts(8080)
            .withCopyToContainer(warFile, "/usr/local/tomcat/webapps/ROOT/index.html")
//            .withStartupCheckStrategy(new OneShotStartupCheckStrategy().withTimeout(Duration.ofSeconds(6)))
            .waitingFor(Wait.forLogMessage(".* Server startup .*\\s", 1))
//            .withCommand("--deploy \\usr\\local\\tomcat\\webapps\\ROOT.war ----contextRoot /")
            ;
//    public GenericContainer<?> postgres = new GenericContainer<>(
//            DockerImageName.parse("postgres:10"))
//            .withNetwork(net);

    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withNetwork(net)
            .withNetworkAliases("chrome")
            .withCapabilities(new ChromeOptions());
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, Paths.get("C:\\Users\\Nastia\\Documents").toFile());
//            .withNetwork(NETWORK);

    private Integer localPort= 8090;
    private Integer tomcatPort;
    private String tomcatHost;


//    @Before
//    public void setupLocalServer() throws Exception {
//        // Set up a local Jetty HTTP server
//        Server server = new Server();
//        server.addConnector();
//
//        ResourceHandler resourceHandler = new ResourceHandler();
//        resourceHandler.setResourceBase("src/test/resources/server");
//        server.addHandler(resourceHandler);
//        server.start();
//
//        // The server will have a random port assigned, so capture that
//        localPort = server.getConnectors()[0].getLocalPort();
//    }


//    @Before
//    public void setUp() {
//        tomcatHost = container.getHost();
//        tomcatPort = container.getFirstMappedPort();
//
//    }


    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {
        tomcatHost = tomcat.getHost();
        tomcatPort = tomcat.getFirstMappedPort();
        System.out.println(Paths.get(pathNameWar).toAbsolutePath());


        final String rootUrl = String.format("http://%s:%d/", tomcatHost, tomcatPort);
        System.out.println(rootUrl);


        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://tomcat:8080/index.html");

        try {
            String heading = driver.getTitle();
            while (heading.isEmpty()) {
                Thread.sleep(1000);
                heading = driver.getTitle();
            }
            System.out.println(heading);
//        assertEquals("Приложение не доступно на этом сервере.", heading);

            Thread.sleep(3000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(false);
    }
}
