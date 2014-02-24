package com.redditspider.dao.elasticsearch;

import org.springframework.stereotype.Component;

import com.redditspider.model.Link;

/**
 * Converts objects to ElasticSearch and back.
 */
@Component
public class ElasticsearchConverter {

    public ElasticLink convert(Link link) {
        ElasticLink elasticLink = new ElasticLink();
        elasticLink.setId(link.getId());
        elasticLink.setRating(link.getUp() - link.getDown());
        elasticLink.setUri(link.getUri());
        elasticLink.setText(link.getText());
        elasticLink.setCreated(link.getCreated());
        return elasticLink;
    }
}
