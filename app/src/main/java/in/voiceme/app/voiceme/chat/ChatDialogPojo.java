package in.voiceme.app.voiceme.chat;

/**
 * Created by harish on 2/27/2017.
 */

public class ChatDialogPojo {

    int avatarUrl;
    int lastDialogAvatarUrl;
    String id;
    String username;
    String timeStamp;
    String unRead;
    String lastMessage;

    public ChatDialogPojo(int avatarUrl, int lastDialogAvatarUrl, String id, String username, String lastMessage, String timeStamp, String unRead) {
        this.avatarUrl = avatarUrl;
        this.lastDialogAvatarUrl = lastDialogAvatarUrl;
        this.id = id;
        this.lastMessage = lastMessage;
        this.username = username;
        this.timeStamp = timeStamp;
        this.unRead = unRead;
    }

    public int getLastDialogAvatarUrl() {
        return lastDialogAvatarUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getAvatarUrl() {
        return avatarUrl;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUnRead() {
        return unRead;
    }
}
