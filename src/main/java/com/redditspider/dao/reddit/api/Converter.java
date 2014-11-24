package com.redditspider.dao.reddit.api;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;

import com.github.jreddit.entity.Submission;
import com.google.common.base.Function;
import com.redditspider.model.Link;
import com.redditspider.model.Subreddit;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * Class that converts between a link and a submission.
 */
@Component
public class Converter {

    Collection<Link> convert(Collection<Submission> submissions) {
        return from(submissions).transform(new Function<Submission, Link>() {
            @Override
            public Link apply(Submission input) {
                return convert(input);
            }
        }).toList();
    }

    Link convert(Submission submission) {
        Link link = new Link();
        link.setUri(submission.getURL());
        link.setPermalink(submission.getPermalink());
        link.setTitle(submission.getTitle());
        link.setUp(submission.getUpVotes().intValue());
        link.setDown(submission.getDownVotes().intValue());
        link.setSubreddit(submission.getSubreddit());
        link.setCreated(new Date(submission.getCreatedUTC().longValue() * 1000));
        return link;
    }

    Collection<Subreddit> convertSubreddits(Collection<com.github.jreddit.entity.Subreddit> subreddits) {
        Collection<Subreddit> result = newArrayList();
        if (subreddits != null) {
            result = from(subreddits).transform(new Function<com.github.jreddit.entity.Subreddit, Subreddit>() {
                @Override
                public Subreddit apply(com.github.jreddit.entity.Subreddit input) {
                    return convert(input);
                }
            }).toList();
        }

        return result;
    }

    Subreddit convert(com.github.jreddit.entity.Subreddit input) {
        Subreddit subreddit = new Subreddit();
        subreddit.setId(input.getIdentifier());
        subreddit.setName(input.getDisplayName());
        subreddit.setUpdated(new Date());
        subreddit.setCreated(new Date((long) input.getCreatedUTC() * 1000));
        return subreddit;
    }
}
