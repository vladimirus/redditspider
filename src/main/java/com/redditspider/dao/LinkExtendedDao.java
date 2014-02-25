package com.redditspider.dao;

import java.util.List;

import com.redditspider.model.Link;

/**
 * Interface to save links.
 */
public interface LinkExtendedDao extends LinkDao {
    void save(List<Link> links);
    List<Link> findAll();
    Link findById(String id);
    List<Link> findToBroadcast();
    void delete(Link link);
}
