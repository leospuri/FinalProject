package in.voiceme.app.voiceme.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/21/2017.
 */

public class AboutmeResponse extends AbstractResponse {
    private Info info;

    public class Info {
        @SerializedName("user_id") @Expose private String userId;
        @SerializedName("username") @Expose private String username;
        @SerializedName("about_me") @Expose private String aboutMe;
    }
}
