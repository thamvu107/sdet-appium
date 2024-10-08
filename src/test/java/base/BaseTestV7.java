package base;

import driverFactory.DriverFactoryV7;
import enums.DeviceType;
import enums.PlatformType;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

@Slf4j
public abstract class BaseTestV7 {

  protected AppiumDriver driver;

  @BeforeSuite
  public void beforeSuite() {

    MDC.put("logDir", "logs");
  }

  @BeforeMethod(alwaysRun = true)
  @Parameters({"platformType", "deviceType", "configureFile"})
  public void setUp(String platformType, String deviceType, String configureFile) {

    setLogParams(platformType, deviceType, configureFile);

    DriverFactoryV7 driverFactory =
      new DriverFactoryV7(PlatformType.valueOf(platformType), DeviceType.valueOf(deviceType), configureFile);
    driver = driverFactory.createDriver();
  }

  @AfterMethod(alwaysRun = true)
  public void tearDown() {

    MDC.clear(); // Mapped Diagnostic Context

    driver.quit();

//
  }

  protected void setLogParams(String platformType, String deviceType, String configureFile) {
    MDC.put("baseTest PlatformType:: ", platformType);
    MDC.put("baseTest PlatformType:: ", deviceType);
    MDC.put("baseTest PlatformType:: ", configureFile);
  }

  private static String convertDeviceName(String deviceName) {
    System.out.println(deviceName);
    if (deviceName == null || deviceName.isEmpty()) {
      throw new IllegalArgumentException("Device name cannot be null or empty");
    }
    return deviceName.replaceAll(" ", "_");
  }

}
