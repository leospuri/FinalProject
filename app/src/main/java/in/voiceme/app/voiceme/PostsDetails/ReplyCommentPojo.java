package in.voiceme.app.voiceme.PostsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 6/15/2017.
 */
public class ReplyCommentPojo {

        @SerializedName("user_name") @Expose private String userName;
        @SerializedName("id_post_comments") @Expose private String commentId;
        @SerializedName("id_user_name") @Expose private String postUserId;
        @SerializedName("avatar") @Expose private String avatar;
        @SerializedName("message") @Expose private String comment;
        @SerializedName("id_post_user_name") @Expose private String idPostUserName;
        @SerializedName("commentUserId") @Expose private String commentUserId;
        @SerializedName("comment_time") @Expose private String commentTime;
        @SerializedName("user_name_reply") @Expose private String user_name_reply;

    public String getUserName() {
        return userName;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getComment() {
        return comment;
    }

    public String getIdPostUserName() {
        return idPostUserName;
    }

    public String getUser_name_reply() {
        return user_name_reply;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public ReplyCommentPojo(String message, String imageUri, String userName, String user_name_reply) {
        this.comment = message;
        this.avatar = imageUri;
        this.userName = userName;
        this.user_name_reply = user_name_reply;
    }


}
