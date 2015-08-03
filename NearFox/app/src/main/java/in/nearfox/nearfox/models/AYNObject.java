package in.nearfox.nearfox.models;

import android.util.Log;

/**
 * Created by tangbang on 6/19/2015.
 */
public class AYNObject {
    private static String TAG = "MyDebug" ;

    private String userName ;
    private String period ;
    private String userImageURL ;
    private String imageURL ;
    private String title ;
    private String upVoteCount ;
    private String downVoteCount ;
    private String commentCount ;
    private String id ;
    private boolean whetherUpvoted ;
    private boolean whetherDownvoted ;
    private boolean whetherCommented ;

    public AYNObject(){
    }

    public AYNObject(String userName, String period, String userImageURL, String imageURL, String title, String upVoteCount, String downVoteCount, String id, boolean whetherUpvoted, boolean whetherDownvoted, boolean whetherCommented, String commentCount){
        this.userName = userName ;
        this.period = period ;
        this.userImageURL = userImageURL ;
        this.imageURL = imageURL ;
        this.title = title ;
        this.upVoteCount = upVoteCount ;
        this.downVoteCount = downVoteCount ;
        this.commentCount = commentCount ;
        this.id = id ;
        this.whetherUpvoted = whetherUpvoted ;
        this.whetherDownvoted = whetherDownvoted ;
        Log.d(TAG, "whether commented: " + whetherCommented) ;
        this.whetherCommented = whetherCommented ;
    }


    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public void setWhetherCommented(boolean whetherCommented) {
        this.whetherCommented = whetherCommented;
    }

    public String getCommentCount() {

        return commentCount;
    }

    public boolean isWhetherCommented() {
        return whetherCommented;
    }

    public void setWhetherUpvoted(boolean whetherUpvoted) {
        this.whetherUpvoted = whetherUpvoted;
    }

    public void setWhetherDownvoted(boolean whetherDownvoted) {
        this.whetherDownvoted = whetherDownvoted;
    }

    public boolean isWhetherUpvoted() {

        return whetherUpvoted;
    }

    public boolean isWhetherDownvoted() {
        return whetherDownvoted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setUpVoteCount(String upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public void setDownVoteCount(String downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public String getUpVoteCount() {

        return upVoteCount;
    }

    public String getDownVoteCount() {
        return downVoteCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {

        return userName;
    }

    public String getPeriod() {
        return period;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

}