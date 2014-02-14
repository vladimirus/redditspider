package com.redditspider.dao;

import com.redditspider.model.Link;

public interface ElasticSearchDao {
    void save(Link link);
}
