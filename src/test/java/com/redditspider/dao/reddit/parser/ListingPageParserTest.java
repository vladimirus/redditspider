package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(MockitoJUnitRunner.class)
public class ListingPageParserTest {
    private ListingPageParser parser;
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement rawEntry;

    @Before
    public void before() {
        parser = new ListingPageParser(driver);
    }

    @Test
    public void shouldGetGroupUri() {
        // given
        given(rawEntry.findElement(isA(By.class))).willReturn(rawEntry);
        given(rawEntry.getAttribute("href")).willReturn("subreddit");

        // when
        String actual = parser.getGroupUri(rawEntry);

        // then
        assertThat(actual, equalTo("subreddit"));
    }

    @Test
    public void shouldNotGetGroupUri() {
        // given
        given(rawEntry.findElement(isA(By.class))).willReturn(null);

        // when
        String actual = parser.getGroupUri(rawEntry);

        // then
        assertThat(actual, is(nullValue()));
    }
}