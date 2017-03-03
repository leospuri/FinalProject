package in.voiceme.app.voiceme.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 3/3/2017.
 */

public class SingleMessage {

    @SerializedName("Message")
    @Expose
    private MessagePojo message;

    public MessagePojo getMessage() {
        return message;
    }

    public void setMessage(MessagePojo message) {
        this.message = message;
    }
}
