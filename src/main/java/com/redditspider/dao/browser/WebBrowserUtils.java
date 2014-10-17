package com.redditspider.dao.browser;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.firefox.FirefoxDriver.PROFILE;
import static org.openqa.selenium.remote.DesiredCapabilities.firefox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Utils for webBrowser.
 */
public final class WebBrowserUtils {

    private WebBrowserUtils() {
        // do not instantiate
    }

    public static WebDriver aFirefoxDriver() {
        int domMaxChromeScriptRunTime = 4500;
        int domMaxScriptRunTime = 3500;

        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("dom.max_chrome_script_run_time", domMaxChromeScriptRunTime);
        firefoxProfile.setPreference("dom.max_script_run_time", domMaxScriptRunTime);

        DesiredCapabilities capabilities = firefox();
        capabilities.setCapability(PROFILE, firefoxProfile);

        WebDriver firefoxDriver = new FirefoxDriver(capabilities);
        firefoxDriver.manage().timeouts().implicitlyWait(60, SECONDS);
        firefoxDriver.manage().timeouts().pageLoadTimeout(60, SECONDS);
        firefoxDriver.manage().timeouts().setScriptTimeout(60, SECONDS);
        return firefoxDriver;
    }
}
