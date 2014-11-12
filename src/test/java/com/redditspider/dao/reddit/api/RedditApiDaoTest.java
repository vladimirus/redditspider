package com.redditspider.dao.reddit.api;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedditApiDaoTest {
    private RedditApiDao redditApiDao;

    @Mock
    private RedditApiUserManager userManager;

    @Before
    public void before() {
        this.redditApiDao = new RedditApiDao();
        redditApiDao.userManager = userManager;
    }

    @Test
    @Ignore
    public void should() {
        // given
        SearchQuery query = new SearchQuery("test");

        // when
        SearchResult actual = redditApiDao.search(query);

        // then
        assertThat(actual.getLinks(), hasSize(1));

    }
}
