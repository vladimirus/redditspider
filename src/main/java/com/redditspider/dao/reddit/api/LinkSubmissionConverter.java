package com.redditspider.dao.reddit.api;

import static com.google.common.collect.FluentIterable.from;

import com.github.jreddit.entity.Submission;
import com.google.common.base.Function;
import com.redditspider.model.Link;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * Class that converts between a link and a submission.
 */
@Component
public class LinkSubmissionConverter {

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
        link.setCommentsUri(submission.getPermalink());
        link.setTitle(submission.getTitle());
        link.setUp(submission.getUpVotes().intValue());
        link.setDown(submission.getDownVotes().intValue());
        link.setSubreddit(submission.getSubreddit());
        link.setCreated(new Date(submission.getCreatedUTC().longValue()));
        return link;
    }
}
