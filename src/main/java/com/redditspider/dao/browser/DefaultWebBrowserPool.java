package com.redditspider.dao.browser;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;


/**
 * Default implementation of the browser pool.
 */
@Component
public class DefaultWebBrowserPool implements WebBrowserPool {
    private static final transient Logger LOG = Logger.getLogger(DefaultWebBrowserPool.class);

    Integer numberOfAttempts = 10;
    Integer secondsToSleepWhileAttempt = 10;
    Integer totalNumberOfWebBrowsers = 1000;
    List<WebBrowser> pool = newArrayList();
    WebDriver defaultWebClient;

    @Override
    public synchronized WebBrowser get() {
        WebBrowser browser = null;

        for (int i = 0; i < numberOfAttempts; i++) {
            browser = getWebBrowserFromPool();

            if (browser != null) {
                break;
            }

            sleepUninterruptibly(secondsToSleepWhileAttempt, SECONDS);
        }

        return browser;
    }

    @Override
    public void release(WebBrowser browser) {
        browser.setAvailable(true);
    }

    @Override
    @PreDestroy
    public void closeAll() {
        LOG.debug("Closing all browsers...");
        for (WebBrowser br : pool) {
            close(br);
        }

        pool.clear();
    }

    @Override
    public void close(WebBrowser browser) {
        try {
            browser.setAvailable(false);
            pool.remove(browser);
            browser.close();
        } catch (Exception e) {
            LOG.error("error closing", e);
        }
    }

    private WebBrowser getWebBrowserFromPool() {
        WebBrowser browser = null;
        int count = 0;

        Set<WebBrowser> cleanUp = newHashSet();
        for (WebBrowser br : pool) {
            count++;
            if (!toCloseWebBrowser(br)) {
                if (br.isAvailable()) {
                    browser = br;
                    break;
                }
            } else {
                cleanUp.add(br);
            }
        }

        closeWebBrowsers(cleanUp);

        if (browser == null && count < totalNumberOfWebBrowsers) {
            browser = createWebBrowser();
            pool.add(browser);
        }

        if (browser != null) {
            browser.setAvailable(false);
        }
        return browser;
    }

    private void closeWebBrowsers(Set<WebBrowser> cleanUp) {
        if (cleanUp != null && !cleanUp.isEmpty()) {
            for (WebBrowser br : cleanUp) {
                close(br);
            }
        }
    }

    private boolean toCloseWebBrowser(WebBrowser browser) {
        boolean expired = false;

        if (browser.isExpired()) {
            expired = true;
        } else if (browser.getDriver() == null) {
            expired = true;
        }

        return expired;
    }

    private WebBrowser createWebBrowser() {
        WebBrowser browser = null;
        try {
            browser = new WebBrowser(defaultWebClient);
        } catch (Exception e) {
            LOG.error(e);
        }
        return browser;
    }
}
