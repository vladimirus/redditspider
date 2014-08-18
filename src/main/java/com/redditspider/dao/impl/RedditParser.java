package com.redditspider.dao.impl;

import static org.springframework.util.StringUtils.hasText;

import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Parsing reddit.
 */
public class RedditParser {
    private static final transient Logger LOG = Logger.getLogger(RedditParser.class);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private final WebDriver driver;
    private final SearchResult searchResult;

    public RedditParser(WebDriver driver, SearchResult searchResult) {
        this.driver = driver;
        this.searchResult = searchResult;
    }

    public void parse() {
        WebElement siteTable = driver.findElement(By.id("siteTable"));
        processLinks(siteTable.findElements(By.className("link")));
        processPaginationUris(siteTable.findElements(By.cssSelector("span.nextprev a")));
    }

    private void processLinks(List<WebElement> links) {
        if (links != null && links.size() > 0) {
            for (WebElement rawLink : links) {
                Link link = null;
                try {
                    if (rawLink.isDisplayed()) {
                        link = processLink(rawLink);
                    }
                } catch (Exception ignore) {
                    LOG.warn("Can't parse link, ignoring: " + rawLink.getText(), ignore);
                }

                if (link != null && hasText(link.getUri())) {
                    searchResult.getLinks().add(link);
                }
            }
        }
    }

    private Link processLink(WebElement rawLink) {
        Link link = new Link();
        WebElement rawRank = rawLink.findElement(By.className("rank"));
        String rank = rawRank.getText();

        if (!hasText(rank)) {
            rank = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML", rawRank);
        }

        if (hasText(rank)) {
            WebElement rawEntry = rawLink.findElement(By.className("entry"));
            WebElement rawTitle = rawEntry.findElement(By.cssSelector("a.title"));
            WebElement rawComments = rawEntry.findElement(By.cssSelector("a.comments"));
            populateGroupUri(rawEntry, link);

            String uri = rawTitle.getAttribute("href");
            String text = rawTitle.getText();
            String commentsUri = rawComments.getAttribute("href");

            if (hasText(uri) && hasText(text)) {
                populateScore(rawLink, link);
                link.setUri(uri);
                link.setText(text);
                link.setCreated(dateFromString(rawEntry));
                link.setCommentsUri(commentsUri);
            }
        }
        return link;
    }

    private void populateGroupUri(WebElement rawEntry, Link link) {
        try {
            WebElement rawSubreddit = rawEntry.findElement(By.cssSelector("a.subreddit"));
            String groupUri = rawSubreddit.getAttribute("href");
            link.setGroupUri(groupUri);
        } catch (NoSuchElementException ignore) {
            link.setGroupUri(null);
        }
    }

    private void populateScore(WebElement rawLink, Link link) {
        try {
            link.setDown(NumberUtils.parseNumber(rawLink.getAttribute("data-downs"), Integer.class));
            link.setUp(NumberUtils.parseNumber(rawLink.getAttribute("data-ups"), Integer.class));
        } catch (Exception e) { // then fallback
//            LOG.warn("Cannot populate score, trying fallback...");
            populateScoreFallback(rawLink, link);
        }
    }

    private void populateScoreFallback(WebElement rawLink, Link link) {
        Integer down = 0;
        Integer up = 0;
        try {
            Integer combined = NumberUtils.parseNumber(rawLink.findElement(By.cssSelector("div.score.unvoted"))
                    .getText(), Integer.class);

            if (combined.intValue() >= 0) {
                up = combined;
            } else {
                down = combined;
            }
        } catch (Exception ignore) {
            LOG.warn("Cannot populate score even with fallback... returning 0");
        }

        link.setDown(down);
        link.setUp(up);
    }

    private Date dateFromString(WebElement rawEntry) {
        Date date = null;
        try {
            WebElement rawTime = rawEntry.findElement(By.cssSelector("time"));
            String dateStr = rawTime.getAttribute("datetime");
            date = dateFormatter.parse(dateStr);
        } catch (Exception e) {
            LOG.error("Can't convert date", e);
            date = new Date();
        }
        return date;
    }

    private void processPaginationUris(List<WebElement> uris) {
        if (uris != null) {
            for (WebElement uri : uris) {
                String rel = uri.getAttribute("rel");
                if (hasText(rel)) {
                    if (rel.contains("next")) {
                        searchResult.setNextPage(processPaginationUri(uri));
                    } else if (rel.contains("prev")) {
                        searchResult.setPrevPage(processPaginationUri(uri));
                    }
                }
            }
        }
    }

    private String processPaginationUri(WebElement rawUri) {
        String uri = null;
        if (rawUri != null) {
            uri = rawUri.getAttribute("href");
            if (!hasText(uri)) {
                uri = null; //reset if empty
            }
        }
        return uri;
    }
}
