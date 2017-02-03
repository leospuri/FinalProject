package in.voiceme.app.voiceme.ProfilePage;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/4/2017.
 */

public class ProfileUserModel implements Parcelable {

    public static final Creator<ProfileUserModel> CREATOR = new Creator<ProfileUserModel>() {
        @Override
        public ProfileUserModel createFromParcel(Parcel in) {
            return new ProfileUserModel(in);
        }

        @Override
        public ProfileUserModel[] newArray(int size) {
            return new ProfileUserModel[size];
        }
    };

    @SerializedName("id_user_name")
    @Expose
    private String idUserName;
    @SerializedName("user_nick_name")
    @Expose
    private String userNickName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("user_date_of_birth")
    @Expose
    private String userDateOfBirth;
    @SerializedName("avatar_pics")
    @Expose
    private String avatarPics;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("user_registered")
    @Expose
    private String userRegistered;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("about_me")
    @Expose
    private String aboutMe;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("posts")
    @Expose
    private String posts;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("following")
    @Expose
    private String following;

    protected ProfileUserModel(Parcel in) {
        idUserName = in.readString();
        userNickName = in.readString();
        email = in.readString();
        gender = in.readString();
        userDateOfBirth = in.readString();
        avatarPics = in.readString();
        location = in.readString();
        userid = in.readString();
        userRegistered = in.readString();
        name = in.readString();
        aboutMe = in.readString();
        phoneNumber = in.readString();
        posts = in.readString();
        followers = in.readString();
        following = in.readString();
    }

    public String getIdUserName() {
        return idUserName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public String getAvatarPics() {
        return avatarPics;
    }

    public String getLocation() {
        return location;
    }

    public String getUserid() {
        return userid;
    }

    public String getUserRegistered() {
        return userRegistered;
    }

    public String getName() {
        return name;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPosts() {
        return posts;
    }

    public String getFollowers() {
        return followers;
    }

    public String getFollowing() {
        return following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idUserName);
        parcel.writeString(userNickName);
        parcel.writeString(email);
        parcel.writeString(gender);
        parcel.writeString(userDateOfBirth);
        parcel.writeString(avatarPics);
        parcel.writeString(location);
        parcel.writeString(userid);
        parcel.writeString(userRegistered);
        parcel.writeString(name);
        parcel.writeString(aboutMe);
        parcel.writeString(phoneNumber);
        parcel.writeString(posts);
        parcel.writeString(followers);
        parcel.writeString(following);
    }
}
