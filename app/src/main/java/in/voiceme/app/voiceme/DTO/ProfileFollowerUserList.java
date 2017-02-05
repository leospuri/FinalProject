package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 1/4/2017.
 */

public class ProfileFollowerUserList {

    @SerializedName("status") @Expose private Integer status;
    @SerializedName("data") @Expose private List<ProfileFollowerUserModel> follower = null;

    public Integer getStatus() {
        return status;
    }

    public List<ProfileFollowerUserModel> getFollower() {
        return follower;
    }
}
