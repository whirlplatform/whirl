//package org.whirlplatform.integration.sideex;
//
//import org.json.JSONObject;
//import org.junit.Rule;
//import org.junit.Test;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testcontainers.containers.*;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.testcontainers.utility.DockerImageName;
//import org.testcontainers.utility.MountableFile;
//import sideex.ProtocalType;
//import sideex.SideeXWebServiceClientAPI;
//
//import java.io.File;
//import java.nio.file.Paths;
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SideExDocker {
//
//    private final String contextFile = "../../docker/conf/postgresql/context.xml.default";
//    private final String pathNameWar = "target/whirl-app-server-0.3.0-SNAPSHOT.war";
//    private String appPath = "../../.whirl-work/";
//    private Network net = Network.newNetwork();
//    @Rule
//    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            DockerImageName.parse("postgres:10"))
//            .withUsername("postgres")
//            .withNetwork(net)
//            .withNetworkAliases("postgresql")
//            .withExposedPorts(5432)
//            .withFileSystemBind("../../docker/db/postgresql/",
//                    "/docker-entrypoint-initdb.d/");
//    @Rule
//    public FixedHostPortGenericContainer<?> tomcat = new FixedHostPortGenericContainer<>(
//            "tomcat:9-jdk8")
//            .withNetwork(net)
//            .withNetworkAliases("tomcat")
//            .withFixedExposedPort(8090, 8080)
//            .withCopyToContainer(MountableFile.forHostPath(Paths.get(pathNameWar), 0777),
//                    "/usr/local/tomcat/webapps/ROOT.war")
//            .withCopyToContainer(MountableFile.forHostPath(Paths.get(contextFile), 0777),
//                    "/usr/local/tomcat/conf/Catalina/localhost/context.xml.default")
//            .waitingFor(Wait.forLogMessage(".*Server startup.*\\s", 1)
//                    .withStartupTimeout(Duration.ofMinutes(2)))
//            .withCopyToContainer(MountableFile.forHostPath(Paths.get(appPath), 0777),
//                    "/usr/local/whirl")
//            .dependsOn(postgres);
//    @Rule
//    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
//            .withAccessToHost(true)
//            .withNetwork(net)
//            .withNetworkAliases("chrome")
//            .withCapabilities(new ChromeOptions())
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
//                    //Paths.get("C:/Users/Nastia/Documents").toFile())
//                    Paths.get("C:/Users/User/Documents").toFile())
//            ;
//
//    @Rule
//    public GenericContainer<?> selenium = new GenericContainer<>(
//            DockerImageName.parse("selenium/hub"))
//            .withNetwork(net)
//            .withNetworkAliases("hub")
//            .withExposedPorts(4444)
//            .withCopyToContainer(MountableFile.forHostPath(
//                            Paths.get("../../src/test/resources/serviceconfig.json"), 0777),
//                    "/opt/sideex-webservice/serviceconfig.json")
////            .waitingFor(Wait.forLogMessage(".*Server startup.*\\s", 1)
////                    .withStartupTimeout(Duration.ofMinutes(2)))
//            .dependsOn(tomcat)
//            ;
//
//    @Rule
//    public GenericContainer<?> nodeChrome = new GenericContainer<>(
//            DockerImageName.parse("selenium/node-chrome-debug"))
//            .withNetwork(net)
//            .withNetworkAliases("nodeChrome")
//            .withExposedPorts(5900)
//            .dependsOn(selenium)
//            ;
//
//    @Rule
//    public GenericContainer<?> sideex = new GenericContainer<>(
//            DockerImageName.parse("sideex/webservice"))
//            .withNetwork(net)
//            .withNetworkAliases("sideex")
//            .withExposedPorts(50000)
//            .dependsOn(nodeChrome)
//            ;
//
//    @Test
//    public void openSideex() {
//        try {
//            SideeXWebServiceClientAPI wsClient = new SideeXWebServiceClientAPI("http://127.0.0.1:05000", ProtocalType.HTTPS_DISABLE);
//            File file = new File("C:/1-must-have/1-workspace/5-job/testcase.zip");
//
//            Map<String, File> fileParams = new HashMap<String, File>();
//            fileParams.put(file.getName(), file);
//
//            String token = new JSONObject(wsClient.runTestSuite(fileParams)).getString("token"); // get the token
//            boolean flag = false;
//
//            while (!flag) {
//                //Get the current state
//                String state = new JSONObject(wsClient.getState(token)).getJSONObject("webservice").getString("state");
//                if (!state.equals("complete") && !state.equals("error")) {
//                    System.out.println(state);
//                    Thread.sleep(2000);
//                }
//                //If test is error
//                else if (state.equals("error")) {
//                    System.out.println(state);
//                    flag = true;
//                }
//                //If test is complete
//                else {
//                    System.out.println(state);
//                    Map<String, String> formData = new HashMap<String, String>();
//                    formData.put("token", token);
//                    formData.put("file", "reports.zip");
//                    //Download the test report
//                    wsClient.download(formData, "./reports.zip", 0);
//
//                    formData = new HashMap<String, String>();
//                    formData.put("token", token);
//                    //Download the logs
//                    wsClient.download(formData, "./logs.zip", 1);
//                    flag = true;
//
//                    //Delete the test case and report from the server
//                    System.out.println(wsClient.deleteReport(token));
//                }
//            }
//            System.out.println(wsClient.runTestSuite(fileParams));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    void waitForLoad(WebDriver driver) {
//        ExpectedCondition<Boolean> pageLoadCondition = new
//                ExpectedCondition<Boolean>() {
//                    public Boolean apply(WebDriver driver) {
//                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
//                    }
//                };
//        WebDriverWait wait = new WebDriverWait(driver, 30);
//        wait.until(pageLoadCondition);
//    }
//
//}
//
