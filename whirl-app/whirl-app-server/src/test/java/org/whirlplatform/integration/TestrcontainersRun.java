package org.whirlplatform.integration;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import sideex.ProtocalType;
import sideex.SideeXWebServiceClientAPI;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestrcontainersRun {

    private final String contextFile = "../../docker/conf/postgresql/context.xml";
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
//    @Rule
//    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
//            .withAccessToHost(true)
//            .withNetwork(net)
//            .withNetworkAliases("chrome")
//            .withCapabilities(new ChromeOptions())
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
//                    Paths.get("C:/Users/Nastia/Documents").toFile());


    @Rule
    public GenericContainer<?> selenium = new GenericContainer<>(
            DockerImageName.parse("selenium/hub:3.141.59"))
            .withNetwork(net)
            .withNetworkAliases("hub")
            .dependsOn(tomcat)
            ;

    @Rule
    public GenericContainer<?> nodeChrome = new GenericContainer<>(
            DockerImageName.parse("selenium/node-chrome:3.141.59"))
            .withNetwork(net)
            .withNetworkAliases("node-chrome")
            .withEnv("HUB_HOST", "hub")
            .dependsOn(selenium)
            ;

    @Rule
    public FixedHostPortGenericContainer<?> sideex = new FixedHostPortGenericContainer<>("sideex/webservice")
            .withNetwork(net)
            .withNetworkAliases("sideex")
            .withFixedExposedPort(50000, 50000)
            .withCopyToContainer(MountableFile.forClasspathResource("serviceconfig.json"),
                    "/opt/sideex-webservice/serviceconfig.json")
            .withCopyToContainer(MountableFile.forClasspathResource("tests/"),
                    "/opt/sideex-webservice/tests/")
            .dependsOn(nodeChrome);

//    public GenericContainer

//    @Test
//    // переименовать метод
//    public void openShowCaseAppTest() throws InterruptedException, IOException {
//        Integer tomcatPort = tomcat.getMappedPort(8080);
//
//
////        Thread.sleep();
////        postgres.withStartupTimeoutSeconds(1000000);
//        postgres.execInContainer("psql", "-U", "whirl", "-c",
//                "INSERT INTO whirl.WHIRL_USER_GROUPS (ID, DELETED, R_WHIRL_USERS, GROUP_CODE, NAME) VALUES (2, NULL, 1, 'whirl-showcase-user-group', '')");
//
////        postgres.setCommand();
////        postgres.
//        String tomcatHost = tomcat.getHost();
//        final String rootUrl = String.format("http://%s:%d/", tomcatHost, tomcatPort);
//        System.out.println(rootUrl);
//
//        RemoteWebDriver driver = chrome.getWebDriver();
//        driver.get("http://tomcat:8080/app?application=whirl-showcase");
//
//
//        String heading = driver.getTitle();
//        while (heading.isEmpty()) {
//            Thread.sleep(10000);
//        }
//
//        Thread.sleep(10000000);
//
//        assertEquals("Whirl Platform", heading);
//
//    }

    @Test
    public void openSideex() {
        try {
            //Connect to a SideeX WebService server
            SideeXWebServiceClientAPI wsClient = new SideeXWebServiceClientAPI("http://127.0.0.1:50000", ProtocalType.HTTPS_DISABLE);

            URL resource = getClass().getClassLoader().getResource("tests/tescase.zip");
            File file = new File(resource.toURI());


            Map<String, File> fileParams = new HashMap<String, File>();
            fileParams.put(file.getName(), file);

            Thread.sleep(10000000);

            String token = wsClient.runTestSuite(fileParams); // get the token
            boolean flag = false;

            while (!flag) {
                //Get the current state
                String state = new JSONObject(wsClient.getState(token)).getJSONObject("webservice").getString("state");
                if (!state.equals("complete") && !state.equals("error")) {
                    System.out.println(state);
                    Thread.sleep(2000);
                }
                //If test is error
                else if (state.equals("error")) {
                    System.out.println(state);
                    flag = true;
                }
                //If test is complete
                else {
                    System.out.println(state);
                    Map<String, String> formData = new HashMap<String, String>();
                    formData.put("token", token);
                    formData.put("file", "reports.zip");
                    //Download the test report
                    wsClient.download(formData, "./reports.zip", 0);

                    formData = new HashMap<String, String>();
                    formData.put("token", token);
                    //Download the logs
                    wsClient.download(formData, "./logs.zip", 1);
                    flag = true;

                    //Delete the test case and report from the server
                    System.out.println(wsClient.deleteReport(token));
                }
            }
            System.out.println(wsClient.runTestSuite(fileParams));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Test
//    public void openApplicationTest(){
//        WebElement button = driver.findElement(By.xpath("//div[text()='Event']"));
//        assertNotNull(button);
//    }

}
