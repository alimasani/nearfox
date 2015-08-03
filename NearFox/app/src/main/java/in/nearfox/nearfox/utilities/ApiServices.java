package in.nearfox.nearfox.utilities;

import in.nearfox.nearfox.models.DataModels;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by tangbang on 5/25/2015.
 */
public class ApiServices {

    private  static String NEWS_API = "/app_detailed_news?news_id={id}";

    public static String getNewsSharingAPI(String newsId){
        String sharingAPI = RestClient.BASE_URL + NEWS_API;
        sharingAPI = sharingAPI.replace("{id}", newsId);
        return sharingAPI;
    }

    public interface ApiService {

        @FormUrlEncoded
        @POST("/insertUser")
        void postLocation(@Field("lat") String latitude,
                          @Field("lon") String longitude,
                          @Field("device_id") String deviceId,
                          Callback<DataModels.HomeLocation> callback);

        @GET("/events")
        void getEvents(@Query("email_id") String emailId,
                       @Query("current_time_stamp") String lastTimeStamp,
                       Callback<DataModels.Events> callback);

        @GET("/events")
        void getEventsNotSignedIn(@Query("email_id") String emailId,
                                  @Query("lat") Double lat,
                                  @Query("lon") Double lon,
                                  @Query("current_time_stamp") String lastTimeStamp,
                                  Callback<DataModels.Events> callback);

        @GET("/events/category")
        void getEventsParticularCategoryNotSignedIn(@Query("email_id") String emailId,
                                         @Query("lat") Double lat,
                                         @Query("lon") Double lon,
                                         @Query("current_time_stamp") String lastTimeStamp,
                                         @Query("categories") String category,
                                         Callback<DataModels.Events> callback);

        @GET("/events/category")
        void getEventsParticularCategory(@Query("email_id") String emailId,
                                         @Query("current_time_stamp") String lastTimeStamp,
                                         @Query("categories") String category,
                                         Callback<DataModels.Events> callback);

        @GET("/news")
        void getRecentNews(@Query("off") int start,
                           @Query("limit") int numberOfItems,
                           @Query("email") String emailId,
                           @Query("current_time_stamp") String lastTimeStamp,
                           Callback<DataModels.News> callback);

        @GET("/news")
        void getRecentNewsNotSignedIn(@Query("off") int start,
                                      @Query("limit") int numberOfItems,
                                      @Query("email") String emailId,
                                      @Query("lat") Double lat,
                                      @Query("lon") Double lon,
                                      @Query("current_time_stamp") String lastTimeStamp,
                                      Callback<DataModels.News> callback);

        @GET("/zipnews/mobileapp/locality/category/")
        void getLocalityInfoParticularCategory(@Query("category") String category,
                                               Callback<DataModels.Infos> callback);

        @GET("/zipnews/mobileapp/locality/info")
        void getSingleInfo(@Query("id") String id,
                           Callback<DataModels.SingleInfo> callback);

        @GET("/app_detailed_news")
        void getSingleNews(@Query("news_id") String id,
                           Callback<DataModels.SingleNews> callback);

        @FormUrlEncoded
        @POST("/user/login")
        void addUser(@Field("firstname") String firstname,
                     @Field("lastname") String lastname,
                     @Field("profile_link") String link,
                     @Field("email") String email,
                     @Field("image_url") String image_url,
                     @Field("oauth_provider") String google_or_fb,
                     @Field("oauth_id") String oAuthId,
                     @Field("gcm_reg_id") String gcm_id,
                     @Field("device_id") String device_id,
                     Callback<DataModels.RegistedUserResult> callback);

        @FormUrlEncoded
        @POST("/user/login")
        void addUser(@Field("firstname") String firstname,
                     @Field("lastname") String lastname,
                     @Field("profile_link") String link,
                     @Field("email") String email,
                     @Field("image_url") String image_url,
                     @Field("oauth_provider") String google_or_fb,
                     @Field("oauth_id") String oAuthId,
                     @Field("gcm_reg_id") String gcm_id,
                     @Field("lat") Double latitude,
                     @Field("lon") Double longitude,
                     @Field("device_id") String device_id,
                     Callback<DataModels.RegistedUserResult> callback);

