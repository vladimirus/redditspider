package com.redditspider.dao.reddit.api;

import static com.google.common.collect.Lists.newArrayList;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aListing;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aMediaEmbedObject;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aMediaObject;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aSubmissionWrapper;
import static com.redditspider.model.DomainFactory.aLink;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.reddit.api.utils.UtilResponse;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedditApiDaoTest {
    private RedditApiDao redditApiDao;
    @Mock
    private RedditApiUserManager userManager;
    @Mock
    private RestClient restClient;
    @Mock
    private LinkSubmissionConverter converter;
    private User user = new User(null, "username", "password");
    private UtilResponse normalResponse;

    @Before
    public void before() {
        this.redditApiDao = new RedditApiDao();
        this.redditApiDao.userManager = userManager;
        this.redditApiDao.restClient = restClient;
        this.redditApiDao.converter = converter;

        user = new User(null, "username", "password");
        normalResponse = new UtilResponse(
                null, aListing(
                aSubmissionWrapper("t3_redditObjName1", false, aMediaObject(), aMediaEmbedObject()),
                aSubmissionWrapper("t3_redditObjName2", false, aMediaObject(), aMediaEmbedObject())),
                200
        );
    }

    @Test
    public void shouldConnectOk() throws Exception {
        // given
        given(userManager.getUser()).willReturn(user);
        given(restClient.get(isA(String.class), (String) isNull())).willReturn(normalResponse);
        given(converter.convert(isA(List.class))).willReturn(newArrayList(aLink(), aLink()));
        SearchQuery query = new SearchQuery("test");

        // when
        SearchResult actual = redditApiDao.search(query);

        // then
        assertThat(actual.getLinks(), hasSize(2));
        verify(restClient).get(isA(String.class), (String) isNull());
    }
}
