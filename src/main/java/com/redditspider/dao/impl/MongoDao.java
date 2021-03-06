package com.redditspider.dao.impl;

import static com.google.common.collect.FluentIterable.from;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;

import com.google.common.base.Function;
import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.Link;
import com.redditspider.model.Subreddit;
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
public class MongoDao implements LinkExtendedDao {
    @Autowired
    MongoOperations mongoOperation;

    @Override
    public Link save(Link link) {
        link.setUpdated(new Date());
        mongoOperation.save(link);
        return link;
    }

    @Override
    public Iterable<Link> save(Iterable<Link> links) {
        return from(links).transform(new Function<Link, Link>() {
            @Override
            public Link apply(Link input) {
                return save(input);
            }
        }).toList();
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
                .with(new Sort(DESC, "updated"))
                .limit(1000);
        return mongoOperation.find(query, Link.class);
    }

    @Override
    public Subreddit next() {
        Query query = new Query()
                .addCriteria(where("subscribers").gte(10000))
                .with(new Sort(ASC, "crawled"))
                .limit(1);

        Update update = update("crawled", new Date());
        return mongoOperation.findAndModify(query, update, Subreddit.class);
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
    public void insert(Subreddit subreddit) {
        mongoOperation.insert(subreddit);
    }

    @Override
    public Subreddit findSubredditById(String id) {
        return mongoOperation.findById(id, Subreddit.class);
    }

    @Override
    public void delete(Subreddit subreddit) {
        mongoOperation.remove(subreddit);
    }
}
