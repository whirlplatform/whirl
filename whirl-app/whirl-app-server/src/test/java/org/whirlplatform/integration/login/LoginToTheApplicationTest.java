package org.whirlplatform.integration.login;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginToTheApplicationTest {

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
                    "/docker-entrypoint-initdb.d/")
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
            .withCopyToContainer(app, "/usr/local/whirl")
            .dependsOn(postgres)
            ;

    @Rule
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withAccessToHost(true)
            .withNetwork(net)
            .withNetworkAliases("chrome")
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
                    //Paths.get("C:/Users/Nastia/Documents").toFile());
                    Paths.get("C:/Users/User/Documents").toFile())
            ;

    @Test
    public void loginAndOpenApplicationTest() throws InterruptedException {
        RemoteWebDriver driver = chrome.getWebDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://tomcat:8080/app?application=whirl-showcase");

        wait.until(ExpectedConditions.titleIs("Whirl Platform"));

        WebElement loginTextField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("login-field")));
        loginTextField.sendKeys("whirl-admin");

        WebElement passwordTextField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pwd-field")));
        passwordTextField.sendKeys("password");

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("submit-btn")));
        driver.executeScript("arguments[0].click();", submitButton);

        wait.until(ExpectedConditions.titleIs("Showcase"));
        System.out.println("Current title: " + driver.getTitle());
        assertEquals(driver.getTitle(), "Showcase");

        waitForLoad(driver);
        System.out.println("\n\n" + driver.getPageSource());

        WebElement showcaseButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("EWFIG3-s-r")));
        assertNotNull(showcaseButton);
        Thread.sleep(1000000);
    }

    // @Order(1)

    void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}