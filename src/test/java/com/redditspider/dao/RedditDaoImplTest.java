package com.redditspider.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.io.ClassPathResource;

import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.dao.browser.WebBrowserPoolImpl;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

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

    @Test
    public void doSearchFromStaticFile() {
        //given
        SearchResult searchResult = new SearchResult();
        given(query.getSearchUri()).willReturn(getFileLocation("reddit-html/reddit-01.html"));
        WebBrowserPoolImpl webBrowserPool = new WebBrowserPoolImpl();
        webBrowserPool.setDefaultWebClient(new HtmlUnitDriver());
        WebBrowser browser = webBrowserPool.get();

        // when
        dao.doSearch(query, searchResult, browser);

        // then
        assertEquals(25, searchResult.getLinks().size());
        assertEquals("http://www.reddit.com/?count=25&after=t3_1toimn", searchResult.getNextPage());

        assertEquals(Integer.valueOf(3405), searchResult.getLinks().get(5).getDown());
        assertEquals(Integer.valueOf(6126), searchResult.getLinks().get(5).getUp());
        assertEquals("The true meaning of Christmas", searchResult.getLinks().get(5).getText());
        assertEquals("http://i.imgur.com/lOqtfFN.png", searchResult.getLinks().get(5).getUri());
        assertNull(searchResult.getLinks().get(5).getId());
        assertEquals(dateFromString("2013-12-25T05:42:42-08:00"), searchResult.getLinks().get(5).getCreated());
    }

    private Date dateFromString(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (Exception ignore) {
            fail("Can't convert date");
        }
        return date;
    }

    private String getFileLocation(String filename) {
        String filelocation = null;
        ClassPathResource resource = new ClassPathResource(filename);
        try {
            File file = resource.getFile();
            if (file.exists()) {
                filelocation = "file://" + file.getAbsolutePath();
            }
        } catch (Exception ignore) {
            fail("Can't find the file");
        }

        return filelocation;
    }
}
