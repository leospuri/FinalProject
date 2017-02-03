package in.voiceme.app.voiceme.userpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/3/2017.
 */

public class UserResponse {

    @SerializedName("status") @Expose private Integer status;
    @SerializedName("msg") @Expose private String msg;

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
