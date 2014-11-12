package com.redditspider.dao.reddit.web;

import static com.redditspider.model.DomainFactory.aLinkWithId;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.dao.reddit.web.parser.Parser;
import com.redditspider.dao.reddit.web.parser.ParserFactory;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import com.redditspider.model.reddit.WebSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(MockitoJUnitRunner.class)
public class RedditWebDaoTest {
    private RedditWebDao dao;
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
    private RedditWebAuthenticator redditWebAuthenticator;
    @Mock
    private ParserFactory parserFactory;
    @Mock
    private Parser parser;


    @Before
    public void before() {
        this.dao = new RedditWebDao();
        this.dao.webBrowserPool = webBrowserPool;
        this.dao.authenticator = redditWebAuthenticator;
        this.dao.parserFactory = parserFactory;
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
    public void searchMultiple() {
        // given
        WebSearchResult result1 = new WebSearchResult();
        result1.setNextPage("nextPage");
        result1.getLinks().add(aLinkWithId("11"));
        result1.getLinks().add(aLinkWithId("22"));
        WebSearchResult result2 = new WebSearchResult();
        result2.getLinks().add(aLinkWithId("33"));

        given(webBrowserPool.get()).willReturn(webBrowser);
        given(webBrowser.getDriver()).willReturn(driver);
        given(redditWebAuthenticator.isLoggedIn(driver)).willReturn(true);
        given(parserFactory.getParser(driver)).willReturn(parser);
        given(parser.parse()).willReturn(result1, result2);

        // when
        SearchResult actual = dao.search(new SearchQuery("test"));

        // then
        verify(webBrowserPool, times(1)).get();
        verify(parser, times(2)).parse();
        assertThat(actual.getLinks(), hasSize(3));
        assertThat(actual.getLinks().get(2).getId(), is(equalTo("33")));
    }

    @Test
    public void doSearchEmptyHtml() {
        // given
        given(query.getSearchUri()).willReturn("test_uri");
        given(webBrowser.getDriver()).willReturn(driver);
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.findElements(isA(By.class))).willReturn(null);

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
        given(redditWebAuthenticator.isLoggedIn(driver)).willReturn(false);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        SearchResult actual = dao.doSearch("test_uri", driver);

        // then
        verify(redditWebAuthenticator).login(driver);
        assertThat(actual.getLinks(), empty());
    }

    @Test
    public void doSearchDontLogin() {
        // given
        given(redditWebAuthenticator.isLoggedIn(driver)).willReturn(true);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        SearchResult actual = dao.doSearch("test_uri", driver);

        // then
        verify(redditWebAuthenticator, never()).login(driver);
        assertThat(actual.getLinks(), empty());
    }
}
