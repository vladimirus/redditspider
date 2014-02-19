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
public class ElasticsearchDaoImplTest {
    @Mock
    ElasticsearchTemplate elasticsearchTemplate;
    @Mock
    ElasticsearchConverter elasticsearchConverter;

    private ElasticsearchDaoImpl dao;

    @Before
    public void before() {
        this.dao = new ElasticsearchDaoImpl();
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
}
