package in.voiceme.app.voiceme.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 6/15/2017.
 */
public class OnlineStatusCheck {

    @SerializedName("onlineStatus")
    @Expose
    private boolean onlineStatus;
    @SerializedName("lastTimeOnline")
    @Expose
    private String lastTimeOnline;

    public boolean getOnlineStatus() {
        return onlineStatus;
    }

    public String getLastTimeOnline() {
        return lastTimeOnline;
    }
}
