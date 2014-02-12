package com.redditspider.dao.browser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@RunWith(MockitoJUnitRunner.class)
public class WebBrowserTest {

    @Test
    public void initWebBrowser() throws Exception {
        // given
        WebDriver driver = new HtmlUnitDriver();

        // when
        WebBrowser actual = new WebBrowser(driver);

        // then
        assertTrue(actual.isAvailable());

        // and when
        actual.close();

        // then
        assertFalse(actual.isAvailable());
    }
}
