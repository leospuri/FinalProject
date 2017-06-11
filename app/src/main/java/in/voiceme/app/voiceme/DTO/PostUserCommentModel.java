package in.voiceme.app.voiceme.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/17/2017.
 */

public class PostUserCommentModel implements Parcelable {

    public static final Creator<PostUserCommentModel> CREATOR = new Creator<PostUserCommentModel>() {
        @Override
        public PostUserCommentModel createFromParcel(Parcel in) {
            return new PostUserCommentModel(in);
        }

        @Override
        public PostUserCommentModel[] newArray(int size) {
            return new PostUserCommentModel[size];
        }
    };

    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("id_post_user_name") @Expose private String id_post_user_name;
    @SerializedName("commentId") @Expose private String commentId;
    @SerializedName("avatar") @Expose private String avatar;
    @SerializedName("comment") @Expose private String comment;
    @SerializedName("postUserId") @Expose private String postUserId;
    @SerializedName("commentUserId") @Expose private String commentUserId;
    @SerializedName("comment_time") @Expose private String commentTime;
    @SerializedName("level") @Expose private int level;;

    public String getUserName() {
        return userName;
    }

    public int getLevel() {
        return level;
    }

    public String getId_post_user_name() {
        return id_post_user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public PostUserCommentModel(String message, String imageUri, String userName, int level) {
        this.comment = message;
        this.avatar = imageUri;
        this.userName = userName;
        this.level = level;
    }



    protected PostUserCommentModel(Parcel in) {
        userName = in.readString();
        avatar = in.readString();
        comment = in.readString();
        commentTime = in.readString();
        level = in.readInt();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(avatar);
        parcel.writeString(comment);
        parcel.writeString(commentTime);
        parcel.writeInt(level);
    }
}
