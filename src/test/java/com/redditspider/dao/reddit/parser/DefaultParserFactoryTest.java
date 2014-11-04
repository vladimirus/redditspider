package com.redditspider.dao.reddit.parser;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

@RunWith(MockitoJUnitRunner.class)
public class DefaultParserFactoryTest {
    private DefaultParserFactory defaultParserFactory;
    @Mock
    private WebDriver driver;

    @Before
    public void init() {
        this.defaultParserFactory = new DefaultParserFactory();
        this.defaultParserFactory.parserCreators = newArrayList(
                new ListingPageParserCreator(),
                new HomePageParserCreator());
    }

    @Test
    public void shouldReturnListingPageParser() {
        // given
        given(driver.getCurrentUrl()).willReturn(("http://www.reddit.com/r/LifeProTips/?count=25&after=t3_2l333h"));

        // when
        Parser actual = defaultParserFactory.getParser(driver);

        // then
        assertThat(actual, instanceOf(ListingPageParser.class));
    }

    @Test
    public void shouldReturnHomePageParser() {
        // given
        given(driver.getCurrentUrl()).willReturn(("http://www.reddit.com/?count=25&after=t3_2l5cou"));

        // when
        Parser actual = defaultParserFactory.getParser(driver);

        // then
        assertThat(actual, instanceOf(HomePageParser.class));
    }
}
