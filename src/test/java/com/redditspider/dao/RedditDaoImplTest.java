package com.redditspider.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
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
	
	@Before
	public void before() {
		this.dao = new RedditDaoImpl();
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
	
	@Ignore
	@Test
	public void searchWhenQueryIsCorrect() {
		// given
		given(query.getSearchUri()).willReturn("test_uri");
		given(webBrowserPool.get()).willReturn(webBrowser);
		
		// when
		SearchResult actual = dao.search(query);
		
		// then
		assertEquals(2, actual.getLinks().size());
	}
	
	@Ignore
	@Test
	public void doSearch() {
		// given
		SearchResult searchResult = new SearchResult();
		given(query.getSearchUri()).willReturn("test_uri");
		given(webBrowser.getDriver()).willReturn(driver);
		
		// when
		dao.doSearch(query, searchResult, webBrowser);
		
		// then
		verify(driver).get("test_uri");
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
	}

	private String getFileLocation(String filename) {
		String filelocation = null;
		ClassPathResource resource = new ClassPathResource(filename);
		try {
			File file = resource.getFile();
			if (file.exists()) {
				filelocation = "file://" + file.getAbsolutePath();
			}
		} catch (Exception ignore) { }
		
		return filelocation;
	}
}
