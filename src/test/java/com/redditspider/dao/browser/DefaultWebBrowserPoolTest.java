package com.redditspider.dao.browser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWebBrowserPoolTest {
    private DefaultWebBrowserPool webBrowserPool;

    @Before
    public void before() {
        this.webBrowserPool = new DefaultWebBrowserPool();
        this.webBrowserPool.defaultWebClient = new HtmlUnitDriver();
    }

    @Test
    public void getFirstWebBrowser() throws Exception {
        // given
        given();

        // when
        WebBrowser actual = webBrowserPool.get();

        // then
        assertEquals(1, webBrowserPool.pool.size());
        assertFalse(actual.isAvailable());
    }

    @Test
    public void getTwoWebBrowsersWhenOnlyOneAllowed() {
        // given
        given();
        this.webBrowserPool.totalNumberOfWebBrowsers = 1;

        // when
        WebBrowser actual1 = webBrowserPool.get();
        WebBrowser actual2 = webBrowserPool.get();

        // then
        assertEquals(1, webBrowserPool.pool.size());
        assertFalse(actual1.isAvailable());
        assertNull(actual2);
    }

    @Test
    public void getThreeWebBrowsersWhenTwoAllowed() {
        // given
        given();

        // when
        WebBrowser actual1 = webBrowserPool.get();
        WebBrowser actual2 = webBrowserPool.get();
        WebBrowser actual3 = webBrowserPool.get();

        // then
        assertEquals(2, webBrowserPool.pool.size());
        assertFalse(actual1.isAvailable());
        assertFalse(actual2.isAvailable());
        assertNull(actual3);
    }

    @Test
    public void getThreeWebBrowsersWhenTwoAllowedAndOneReleased() {
        // given
        given();

        // when
        WebBrowser actual1 = webBrowserPool.get();
        WebBrowser actual2 = webBrowserPool.get();
        webBrowserPool.release(actual1);
        WebBrowser actual3 = webBrowserPool.get();

        // then
        assertEquals(2, webBrowserPool.pool.size());
        assertFalse(actual1.isAvailable());
        assertFalse(actual2.isAvailable());
        assertSame(actual1, actual3);
    }

    @Test
    public void releaseWebBrowser() {
        // given
        WebBrowser actual1 = webBrowserPool.get();
        WebBrowser actual2 = webBrowserPool.get();

        // when
        webBrowserPool.release(actual1);

        // then
        assertTrue(actual1.isAvailable());
        assertFalse(actual2.isAvailable());
        assertEquals(2, webBrowserPool.pool.size());
    }

    @Test
    public void closeWebBrowser() {
        // given
        WebBrowser actual1 = webBrowserPool.get();
        WebBrowser actual2 = webBrowserPool.get();

        // when
        webBrowserPool.close(actual1);

        // then
        assertFalse(actual1.isAvailable());
        assertFalse(actual2.isAvailable());
        assertEquals(1, webBrowserPool.pool.size());
    }

    @Test
    public void closeAllWebBrowsers() {
        // given
        WebBrowser actual1 = webBrowserPool.get();
        WebBrowser actual2 = webBrowserPool.get();

        // when
        webBrowserPool.closeAll();

        // then
        assertFalse(actual1.isAvailable());
        assertFalse(actual2.isAvailable());
        assertEquals(0, webBrowserPool.pool.size());
    }

    private void given() {
        this.webBrowserPool.totalNumberOfWebBrowsers = 2;
        this.webBrowserPool.numberOfAttempts = 1;
        this.webBrowserPool.millisToSleepWhileAttempt = 1L;
    }
}
