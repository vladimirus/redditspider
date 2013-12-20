package com.redditspider.biz.manager;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

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
		SearchResult result = new SearchResult();
		given(redditDao.search(isA(SearchQuery.class))).willReturn(result);
		
		//when
		manager.findNewLinks();
		
		// then
		verify(redditDao).search(isA(SearchQuery.class));
	}
}
