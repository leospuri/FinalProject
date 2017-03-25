package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlyToken extends AbstractResponse {
    private Info info;

    public class Info {
        @SerializedName("user_id") @Expose private String userId;
    }


}
