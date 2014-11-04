package com.redditspider.dao.reddit.parser;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.NumberUtils.parseNumber;
import static org.springframework.util.StringUtils.hasText;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Parsing reddit.
 */
public abstract class AbstractListingPageParser implements Parser {
    private static final transient Logger LOG = Logger.getLogger(AbstractListingPageParser.class);
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    final WebDriver driver;

    public AbstractListingPageParser(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public SearchResult parse() {
        SearchResult searchResult = new SearchResult();
        WebElement siteTable = driver.findElement(By.id("siteTable"));
        searchResult.getLinks().addAll(processLinks(siteTable.findElements(By.className("link"))));
        searchResult.setNextPage(processPaginationUris(siteTable.findElements(By.cssSelector("span.nextprev a")), "next"));
        searchResult.setPrevPage(processPaginationUris(siteTable.findElements(By.cssSelector("span.nextprev a")), "prev"));
        return searchResult;
    }

    private Collection<Link> processLinks(Collection<WebElement> links) {
        Collection<Link> found = newArrayList();
        if (links != null && links.size() > 0) {
            found = from(links).filter(new Predicate<WebElement>() {
                @Override
                public boolean apply(WebElement input) {
                    return input.isDisplayed() && hasRank(input);
                }
            }).transform(new Function<WebElement, Link>() {
                @Override
                public Link apply(WebElement input) {
                    return processLink(input);
                }
            }).filter(new Predicate<Link>() {
                @Override
                public boolean apply(Link input) {
                    return hasText(input.getUri());
                }
            }).toList();
        }

        return found;
    }

    private Link processLink(WebElement rawLink) {
        Link link = new Link();
        try {
            WebElement rawEntry = rawLink.findElement(By.className("entry"));
            WebElement rawTitle = rawEntry.findElement(By.cssSelector("a.title"));
            WebElement rawComments = rawEntry.findElement(By.cssSelector("a.comments"));

            link.setGroupUri(getGroupUri(rawEntry));

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
        } catch (Exception ignore) {
            LOG.warn("Can't parse link, ignoring: " + rawLink.getText(), ignore);
        }
        return link;
    }

    private boolean hasRank(WebElement rawLink) {
        String rank = null;
        try {
            WebElement rawRank = rawLink.findElement(By.className("rank"));
            rank = rawRank.getText();

            if (!hasText(rank)) {
                rank = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML", rawRank);
            }
        } catch (Exception ignore) {
            LOG.warn("Can't parse rank, ignoring: " + rawLink.getText(), ignore);
        }
        return hasText(rank);
    }

    protected abstract String getGroupUri(WebElement rawEntry);

    private void populateScore(WebElement rawLink, Link link) {
        try {
            link.setDown(parseNumber(rawLink.getAttribute("data-downs"), Integer.class));
            link.setUp(parseNumber(rawLink.getAttribute("data-ups"), Integer.class));
        } catch (Exception e) { // then fallback
            populateScoreFallback(rawLink, link);
        }
    }

    private void populateScoreFallback(WebElement rawLink, Link link) {
        Integer down = 0;
        Integer up = 0;
        try {
            Integer combined = parseNumber(rawLink.findElement(By.cssSelector("div.score.unvoted"))
                    .getText(), Integer.class);

            if (combined >= 0) {
                up = combined;
            } else {
                down = combined;
            }
        } catch (Exception ignore) {
            link.setDown(0);
            link.setUp(0);
        }

        link.setDown(down);
        link.setUp(up);
    }

    private Date dateFromString(WebElement rawEntry) {
        Date date = null;
        try {
            WebElement rawTime = rawEntry.findElement(By.cssSelector("time"));
            String dateStr = rawTime.getAttribute("datetime");
            date = DATE_FORMATTER.parse(dateStr);
        } catch (Exception e) {
            LOG.error("Can't convert date", e);
            date = new Date();
        }
        return date;
    }

    private String processPaginationUris(Iterable<WebElement> uris, final String key) {
        String found = null;
        if (uris != null) {
            found = processPaginationUri(find(uris, new Predicate<WebElement>() {
                @Override
                public boolean apply(WebElement input) {
                    String rel = input.getAttribute("rel");
                    return hasText(rel) && rel.contains(key);
                }
            }, null));
        }
        return found;
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
