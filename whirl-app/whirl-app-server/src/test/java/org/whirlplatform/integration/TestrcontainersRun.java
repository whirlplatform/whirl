package org.whirlplatform.integration;

import ch.qos.logback.core.net.SocketConnector;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.handler.ResourceHandler;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestrcontainersRun {

    private final String pathNameIndex = "src/test/resources/index.html";
    private final String pathNameWar = "C:\\Projects\\whirl_\\whirl-app\\whirl-app-server\\target\\whirl-app-server-0.3.0-SNAPSHOT.war";
    MountableFile warFile = MountableFile.forHostPath(
            Paths.get(pathNameWar)
                    .toAbsolutePath(), 0777);

    @Rule
    public GenericContainer<?> container = new GenericContainer<>(
            DockerImageName.parse("tomcat:9"))
            .withExposedPorts(8080)
            .withCopyToContainer(warFile, "\\usr\\local\\tomcat\\webapps\\ROOT.war")
//            .withStartupCheckStrategy(new OneShotStartupCheckStrategy().withTimeout(Duration.ofSeconds(6)))
            .waitingFor(Wait.forLogMessage(".* Server startup .*\\s", 6))
//            .withCommand("--deploy \\usr\\local\\tomcat\\webapps\\ROOT.war ----contextRoot /")
                ;
    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, Paths.get("C:\\Users\\Nastia\\Documents").toFile());
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


    @Before
    public void setUp() {
        tomcatHost = container.getHost();
        tomcatPort = container.getFirstMappedPort();

    }


    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {
        final String rootUrl = String.format("http://%s:%d/", tomcatHost, tomcatPort);
        System.out.println(rootUrl);

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        RemoteWebDriver driver = chrome.getWebDriver();
//        driver.get(rootUrl);


        Testcontainers.exposeHostPorts(localPort);
        driver.get("http://localhost:"+localPort+"/whirl-demo.jelastic.regruhosting.ru/");


//        driver.get("http://whirl-demo.jelastic.regruhosting.ru/");
//        String heading = driver.findElement(By.xpath("//div[contains(text(),'Приложение')]"))
//                .getText();
//        assertEquals("Приложение не доступно на этом сервере.", heading);

        assertTrue(false);
    }
}
