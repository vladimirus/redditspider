package com.redditspider.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing mongodb.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appCtx/*.xml")
public class ParseRedditIT {
    @Autowired
    private SearchDao searchDao;

    @Ignore
    @Test
    public void search() {
        // given
        SearchQuery query = new SearchQuery("http://www.reddit.com/r/Futurology/");

        // when
        SearchResult actual = searchDao.search(query);

        // then
        assertThat(actual.getLinks(), hasSize(25));
    }
}
