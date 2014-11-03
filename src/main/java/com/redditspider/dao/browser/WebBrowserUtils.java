package com.redditspider.dao.browser;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.DesiredCapabilities.firefox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Utils for webBrowser.
 */
public final class WebBrowserUtils {

    private WebBrowserUtils() {
        // do not instantiate
    }

    public static WebDriver aFirefoxDriver() {
        WebDriver firefoxDriver = new FirefoxDriver(firefox());
        firefoxDriver.manage().timeouts().implicitlyWait(10, SECONDS);
        firefoxDriver.manage().timeouts().pageLoadTimeout(60, SECONDS);
        firefoxDriver.manage().timeouts().setScriptTimeout(60, SECONDS);
        return firefoxDriver;
    }
}
