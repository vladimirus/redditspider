package com.redditspider.biz.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.redditspider.dao.LinkDao;
import com.redditspider.model.Link;

@RunWith(MockitoJUnitRunner.class)
public class LinkManagerImplTest {
	private LinkManagerImpl manager;
	@Mock
	private LinkDao linkDao;
	@Mock
	private ThreadPoolTaskExecutor taskExecutor;
	@Mock
	private RedditManager redditManager;
	
	@Before
	public void before() {
		this.manager = new LinkManagerImpl();
		this.manager.linkDao = linkDao;
		this.manager.taskExecutor = taskExecutor;
		this.manager.redditManager = redditManager;
	}

	@Test
	public void findAll() {
		// when
		List<Link> links = manager.findAll();
		
		// then
		assertNotNull(links);
		assertEquals(2, links.size());
	}
	
	@Test
	public void save() {
		// given
		Link link = new Link("test");
		
		// when
		Link actual = manager.save(link);
		
		// then
		verify(linkDao).save(link);
		assertEquals("098f6bcd4621d373cade4e832627b4f6", actual.getId());
	}
	
	@Test
	public void saveNull() {
		// given
		Link link = null;
		
		// when
		manager.save(link);
		
		// then
		verify(linkDao, never()).save(link);
	}
	
	@Test
	public void startIndex() {
		
		// when
		manager.startIndexThread();
		
		// then
		verify(taskExecutor).execute(isA(Runnable.class));
	}

	@Test
	public void saveMany() {
		// given
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("test1"));
		links.add(new Link("test2"));
		
		// when
		manager.save(links);
		
		// then
		verify(linkDao).save(links);
	}
	
	@Test
	public void saveNone() {
		// given
		List<Link> links = new ArrayList<Link>();
		
		// when
		manager.save(links);
		
		// then
		verify(linkDao, never()).save(links);
	}
	
	@Test
	public void saveNullLinks() {
		// given
		List<Link> links = null;
		
		// when
		manager.save(links);
		
		// then
		verify(linkDao, never()).save(links);
	}
	
	@Test
	public void index() {
		// given
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("test1"));
		links.add(new Link("test2"));
		
		// when
		manager.index();
		
		// then
		verify(redditManager).findNewLinks();
//		verify(linkDao).save(links);
	}
}
