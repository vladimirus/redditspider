package com.redditspider.dao.impl;

import static com.redditspider.model.DomainFactory.aLink;
import static com.redditspider.model.DomainFactory.anElasticLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import com.redditspider.dao.elasticsearch.ElasticLink;
import com.redditspider.dao.elasticsearch.ElasticsearchConverter;
import com.redditspider.model.Link;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

@RunWith(MockitoJUnitRunner.class)
public class ElasticsearchDaoTest {
    @Mock
    ElasticsearchTemplate elasticsearchTemplate;
    @Mock
    ElasticsearchConverter elasticsearchConverter;

    private ElasticsearchDao dao;

    @Before
    public void before() {
        this.dao = new ElasticsearchDao();
        this.dao.elasticsearchConverter = elasticsearchConverter;
        this.dao.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Test
    public void save() {
        // given
        Link link = aLink();
        given(elasticsearchConverter.convert(link)).willReturn(anElasticLink());

        // when
        dao.save(link);

        // then
        verify(elasticsearchConverter).convert(link);
        verify(elasticsearchTemplate).index(isA(IndexQuery.class));
    }

    @Test
    public void delete() {

        // when
        dao.deleteAll();

        // then
        verify(elasticsearchTemplate).deleteIndex(ElasticLink.class);
    }
}
