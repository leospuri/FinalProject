package in.voiceme.app.voiceme.userpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/4/2017.
 */

public class BaseResponse {
    @SerializedName("success")
    @Expose
    protected Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
