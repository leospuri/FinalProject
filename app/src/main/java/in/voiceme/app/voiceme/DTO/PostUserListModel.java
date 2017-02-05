package in.voiceme.app.voiceme.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/3/2017.
 */

public class PostUserListModel extends AbstractResponse implements Parcelable {

    public static final Creator<PostUserListModel> CREATOR = new Creator<PostUserListModel>() {
        @Override
        public PostUserListModel createFromParcel(Parcel in) {
            return new PostUserListModel(in);
        }

        @Override
        public PostUserListModel[] newArray(int size) {
            return new PostUserListModel[size];
        }
    };

    @SerializedName("name") private String name;
    @SerializedName("avatar") private String avatar;
    @SerializedName("id_user_name") private String idUserName;

    protected PostUserListModel(Parcel in) {
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

