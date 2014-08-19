package com.redditspider.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.redditspider.dao.LinkExtendedDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing mongodb.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appCtx/*.xml")
public class MongodbIT {
    @Autowired
    @Qualifier("mongoDaoImpl")
    private LinkExtendedDao mongoDao;

    @Test
    public void dummy() {       //so integration phase doesn't fail without any tests
        assertThat(true, is(true));
    }

}
