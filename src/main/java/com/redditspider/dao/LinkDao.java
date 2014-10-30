package com.redditspider.dao;

import com.redditspider.model.Link;

public interface LinkDao {
    Link save(Link link);
    void deleteAll();
}
