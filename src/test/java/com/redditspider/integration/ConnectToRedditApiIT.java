package com.redditspider.integration;

import static com.google.common.collect.Iterables.getFirst;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.redditspider.dao.SearchDao;
import com.redditspider.model.Subreddit;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

/**
 * Integration test talking to real reddit api.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appCtx/*.xml")
public class ConnectToRedditApiIT {
    @Autowired
    @Qualifier("redditApiDao")
    private SearchDao redditApiDao;

    @Test
    @Ignore
    public void searchApi() {
        // given
        SearchQuery query = new SearchQuery("futurology");

        // when
        SearchResult actual = redditApiDao.search(query);

        // then
        assertThat(actual.getLinks(), hasSize(25));
        assertThat(getFirst(actual.getLinks(), null).getSubreddit(), equalToIgnoringCase("futurology"));
    }

    @Test
    @Ignore
    public void subredditApi() {

        // when
        Collection<Subreddit> actual = redditApiDao.discoverSubreddits();
        redditApiDao.discoverSubreddits();

        // then
        assertThat(actual, hasSize(100));
    }
}
