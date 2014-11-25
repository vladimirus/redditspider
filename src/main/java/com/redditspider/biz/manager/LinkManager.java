package com.redditspider.biz.manager;

import com.redditspider.model.Link;
import com.redditspider.model.Subreddit;

import java.util.Collection;

/**
 * Manager to deal with links.
 */
public interface LinkManager {

    Collection<Link> findAll();

    Link save(Link link);

    void save(Collection<Link> links);

    /**
     * Starts a thread and leaves it.
     */
    void startIndexThread();

    /**
     * Does not start a new thread.
     */
    void index();

    Link findById(String id);

    void startBroadcastThread();

    void broadcast();

    void deleteAll();

    Collection<Subreddit> discoverSubreddits();
}
