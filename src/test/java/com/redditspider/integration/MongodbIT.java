package com.redditspider.integration;

import static com.redditspider.model.DomainFactory.aSubreddit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.Subreddit;
import org.junit.Ignore;
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

    private Subreddit subreddit;

    @Test
    public void dummy() {       //so integration phase doesn't fail without any tests
        assertThat(true, is(true));
    }

    //    @Before
    public void before() {
        subreddit = aSubreddit();
        if (mongoDao.findSubredditById(subreddit.getId()) != null) {
            mongoDao.insert(subreddit);
        }
    }

    //    @After
    public void after() {
        mongoDao.delete(subreddit);
    }

    @Test
    @Ignore
    public void nextSubreddit() {

        // when
        Subreddit subreddit1 = mongoDao.next();
        Subreddit subreddit2 = mongoDao.next();

        // then
        assertThat(subreddit1.getUpdated(), is(notNullValue()));
        assertThat(subreddit1.getUpdated(), is(not(equalTo(subreddit2.getUpdated()))));
    }
}
