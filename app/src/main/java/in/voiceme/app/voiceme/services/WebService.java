package in.voiceme.app.voiceme.services;

import android.net.Uri;

import java.util.List;

import in.voiceme.app.voiceme.DTO.BaseResponse;
import in.voiceme.app.voiceme.DTO.ContactAddResponse;
import in.voiceme.app.voiceme.DTO.LoginResponse;
import in.voiceme.app.voiceme.DTO.OnlyToken;
import in.voiceme.app.voiceme.DTO.PostLikesResponse;
import in.voiceme.app.voiceme.DTO.PostSuperUserListModel;
import in.voiceme.app.voiceme.DTO.PostUserCommentModel;
import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.DTO.ProfileAboutMe;
import in.voiceme.app.voiceme.DTO.ProfileFollowerUserList;
import in.voiceme.app.voiceme.DTO.ProfileUserList;
import in.voiceme.app.voiceme.DTO.ReportResponse;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.DTO.ChatDialogPojo;
import in.voiceme.app.voiceme.DTO.MessagePojo;
import in.voiceme.app.voiceme.DTO.AllCategoryPojo;
import in.voiceme.app.voiceme.DTO.AllPopularTagsPojo;
import in.voiceme.app.voiceme.DTO.NewCategoryAdded;
import in.voiceme.app.voiceme.NotificationsPage.NotificationPojo;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface WebService {

    @GET("posts.php")
    Observable<List<PostsModel>> getLatestFeed(@Query("user_id") String userID,
                                               @Query("page") int page);

    @GET("get_messages_new.php")
    Observable<List<MessagePojo>> getChatMessages(@Query("from_user_id") String userID,
                                                  @Query("to_user_id") String toUserID);

    @GET("get_all_chats_new.php")
    Observable<List<ChatDialogPojo>> getAllChatMessages(@Query("user_id") String userID);

    @GET("get_hashtags.php")
    Observable<List<AllCategoryPojo>> getAllHashTags();

    @GET("get_trending_hastags_new.php")
    Observable<List<AllPopularTagsPojo>> getPopularHashTags();

    // Todo donot know about this call
    @GET("posts.php")
    Observable<List<PostsModel>> getFollowers(@Query("user_id") String userID,
                                              @Query("follower") String user_id);

    @GET("posts.php")
    Observable<List<PostsModel>> getPopulars(@Query("user_id") String userID,
                                             @Query("popular") String booleann,
                                             @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getTrending(@Query("user_id") String userID,
                                             @Query("trending") String booleann,
                                             @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getSinglePost(@Query("id_posts") String booleann,
                                               @Query("user_id") String user_id);

    @GET("posts.php")
    Observable<List<PostsModel>> getSingleUserPosts(@Query("id_user") String id_user,
                                                    @Query("user_id") String user_id,
                                                    @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getUserFollowerPost(@Query("follower") String follower,
                                                     @Query("user_id") String user_id,
                                                     @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getActivityPosts(@Query("id_user") String id_user,
                                                  @Query("filtered") String filtered,
                                                  @Query("user_id") String user_id,
                                                  @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getContactPosts(@Query("id_user_name") String id_user_name,
                                                 @Query("user_id") String user_id,
                                                 @Query("contacts") String contacts,
                                                 @Query("page") int page);

    @GET("get_comments_new.php")
    Observable<List<PostUserCommentModel>> getUserComments(
            @Query("id_posts") String id_posts);



    @GET("posts.php")
    Observable<List<PostsModel>> getEmotionPosts(@Query("feeling_id") String feeling_id,
                                                 @Query("user_id") String userID,
                                                 @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getCategoryPosts(@Query("category_id") String category_id,
                                                  @Query("user_id") String userID,
                                                  @Query("page") int page);


    @FormUrlEncoded
    @POST("likes.php")
    Observable<PostLikesResponse> likes(@Field("user_id") String userId,
                                        @Field("post_id") String postId,
                                        @Field("like") int like,
                                        @Field("hug") int hug,
                                        @Field("same") int same,
                                        @Field("listen") int listen);

    @FormUrlEncoded
    @POST("aboutme.php")
    Observable<ProfileAboutMe> LoginUserName(@Field("user_id") String userId,
                                             @Field("username") String username,
                                             @Field("about_me") String about_me,
                                             @Field("token") String token);

    @FormUrlEncoded
    @POST("tokenonly.php")
    Observable<OnlyToken> onlyToken(@Field("user_id") String userId,
                                    @Field("token") String token);


    @FormUrlEncoded
    @POST("unlike.php")
    Observable<PostLikesResponse> unlikes(@Field("user_id") String userId,
                                          @Field("post_id") String postId,
                                          @Field("like") int like,
                                          @Field("hug") int hug,
                                          @Field("same") int same,
                                          @Field("audio") int listen);

    @FormUrlEncoded
    @POST("login_new_current_latest.php")
    Observable<LoginResponse> login(
            @Field("name") String name,
            @Field("email") String email,
            @Field("user_id") String userId
    );

    /*
    @FormUrlEncoded
    @POST("login_new_current.php")
    Observable<LoginResponse> loginWithoutProfile(
            @Field("name") String name,
            @Field("email") String email,
            @Field("user_id") String userId
    );
    */

    @FormUrlEncoded
    @POST("postStatus.php")
    Observable<UserResponse> postStatus(
            @Field("user_id") String user_id,
            @Field("post_text") String postStatus,
            @Field("cat_id") String cat_id,
            @Field("feeling_id") String feeling_id,
            @Field("audio") String audio,
            @Field("audio_duration") String audio_duration
    );



    @GET("get_likers.php")
    Observable<PostSuperUserListModel> getInteractionPosts(
            @Query("id_posts") String id_posts);

    @GET("get_notification.php")
    Observable<List<NotificationPojo>> getNotificationPosts(
            @Query("id_user_name") String id_posts,
            @Query("page") String page);

    @FormUrlEncoded
    @POST("register_mobile.php")
    Observable<BaseResponse> registerMobile(
            @Field("id_user_name") String user_id,
            @Field("phone_number") String phone_number
    );

    // Todo adding all contacts mobile left
    @FormUrlEncoded
    @POST("register_user_contacts.php")
    Observable<ContactAddResponse> addAllContacts(
            @Field("id_user_name") String user_id,
            @Field("contacts") String contacts
    );

    @FormUrlEncoded
    @POST("follower.php")
    Observable<UserResponse> addFollower(
            @Field("user_id") String user_id,
            @Field("follower_id") String follower_id,
            @Field("action") String action
    );


    @GET("follower.php")
    Observable<ProfileFollowerUserList> getUserFollow(@Query("user_id") String user_id);

    @GET("follower.php")
    Observable<ProfileFollowerUserList> getUserFollowing(@Query("follower_id") String feeling_id);

    @GET("get_user.php")
    Observable<ProfileUserList> getUserProfile(
            @Query("user_id") String user_id);

    @GET("check_username_availability.php")
    Observable<String> checkUsername(
            @Query("user_id") String user_id);

    @GET("get_user.php")
    Observable<ProfileUserList> getOtherUserProfile(
            @Query("user_id") String user_id,
            @Query("follow_id") String follower);

    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST("audio_upload/index.php")
    Observable<String> uploadFile(@Part MultipartBody.Part file);


    @GET("http://voiceme.us-east-1.elasticbeanstalk.com/fcm")
    Observable<String> getResponse(
            @Query("dataMsg") String dataMessage,
            @Query("chatText") String chatText
    );

    @GET("http://voiceme.us-east-1.elasticbeanstalk.com/fcm")
    Observable<String> sendLikeNotification(
            @Query("dataMsg") String dataMessage
    );

    @GET("http://voiceme.us-east-1.elasticbeanstalk.com/fcm")
    Observable<String> sendFollowNotification(
            @Query("dataMsg") String dataMessage
    );

    @FormUrlEncoded
    @POST("postComments.php")
    Observable<UserResponse> sendComment(
            @Field("id_user_name") String user_id,
            @Field("id_post_user_name") String id_post_user_name,
            @Field("id_posts") String id_posts,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("report_abuse.php")
    Observable<ReportResponse> reportAbuse(
            @Field("id_user_name") String id_user_name,
            @Field("sender_user_id") String sender_user_id,
            @Field("id_posts") String id_posts,
            @Field("abuse_message") String message
    );

    @FormUrlEncoded
    @POST("insertCategory_new.php")
    Observable<NewCategoryAdded> insertCategory(
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("update_post.php")
    Observable<ReportResponse> deletePost(
            @Field("id_posts") String category,
            @Field("action") String action
    );

    @FormUrlEncoded
    @POST("update_contact.php")
    Observable<ReportResponse> updateContact(
            @Field("id_user_name") String id_user_name,
            @Field("contact") String contact
    );

    @FormUrlEncoded
    @POST("update_post.php")
    Observable<ReportResponse> updatePost(
            @Field("id_posts") String category,
            @Field("action") String action,
            @Field("text_status") String text_status
    );

    @FormUrlEncoded
    @POST("update_profile.php")
    Observable<ReportResponse> updateProfile(
            @Field("id_user_name") String id_user_name,
            @Field("avatar_url") Uri avatar_url,
            @Field("user_nick_name") String user_nick_name,
            @Field("age") String age,
            @Field("gender") String gender,
            @Field("about_me") String about_me
    );

    @FormUrlEncoded
    @POST("delete_chat.php")
    Observable<UserResponse> deleteChat(
            @Field("messageId") String messageId
    );

    @FormUrlEncoded
    @POST("delete_comment.php")
    Observable<UserResponse> deleteComment(
            @Field("id_post_comments") String id_post_comments
    );

    @FormUrlEncoded
    @POST("save_token.php")
    Observable<ReportResponse> save_token(
            @Field("id_user_name") String id_user_name,
            @Field("pushnotificationToken") String pushnotificationToken
    );

}
