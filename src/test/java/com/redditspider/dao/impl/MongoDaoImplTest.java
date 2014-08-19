package com.redditspider.dao.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.redditspider.model.DomainFactory.aLink;
import static com.redditspider.model.DomainFactory.anEntryLink;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.redditspider.model.EntryLink;
import com.redditspider.model.Link;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MongoDaoImplTest {
    private MongoDaoImpl dao;

    @Mock
    private MongoOperations mongoOperation;

    @Before
    public void before() {
        this.dao = new MongoDaoImpl();
        this.dao.mongoOperation = mongoOperation;
    }

    @Test
    public void save() {
        // given
        Link aLink = aLink();

        // when
        dao.save(aLink);

        // then
        verify(mongoOperation).save(aLink);
    }

    @Test
    public void saveMany() {
        // given
        Link aLink1 = aLink();
        Link aLink2 = aLink();
        List<Link> links = newArrayList();
        links.add(aLink1);
        links.add(aLink2);

        // when
        dao.save(links);

        // then
        verify(mongoOperation, times(1)).save(aLink1);
        verify(mongoOperation, times(1)).save(aLink2);
    }

    @Test
    public void findAll() {

        // when
        dao.findAll();

        // then
        verify(mongoOperation).findAll(Link.class);
    }

    @Test
    public void findById() {

        // when
        dao.findById("1");

        // then
        verify(mongoOperation).findById("1", Link.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findToBroadcast() {

        // when
        dao.findToBroadcast();

        // then
        verify(mongoOperation).find(isA(Query.class), any(Class.class));
    }

    @Test
    public void deleteAll() {

        // when
        dao.deleteAll();

        // then
        verify(mongoOperation).dropCollection(Link.class);
    }

    @Test
    public void delete() {
        // give
        Link aLink = aLink();

        // when
        dao.delete(aLink);

        // then
        verify(mongoOperation).remove(aLink);
    }

    @Test
    public void nextEntryLink() {

        //when
        dao.nextEntryLink();

        // then
        verify(mongoOperation).findAndModify(isA(Query.class), isA(Update.class), any(Class.class));
    }

    @Test
    public void insertEntryLink() {
        // given
        EntryLink entryLink = new EntryLink("test", "test");

        // when
        dao.insertEntryLink(entryLink);

        // then
        verify(mongoOperation).insert(entryLink);
    }

    @Test
    public void findEntryLinkById() {

        // when
        dao.findEntryLinkById("1");

        // then
        verify(mongoOperation).findById("1", EntryLink.class);
    }

    @Test
    public void deleteEntryLink() {
        // give
        EntryLink link = anEntryLink();

        // when
        dao.deleteEntryLink(link);

        // then
        verify(mongoOperation).remove(link);
    }
}
