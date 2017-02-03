package in.voiceme.app.voiceme.PostsDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/17/2017.
 */

public class UserCommentModel implements Parcelable {

    public static final Creator<UserCommentModel> CREATOR = new Creator<UserCommentModel>() {
        @Override
        public UserCommentModel createFromParcel(Parcel in) {
            return new UserCommentModel(in);
        }

        @Override
        public UserCommentModel[] newArray(int size) {
            return new UserCommentModel[size];
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

    public UserCommentModel(String message, String imageUri, String userName) {
        this.comment = message;
        this.avatar = imageUri;
        this.userName = userName;
    }



    protected UserCommentModel(Parcel in) {
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
