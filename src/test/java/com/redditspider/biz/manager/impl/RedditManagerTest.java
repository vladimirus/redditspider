package com.redditspider.biz.manager.impl;

import static com.google.common.collect.Iterables.get;
import static com.redditspider.model.DomainFactory.aLinkWithId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.SearchQuery;
import com.redditspider.model.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class RedditManagerTest {
    private RedditManager manager;
    @Mock
    private SearchDao searchDao;

    @Before
    public void before() {
        this.manager = new RedditManager();
        this.manager.searchDao = searchDao;
    }

    @Test
    public void findLinks() {
        // given
        SearchResult result = new SearchResult();
        result.getLinks().add(aLinkWithId("11"));
        given(searchDao.search(isA(SearchQuery.class))).willReturn(result);

        // when
        Collection<Link> links = manager.findLinks(new SearchQuery("test"));

        // then
        verify(searchDao).search(isA(SearchQuery.class));
        assertThat(links, hasSize(1));
        assertThat(get(links, 0).getId(), is(equalTo("11")));
    }

    @Test
    public void discoverSubreddits() {

        // when
        manager.discoverSubreddits();

        // then
        verify(searchDao).discoverSubreddits();
    }
}
