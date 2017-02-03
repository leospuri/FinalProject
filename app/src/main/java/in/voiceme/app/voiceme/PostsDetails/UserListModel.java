package in.voiceme.app.voiceme.PostsDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import in.voiceme.app.voiceme.login.AbstractResponse;

/**
 * Created by harish on 1/3/2017.
 */

public class UserListModel extends AbstractResponse implements Parcelable {

    public static final Creator<UserListModel> CREATOR = new Creator<UserListModel>() {
        @Override
        public UserListModel createFromParcel(Parcel in) {
            return new UserListModel(in);
        }

        @Override
        public UserListModel[] newArray(int size) {
            return new UserListModel[size];
        }
    };

    @SerializedName("name") private String name;
    @SerializedName("avatar") private String avatar;
    @SerializedName("id_user_name") private String idUserName;

    protected UserListModel(Parcel in) {
        name = in.readString();
        avatar = in.readString();
        idUserName = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIdUserName() {
        return idUserName;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(avatar);
        parcel.writeString(idUserName);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}

