package com.redditspider.dao;

import org.springframework.stereotype.Repository;

import com.redditspider.model.Link;

/**
 * This class talks to an instance of elastic search.
 * check https://github.com/spring-projects/spring-data-elasticsearch
 */
@Repository
public class ElasticSearchDaoImpl implements ElasticSearchDao {

    @Override
    public void save(Link link) {
        // TODO Auto-generated method stub
    }
}
