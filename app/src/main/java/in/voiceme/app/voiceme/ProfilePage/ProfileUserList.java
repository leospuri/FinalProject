package in.voiceme.app.voiceme.ProfilePage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/4/2017.
 */

public class ProfileUserList {

    @SerializedName("status") @Expose private Integer status;
    @SerializedName("data") @Expose private ProfileUserModel data;
    @SerializedName("follower") @Expose private Boolean follower;

    public Integer getStatus() {
        return status;
    }

    public ProfileUserModel getData() {
        return data;
    }

    public Boolean getFollower() {
        return follower;
    }
}
