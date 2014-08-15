package com.redditspider.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
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

    @Before
    public void before() {
        this.dao = new RedditDaoImpl();
        this.dao.webBrowserPool = webBrowserPool;
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
        dao.doSearch(query, searchResult, webBrowser);

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
        dao.doSearch(query, searchResult, webBrowser);

        // then
        // no exception
    }
}
