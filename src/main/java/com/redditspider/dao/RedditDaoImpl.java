package com.redditspider.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@Repository
public class RedditDaoImpl implements RedditDao {
	private final transient Logger log = Logger.getLogger(this.getClass());

	@Autowired
	WebBrowserPool webBrowserPool;
	
	public SearchResult search(SearchQuery query) {
		SearchResult searchResult = new SearchResult();
		if (query != null && StringUtils.hasText(query.getSearchUri())) {
			WebBrowser browser = webBrowserPool.get();
			if (browser != null) {
				doSearch(query, searchResult, browser);
			}
		}
		return searchResult;
	}

	void doSearch(SearchQuery query, SearchResult searchResult, WebBrowser browser) {
		WebDriver driver = browser.getDriver();
		driver.get(query.getSearchUri());
		WebElement siteTable = driver.findElement(By.id("siteTable"));
		processLinks(searchResult, siteTable.findElements(By.className("link")));
		processNextUri(searchResult, siteTable.findElement(By.cssSelector("span.nextprev a")));
	}

	private void processLinks(SearchResult searchResult, List<WebElement> links) {
		if (links != null && links.size() > 0) {
			for (WebElement rawLink : links) {
				Link link = null;
				try {
					if (rawLink.isDisplayed()) {
						link = processLink(rawLink);
					}
				} catch (Exception ignore) {
					log.debug("Can't parse link, ignoring...", ignore);
				}

				if (link != null && StringUtils.hasText(link.getUri())) {
					searchResult.getLinks().add(link);
				}
			}
		}
	}

	private Link processLink(WebElement rawLink) {
		Link link = new Link();
		WebElement rawRank = rawLink.findElement(By.className("rank"));
		String rank = rawRank.getText();
		
		if (StringUtils.hasText(rank)) {
			Integer down = NumberUtils.parseNumber(rawLink.getAttribute("data-downs"), Integer.class);
			Integer up = NumberUtils.parseNumber(rawLink.getAttribute("data-ups"), Integer.class);
			WebElement rawEntry = rawLink.findElement(By.className("entry"));
			WebElement rawTitle = rawEntry.findElement(By.cssSelector("a.title"));
			String uri = rawTitle.getAttribute("href");
			String text = rawTitle.getText();
			
			if (StringUtils.hasText(uri) && StringUtils.hasText(text)) {
				link.setDown(down);
				link.setUp(up);
				link.setUri(uri);
				link.setText(text);
			}
		}
		return link;
	}
	
	private void processNextUri(SearchResult searchResult, WebElement nextUri) {
		if (nextUri != null) {
			String uri = nextUri.getAttribute("href");
			if (StringUtils.hasText(uri)) {
				searchResult.setNextPage(uri);
			}
		}
	}
}
