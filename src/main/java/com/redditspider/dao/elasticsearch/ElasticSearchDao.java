package com.redditspider.dao.elasticsearch;

import com.redditspider.model.Link;

public interface ElasticSearchDao {
    void save(Link link);
}
