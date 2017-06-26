package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/**
 * Created by harish on 3/2/2017.
 */

public class MessagePojo implements IMessage, MessageContentType.Image {
    @SerializedName("Id") @Expose private String id;
    @SerializedName("Text") @Expose private String text;
    @SerializedName("CreatedAt") @Expose private String createdAt;
    @SerializedName("User") @Expose private UserPojo user;
    @SerializedName("chatImage") @Expose private Image chatImage;

    public MessagePojo(String id, String text, String createdAt, UserPojo user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }

    public MessagePojo(String id, String text, UserPojo user) {
        this.id = id;
        this.text = text;
        this.user = user;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public void setImage(Image image) {
        this.chatImage = chatImage;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
      //  int currentTime = (int) (System.currentTimeMillis()/1000 - Integer.parseInt(createdAt));
      //  Date date = new Date(currentTime);
        if (createdAt == null){
            return new Date();

        } else {
            Long datetime = Long.parseLong(createdAt);
            Date date=new Date(datetime);
            return date;
        }
    }

    public String getStatus() {
        return "Sent";
    }

    @Override
    public String getImageUrl() {
        return chatImage == null ? null : chatImage.url;
    }
}

