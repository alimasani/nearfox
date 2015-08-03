package in.nearfox.nearfox.models;


/**
 * Created by tangbang on 6/23/2015.
 */
public class AYNAnswerObject {
    private String id ;
    private String username ;
    private String user_image_url ;
    private String comment_content ;
    private boolean whether_upvoted ;
    private String upvote_count ;
    private boolean whether_downvoted ;
    private String downvote_count ;
    private String period ;


    public AYNAnswerObject(String id, String username, String user_image_url, String comment_content, boolean whether_upvoted, String upvote_count, boolean whether_downvoted, String downvote_count, String period){
        this.id = id ;
        this.username = username ;
        this.user_image_url = user_image_url ;
        this.comment_content = comment_content ;
        this.whether_upvoted = whether_upvoted ;
        this.upvote_count = upvote_count ;
        this.whether_downvoted = whether_downvoted ;
        this.downvote_count = downvote_count ;
        this.period = period ;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setWhether_upvoted(boolean whether_upvoted) {
        this.whether_upvoted = whether_upvoted;
    }

    public void setWhether_downvoted(boolean whether_downvoted) {
        this.whether_downvoted = whether_downvoted;
    }

    public boolean isWhether_upvoted() {

        return whether_upvoted;
    }

    public boolean isWhether_downvoted() {

        return whether_downvoted;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {

        return period;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }


    public void setUpvote_count(String upvote_count) {
        this.upvote_count = upvote_count;
    }


    public void setDownvote_count(String downvote_count) {
        this.downvote_count = downvote_count;
    }

    public String getUsername() {

        return username;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public String getComment_content() {
        return comment_content;
    }


    public String getUpvote_count() {
        return upvote_count;
    }

    public String getDownvote_count() {
        return downvote_count;
    }
}