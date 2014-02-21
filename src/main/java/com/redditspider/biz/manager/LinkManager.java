package com.redditspider.biz.manager;

import java.util.List;

import com.redditspider.model.Link;

/**
 * Manager to deal with links.
 */
public interface LinkManager {

    List<Link> findAll();

    Link save(Link link);

    void save(List<Link> links);

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
}
