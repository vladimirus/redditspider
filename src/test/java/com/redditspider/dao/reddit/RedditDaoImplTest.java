package com.redditspider.dao.reddit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
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
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getLinks(), empty());
    }

    @Test
    public void searchWhenQueryIsNull() {
        // given
        given(query.getSearchUri()).willReturn(null);

        // when
        SearchResult actual = dao.search(query);

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getLinks(), empty());
    }

    @Test
    public void searchWhenNoBrowserAvailable() {
        // given
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowserPool.get()).willReturn(null);

        // when
        SearchResult actual = dao.search(query);

        // then
        assertThat(actual.getLinks(), empty());
        verify(webBrowserPool).get();
    }

    @Test
    public void doSearchEmptyHtml() {
        // given
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowser.getDriver()).willReturn(driver);
        given(driver.findElement(Mockito.isA(By.class))).willReturn(webElement);
        given(webElement.findElements(Mockito.isA(By.class))).willReturn(null);

        // when
        SearchResult actual = dao.doSearch("test_uri", driver);

        // then
        verify(driver).get("test_uri");
        assertThat(actual.getLinks(), empty());
    }

    @Test
    public void doSearchException() {
        // given
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowser.getDriver()).willReturn(null); //NullPointerException will be raised

        // when
        SearchResult actual = dao.doSearch("test_uri", null);

        // then
        // no exception
        assertThat(actual.getLinks(), empty());
    }

    @Test
    public void doSearchLogin() {
        // given
        given(redditAuthenticator.isLoggedIn(driver)).willReturn(false);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        SearchResult actual = dao.doSearch("test_uri", driver);

        // then
        verify(redditAuthenticator).login(driver);
        assertThat(actual.getLinks(), empty());
    }

    @Test
    public void doSearchDontLogin() {
        // given
        given(redditAuthenticator.isLoggedIn(driver)).willReturn(true);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        SearchResult actual = dao.doSearch("test_uri", driver);

        // then
        verify(redditAuthenticator, never()).login(driver);
        assertThat(actual.getLinks(), empty());
    }
}
