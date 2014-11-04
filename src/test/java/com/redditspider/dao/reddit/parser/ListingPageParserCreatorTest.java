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
public class ListingPageParserCreatorTest {
    private ListingPageParserCreator creator;
    @Mock
    private WebDriver driver;

    @Before
    public void before() {
        creator = new ListingPageParserCreator();
    }

    @Test
    public void shouldBeApplicable() {
        // given
        given(driver.getCurrentUrl()).willReturn("http://www.reddit.com/r/funny/?count=25&after=t3_2l442c");

        // when
        boolean actual = creator.isApplicable(driver);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeApplicable() {
        // given
        given(driver.getCurrentUrl()).willReturn("http://www.reddit.com/r/funny/comments/2l5sgj/dog_v_fish/");

        // when
        boolean actual = creator.isApplicable(driver);

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void shouldReturnListingPageParser() {

        // when
        Parser actual = creator.getInstance(driver);

        // then
        assertThat(actual, instanceOf(ListingPageParser.class));
    }
}
