package in.voiceme.app.voiceme.login;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harish on 1/20/2017.
 */

public class LoginUserObjectPojo implements Parcelable {

    public static final Creator<LoginUserObjectPojo> CREATOR = new Creator<LoginUserObjectPojo>() {
        @Override
        public LoginUserObjectPojo createFromParcel(Parcel in) {
            return new LoginUserObjectPojo(in);
        }

        @Override
        public LoginUserObjectPojo[] newArray(int size) {
            return new LoginUserObjectPojo[size];
        }
    };

    private String name;
    private String email;
    private String location;
    private String dateOfBirth;
    private String userId;
    private Uri profile;
    private String gender;
    private String aboutme;
    private String userNickName;


    protected LoginUserObjectPojo(Parcel in) {
        name = in.readString();
        email = in.readString();
        location = in.readString();
        dateOfBirth = in.readString();
        userId = in.readString();
        profile = in.readParcelable(Uri.class.getClassLoader());
        gender = in.readString();
        aboutme = in.readString();
        userNickName = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Uri getProfile() {
        return profile;
    }

    public void setProfile(Uri profile) {
        this.profile = profile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(location);
        parcel.writeString(dateOfBirth);
        parcel.writeString(userId);
        parcel.writeParcelable(profile, i);
        parcel.writeString(gender);
        parcel.writeString(aboutme);
        parcel.writeString(userNickName);
    }
}
