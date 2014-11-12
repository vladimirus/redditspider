package com.redditspider.dao.reddit.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.Response;
import com.github.jreddit.utils.restclient.RestClient;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedditApiUserManagerTest {
    private RedditApiUserManager userManager;
    @Mock
    private RestClient restClient;
    @Mock
    private Response response;
    @Mock
    private JSONObject jsonObject;

    @Before
    public void before() {
        this.userManager = new RedditApiUserManager();
        userManager.restClient = restClient;
        userManager.username = "myUsername";
        userManager.password = "myPassword";
    }

    @Test
    public void shouldGetUser() throws Exception {
        // given
        given(restClient.post(anyString(), anyString(), anyString())).willReturn(response);
        given(response.getResponseObject()).willReturn(jsonObject);
        given(jsonObject.get(isA(String.class))).willReturn(jsonObject);
        given(jsonObject.toString()).willReturn("test");

        // when
        User user = userManager.getUser();

        // then
        assertThat(user.getModhash(), is("test"));
        assertThat(user.getUsername(), is("myUsername"));
    }
}