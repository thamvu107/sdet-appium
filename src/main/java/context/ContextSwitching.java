package context;

import enums.Contexts;
import enums.Platform;
import exceptions.PlatformNotSupportException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import utils.PlatformUtil;
import utils.WaitUtils;

import java.util.Set;

public class ContextSwitching {

    private final AppiumDriver driver;
    private final WaitUtils waitUtils;

    private final Platform currentPlatform;

    public ContextSwitching(AppiumDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
        this.currentPlatform = new PlatformUtil(this.driver).getCurrentPlatform();
    }

    public Boolean hasMultiContexts(AppiumDriver driver) {
        try {
            return waitUtils.explicitWait()
                    .until(ContextExpectedConditions.hasMultipleContexts(driver));
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean hasMultiContexts(AppiumDriver driver, long timeoutInSeconds) {
        try {
            return waitUtils.createWebDriverWait(timeoutInSeconds)
                    .until(ContextExpectedConditions.hasMultipleContexts(driver));
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean hasWebViewContext(AppiumDriver driver, long timeoutInSeconds) {
        try {
            return waitUtils.createWebDriverWait(timeoutInSeconds)
                    .until(expectedConditionHasContext(driver, Contexts.WEBVIEW));
        } catch (Exception e) {
            return false;
        }
    }

    public WebDriver switchToNativeContext(long timeoutInSeconds) {
        return switchToContext(Contexts.NATIVE);
    }

    public WebDriver switchToWebViewContext(long timeoutInSeconds) {
        if (hasWebViewContext(driver, timeoutInSeconds)) {
            return switchToContext(Contexts.WEBVIEW);
        }

        return null;
    }

    private WebDriver switchToContext(Contexts context) {

        String expectedContextName = context.getContextName();
        Set<String> contexts;
        WebDriver webDriver;
        switch (currentPlatform) {
            case ANDROID:
                webDriver = ((AndroidDriver) driver).context(expectedContextName);
                break;
            case IOS:
                webDriver = ((IOSDriver) driver).context(expectedContextName);
                break;
            default:
                throw new PlatformNotSupportException("Platform not supported: " + currentPlatform);
        }

        return webDriver;
    }


    private ExpectedCondition<Boolean> expectedConditionHasContext(AppiumDriver driver, Contexts context) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Set<String> contexts;
                String contextName = context.getContextName();
                switch (currentPlatform) {
                    case ANDROID:
                        contexts = ((AndroidDriver) driver).getContextHandles();
                        return contexts.size() > 1 && contexts.contains(contextName);
                    case IOS:
                        contexts = ((IOSDriver) driver).getContextHandles();
                        return contexts.size() > 1 && contexts.contains(contextName);
                    default:
                        throw new PlatformNotSupportException("Platform not supported: " + currentPlatform);
                }
            }


            public String toString() {
                return String.format("%s platform is more than one context", currentPlatform);
            }
        };
    }

    public String getCurrentContext(AppiumDriver driver) {
        switch (currentPlatform) {
            case ANDROID:
                return ((AndroidDriver) driver).getContext();
            case IOS:
                return ((IOSDriver) driver).getContext();
            default:
                throw new PlatformNotSupportException("Platform not supported: " + currentPlatform);
        }
    }
}
