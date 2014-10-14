package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class ListingPageParserTest {
    @Mock
    private SearchQuery query;

    private SearchResult searchResult;
    private WebDriver driver;

    @Before
    public void before() {
        searchResult = new SearchResult();
        driver = new HtmlUnitDriver(true);
    }

    @After
    public void after() {
        driver.quit();
    }

    @Test
    public void doSearchFromStaticFile01() {
        //given
        driver.get(file(("reddit-01.html")));
        ListingPageParser listingPageParser = new ListingPageParser(driver, searchResult);

        // when
        listingPageParser.parse();

        // then
        assertThat(searchResult.getLinks(), hasSize(25));
        assertThat(searchResult.getNextPage(), is(equalTo("http://www.reddit.com/?count=25&after=t3_1toimn")));
        assertThat(searchResult.getLinks().get(5).getText(), is(equalTo("The true meaning of Christmas")));
        assertThat(searchResult.getLinks().get(5).getUri(), is(equalTo("http://i.imgur.com/lOqtfFN.png")));
        assertNull(searchResult.getLinks().get(5).getId());
        assertThat(searchResult.getLinks().get(0).getGroupUri(), is(equalTo("http://www.reddit.com/r/videos/")));
    }

    @Test
    public void doSearchFromStaticFile02() {
        //given
        driver = new FirefoxDriver(); //javascript only works in firefox (not html driver) for some reason...
        driver.get(file(("reddit-02.html")));
        ListingPageParser listingPageParser = new ListingPageParser(driver, searchResult);

        // when
        listingPageParser.parse();

        // then
        assertThat(searchResult.getLinks(), hasSize(25));
    }

    private String file(String filename) {
        String fileLocation = null;
        ClassPathResource resource = new ClassPathResource("reddit-html/" + filename);
        try {
            File file = resource.getFile();
            if (file.exists()) {
                fileLocation = "file://" + file.getAbsolutePath();
            }
        } catch (Exception ignore) {
            fail("Can't find the file");
        }
        return fileLocation;
    }
}
