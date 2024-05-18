package practice;

import constants.WaitConstant;
import driver.Platforms;
import driverFactory.Driver;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import server.ServerConfig;

import java.time.Duration;

import static constants.ServerConstants.LOCAL_SERVER_IP;
import static constants.ServerConstants.SERVER_PORT;

public abstract class BaseTest {

    protected AppiumDriver driver;
    protected static WebDriverWait wait;
    protected static FluentWait<AppiumDriver> fluentWait;

    @BeforeClass
    public void setUpAppium() {

        driver = Driver.getDriver(new ServerConfig(LOCAL_SERVER_IP, SERVER_PORT), Platforms.ANDROID);
        wait = new WebDriverWait(driver, Duration.ofMillis(WaitConstant.SHORT_EXPLICIT_WAIT));
        fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMillis(7000))
                .pollingEvery(Duration.ofMillis(10))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);

    }

    @AfterClass
    public void tearDown() {
        Driver.quitDriver(driver);
    }

}
