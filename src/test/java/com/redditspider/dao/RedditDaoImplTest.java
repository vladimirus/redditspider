package com.redditspider.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@RunWith(MockitoJUnitRunner.class)
public class RedditDaoImplTest {
	private RedditDaoImpl dao;

	@Mock
	private SearchQuery query;
	
	@Before
	public void before() {
		this.dao = new RedditDaoImpl();
	}
	
	@Test
	public void searchWhenQueryIsEmpty() {
		// given
		given(query.getSearchUri()).willReturn("");
		
		// when
		SearchResult actual = dao.search(query);
		
		// then
		assertNotNull(actual);
		assertEquals(0, actual.getLinks().size());
	}
	
	@Test
	public void searchWhenQueryIsNull() {
		// given
		given(query.getSearchUri()).willReturn(null);
		
		// when
		SearchResult actual = dao.search(query);
		
		// then
		assertNotNull(actual);
		assertEquals(0, actual.getLinks().size());
	}
}
