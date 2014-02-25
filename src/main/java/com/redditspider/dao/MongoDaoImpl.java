package com.redditspider.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.redditspider.model.Link;
//import static org.springframework.data.mongodb.core.query.Criteria.where;
//import static org.springframework.data.mongodb.core.query.Criteria.query;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class connects to mongodb to save links.
 *
 */
@Repository
public class MongoDaoImpl implements LinkDao {
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
                .with(new Sort(Sort.Direction.DESC, "up"))
                .limit(50);
        return mongoOperation.find(query, Link.class);
    }

    @Override
    public void dropLinkCollection() {
        mongoOperation.dropCollection(Link.class);
    }

    @Override
    public void delete(Link link) {
        mongoOperation.remove(link);
    }
}
