package in.voiceme.app.voiceme.NotificationsPage;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by harish on 2/22/2017.
 */

public class ChatTextPojo implements Parcelable {

    private Long timeStamp;
    private String senderName;
    private String senderId;
    private String chatText;
    private String chat;
    private String receiverId;
    private String newStringWithEmojis2;


    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getChatText() {
      //  decodeChatMessage();
        return chatText;
    }

    private void decodeChatMessage() {
        byte[] data = Base64.decode(chatText, Base64.DEFAULT);
        try {
            newStringWithEmojis2 = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getChat() {
        return chat;
    }

    public String getReceiverId() {
        return receiverId;
    }

    protected ChatTextPojo(Parcel in) {
        senderName = in.readString();
        senderId = in.readString();
        chatText = in.readString();
        chat = in.readString();
        receiverId = in.readString();
    }

    public static final Creator<ChatTextPojo> CREATOR = new Creator<ChatTextPojo>() {
        @Override
        public ChatTextPojo createFromParcel(Parcel in) {
            return new ChatTextPojo(in);
        }

        @Override
        public ChatTextPojo[] newArray(int size) {
            return new ChatTextPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(senderName);
        parcel.writeString(senderId);
        parcel.writeString(chatText);
        parcel.writeString(chat);
        parcel.writeString(receiverId);
    }
}
