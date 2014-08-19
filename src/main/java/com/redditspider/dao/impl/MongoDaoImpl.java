package com.redditspider.dao.impl;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.query.Update.update;

import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.EntryLink;
import com.redditspider.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * This class connects to mongodb to save links.
 *
 */
@Repository
public class MongoDaoImpl implements LinkExtendedDao {
    @Autowired
    MongoOperations mongoOperation;

    @Override
    public void save(Link link) {
        mongoOperation.save(link);
    }

    @Override
    public void save(List<Link> links) {
        for (Link link : links) {
            save(link);
        }
    }

    @Override
    public List<Link> findAll() {
        return mongoOperation.findAll(Link.class);
    }

    @Override
    public Link findById(String id) {
        return mongoOperation.findById(id, Link.class);
    }

    @Override
    public List<Link> findToBroadcast() {
        Query query = new Query()
                .with(new Sort(DESC, "up"))
                .limit(50);
        return mongoOperation.find(query, Link.class);
    }

    @Override
    public EntryLink nextEntryLink() {
        Query query = new Query()
                .with(new Sort(DESC, "updated"))
                .limit(1);

        Update update = update("updated", new Date());
        return mongoOperation.findAndModify(query, update, EntryLink.class);
    }

    @Override
    public void deleteAll() {
        mongoOperation.dropCollection(Link.class);
    }

    @Override
    public void delete(Link link) {
        mongoOperation.remove(link);
    }

    @Override
    public void insertEntryLink(EntryLink entryLink) {
        mongoOperation.insert(entryLink);
    }

    @Override
    public EntryLink findEntryLinkById(String id) {
        return mongoOperation.findById(id, EntryLink.class);
    }

    @Override
    public void deleteEntryLink(EntryLink link) {
        mongoOperation.remove(link);
    }
}
