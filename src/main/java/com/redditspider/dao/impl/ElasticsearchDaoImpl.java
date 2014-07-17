package com.redditspider.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Repository;

import com.redditspider.dao.LinkDao;
import com.redditspider.dao.elasticsearch.ElasticLink;
import com.redditspider.dao.elasticsearch.ElasticsearchConverter;
import com.redditspider.model.Link;

/**
 * This class talks to an instance of elastic search.
 * check https://github.com/spring-projects/spring-data-elasticsearch
 */
@Repository
public class ElasticsearchDaoImpl implements LinkDao {
    @Autowired
    ElasticsearchConverter elasticsearchConverter;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    private final transient Logger log = Logger.getLogger(this.getClass());

    @Override
    public void save(Link link) {
        ElasticLink elasticLink = elasticsearchConverter.convert(link);
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId(elasticLink.getId());
        indexQuery.setObject(elasticLink);
        log.debug("indexing: " + elasticLink.toString());
        elasticsearchTemplate.index(indexQuery);
    }

    @Override
    public void deleteAll() {
        elasticsearchTemplate.deleteIndex(ElasticLink.class);
    }
}
