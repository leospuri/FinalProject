package in.voiceme.app.voiceme.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/4/2017.
 */

public class ProfileFollowerUserModel implements Parcelable {

    public static final Creator<ProfileFollowerUserModel> CREATOR = new Creator<ProfileFollowerUserModel>() {
        @Override
        public ProfileFollowerUserModel createFromParcel(Parcel in) {
            return new ProfileFollowerUserModel(in);
        }

        @Override
        public ProfileFollowerUserModel[] newArray(int size) {
            return new ProfileFollowerUserModel[size];
        }
    };

    @SerializedName("id_user_name") @Expose private String idUserName;
    @SerializedName("user_nick_name") @Expose private String name;
    @SerializedName("avatar_pics") @Expose private String avatarPics;

    protected ProfileFollowerUserModel(Parcel in) {
        idUserName = in.readString();
        name = in.readString();
        avatarPics = in.readString();
    }

    public String getIdUserName() {
        return idUserName;
    }

    public String getName() {
        return name;
    }

    public String getAvatarPics() {
        return avatarPics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idUserName);
        parcel.writeString(name);
        parcel.writeString(avatarPics);
    }
}
