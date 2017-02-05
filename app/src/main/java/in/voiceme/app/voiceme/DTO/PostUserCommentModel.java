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
    @SerializedName("avatar") @Expose private String avatar;
    @SerializedName("comment") @Expose private String comment;
    @SerializedName("comment_time") @Expose private String commentTime;

    public String getUserName() {
        return userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public PostUserCommentModel(String message, String imageUri, String userName) {
        this.comment = message;
        this.avatar = imageUri;
        this.userName = userName;
    }



    protected PostUserCommentModel(Parcel in) {
        userName = in.readString();
        avatar = in.readString();
        comment = in.readString();
        commentTime = in.readString();
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
    }
}