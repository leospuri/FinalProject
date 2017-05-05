package in.voiceme.app.voiceme.NotificationsPage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 5/5/2017.
 */
public class NotificationPojo {
    @SerializedName("avatar_pic") @Expose private String avatarPic;
    @SerializedName("username") @Expose private String username;
    @SerializedName("senderId") @Expose private String senderId;
    @SerializedName("postId") @Expose private String postId;
    @SerializedName("activity") @Expose private String activity;
    @SerializedName("time") @Expose private String time;
    @SerializedName("text_status") @Expose private String textStatus;

    public String getAvatarPic() {
        return avatarPic;
    }

    public String getUsername() {
        return username;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getPostId() {
        return postId;
    }

    public String getActivity() {
        return activity;
    }

    public String getTime() {
        return time;
    }

    public String getTextStatus() {
        return textStatus;
    }
}
