package com.redditspider.dao.browser;

import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * WebBrowser contains webdriver.
 */
public class WebBrowser {
    Date created = new Date();
    private WebDriver driver;
    private boolean available;
    private final int domMaxChromeScriptRunTime = 4500;
    private final int domMaxScriptRunTime = 3500;

    /**
     * Instantiates proxy server and webdriver.
     * @param driver - webdriver to use
     * @throws Exception - raised if fails to create
     */
    public WebBrowser(WebDriver driver) throws Exception {
        if (driver == null) {
            this.driver = getFirefoxDriver();
        } else {
            this.driver = driver;
        }
        this.available = true;
    }

    private WebDriver getFirefoxDriver() throws Exception {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("dom.max_chrome_script_run_time", domMaxChromeScriptRunTime);
        firefoxProfile.setPreference("dom.max_script_run_time",  domMaxScriptRunTime);
        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
        return new FirefoxDriver(capabilities);
    }

    /**
     * Stops everything.
     * @throws Exception raised if couldnt be closed
     */
    public void close() throws Exception {
        driver.quit();
        available = false;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getCreated() {
        return created;
    }

    public boolean isExpired() {
        boolean isExpired = false;

        Calendar timeFromCreated = Calendar.getInstance();
        timeFromCreated.setTime(created);
        timeFromCreated.add(Calendar.HOUR, 1);

        Date now = new Date();

        if (now.after(timeFromCreated.getTime())) {
            isExpired = true;
        }

        return isExpired;
    }
}
