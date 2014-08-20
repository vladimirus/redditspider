package com.redditspider.biz.manager.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.redditspider.model.DomainFactory.aLink;
import static com.redditspider.model.DomainFactory.anEntryLinkWithId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.redditspider.biz.manager.SearchManager;
import com.redditspider.dao.LinkDao;
import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.EntryLink;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * Test for LinkManager.
 */
@RunWith(MockitoJUnitRunner.class)
public class LinkManagerImplTest {
    private LinkManagerImpl manager;
    @Mock
    private LinkExtendedDao mongoDao;
    @Mock
    private ThreadPoolTaskExecutor taskExecutor;
    @Mock
    private SearchManager redditManager;
    @Mock
    private LinkDao elasticsearchDao;

    @Before
    public void before() {
        this.manager = new LinkManagerImpl();
        this.manager.mongoDao = mongoDao;
        this.manager.taskExecutor = taskExecutor;
        this.manager.redditManager = redditManager;
        this.manager.elasticsearchDao = elasticsearchDao;
    }

    @Test
    public void findAll() {
        // given
        List<Link> links = newArrayList();
        links.add(aLink());
        links.add(aLink());
        given(mongoDao.findAll()).willReturn(links);

        // when
        List<Link> actual = manager.findAll();

        // then
        assertThat(actual, hasSize(2));
    }

    @Test
    public void save() {
        // given
        Link link = new Link();
        link.setCommentsUri("test");

        // when
        Link actual = manager.save(link);

        // then
        verify(mongoDao).save(isA(Iterable.class));
        assertThat(actual.getId(), equalTo("098f6bcd4621d373cade4e832627b4f6"));
    }

    @Test
    public void saveNull() {
        // given
        Link link = null;

        // when
        manager.save(link);

        // then
        verify(mongoDao, never()).save(isA(List.class));
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
        List<Link> links = newArrayList();
        links.add(aLink());
        links.add(aLink());

        // when
        manager.save(links);

        // then
        verify(mongoDao).save(links);
    }

    @Test
    public void saveNone() {
        // given
        List<Link> links = newArrayList();

        // when
        manager.save(links);

        // then
        verify(mongoDao, never()).save(links);
    }

    @Test
    public void saveNullLinks() {
        // given
        List<Link> links = null;

        // when
        manager.save(links);

        // then
        verify(mongoDao, never()).save(links);
    }

    @Test
    public void index() {
        // given
        List<Link> links = newArrayList();
        links.add(aLink());
        links.add(aLink());
        given(redditManager.findLinks(isA(SearchQuery.class))).willReturn(links);

        // when
        manager.index();

        // then
        verify(redditManager).findLinks(isA(SearchQuery.class));
        verify(mongoDao).save(links);
    }

    @Test
    public void findById() {
        // given
        given(mongoDao.findById("1")).willReturn(aLink());

        // when
        Link link = manager.findById("1");

        // then
        verify(mongoDao).findById("1");
        assertThat(link, is(notNullValue()));
    }

    @Test
    public void startBroadcast() {

        // when
        manager.startBroadcastThread();

        // then
        verify(taskExecutor).execute(isA(Runnable.class));
    }

    @Test
    public void broadcast() {
        // given
        List<Link> links = newArrayList();
        links.add(aLink());
        links.add(aLink());
        given(mongoDao.findToBroadcast()).willReturn(links);

        // when
        manager.broadcast();

        // then
        verify(mongoDao).findToBroadcast();
        verify(elasticsearchDao, times(2)).save(isA(Link.class));
        verify(mongoDao, times(2)).delete(isA(Link.class));
    }

    @Test
    public void deleteAll() {

        // when
        manager.deleteAll();

        // then
        verify(mongoDao).deleteAll();
        verify(elasticsearchDao).deleteAll();
    }

    @Test
    public void saveEntryLinks() {
        // given
        EntryLink entryLink1 = anEntryLinkWithId("entryLinkId1"); // new entry
        EntryLink entryLink2 = anEntryLinkWithId("entryLinkId2"); // assume this is already exists
        given(mongoDao.findEntryLinkById(isA(String.class))).willReturn(null, entryLink2);

        // when
        manager.saveEntryLinks(newHashSet(entryLink1, entryLink2));

        // then
        verify(mongoDao, times(1)).findEntryLinkById("entryLinkId1");
        verify(mongoDao, times(1)).findEntryLinkById("entryLinkId2");
        verify(mongoDao, times(1)).insertEntryLink(isA(EntryLink.class));
    }
}
