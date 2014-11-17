package com.redditspider.dao.reddit.api;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RedditApiDao implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditApiDao.class);
    @Autowired
    RestClient restClient;
    @Autowired
    RedditApiUserManager userManager;
    @Autowired
    LinkSubmissionConverter converter;

    @Override
    public SearchResult search(SearchQuery query) {
        SearchResult result = new SearchResult();
        try {
            Submissions submissions = new Submissions(restClient, userManager.getUser());
            result.setLinks(converter.convert(submissions.ofSubreddit(query.getSearchUri(), TOP, 0, 25, null, null, true)));
        } catch (Exception e) {
            LOG.error("Cannot search using reddit's api", e);
        }
        return result;
    }
}
