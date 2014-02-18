package com.redditspider.dao.elasticsearch;

import static com.redditspider.model.DomainFactory.anElasticLink;
import static com.redditspider.model.DomainFactory.aLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.redditspider.model.Link;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchDaoImplTest {
    @Mock
    ElasticsearchTemplate elasticsearchTemplate;
    @Mock
    ElasticsearchConverter elasticSearchConverter;

    private ElasticSearchDaoImpl dao;

    @Before
    public void before() {
        this.dao = new ElasticSearchDaoImpl();
        this.dao.elasticsearchConverter = elasticSearchConverter;
        this.dao.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Test
    public void save() {
        // given
        Link link = aLink();
        given(elasticSearchConverter.convert(link)).willReturn(anElasticLink());

        // when
        dao.save(link);

        // then
        verify(elasticSearchConverter).convert(link);
        verify(elasticsearchTemplate).index(isA(IndexQuery.class));
    }
}
