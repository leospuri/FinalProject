package in.voiceme.app.voiceme.NewFacebookFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 7/1/2017.
 */
public class Cursors {
    @SerializedName("before") @Expose private String before;
    @SerializedName("after") @Expose private String after;

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }
}
