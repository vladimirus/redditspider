package com.redditspider.dao.elasticsearch;

import static com.redditspider.model.DomainFactory.aLink;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.redditspider.model.Link;

public class ElasticSearchConverterTest {
    private ElasticsearchConverter converter;

    @Before
    public void before() {
        this.converter = new ElasticsearchConverter();
    }

    @Test
    public void shouldConvert() {
        // given
        Link link = aLink();

        // when
        ElasticLink actualElasticLink = converter.convert(link);

        // then
        assertEquals(Integer.valueOf(6), actualElasticLink.getRating());
        assertEquals("http://example.com", actualElasticLink.getUri());
    }
}
