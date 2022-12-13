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
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ServerIT {
    private static final String CONTEXT_FILE = "../../docker/conf/postgresql/context.xml.default";
    private static final String WAR_PATH = "target/whirl-app-server-0.3.0-SNAPSHOT.war";
    private static final String APP_PATH = "../../.whirl-work/";
    private static Network net = Network.newNetwork();
    @ClassRule
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        // TODO: параметр для версии контейнера - переменная
        DockerImageName.parse("postgres:10"))
        .withUsername("postgres")
        .withNetwork(net)
        .withNetworkAliases("postgresql")
        .withExposedPorts(5432)
        .withFileSystemBind("../../docker/db/postgresql/",
            "/docker-entrypoint-initdb.d/");
    @ClassRule
    public static FixedHostPortGenericContainer<?> tomcat = new FixedHostPortGenericContainer<>(
        "tomcat:9-jdk8")
        .withNetwork(net)
        .withNetworkAliases("tomcat")
        .withFixedExposedPort(8090, 8080)
        .withCopyToContainer(MountableFile.forHostPath(Paths.get(WAR_PATH), 0777),
            "/usr/local/tomcat/webapps/ROOT.war")
        .withCopyToContainer(MountableFile.forHostPath(Paths.get(CONTEXT_FILE), 0777),
            "/usr/local/tomcat/conf/Catalina/localhost/context.xml.default")
        // TODO: у Насти по коду взять "Server startup"
        .waitingFor(Wait.forLogMessage(".*Server startup.*\\s", 1)
            .withStartupTimeout(Duration.ofMinutes(2)))
        .withCopyToContainer(MountableFile.forHostPath(Paths.get(APP_PATH), 0777),
            "/usr/local/whirl")
        .dependsOn(postgres);

    @ClassRule
    public static GenericContainer<?> selenium = new GenericContainer<>(
        DockerImageName.parse("selenium/hub:3.141.59"))
        .withNetwork(net)
        .withNetworkAliases("hub")
        .dependsOn(tomcat);

    @ClassRule
    public static GenericContainer<?> nodeChrome = new GenericContainer<>(
        DockerImageName.parse("selenium/node-chrome:3.141.59"))
        .withNetwork(net)
        .withNetworkAliases("node-chrome")
        .withEnv("HUB_HOST", "hub")
        .dependsOn(selenium);

    @ClassRule
    public static GenericContainer<?> sideex = new GenericContainer<>(
        new ImageFromDockerfile()
            // TODO: resoursePath в константы
            .withFileFromClasspath("Dockerfile", "tests/SideEx-Webservice.Dockerfile")
            .withFileFromClasspath("tests/prepare-test/config.json", "tests/prepare-test/config.json")
    )
        .withNetwork(net)
        .withNetworkAliases("sideex")
        .withExposedPorts(50000)
        .waitingFor(Wait.forLogMessage(".*SideeX WebService is up and running.*\\s", 1)
            .withStartupTimeout(Duration.ofMinutes(2)))
        .dependsOn(nodeChrome)
        ;

    @Test
    public void openSideex() throws IOException, InterruptedException, URISyntaxException {

        // TODO: убрать, когда пойдут приложения
        postgres.execInContainer("psql", "-U", "whirl", "-c",
            "INSERT INTO whirl.WHIRL_USER_GROUPS (ID, DELETED, R_WHIRL_USERS, GROUP_CODE, NAME) VALUES (2, NULL, 1, 'whirl-showcase-user-group', '')");
        postgres.execInContainer("psql", "-U", "postgres", "-c", "ALTER ROLE whirl superuser;");
        postgres.execInContainer("psql", "-U", "whirl", "-c", "CREATE EXTENSION IF NOT EXISTS pgcrypto");
        postgres.execInContainer("psql", "-U", "postgres", "-c", "ALTER ROLE whirl nosuperuser");

        URL resource = getClass().getClassLoader().getResource("test-cases.zip");
        makeConnection(resource);
        Thread.sleep(10000000);
    }

    static void makeConnection(URL resource) throws URISyntaxException {
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            File file = new File(resource.toURI());
            Map<String, File> fileParams = new HashMap<String, File>();
            fileParams.put(file.getName(), file);

            String port = sideex.getFirstMappedPort().toString();
            String url = "http://127.0.0.1:" + port + "/sideex-webservice/";
            System.out.println("\nPort: " + port + "\n");

            HttpPost runTestSuitesPost = new HttpPost(url + "runTestSuites");
            HttpEntity data = MultipartEntityBuilder
                .create()
                .setMode(HttpMultipartMode.EXTENDED)
                .addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName())
                .build();
            runTestSuitesPost.setEntity(data);

            CloseableHttpResponse response = httpclient.execute(runTestSuitesPost);
            String body = EntityUtils.toString(response.getEntity());
            System.out.println("Executing request " + body);

            JSONObject jsonRunSuite = new JSONObject(body);
            String token = jsonRunSuite.getString("token");
            System.out.println("Json token: "+token);

            runningTests(httpclient, url, token, response);

            // TODO: сделать конкретный exception
        } catch (Exception e) {
            // TODO: error через Logger
            e.printStackTrace();
        }
    }

    static void runningTests(CloseableHttpClient httpclient, String url, String token, CloseableHttpResponse response)
        throws IOException, ParseException, InterruptedException {

        boolean flag = false;
        while (!flag) {
            HttpGet getStateGet = new HttpGet(url + "getState?token="+token);

            response  = httpclient.execute(getStateGet);

            String stateSt = EntityUtils.toString(response.getEntity());
            System.out.println("Test status "+ stateSt);

            JSONObject jsonState = new JSONObject(stateSt);

            String state = jsonState.getJSONObject("webservice").getString("state");
            if (!state.equals("complete") && !state.equals("error")) {
                System.out.println(state);
                Thread.sleep(2000);
            } else if (state.equals("error")) {
                System.out.println(state);
                flag = true;
            } else {
                // TODO: логирование через Logger
                System.out.println(state);
                Map<String, String> formData = new HashMap<String, String>();
                formData.put("token", token);
                formData.put("file", "reports.zip");

                //Download the test report
                //wsClient.download(formData, "./reports.zip", 0);

                formData = new HashMap<String, String>();
                formData.put("token", token);
                //Download the logs
                //wsClient.download(formData, "./logs.zip", 1);
                flag = true;

                //Delete the test case and report from the server
                //System.out.println(wsClient.deleteReport(token));

                // here is nastya`s json
                JsonUtils jsonUtils = new JsonUtils(new URL(url), token);
                System.out.println(jsonUtils.getLog());
                //System.out.println(jsonUtils.getCountCaseFailed());
                // формируется лог
                Thread.sleep(1000000000);
                jsonUtils.close();


            }
        }
    }
}
