package org.whirlplatform.integration;

import ch.qos.logback.core.net.SocketConnector;
import org.junit.Before;
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
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class TestrcontainersRun {

    MountableFile warFile = MountableFile.forHostPath(
            Paths.get("target/whirl-app-server-0.3.0-SNAPSHOT.war").toAbsolutePath(), 0777);;

            public GenericContainer<?> container = new GenericContainer<>(
            DockerImageName.parse("tomcat") // tomcat
    )
            .withExposedPorts(8090, 8091);

    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withCapabilities(new ChromeOptions());
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, target);
//            .withNetwork(NETWORK);

    private int localPort = 8090;


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


    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {
        final String rootUrl = String.format("http://host.testcontainers.internal:%d/",localPort);


        RemoteWebDriver driver = chrome.getWebDriver();

        Testcontainers.exposeHostPorts(localPort);
        driver.get("http://host.testcontainers.internal:" + localPort);

        driver.get("http://whirl-demo.jelastic.regruhosting.ru/");
        String heading = driver.findElement(By.xpath("//div[contains(text(),'Приложение')]"))
                .getText();

        assertEquals("Приложение не доступно на этом сервере.", heading);
    }
}
