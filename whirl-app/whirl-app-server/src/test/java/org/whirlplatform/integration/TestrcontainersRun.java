package org.whirlplatform.integration;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

public class TestrcontainersRun {

    private final String contextFile = "../../docker/conf/postgresql/context.xml.default";
    private final String pathNameWar = "target/whirl-app-server-0.3.0-SNAPSHOT.war";

    String appPath = "../../.whirl-work/";

    private MountableFile warFile = MountableFile.forHostPath(
            Paths.get(pathNameWar), 0777);
    private MountableFile ctxFile = MountableFile.forHostPath(
            Paths.get(contextFile), 0777);

    private MountableFile app = MountableFile.forHostPath(
            Paths.get(appPath), 0777);
    private Network net = Network.newNetwork();
    private DockerImageName POSTGRES_TEST_IMAGE = DockerImageName.parse("postgres:10");
    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_TEST_IMAGE)
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
            .withCopyToContainer(warFile, "/usr/local/tomcat/webapps/ROOT.war")
            .withCopyToContainer(ctxFile, "/usr/local/tomcat/conf/Catalina/localhost/context.xml.default")
            .waitingFor(Wait.forLogMessage(".* Server startup .*\\s", 1))
            .withCopyToContainer(app, "/usr/local/whirl")
            .dependsOn(postgres);
    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withNetwork(net)
            .withNetworkAliases("chrome")
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
                    //Paths.get("C:/Users/Nastia/Documents").toFile());
                    Paths.get("C:/Users/User/Documents").toFile());

//    @Test
//    public void whenNavigatedToPage_thenHeadingIsInThePage() throws InterruptedException, IOException {
//        postgres.execInContainer("psql", "-U", "postgres", "-c", "insert into whirl_user_groups(id, r_whirl_users, group_code) values(5,1,'whirl-admin')");
//
//        String tomcatHost = tomcat.getHost();
//
//        Integer tomcatPort = tomcat.getMappedPort(8080);
//        final String rootUrl = String.format("http://%s:%d/", tomcatHost, tomcatPort);
//        System.out.println(rootUrl);
//        RemoteWebDriver driver = chrome.getWebDriver();
//        driver.get("http://tomcat:8080/");
//
//        String heading = driver.getTitle();
//        while (heading.isEmpty()) {
//            Thread.sleep(100);
//        }
//        //Thread.sleep(1000000);
//        assertEquals("Whirl Platform", heading);
//        System.out.println("Successfully enter into the platform");
//    }

    @Test
    public void loginAndOpenApplicationTest() throws InterruptedException {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://tomcat:8080/app?application=whirl-showcase");

        String heading = driver.getTitle();
        while (heading.isEmpty()) {
            Thread.sleep(100);
        }
        System.out.println("current title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());

        System.out.println(driver.getPageSource());
        System.out.println("\n\n\n");

//        WebElement loginTextField = driver.findElementByXPath(
//                "//html/body/div[3]/form/table/tbody/tr[1]/td[2]/input"
//        );
//        assertNotNull(loginTextField);
//        System.out.println("Login field text: " + loginTextField.getText());
//        //assertEquals(loginTextField.getText(), "whirl");
//
//        WebElement passwordTextField = driver.findElementByXPath(
//                "//html/body/div[3]/form/table/tbody/tr[2]/td[2]/input"
//        );
//        assertNotNull(passwordTextField);
        //System.out.println("Password field text: " + passwordTextField.getText());
        //assertEquals(passwordTextField.getText(), "password");

        driver.findElement(By.name("login-field")).sendKeys("whirl");
        driver.findElement(By.name("pwd-field")).sendKeys("password");

        WebElement submitButton = driver.findElementByXPath(
                "//html/body/div[3]/form/table/tbody/tr[3]/td[2]/input"
        );
        System.out.println("Button text: " + submitButton.getAttribute("value"));

        //loginTextField.sendKeys("whirl");
        //passwordTextField.sendKeys("password");

        System.out.println("Finally:");
        driver.executeScript("arguments[0].click();", submitButton);
        System.out.println("Current URL: " + driver.getCurrentUrl());
        Thread.sleep(1000000);
    }

//    @Test
//    public void comboboxApplicationTest() throws InterruptedException {
//        RemoteWebDriver driver = chrome.getWebDriver();
//        driver.get("http://tomcat:8080/app?application=combobox_test");
//
//        String heading = driver.getTitle();
//        while (heading.isEmpty()) {
//            Thread.sleep(100);
//        }
//        System.out.println("current title: " + driver.getTitle());
//        System.out.println("Current URL: " + driver.getCurrentUrl());
//
//        Thread.sleep(1000000);
//
//        //assertTrue(true);
//    }

}
