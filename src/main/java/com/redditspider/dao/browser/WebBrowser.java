package com.redditspider.dao.browser;

import static com.redditspider.dao.browser.WebBrowserUtils.aFirefoxDriver;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.getInstance;

import org.openqa.selenium.WebDriver;

import java.util.Calendar;
import java.util.Date;

/**
 * WebBrowser encapsulates WebDriver.
 */
public class WebBrowser {
    private final WebDriver driver;
    Date created = new Date();
    private boolean available = true;

    /**
     * Instantiates WebBrowser and webdriver.
     *
     * @param driver - WebDriver to use
     */
    public WebBrowser(WebDriver driver) {
        if (driver == null) {
            this.driver = aFirefoxDriver();
        } else {
            this.driver = driver;
        }
    }

    /**
     * Stops everything.
     *
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

    void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isExpired() {
        Calendar timeFromCreated = getInstance();
        timeFromCreated.setTime(created);
        timeFromCreated.add(HOUR, 1);

        Date now = new Date();
        return now.after(timeFromCreated.getTime());
    }
}
