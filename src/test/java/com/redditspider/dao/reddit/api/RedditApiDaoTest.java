package com.redditspider.dao.reddit.api;

import static com.github.jreddit.entity.Kind.LINK;
import static com.github.jreddit.entity.Kind.LISTING;
import static com.github.jreddit.entity.Kind.SUBREDDIT;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Lists.newArrayList;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aResponse;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aResponseWithChildren;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aSubmission;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aSubreddit;
import static com.redditspider.model.DomainFactory.aLink;
import static com.redditspider.model.DomainFactory.aLinkWithId;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.reddit.api.utils.UtilResponse;
import com.redditspider.model.DomainFactory;
import com.redditspider.model.SearchQuery;
import com.redditspider.model.SearchResult;
import com.redditspider.model.Subreddit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class RedditApiDaoTest {
    private RedditApiDao redditApiDao;
    @Mock
    private RedditApiUserManager userManager;
    @Mock
    private RestClient restClient;
    @Mock
    private Converter converter;
    private User user = new User(null, "username", "password");
    private UtilResponse listingResponse;
    private UtilResponse subredditResponse;

    @Before
    public void before() {
        this.redditApiDao = new RedditApiDao();
        this.redditApiDao.userManager = userManager;
        this.redditApiDao.restClient = restClient;
        this.redditApiDao.converter = converter;

        user = new User(null, "username", "password");

        listingResponse = new UtilResponse(
                null,
                aResponseWithChildren(
                        LISTING,
                        aResponse(LINK, aSubmission("link1")),
                        aResponse(LINK, aSubmission("link2"))),
                200
        );

        subredditResponse = new UtilResponse(
                null,
                aResponseWithChildren(
                        LISTING,
                        aResponse(SUBREDDIT, aSubreddit("subA")),
                        aResponse(SUBREDDIT, aSubreddit("subB"))),
                200
        );
    }

    @Test
    public void shouldConnectOk() throws Exception {
        // given
        given(userManager.getUser()).willReturn(user);
        given(restClient.get(isA(String.class), (String) isNull())).willReturn(listingResponse);
        given(converter.convert(anyCollectionOf(Submission.class))).willReturn(newArrayList(aLinkWithId("id1"), aLink()));
        SearchQuery query = new SearchQuery("test");

        // when
        SearchResult actual = redditApiDao.search(query);

        // then
        assertThat(actual.getLinks(), hasSize(2));
        assertThat(getFirst(actual.getLinks(), null).getId(), is("id1"));
        verify(restClient).get(isA(String.class), (String) isNull());
    }

    @Test
    public void shouldDiscoverSubreddits() throws Exception {
        // given
        given(userManager.getUser()).willReturn(user);
        given(restClient.get(isA(String.class), (String) isNull())).willReturn(subredditResponse);
        given(converter.convertSubreddits(anyCollectionOf(com.github.jreddit.entity.Subreddit.class))).willReturn(
                newArrayList(DomainFactory.aSubreddit(), DomainFactory.aSubreddit()));

        // when
        Collection<Subreddit> actual = redditApiDao.discoverSubreddits();

        // then
        assertThat(actual, hasSize(2));
    }


}
