package com.redditspider.dao.reddit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Test for RedditAuthenticator.
 */
@RunWith(MockitoJUnitRunner.class)
public class RedditAuthenticatorTest {
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement webElement;

    private RedditAuthenticator redditAuthenticator;

    @Before
    public void before() {
        this.redditAuthenticator = new RedditAuthenticator();
        redditAuthenticator.name = "test";
        redditAuthenticator.password = "testpass";
        redditAuthenticator.afterLoginSleepSeconds = 0;
    }

    @Test
    public void shouldBeLoggedIn() {
        // given
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.getText()).willReturn("logged in as test");

        // when
        boolean actual = redditAuthenticator.isLoggedIn(driver);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeLoggedIn() {
        // given
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.getText()).willReturn("login here");

        // when
        boolean actual = redditAuthenticator.isLoggedIn(driver);

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void shouldLogin() {
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        redditAuthenticator.login(driver);

        // then
        verify(driver).get("https://www.reddit.com/login");
        verify(driver, times(3)).findElement(isA(By.class));
    }

    @Ignore
    @Test
    public void realTest() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.reddit.com/");

        if (!redditAuthenticator.isLoggedIn(driver)) {
            redditAuthenticator.login(driver);
        }

        driver.get("http://www.reddit.com/");

        if (!redditAuthenticator.isLoggedIn(driver)) {
            redditAuthenticator.login(driver);
        }
    }
}
