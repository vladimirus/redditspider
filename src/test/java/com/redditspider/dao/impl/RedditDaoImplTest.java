package com.redditspider.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(MockitoJUnitRunner.class)
public class RedditDaoImplTest {
    private RedditDaoImpl dao;
    @Mock
    private SearchQuery query;
    @Mock
    private WebBrowserPool webBrowserPool;
    @Mock
    private WebBrowser webBrowser;
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement webElement;
    @Mock
    private RedditAuthenticator redditAuthenticator;

    @Before
    public void before() {
        this.dao = new RedditDaoImpl();
        this.dao.webBrowserPool = webBrowserPool;
        this.dao.authenticator = redditAuthenticator;
    }

    @Test
    public void searchWhenQueryIsEmpty() {
        // given
        given(query.getSearchUri()).willReturn("");

        // when
        SearchResult actual = dao.search(query);

        // then
        assertNotNull(actual);
        assertEquals(0, actual.getLinks().size());
    }

    @Test
    public void searchWhenQueryIsNull() {
        // given
        given(query.getSearchUri()).willReturn(null);

        // when
        SearchResult actual = dao.search(query);

        // then
        assertNotNull(actual);
        assertEquals(0, actual.getLinks().size());
    }

    @Test
    public void searchWhenNoBrowserAvailable() {
        // given
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowserPool.get()).willReturn(null);

        // when
        SearchResult actual = dao.search(query);

        // then
        assertEquals(0, actual.getLinks().size());
        verify(webBrowserPool).get();
    }

    @Test
    public void doSearchEmptyHtml() {
        // given
        SearchResult searchResult = new SearchResult();
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowser.getDriver()).willReturn(driver);
        given(driver.findElement(Mockito.isA(By.class))).willReturn(webElement);
        given(webElement.findElements(Mockito.isA(By.class))).willReturn(null);

        // when
        dao.doSearch("test_uri", searchResult, driver);

        // then
        verify(driver).get("test_uri");
    }

    @Test
    public void doSearchException() {
        // given
        SearchResult searchResult = new SearchResult();
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowser.getDriver()).willReturn(null); //NullPointerException will be raised

        // when
        dao.doSearch("test_uri", searchResult, null);

        // then
        // no exception
    }

    @Test
    public void doSearchLogin() {
        // given
        SearchResult searchResult = new SearchResult();
        given(redditAuthenticator.isLoggedIn(driver)).willReturn(false);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        dao.doSearch("test_uri", searchResult, driver);

        // then
        verify(redditAuthenticator).login(driver);
    }

    @Test
    public void doSearchDontLogin() {
        // given
        SearchResult searchResult = new SearchResult();
        given(redditAuthenticator.isLoggedIn(driver)).willReturn(true);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        dao.doSearch("test_uri", searchResult, driver);

        // then
        verify(redditAuthenticator, never()).login(driver);
    }
}
