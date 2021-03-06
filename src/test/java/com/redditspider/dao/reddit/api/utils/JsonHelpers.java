package com.redditspider.dao.reddit.api.utils;

import static java.util.Collections.addAll;

import com.github.jreddit.entity.Kind;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked") //JSONSimple is not great..
public class JsonHelpers {

    /**
     * Create a JSON Object which has the structure and contents
     * of the User Login response
     *
     * @param cookie  Cookie
     * @param modHash Modulo hash of the session identifier
     * @return JSON Object with Reddit User Login object structure and contents.
     */
    public static JSONObject userLoginResponse(String cookie, String modHash) {
        JSONObject data = new JSONObject();
        data.put("cookie", cookie);
        data.put("modhash", modHash);

        JSONObject json = new JSONObject();
        json.put("data", data);
        json.put("errors", new JSONArray());

        JSONObject root = new JSONObject();
        root.put("json", json);

        return root;
    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a User Info object.
     *
     * @param username Username
     * @return JSON Object with Reddit User Info object structure and contents.
     */
    public static JSONObject aUserInfo(String username) {
        JSONObject data = new JSONObject();
        data.put("comment_karma", 0L);
        data.put("created", 1395606076.0);
        data.put("created_utc", 1395577276.0);
        data.put("has_mail", true);
        data.put("has_mod_mail", false);
        data.put("has_verified_email", false);
        data.put("id", "fte4m");
        data.put("is_friend", false);
        data.put("is_gold", false);
        data.put("is_mod", false);
        data.put("link_karma", 1L);
        data.put("modhash", "modhash");
        data.put("name", username);
        data.put("over_18", false);
        return data;
    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a User About object.
     *
     * @param username Username
     * @return JSON Object with Reddit User About object structure and contents.
     */
    public static JSONObject aUserAbout(String username) {
        JSONObject data = new JSONObject();
        data.put("comment_karma", 0L);
        data.put("created", 1395606076.0);
        data.put("created_utc", 1395577276.0);
        data.put("has_verified_email", false);
        data.put("id", "fte4m");
        data.put("is_friend", false);
        data.put("is_gold", false);
        data.put("is_mod", false);
        data.put("link_karma", 1L);
        data.put("name", username);
        return data;
    }

    /**
     * Create a Reddit Listing JSON Object.
     * @param kind - type of response
     * @param children Children of the object
     * @return wrapped object
     */
    public static JSONObject aResponseWithChildren(Kind kind, JSONObject... children) {
        JSONObject data = new JSONObject();
        data.put("after", null);
        data.put("before", null);
        data.put("children", jsonArrayOf(children));
        data.put("modhash", "");
        return aResponse(kind, data);
    }

    /**
     * Single Reddit Listing JSON Object.
     * @param kind - type of object
     * @param data - the body of the object
     * @return wrapped object
     */
    public static JSONObject aResponse(Kind kind, JSONObject data) {
        JSONObject root = new JSONObject();
        root.put("data", data);
        root.put("kind", kind.value());
        return root;
    }

    /**
     * Create a JSON Reddit error object.
     *
     * @param errorCode Error code
     * @return Reddit Error JSON object
     */
    public static JSONObject aRedditError(int errorCode) {
        JSONObject data = new JSONObject();
        data.put("error", errorCode);
        return data;
    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a Reddit Message object.
     *
     * @param author     Author
     * @param messageId  Identifier
     * @param parentId   Parent identifier
     * @param newFlag    Whether it is new
     * @param wasComment Whether it was a comment
     * @return JSON Object with Reddit Message object structure and contents.
     */
    public static JSONObject aMessage(String author, String messageId, String parentId, boolean newFlag, boolean wasComment) {
        JSONObject data = new JSONObject();
        data.put("author", author);
        data.put("body", "message body");
        data.put("body_html", "&lt;!-- SC_OFF --&gt;&lt;div class=\"md\"&gt;&lt;p&gt;message body&lt;/p&gt;\n&lt;/div&gt;&lt;!-- SC_ON --&gt;");
        data.put("context", "");
        data.put("created", 1395989716.0);
        data.put("created_utc", 1395989716.0);
        data.put("dest", "destinationUser");
        data.put("first_message", null);
        data.put("first_message_name", null);
        data.put("id", messageId);
        data.put("name", "t4_" + messageId);
        data.put("new", newFlag);
        data.put("parent_id", parentId);
        data.put("replies", "");
        data.put("subject", "TestMessage");
        data.put("subreddit", null);
        data.put("was_comment", wasComment);
        return data;
    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a Reddit Comment object.
     *
     * @param fullName Full name of the comment
     * @param id       Identifier of the comment
     * @param parentId Identifier of the parent
     * @param author   Name of the author
     * @return JSON Object with Reddit Comment object structure and contents.
     */
    public static JSONObject aComment(String fullName, String id, String parentId, String author, String body, JSONObject replies) {
        JSONObject data = new JSONObject();
        data.put("subreddit_id", "SubrID");
        data.put("banned_by", null);
        data.put("subreddit", "SubredditName");
        data.put("likes", null);
        data.put("replies", replies);
        data.put("saved", false);
        data.put("id", id);
        data.put("gilded", 0);
        data.put("author", author);
        data.put("parent_id", parentId);
        data.put("score", 2);
        data.put("approved_by", null);
        data.put("controversiality", 0);
        data.put("body", body);
        data.put("edited", false);
        data.put("author_flair_css_class", null);
        data.put("downs", 0);
        data.put("body_html", "&lt;div&gt;" + body + "&lt;/div&gt;");
        data.put("link_id", "LinkIdentifer");
        data.put("score_hidden", "false");
        data.put("name", fullName);
        data.put("created", 1404969798.0);
        data.put("author_flair_text", null);
        data.put("created_utc", 1404940998.0);
        data.put("ups", "2");
        data.put("num_reports", null);
        data.put("distinguished", null);
        return data;
    }

    public static JSONObject aSubmission(String title) {
        JSONObject data = new JSONObject();
        data.put("approved_by", null);
        data.put("author", "vladimirus");
        data.put("author_flair_css_class", null);
        data.put("author_flair_text", null);
        data.put("banned_by", null);
        data.put("clicked", false);
        data.put("created", 1374180782.0);
        data.put("created_utc", 1374177182.0);
        data.put("distinguished", null);
        data.put("domain", "example.com");
        data.put("downs", 0L);
        data.put("edited", false);
        data.put("gilded", 0);
        data.put("hidden", false);
        data.put("id", "1ikxpg");
        data.put("is_self", false);
        data.put("likes", true);
        data.put("link_flair_css_class", null);
        data.put("link_flair_text", null);
        data.put("media", aMediaObject());
        data.put("media_embed", aMediaEmbedObject());
        data.put("name", "t3_" + title);
        data.put("num_comments", 0L);
        data.put("num_reports", 0);
        data.put("over_18", false);
        data.put("permalink", "/r/permalink/");
        data.put("saved", false);
        data.put("score", 1L);
        data.put("secure_media", null);
        data.put("secure_media_embed", new JSONObject());
        data.put("selftext", "");
        data.put("selftext_html", null);
        data.put("stickied", false);
        data.put("subreddit", "jReddit");
        data.put("subreddit_id", "t5_2xwsy");
        data.put("thumbnail", "");
        data.put("title", title);
        data.put("ups", 1L);
        data.put("url", "https://example.com/something");
        data.put("visited", false);
        return data;
    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a Reddit Subreddit object.
     *
     * @param displayName   Display name
     * @return JSON Object with Reddit Subreddit object structure and contents.
     */
    public static JSONObject aSubreddit(String displayName) {
        JSONObject data = new JSONObject();
        data.put("submit_text_html", null);
        data.put("user_is_banned", null);
        data.put("id", displayName + "ID");
        data.put("submit_text", "");
        data.put("display_name", displayName);
        data.put("header_img", "http://a.thumbs.redditmedia.com/yyL5sveWcgkCPKbr.png");
        data.put("description_html", "&lt;div&gt; description of " + displayName + "&gt;/div&lt;");
        data.put("title", "title of " + displayName);
        data.put("over18", false);
        data.put("user_is_moderator", null);
        data.put("header_title", "Header title of " + displayName);
        data.put("description", "Welcome to " + displayName + ".\nSome rules...");
        data.put("submit_link_label", null);
        data.put("accounts_active", null);
        data.put("public_traffic", true);
        data.put("header_size", jsonArrayOf(160, 64));
        data.put("subscribers", 2525);
        data.put("submit_text_label", null);
        data.put("name", "t5_" + displayName + "AID");
        data.put("created", 1201242956.0);
        data.put("url", "/r/" + displayName + "/");
        data.put("created_utc", 1201242956.0);
        data.put("user_is_contributor", null);
        data.put("public_description", "");
        data.put("comment_score_hide_mins", 0);
        data.put("subreddit_type", "public");
        data.put("subreddit_type", "any");
        data.put("user_is_subscriber", null);
        return data;

    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a Reddit media object.
     *
     * @return JSON Object with Reddit Media object structure and contents.
     */
    public static JSONObject aMediaObject() {
        JSONObject oembed = new JSONObject();
        oembed.put("author_name", "Imgur");
        oembed.put("author_url", "http://imgur.com/user/Imgur");
        oembed.put("description", "Imgur is home to the web's most popular image content, curated in real time by a dedicated community through commenting, voting and sharing.");
        oembed.put("height", 550);
        oembed.put("html", "&lt;iframe class=\"embedly-embed\" src=\"//cdn.embedly.com/widgets/media.html?src=http%3A%2F%2Fimgur.com%2Fa%2FPs7Ta%2Fembed&amp;"
            + "url=http%3A%2F%2Fimgur.com%2Fa%2FPs7Ta%3F&amp;image=http%3A%2F%2Fi.imgur.com%2FtSrCkSB.jpg&amp;key=2aa3c4d5f3de4f5b9120b660ad850dc9&amp;"
            + "type=text%2Fhtml&amp;schema=imgur\" width=\"550\" height=\"550\" scrolling=\"no\" frameborder=\"0\" allowfullscreen&gt;&lt;/iframe&gt;");
        oembed.put("provider_name", "Imgur");
        oembed.put("provider_url", "http://imgur.com");
        oembed.put("thumbnail_height", 350);
        oembed.put("thumbnail_url", "http://i.imgur.com/tSrCkSB.jpg");
        oembed.put("thumbnail_width", 600);
        oembed.put("title", "$9000 Dream Home - Imgur");
        oembed.put("type", "rich");
        oembed.put("version", "1.0");
        oembed.put("width", 550);

        JSONObject mediaObject = new JSONObject();
        mediaObject.put("oembed", oembed);
        mediaObject.put("type", "imgur.com");
        return mediaObject;
    }

    /**
     * Create a JSON Object which has the structure and contents
     * of a embed Reddit media object.
     *
     * @return JSON Object with emebed Reddit Media object structure and contents.
     */
    public static JSONObject aMediaEmbedObject() {
        JSONObject mediaEmbedObject = new JSONObject();
        mediaEmbedObject.put("content", "&lt;iframe class=\"embedly-embed\" src=\"//cdn.embedly.com/widgets/media.html?src=http%3A%2F%2Fimgur.com%2Fa%2FPs7Ta%2Fembed&amp;"
            + "url=http%3A%2F%2Fimgur.com%2Fa%2FPs7Ta%3F&amp;image=http%3A%2F%2Fi.imgur.com%2FtSrCkSB.jpg&amp;key=2aa3c4d5f3de4f5b9120b660ad850dc9&amp;type=text%2Fhtml&amp;"
            + "schema=imgur\" width=\"550\" height=\"550\" scrolling=\"no\" frameborder=\"0\" allowfullscreen&gt;&lt;/iframe&gt;");
        mediaEmbedObject.put("height", 550);
        mediaEmbedObject.put("scrolling", false);
        mediaEmbedObject.put("width", 550);
        return mediaEmbedObject;
    }

    /**
     * Create an JSON array with the given objects as content.
     *
     * @param args Objects
     * @return JSON array with the given objects in it
     */
    public static JSONArray jsonArrayOf(Object... args) {
        JSONArray array = new JSONArray();
        addAll(array, args);
        return array;
    }
}
