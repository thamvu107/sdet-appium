package driver;

import enums.DeviceType;
import enums.PlatformType;
import exceptions.PlatformNotSupportException;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import utils.ServerURLUtil;
import utils.WaitUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class DriverFactoryV2 {

  private URL serverURL;
  private AppiumDriver driver;
  private final PlatformType platformType;

  private final DeviceType deviceType;

  private final String configureFile;

  public DriverFactoryV2(PlatformType platformType, DeviceType deviceType, String configureFile) {
    this.platformType = platformType;
    this.deviceType = deviceType;
    this.configureFile = configureFile;
  }

  public AppiumDriver createDriver() {
    if (Objects.isNull(driver)) {

      this.serverURL = ServerURLUtil.getServerURL(); //configureServerURL(isRemote);
//      System.out.println("this.serverURL " + this.serverURL);

      switch (platformType) {
        case ANDROID:
          driver = new AndroidDriverManager().createDriver(serverURL, deviceType, configureFile);
          break;

        case IOS:
          driver = new IOSDriverManager().createDriver(serverURL, deviceType, configureFile);
          break;
        default:
          throw new PlatformNotSupportException("Platform " + platformType + " is not supported");
      }

      new WaitUtils(driver).setImplicitWait(0L);
    }

    return driver;
  }

  private void configureServerURL(String isRemote) {
    if (isRemote == null || !isRemote.equalsIgnoreCase("true")) {
      System.out.println("Remote configuration is not enabled.");
      this.serverURL = ServerURLUtil.getServerURL();
      return;
    }

    // Fetch hub IP address from environment variables or system properties
    String hubIpAddress = System.getenv("hubIpAddress");
    if (hubIpAddress == null) {
      System.out.println("Environment variable 'hubIpAddress' not found. Checking system properties...");
      hubIpAddress = System.getProperty("hubIpAddress");
    }

    // Validate the hub IP address
    if (hubIpAddress == null || hubIpAddress.trim().isEmpty()) {
//      throw new RuntimeException("Please provide a valid 'hub' environment variable or system property!");
      hubIpAddress = "localhost";
    }

    try {
      // Construct and assign the server URL
      this.serverURL = new URL("http://" + hubIpAddress + ":4444");
    } catch (MalformedURLException e) {
      throw new RuntimeException("Failed to construct a valid URL with the provided hub IP address: " + hubIpAddress, e);
    }

    // Log the constructed URL
    //System.out.println("Hub URL: " + this.serverURL);
  }
}
