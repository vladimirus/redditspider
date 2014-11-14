package com.redditspider.dao.reddit.api;

import static com.redditspider.dao.reddit.api.utils.JsonHelpers.createMediaEmbedObject;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.createMediaObject;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.createSubmission;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.redditListing;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.reddit.api.utils.UtilResponse;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedditApiDaoTest {
    private RedditApiDao redditApiDao;
    @Mock
    private RedditApiUserManager userManager;
    @Mock
    private RestClient restClient;
    private User user = new User(null, "username", "password");
    private UtilResponse normalResponse;

    @Before
    public void before() {
        this.redditApiDao = new RedditApiDao();
        this.redditApiDao.userManager = userManager;
        this.redditApiDao.restClient = restClient;

        user = new User(null, "username", "password");
        normalResponse = new UtilResponse(null, submissionListings(), 200);
    }

    @Ignore
    @Test
    public void should() throws Exception {
        // given
        given(userManager.getUser()).willReturn(user);
        given(restClient.get(isA(String.class), (String) isNull())).willReturn(normalResponse);
        SearchQuery query = new SearchQuery("test");

        // when
        SearchResult actual = redditApiDao.search(query);

        // then
        assertThat(actual.getLinks(), hasSize(2));

    }

    private JSONObject submissionListings() {
        JSONObject media = createMediaObject();
        JSONObject mediaEmbed = createMediaEmbedObject();
        JSONObject submission1 = createSubmission("t3_redditObjName1", false, media, mediaEmbed);
        JSONObject submission2 = createSubmission("t3_redditObjName2", false, media, mediaEmbed);
        return redditListing(submission1, submission2);
    }
}
