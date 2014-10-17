package com.redditspider.dao.browser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebBrowserUtilsTest {

    @Test
    public void shouldReturnFirefoxDriver() throws Exception {

        // when
        WebDriver actual = WebBrowserUtils.aFirefoxDriver();

        // then
        assertThat(actual, instanceOf(FirefoxDriver.class));

        // clean up
        actual.quit();
    }
}