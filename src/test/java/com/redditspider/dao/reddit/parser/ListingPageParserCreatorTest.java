package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListingPageParserCreatorTest extends TestCase {
    private ListingPageParserCreator creator;

    @Before
    public void before() {
        creator = new ListingPageParserCreator();
    }

    @Test
    public void shouldBeApplicable() {

        // when
        boolean actual = creator.isApplicable(null);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldReturnListingPageParser() {

        // when
        Parser actual = creator.getInstance(null);

        // then
        assertThat(actual, instanceOf(ListingPageParser.class));
    }
}