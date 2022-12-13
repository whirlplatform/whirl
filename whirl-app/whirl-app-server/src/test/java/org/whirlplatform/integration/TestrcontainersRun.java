package org.whirlplatform.integration;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.*;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
            .dependsOn(tomcat);

    @Rule
    public GenericContainer<?> nodeChrome = new GenericContainer<>(
            DockerImageName.parse("selenium/node-chrome:3.141.59"))
            .withNetwork(net)
            .withNetworkAliases("node-chrome")
            .withEnv("HUB_HOST", "hub")
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
//                    Paths.get("C:/Users/Nastia/Documents").toFile())
//            .withCapabilities(new ChromeOptions())
            .dependsOn(selenium);

    @Rule
    public FixedHostPortGenericContainer<?> sideex = new FixedHostPortGenericContainer<>("sideex/webservice")
            .withNetwork(net)
            .withNetworkAliases("sideex")
            .withFixedExposedPort(50000, 50000)
//            .withCopyToContainer(MountableFile.forClasspathResource("serviceconfig.json"),
//                    "/opt/sideex-webservice/serviceconfig.json")
//            .withCopyToContainer(MountableFile.forClasspathResource("tests/"),
//                    "/opt/sideex-webservice/tests/")
            .waitingFor(Wait.forLogMessage(".*SideeX WebService is up and running.*\\s", 1)
                    .withStartupTimeout(Duration.ofMinutes(2)))
            .withEnv("TOMCAT_HOST", "tomcat")
            .dependsOn(nodeChrome);

//    @Test
//    // переименовать метод
//    public void openShowCaseAppTest() throws InterruptedException, IOException {
//        Integer tomcatPort = tomcat.getMappedPort(8080);
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
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
//            postgres.execInContainer("psql", "-U", "whirl", "-c",
//                    "create extension if not exist hstore");

            postgres.execInContainer("psql", "-U", "whirl", "-c",
                    "INSERT INTO whirl.WHIRL_USER_GROUPS (ID, DELETED, R_WHIRL_USERS, GROUP_CODE, NAME) VALUES (2, NULL, 1, 'whirl-showcase-user-group', '')");

            URL resource = getClass().getClassLoader().getResource("test-cases.zip");
//            System.out.println("Print URI: " + resource.toURI());
            File file = new File(resource.toURI());
            Map<String, File> fileParams = new HashMap<>();
            fileParams.put(file.getName(), file);

            String url = "http://127.0.0.1:50000/sideex-webservice/";

            HttpPost runTestSuitesPost = new HttpPost(url + "runTestSuites");
            HttpEntity data = MultipartEntityBuilder.create().setMode(HttpMultipartMode.EXTENDED)
                    .addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName())
                    .build();
            runTestSuitesPost.setEntity(data);

            CloseableHttpResponse respons = httpclient.execute(runTestSuitesPost);


            String body = EntityUtils.toString(respons.getEntity());
            System.out.println("Executing request " + body);

            JSONObject jsonRunSuite = new JSONObject(body);
            String token = jsonRunSuite.getString("token");
            System.out.println("Json token: " + token);


            boolean flag = false;
            while (!flag) {
                HttpGet getStateGet = new HttpGet(url + "getState?token=" + token);

                respons = httpclient.execute(getStateGet);
                String stateSt = EntityUtils.toString(respons.getEntity());
                System.out.println("Test status " + stateSt);

                JSONObject jsonState = new JSONObject(stateSt);


                //Get the current state
                String state = jsonState.getJSONObject("webservice").getString("state");
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
//                    Thread.sleep(1000000);
                    //Download the test report
//                    String dowUrl = url + "downloadReports?token=" + token+"&file=reports.zip";
                    JsonUtils jsonUtils = new JsonUtils(new URL("http://127.0.0.1:50000/sideex-webservice/"), token);
                    System.out.println(jsonUtils.getCountCaseSuccess());
                    System.out.println(jsonUtils.getLogsErrorMassage());
                    System.out.println(jsonUtils.getFailedOperation());
                    System.out.println(jsonUtils.getLog());
                    System.out.println(jsonUtils.getImage());
                    System.out.println(jsonUtils.getCountSuitSuccess());
                    System.out.println(jsonUtils.getCountCaseFailed());
                    System.out.println(jsonUtils.getCountSuitFailed());
                    // формируется лог
//                    jsonUtils.close();
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

