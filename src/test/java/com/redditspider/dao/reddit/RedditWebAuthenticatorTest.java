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
public class RedditWebAuthenticatorTest {
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement webElement;

    private RedditWebAuthenticator redditWebAuthenticator;

    @Before
    public void before() {
        this.redditWebAuthenticator = new RedditWebAuthenticator();
        redditWebAuthenticator.name = "test";
        redditWebAuthenticator.password = "testpass";
        redditWebAuthenticator.afterLoginSleepSeconds = 0;
    }

    @Test
    public void shouldBeLoggedIn() {
        // given
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.getText()).willReturn("logged in as test");

        // when
        boolean actual = redditWebAuthenticator.isLoggedIn(driver);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeLoggedIn() {
        // given
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.getText()).willReturn("login here");

        // when
        boolean actual = redditWebAuthenticator.isLoggedIn(driver);

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void shouldLogin() {
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        redditWebAuthenticator.login(driver);

        // then
        verify(driver).get("https://www.reddit.com/login");
        verify(driver, times(3)).findElement(isA(By.class));
    }

    @Ignore
    @Test
    public void realTest() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.reddit.com/");

        if (!redditWebAuthenticator.isLoggedIn(driver)) {
            redditWebAuthenticator.login(driver);
        }

        driver.get("http://www.reddit.com/");

        if (!redditWebAuthenticator.isLoggedIn(driver)) {
            redditWebAuthenticator.login(driver);
        }
    }
}
