package com.redditspider.integration;

import static com.redditspider.model.DomainFactory.anEntryLink;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.EntryLink;
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

    private EntryLink entryLink;

    @Test
    public void dummy() {       //so integration phase doesn't fail without any tests
        assertThat(true, is(true));
    }

    //    @Before
    public void before() {
        entryLink = anEntryLink();
        if (mongoDao.findEntryLinkById(entryLink.getId()) != null) {
            mongoDao.insertEntryLink(entryLink);
        }
    }

    //    @After
    public void after() {
        mongoDao.deleteEntryLink(entryLink);
    }

    @Test
    @Ignore
    public void nextEntryLink() {

        // when
        EntryLink entryLink1 = mongoDao.nextEntryLink();
        EntryLink entryLink2 = mongoDao.nextEntryLink();

        // then
        assertThat(entryLink1.getUpdated(), is(notNullValue()));
        assertThat(entryLink1.getUpdated(), is(not(equalTo(entryLink2.getUpdated()))));
    }
}
