package com.redditspider.dao.elasticsearch;

import com.redditspider.model.Link;

public interface ElasticsearchDao {
    void save(Link link);
}
