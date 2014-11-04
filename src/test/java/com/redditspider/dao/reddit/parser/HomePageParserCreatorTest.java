package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

@RunWith(MockitoJUnitRunner.class)
public class HomePageParserCreatorTest {
    private HomePageParserCreator creator;
    @Mock
    private WebDriver driver;

    @Before
    public void before() {
        creator = new HomePageParserCreator();
    }

    @Test
    public void shouldBeApplicable() {
        // given
        given(driver.getCurrentUrl()).willReturn(("http://www.reddit.com/?count=25&after=t3_2l5cou"));

        // when
        boolean actual = creator.isApplicable(driver);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeApplicable() {
        // given
        given(driver.getCurrentUrl()).willReturn(("http://www.reddit.com/r/LifeProTips/?count=25&after=t3_2l333h"));

        // when
        boolean actual = creator.isApplicable(driver);

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void shouldReturnListingPageParser() {

        // when
        Parser actual = creator.getInstance(null);

        // then
        assertThat(actual, instanceOf(HomePageParser.class));
    }
}