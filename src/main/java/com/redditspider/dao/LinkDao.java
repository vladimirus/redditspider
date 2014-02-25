package com.redditspider.dao;

import com.redditspider.model.Link;

public interface LinkDao {
    void save(Link link);
    void deleteAll();
}
