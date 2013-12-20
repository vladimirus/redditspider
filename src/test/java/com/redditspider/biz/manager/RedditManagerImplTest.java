package com.redditspider.biz.manager;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.redditspider.dao.RedditDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@RunWith(MockitoJUnitRunner.class)
public class RedditManagerImplTest {
	private RedditManagerImpl manager;
	@Mock
	private RedditDao redditDao;
	@Mock
	private LinkManager linkManager;
	
	@Before
	public void before() {
		this.manager = new RedditManagerImpl();
		this.manager.linkManager = linkManager;
		this.manager.redditDao = redditDao;
	}
	
	@Test
	public void findNewLinks() {
		// given
		SearchResult result1 = new SearchResult();
		result1.setNextPage("nextPage");;
		SearchResult result2 = new SearchResult();
		SearchQuery query = new SearchQuery("test");
		given(redditDao.search(query)).willReturn(result1, result2);
		
		//when
		manager.retrieveSearchResult(query);
		
		// then
		verify(redditDao, times(2)).search(query);
	}
	
	@Test
	public void retrieveSearchResult() {
		// given
		SearchResult result1 = new SearchResult();
		SearchQuery query = new SearchQuery("test");
		given(redditDao.search(query)).willReturn(result1);
		
		//when
		manager.retrieveSearchResult(query);
		
		// then
		verify(redditDao).search(query);
	}
}
