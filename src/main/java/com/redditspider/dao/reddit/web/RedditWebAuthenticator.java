package com.redditspider.dao.reddit.web;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Keys.RETURN;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Deals with reddit's login.
 */
@Service
public class RedditWebAuthenticator {
    private static final transient Logger LOG = Logger.getLogger(RedditWebAuthenticator.class);
    @Value("${ls.reddit.name}")
    String name;
    @Value("${ls.reddit.pass}")
    String password;

    Integer afterLoginSleepSeconds = 5;

    public boolean isLoggedIn(WebDriver driver) {
        boolean result;
        try {
            WebElement username = driver.findElement(By.cssSelector("div#header-bottom-right span.user a"));
            result = username.getText().contains(name);
        } catch (Exception ignore) {
            result = false;
        }
        return result;
    }

    public void login(WebDriver driver) {
        try {
            driver.get("https://www.reddit.com/login");
            driver.findElement(By.id("user_login")).sendKeys(name);
            driver.findElement(By.id("passwd_login")).sendKeys(password);
            driver.findElement(By.id("passwd_login")).sendKeys(RETURN);
            sleepUninterruptibly(afterLoginSleepSeconds, SECONDS);
        } catch (Exception ignore) {
            LOG.error(ignore);
        }
    }
}
