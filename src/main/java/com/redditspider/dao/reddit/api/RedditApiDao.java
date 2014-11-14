package com.redditspider.dao.reddit.api;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;
import static com.google.common.collect.FluentIterable.from;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.utils.restclient.RestClient;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

public class RedditApiDao implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditApiDao.class);
    @Autowired
    RestClient restClient;
    @Autowired
    RedditApiUserManager userManager;

    @Override
    public SearchResult search(SearchQuery query) {
        SearchResult result = new SearchResult();

        try {
            Submissions submissions = new Submissions(restClient, userManager.getUser());
            List<Submission> submissionList = submissions.ofSubreddit("flowers", TOP, -1, 100, null, null, true);
            result.setLinks(convert(submissionList));
        } catch (Exception e) {
            LOG.error("Cannot search using reddit's api", e);
            Throwables.propagate(e); //TODO: remove
        }

        return result;
    }

    private Collection<Link> convert(List<Submission> submissions) {
        return from(submissions).transform(new Function<Submission, Link>() {
            @Override
            public Link apply(Submission input) {
                return convert(input);
            }
        }).toList();
    }

    private Link convert(Submission submission) {
        // TODO Auto-generated method stub
        return null;
    }
}
