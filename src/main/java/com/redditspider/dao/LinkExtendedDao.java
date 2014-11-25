package com.redditspider.dao;

import com.redditspider.model.Link;
import com.redditspider.model.Subreddit;

import java.util.List;

/**
 * Interface to save links.
 */
public interface LinkExtendedDao extends LinkDao {
    Iterable<Link> save(Iterable<Link> links);
    List<Link> findAll();
    Link findById(String id);
    List<Link> findToBroadcast();
    void delete(Link link);

    Subreddit next();
    void insert(Subreddit subreddit);
    Subreddit findSubredditById(String id);

    void delete(Subreddit subreddit);
}
