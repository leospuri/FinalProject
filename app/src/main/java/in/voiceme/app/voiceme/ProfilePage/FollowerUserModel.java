package in.voiceme.app.voiceme.ProfilePage;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/4/2017.
 */

public class FollowerUserModel implements Parcelable {

    public static final Creator<FollowerUserModel> CREATOR = new Creator<FollowerUserModel>() {
        @Override
        public FollowerUserModel createFromParcel(Parcel in) {
            return new FollowerUserModel(in);
        }

        @Override
        public FollowerUserModel[] newArray(int size) {
            return new FollowerUserModel[size];
        }
    };

    @SerializedName("id_user_name") @Expose private String idUserName;
    @SerializedName("name") @Expose private String name;
    @SerializedName("avatar_pics") @Expose private String avatarPics;

    protected FollowerUserModel(Parcel in) {
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
