package com.redditspider.dao.reddit.api;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;
import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;

import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.Subreddits;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.Subreddit;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class RedditApiDao implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditApiDao.class);
    @Autowired
    RestClient restClient;
    @Autowired
    RedditApiUserManager userManager;
    @Autowired
    Converter converter;

    private com.github.jreddit.entity.Subreddit lastDiscoveredSubreddit;


    @Override
    public SearchResult search(SearchQuery query) {
        SearchResult result = new SearchResult();
        try {
            Submissions submissions = new Submissions(restClient, userManager.getUser());
            result.setLinks(converter.convert(submissions.ofSubreddit(query.getQuery(), TOP, 0, 25, null, null, true)));
        } catch (Exception e) {
            LOG.error("Cannot search using reddit's api", e);
        }
        return result;
    }

    @Override
    public Collection<Subreddit> discoverSubreddits() {
        Collection<Subreddit> result = newArrayList();
        try {
            Subreddits subreddits = new Subreddits(restClient, userManager.getUser());
            List<com.github.jreddit.entity.Subreddit> subredditList = subreddits.get(POPULAR, 0, 100, lastDiscoveredSubreddit, null);
            if (subredditList != null && !subredditList.isEmpty()) {
                lastDiscoveredSubreddit = getLast(subredditList);
                result = converter.convertSubreddits(subredditList);
            }
        } catch (Exception e) {
            LOG.error("Cannot find subreddits using reddit's api", e);
        }

        return result;
    }
}