        @GET("/ayn/questions")
        void getAynQuestions(@Query("email") String emailId,
                             @Query("current_time_stamp") String currentTimeStamp,
                             Callback<DataModels.AYNQuestions> callback);

        @GET("/ayn/questions")
        void getAynQuestionsNotSignedIn(@Query("email_id") String emailId,
                                        @Query("current_time_stamp") String currentTimeStamp,
                                        @Query("lat") double lat,
                                        @Query("lon") double lon,
                                        Callback<DataModels.AYNQuestions> callback);

        @GET("/questions")
        void getAynQuestionsNew(@Query("current_time_stamp") String currentTimeStamp,
                                @Query("device_id") String deviceId,
                                Callback<DataModels.AYNQuestions> callback);

        @GET("/questions0")
        void getAynQuestionsNew0(@Query("current_time_stamp") String currentTimeStamp,
                                 @Query("device_id") String deviceId,
                                 Callback<DataModels.AYNQuestions> callback);

        @GET("/ayn/answers")
        void getAYNAnswers(@Query("email") String emailId,
                           @Query("question_id") String postId,
                           @Query("current_time_stamp") String currentTimeStamp,
                           Callback<DataModels.AYNAnswers> callback);

        @GET("/ayn/answers")
        void getAYNAnswersNotSignedIn(@Query("email") String emailId,
                                      @Query("question_id") String postId,
                                      @Query("current_time_stamp") String currentTimeStamp,
                                      Callback<DataModels.AYNAnswers> callback);

        @FormUrlEncoded
        @POST("/ayn/answer/{email}")
        void pushAnswer(@Path("email") String emailId,
                        @Field("question_id") String questionId,
                        @Field("content") String content,
                        Callback<DataModels.AYNPushAnswer> callback);

        @FormUrlEncoded
        @POST("/ayn/question/upvote/{email}")
        void questionVoteUp(@Path("email") String emailId,
                            @Field("question_id") String questionId,
                            Callback<DataModels.AYNAnswerVoteOrAnswer> callback);

        @FormUrlEncoded
        @POST("/ayn/question/downvote/{email}")
        void questionVoteDown(@Path("email") String emailId,
                              @Field("question_id") String questionId,
                              Callback<DataModels.AYNAnswerVoteOrAnswer> callback);

        @FormUrlEncoded
        @POST("/ayn/answer/upvote/{email}")
        void answerVoteUp(@Path("email") String emailId,
                          @Field("answer_id") String answerId,
                          Callback<DataModels.AYNAnswerVoteOrAnswer> callback);

        @FormUrlEncoded
        @POST("/ayn/answer/downvote/{email}")
        void answerVoteDown(@Path("email") String emailId,
                            @Field("answer_id") String answerId,
                            Callback<DataModels.AYNAnswerVoteOrAnswer> callback);


        @FormUrlEncoded
        @POST("/news/{email}")
        void postNews(@Path("email") String email,
                      @Field("title") String title,
                      @Field("content") String content,
                      @Field("image_url") String image_url,
                      @Field("locations") String locations,
                      Callback<DataModels.HomeLocation> callback) ;

        @FormUrlEncoded
        @POST("/ayn/question/{email}")
        void postQuestions(@Path("email") String email,
                           @Field("content") String data,
                           Callback<DataModels.HomeLocation> callback) ;

        @FormUrlEncoded
        @POST("/events/{email}")
        void postEvents(@Path("email") String email,
                        @Field("title") String title,
                        @Field("subtitle") String subTitle,
                        @Field("desc") String desc,
                        @Field("fees") String fees,
                        @Field("link_to_buy") String link_to_buy,
                        @Field("image_url") String imageUrl,
                        @Field("organizer_name") String organizerName,
                        @Field("organizer_email") String organizerEmail,
                        @Field("organizer_phone") String organizerNumber,
                        @Field("dates") String dates,
                        @Field("link") String link,
                        @Field("venue") String venue,
                        @Field("address") String address,


                        Callback<DataModels.HomeLocation> callback) ;

        @FormUrlEncoded
        @POST("/setUserLocation/{email}")
        void setLocation(@Path("email") String email,
                         @Field("location_name") String data,
                         Callback<DataModels.HomeLocation> callback) ;


