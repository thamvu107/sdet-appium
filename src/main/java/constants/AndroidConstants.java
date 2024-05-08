package constants;

import java.time.Duration;

import static java.time.Duration.ofMillis;

public interface AndroidConstants {
    String APP_PACKAGE = "com.wdiodemoapp";
    String APP_ACTIVITY = "com.wdiodemoapp.MainActivity";
    String APP_FILE_NAME = "android.wdio.native.app.v1.0.8.apk";
    String PLATFORM_VERSION = "14";
    String DEVICE_NAME = "Pixel_5_API_34";
    String ADV = "Pixel_5_API_34";
    Duration UIAUTOMATOR2_SERVER_LAUNCH_TIMEOUT = ofMillis(40000);
    Duration UIAUTOMATOR2_SERVER_INSTALL_TIMEOUT = ofMillis(60000);
    Duration ADV_TIMEOUT = ofMillis(40000);

}