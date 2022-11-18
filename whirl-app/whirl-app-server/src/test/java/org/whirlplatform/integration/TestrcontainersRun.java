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


public class TestrcontainersRun {

    private final String contextFile =  "../../docker/conf/postgresql/context.xml";
    private final String pathNameWar =  "target/whirl-app-server-0.3.0-SNAPSHOT.war";
    private final String littleWebAppPath = "target/LittleWebApp.war";
    private final String pathNameIndex = "src/test/resources/index.html";
    private final String urlWhirlDemo = "whirl-demo.jelastic.regruhosting.ru/app";

    MountableFile warFile = MountableFile.forHostPath(
            Paths.get(pathNameWar), 0777);
    MountableFile ctxFile = MountableFile.forHostPath(
            Paths.get(contextFile), 0777);

    Network net = Network.newNetwork();

    DockerImageName POSTGRES_TEST_IMAGE = DockerImageName.parse("postgres:10");
    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_TEST_IMAGE)
            .withUsername("postgres")
            .withNetwork(net)
            .withNetworkAliases("postgresql")
            .withExposedPorts(5432)
            .withFileSystemBind("../../docker/db/postgresql/", "/docker-entrypoint-initdb.d/")
            //.withFileSystemBind("../../../docker/db/postgresql/02-init-db.sh", "/docker-entrypoint-initdb.d/02-init-db.sh")

            //.withInitScript("src/test/resources/init_postgres.sql")
            //.withInitScript("C:/1-must-have/1-workspace/5-job/whirl/whirl-app/whirl-app-server/src/test/resources/init_postgres.sql")
            ;
    @Rule
    public FixedHostPortGenericContainer<?> tomcat = new FixedHostPortGenericContainer<>(
            "tomcat:9-jdk8")
            .withNetwork(net)
            .withNetworkAliases("tomcat")
            .withFixedExposedPort(8090, 8080)
            .withCopyToContainer(warFile, "/usr/local/tomcat/webapps/ROOT.war")
            .withCopyToContainer(ctxFile, "/usr/local/tomcat/conf/Catalina/localhost/context.xml.default")
            .waitingFor(Wait.forLogMessage(".* Server startup .*\\s", 1))
            .dependsOn(postgres)
//            .withCommand("--deploy \\usr\\local\\tomcat\\webapps\\ROOT.war ----contextRoot /")
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

    @Before
    public void setUp() {
        postgres.start();
    }

    ArrayList<String> portBindings = new ArrayList<>();

    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {

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

//            assertEquals("LittleApp", heading);

            Thread.sleep(300000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//       assertTrue(false);
    }
}
