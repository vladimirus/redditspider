package com.redditspider.biz.manager.task;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.redditspider.biz.manager.LinkManager;

@RunWith(MockitoJUnitRunner.class)
public class LinkIndexerTest {
	private LinkIndexer linkIndexer;
	
	@Mock
	private LinkManager linkManager;
	
	@Before
	public void before() {
		this.linkIndexer = new LinkIndexer(linkManager);
	}
	
	@Test
	public void startIndex() {
		
		// when
		linkIndexer.run();
		
		// then
		verify(linkManager).index();
	}
}
