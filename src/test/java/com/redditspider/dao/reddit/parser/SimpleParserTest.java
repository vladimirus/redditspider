package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.redditspider.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleParserTest {
    private SimpleParser simpleParser;

    @Before
    public void before() {
        this.simpleParser = new SimpleParser();
    }


    @Test
    public void shouldNeverBeApplicable() throws Exception {

        // when
        boolean actual = simpleParser.isApplicable("");

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void shouldAlwaysReturnEmptySearchResult() throws Exception {

        SearchResult searchResult = simpleParser.parse(null);
        // then
        assertThat(searchResult.getLinks(), hasSize(0));
    }
}