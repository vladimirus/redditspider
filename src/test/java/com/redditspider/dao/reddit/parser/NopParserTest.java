package com.redditspider.dao.reddit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import com.redditspider.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NopParserTest {
    private NopParser nopParser;

    @Before
    public void before() {
        this.nopParser = new NopParser();
    }

    @Test
    public void shouldAlwaysReturnEmptySearchResult() throws Exception {

        // when
        SearchResult searchResult = nopParser.parse();

        // then
        assertThat(searchResult.getLinks(), hasSize(0));
    }
}
