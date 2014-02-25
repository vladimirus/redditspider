package com.redditspider.dao;

import com.redditspider.model.Link;

public interface ElasticsearchDao {
    void save(Link link);
    void delete();
}
