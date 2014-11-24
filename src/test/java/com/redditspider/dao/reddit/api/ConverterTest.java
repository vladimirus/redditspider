package com.redditspider.dao.reddit.api;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aSubmission;
import static com.redditspider.dao.reddit.api.utils.JsonHelpers.aSubreddit;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.github.jreddit.entity.Submission;
import com.redditspider.model.Link;
import com.redditspider.model.Subreddit;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class ConverterTest {
    private Converter converter;


    @Before
    public void before() {
        this.converter = new Converter();
    }

    @Test
    public void shouldConvertLink() throws Exception {
        // given
        Submission submission = new Submission(aSubmission("link1"));

        // when
        Link actual = converter.convert(submission);

        // then
        assertThat(actual.getTitle(), is("link1"));
    }

    @Test
    public void shouldConvertLinks() throws Exception {
        // given
        Submission submission1 = new Submission(aSubmission("link1"));
        Submission submission2 = new Submission(aSubmission("link2"));

        // when
        Collection<Link> actual = converter.convert(newArrayList(submission1, submission2));

        // then
        assertThat(actual, hasSize(2));
        assertThat(getFirst(actual, null).getTitle(), is("link1"));
        assertThat(getLast(actual, null).getTitle(), is("link2"));
    }

    @Test
    public void shouldConvertSubreddit() throws Exception {
        // given
        com.github.jreddit.entity.Subreddit subreddit = new com.github.jreddit.entity.Subreddit(aSubreddit("subA"));

        // when
        Subreddit actual = converter.convert(subreddit);

        // then
        assertThat(actual.getName(), is("subA"));
    }

    @Test
    public void shouldConvertSubreddits() throws Exception {
        // given
        com.github.jreddit.entity.Subreddit subreddit1 = new com.github.jreddit.entity.Subreddit(aSubreddit("subA"));
        com.github.jreddit.entity.Subreddit subreddit2 = new com.github.jreddit.entity.Subreddit(aSubreddit("subB"));

        // when
        Collection<Subreddit> actual = converter.convertSubreddits(newArrayList(subreddit1, subreddit2));

        // then
        assertThat(actual, hasSize(2));
        assertThat(getFirst(actual, null).getName(), is("subA"));
        assertThat(getLast(actual, null).getName(), is("subB"));
    }
}