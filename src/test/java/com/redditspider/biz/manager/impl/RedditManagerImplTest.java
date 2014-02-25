package com.redditspider.biz.manager.impl;

import static com.redditspider.model.DomainFactory.aLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.redditspider.biz.manager.LinkManager;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@RunWith(MockitoJUnitRunner.class)
public class RedditManagerImplTest {
    private RedditManagerImpl manager;
    @Mock
    private SearchDao searchDao;
    @Mock
    private LinkManager linkManager;

    @Before
    public void before() {
        this.manager = new RedditManagerImpl();
        this.manager.linkManager = linkManager;
        this.manager.searchDao = searchDao;
    }

    @Test
    public void findNewLinks() {
        // given
        SearchResult result1 = new SearchResult();
        result1.setNextPage("nextPage");
        SearchResult result2 = new SearchResult();
        SearchQuery query = new SearchQuery("test");
        given(searchDao.search(query)).willReturn(result1, result2);

        // when
        manager.findNewLinks(query);

        // then
        verify(searchDao, times(2)).search(Mockito.isA(SearchQuery.class));
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
        manager.processSearchResult(result);

        // then
        verify(linkManager).save(links);
    }
}
