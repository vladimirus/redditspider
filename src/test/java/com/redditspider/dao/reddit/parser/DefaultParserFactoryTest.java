package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultParserFactoryTest {
    private DefaultParserFactory defaultParserFactory;

    @Before
    public void init() {
        this.defaultParserFactory = new DefaultParserFactory();
    }

    @Test
    public void shouldReturnListingPageParser() throws Exception {

        // when
        Parser actual = defaultParserFactory.createParser("something");

        // then
        assertThat(actual, instanceOf(ListingPageParser.class));
    }
}