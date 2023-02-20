package org.whirlplatform.integration;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class ServerIT {

    private static final String CONTEXT_FILE = "../../docker/conf/postgresql/context.xml.default";
    private static final String WAR_PATH = "target/whirl-app-server.war";
    private static final String APP_PATH = "tests/applications-to-test/";
    private static Logger _log = LoggerFactory.getLogger(ServerIT.class);
    private static Network net = Network.newNetwork();
    private static String RESOURCE_PATH_DOCKER = "tests/SideEx-Webservice.Dockerfile";
    private static String RESOURCE_PATH_CONFIG = "tests/prepare-test/config.json";
    private static int DATABASE_VERSION = 10;
    @ClassRule
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:" + DATABASE_VERSION))
        .withUsername("postgres")
        .withNetwork(net)
        .withNetworkAliases("postgresql")
        .withExposedPorts(5432)
        .withCopyToContainer(MountableFile.forHostPath("../../docker/db/postgresql/"),
                "/docker-entrypoint-initdb.d/")
        ;

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
        .waitingFor(Wait.forLogMessage(".*Server startup.*\\s", 1)
            .withStartupTimeout(Duration.ofMinutes(2)))
        .withCopyToContainer(MountableFile.forClasspathResource(APP_PATH, 0777),
            "/usr/local/whirl")
        .dependsOn(postgres)
            ;

    @ClassRule
    public static GenericContainer<?> selenium = new GenericContainer<>(
        DockerImageName.parse("selenium/hub:3.141.59"))
        .withNetwork(net)
        .withNetworkAliases("hub")
        .dependsOn(tomcat)
            ;

    @ClassRule
    public static GenericContainer<?> nodeChrome = new GenericContainer<>(
        DockerImageName.parse("selenium/node-chrome:3.141.59"))
        .withNetwork(net)
        .withNetworkAliases("node-chrome")
        .withEnv("HUB_HOST", "hub")
        .dependsOn(selenium)
            ;

    @ClassRule
    public static GenericContainer<?> sideex = new GenericContainer<>(
        new ImageFromDockerfile()
            .withFileFromClasspath("Dockerfile", RESOURCE_PATH_DOCKER)
            .withFileFromClasspath("tests/prepare-test/config.json", RESOURCE_PATH_CONFIG)
    )
        .withNetwork(net)
        .withNetworkAliases("sideex")
        .withExposedPorts(50000)
        .waitingFor(Wait.forLogMessage(".*SideeX WebService is up and running.*\\s", 1)
            .withStartupTimeout(Duration.ofMinutes(2)))
        .dependsOn(nodeChrome)
            ;

    static void makeConnection(URL resource)
        throws URISyntaxException, IOException, ParseException, InterruptedException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        File file = new File(resource.toURI());
        Map<String, File> fileParams = new HashMap<String, File>();
        fileParams.put(file.getName(), file);

        String port = sideex.getFirstMappedPort().toString();
        String url = "http://" + sideex.getHost() + ":" + port + "/sideex-webservice/";

        HttpPost runTestSuitesPost = new HttpPost(url + "runTestSuites");
        HttpEntity data = MultipartEntityBuilder
            .create()
            .setMode(HttpMultipartMode.EXTENDED)
            .addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName())
            .build();
        runTestSuitesPost.setEntity(data);

        CloseableHttpResponse response = httpclient.execute(runTestSuitesPost);
        String body = EntityUtils.toString(response.getEntity());
        _log.info("Executing request " + body);

        JSONObject jsonRunSuite = new JSONObject(body);
        String token = jsonRunSuite.getString("token");
        _log.info("Json token: " + token);

        runningTests(httpclient, url, token, response);
    }

    static void runningTests(CloseableHttpClient httpclient, String url, String token, CloseableHttpResponse response)
        throws IOException, ParseException, InterruptedException {

        boolean flag = false;
        while (!flag) {
            HttpGet getStateGet = new HttpGet(url + "getState?token=" + token);

            response = httpclient.execute(getStateGet);

            String stateSt = EntityUtils.toString(response.getEntity());
            _log.info("Test status " + stateSt);

            JSONObject jsonState = new JSONObject(stateSt);

            String state = jsonState.getJSONObject("webservice").getString("state");
            if (!state.equals("complete") && !state.equals("error")) {
                _log.info(state);
                Thread.sleep(4000);
            } else if (state.equals("error")) {
                _log.info(state);
                flag = true;
            } else {
                _log.info(state);
                flag = true;

                SideexReportParser reportParser = new SideexReportParser(new URL(url), token);
                _log.info(reportParser.getLog() + "\n");
                if (reportParser.getCountCaseFailed() != 0) {
                    throw new RuntimeException("Some of the cases was failed!");
                }
                reportParser.close();
            }
        }
    }

    @Test
    public void openSideex() throws IOException, InterruptedException, URISyntaxException, ParseException {
        URL resource = getClass().getClassLoader().getResource("test-cases.zip");
        makeConnection(resource);
    }
}