        /*@GET("/zipnews/mobileapp/test/index.php")
        void getEvents(Callback<DataModels.Events> callback);


        @GET("/zipnews/mobileapp/test/singleEvent.php")
        void getSingleEvent(@Query("id") String id,
                                   Callback<DataModels.Event> callback) ;

        @GET("/zipnews/mobileapp/test/eventCategory.php")
        void getEventsParticularCategory(@Query("category") String category,
                                             Callback<DataModels.Events> callback) ;

        @GET("/zipnews/mobileapp/news/recentNews.php")
        void getRecentNews(@Query("start") int start,
                                  @Query("numberOfItems") int numberOfItems,
                                  Callback<DataModels.News> callback) ;


        @GET("/zipnews/mobileapp/locality/category/index.php")
        void getLocalityInfoParticularCategory(@Query("category") String category,
                                                      Callback<DataModels.Infos> callback) ;

        @GET("/zipnews/mobileapp/locality/info/index.php")
        void getSingleInfo(@Query("id") String id,
                                  Callback<DataModels.SingleInfo> callback) ;

        @GET("/zipnews/mobileapp/news/particularNewsDetails.php")
        void getSingleNews(@Query("id") String id,
                                  Callback<DataModels.SingleNews> callback) ;


        @GET("/zipnews/mobileapp/users/addUsers/index.php")
        void addUser(@Query("username") String name,
                     @Query("link") String link,
                     @Query("email") String email,
                     @Query("image_url") String image_url,
                     @Query("email_name") String google_or_fb,
                     @Query("gcm_reg_id") String gcm_id,
                     @Query("device_id") String device_id,
                     Callback<DataModels.RegistedUserResult> callback) ;

        @GET("/zipnews/mobileapp/users/addUsers/index.php")
        void addUser(@Query("username") String name,
                     @Query("link") String link,
                     @Query("email") String email,
                     @Query("image_url") String image_url,
                     @Query("email_name") String google_or_fb,
                     @Query("gcm_reg_id") String gcm_id,
                     @Query("lat") Double latitude,
                     @Query("lon") Double longitude,
                     @Query("device_id") String device_id,
                     Callback<DataModels.RegistedUserResult> callback) ;

        @GET("/zipnews/mobileapp/ayn/recentPosts/index.php")
        void getAynQuestions(@Query("device_id") String deviceId,
                         Callback<DataModels.AYNPosts> callback) ;

        @GET("/zipnews/mobileapp/ayn/recentPosts/index.php")
        void getAynQuestions0(@Query("device_id") String deviceId,
                             Callback<DataModels.AYNPosts> callback) ;
        @GET("/zipnews/mobileapp/ayn/postVoteUp/index.php")
        void questionVoteUp(@Query("device_id") String id,
                        @Query("post_id") String postId,
                        Callback<DataModels.AYNPostVoteOrComment> callback) ;

        @GET("/zipnews/mobileapp/ayn/postVoteDown/index.php")
        void questionVoteDown(@Query("device_id") String id,
                        @Query("post_id") String postId,
                        Callback<DataModels.AYNPostVoteOrComment> callback) ;

        @GET("/zipnews/mobileapp/ayn/answerVoteUp/index.php")
        void answerVoteUp(@Query("device_id") String deviceId,
                           @Query("answer_id") String answerId,
                           Callback<DataModels.AYNPostVoteOrComment> callback) ;

        @GET("/zipnews/mobileapp/ayn/answerVoteDown/index.php")
        void answerVoteDown(@Query("device_id") String deviceId,
                          @Query("answer_id") String answerId,
                          Callback<DataModels.AYNPostVoteOrComment> callback) ;

        @GET("/zipnews/mobileapp/ayn/answers/index.php")
        void getAYNAnswers(@Query("ayn_question_id") String postId,
                           @Query("device_id") String deviceId,
                            Callback<DataModels.AYNComments> callback) ;

        @GET("/zipnews/mobileapp/ayn/pushComment/index.php")
        void pushAnswer(@Query("device_id") String deviceId,
                        @Query("ayn_post_id") String postId,
                        @Query("content") String content,
                         Callback<DataModels.AYNPushAnswer> callback) ;*/
    }
}