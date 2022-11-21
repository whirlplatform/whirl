package org.whirlplatform.integration;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

public class ApplicationsTests {
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
        System.out.println();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        // this does not work in the executeScript
//        WebElement submitButton = driver.findElementByXPath(
//                "//html/body/div[3]/form/table/tbody/tr[3]/td[2]/input"
//        );
        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("submit-btn")));
        //WebElement submitButton = driver.findElement(By.name("submit-btn"));
        System.out.println("Button text: " + submitButton.getAttribute("value"));

        // not visible
//        WebElement loginTextField = driver.findElementByXPath(
//                "//html/body/div[3]/form/table/tbody/tr[1]/td[2]/input"
//        );
//        assertNotNull(loginTextField);

        // empty
//        System.out.println("Login field text: " + loginTextField.getText());

    // not visible
//        WebElement passwordTextField = driver.findElementByXPath(
//                "//html/body/div[3]/form/table/tbody/tr[2]/td[2]/input"
//        );
//        assertNotNull(passwordTextField);

        // not visible
        //driver.findElement(By.name("login-field")).sendKeys("whirl");
        //driver.findElement(By.name("pwd-field")).sendKeys("password");

        // not visible
        //loginTextField.sendKeys("whirl");
        //passwordTextField.sendKeys("password");


        //WebElement passwordTextField = driver.findElement(By.name("pwd-field"));
        WebElement passwordTextField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pwd-field")));
        passwordTextField.sendKeys("password");

        String tagName = passwordTextField.getTagName();
        System.out.println("Tagname: " + tagName);

        System.out.println("Script started:");
        driver.executeScript("arguments[0].click();", submitButton);
        System.out.println("Script completed:");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("\n\n");
        Thread.sleep(4000);
        System.out.println(driver.getPageSource());
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