package com.redditspider.biz.manager.impl;

import static com.redditspider.model.DomainFactory.aLink;
import static com.redditspider.model.DomainFactory.aLinkWithId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedditManagerImplTest {
    private RedditManagerImpl manager;
    @Mock
    private SearchDao searchDao;

    @Before
    public void before() {
        this.manager = new RedditManagerImpl();
        this.manager.searchDao = searchDao;
    }

    @Test
    public void findLinks() {
        // given
        SearchResult result1 = new SearchResult();
        result1.setNextPage("nextPage");
        result1.getLinks().add(aLinkWithId("11"));
        result1.getLinks().add(aLinkWithId("22"));
        SearchResult result2 = new SearchResult();
        result2.getLinks().add(aLinkWithId("33"));
        SearchQuery query = new SearchQuery("test");
        given(searchDao.search(isA(SearchQuery.class))).willReturn(result1, result2);

        // when
        List<Link> links = manager.findLinks(query);

        // then
        verify(searchDao, times(2)).search(isA(SearchQuery.class));
        assertThat(links, hasSize(3));
        assertThat(links.get(2).getId(), is(equalTo("33")));
    }

    @Test
    public void retrieveSearchResult() {
        // given
        SearchResult result1 = new SearchResult();
        SearchQuery query = new SearchQuery("test");
        given(searchDao.search(query)).willReturn(result1);

        // when
        manager.retrieveSearchResult(query);

        // then
        verify(searchDao).search(query);
    }

    @Test
    public void processSearchResult() {
        // given
        SearchResult result = new SearchResult();
        List<Link> links = new ArrayList<Link>();
        links.add(aLink());
        result.setLinks(links);

        // when
        List<Link> actual = manager.processSearchResult(result);

        // then
        assertSame(links, actual);
    }
}
