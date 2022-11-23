package org.whirlplatform.integration;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class TestrcontainersRun {

    private final String contextFile = "../../docker/conf/postgresql/context.xml.default";
    private final String pathNameWar = "target/whirl-app-server-0.3.0-SNAPSHOT.war";
    private String appPath = "../../.whirl-work/";
    private Network net = Network.newNetwork();
    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:10"))
            .withUsername("postgres")
            .withNetwork(net)
            .withNetworkAliases("postgresql")
            .withExposedPorts(5432)
            .withFileSystemBind("../../docker/db/postgresql/",
                    "/docker-entrypoint-initdb.d/");
    @Rule
    public FixedHostPortGenericContainer<?> tomcat = new FixedHostPortGenericContainer<>(
            "tomcat:9-jdk8")
            .withNetwork(net)
            .withNetworkAliases("tomcat")
            .withFixedExposedPort(8090, 8080)
            .withCopyToContainer(MountableFile.forHostPath(Paths.get(pathNameWar), 0777),
                    "/usr/local/tomcat/webapps/ROOT.war")
            .withCopyToContainer(MountableFile.forHostPath(Paths.get(contextFile), 0777),
                    "/usr/local/tomcat/conf/Catalina/localhost/context.xml.default")
            .waitingFor(Wait.forLogMessage(".*Server startup.*\\s", 1)
                    .withStartupTimeout(Duration.ofMinutes(2)))
            .withCopyToContainer(MountableFile.forHostPath(Paths.get(appPath), 0777),
                    "/usr/local/whirl")
            .dependsOn(postgres);
    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withNetwork(net)
            .withNetworkAliases("chrome")
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
                    Paths.get("C:/Users/Nastia/Documents").toFile());



    @Test
    // переименовать метод
    public void openShowCaseAppTest() throws InterruptedException, IOException {
        Integer tomcatPort = tomcat.getMappedPort(8080);


//        Thread.sleep();
//        postgres.withStartupTimeoutSeconds(1000000);
        postgres.execInContainer("psql", "-U", "whirl", "-c",
                "INSERT INTO whirl.WHIRL_USER_GROUPS (ID, DELETED, R_WHIRL_USERS, GROUP_CODE, NAME) VALUES (2, NULL, 1, 'whirl-showcase-user-group', '')");

//        postgres.setCommand();
//        postgres.
        String tomcatHost = tomcat.getHost();
        final String rootUrl = String.format("http://%s:%d/", tomcatHost, tomcatPort);
        System.out.println(rootUrl);

        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://tomcat:8080/");


        String heading = driver.getTitle();
        while (heading.isEmpty()) {
            Thread.sleep(10000);
        }

        Thread.sleep(1000000);

        assertEquals("Whirl Platform", heading);
    }

}
