package in.voiceme.app.voiceme.ProfilePage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 1/4/2017.
 */

public class FollowerUserList {

    @SerializedName("status") @Expose private Integer status;
    @SerializedName("data") @Expose private List<FollowerUserModel> follower = null;

    public Integer getStatus() {
        return status;
    }

    public List<FollowerUserModel> getFollower() {
        return follower;
    }
}
