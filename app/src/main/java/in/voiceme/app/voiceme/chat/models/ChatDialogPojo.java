package in.voiceme.app.voiceme.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harish on 3/3/2017.
 */

public class ChatDialogPojo implements IDialog {
    @SerializedName("User") @Expose private UserPojo user;
    @SerializedName("Message") @Expose private MessagePojo message;

    private List<IUser> users;

    @Override
    public String getId() {
        return user.getId();
    }

    public ChatDialogPojo() {
        users = new ArrayList<>();
        users.add(user);
    }

    @Override
    public String getDialogPhoto() {
        return user.getAvatar();
    }

    @Override
    public String getDialogName() {
        return user.getName();
    }

    @Override
    public List<IUser> getUsers() {

        return users;
    }

    public UserPojo getUser() {
        return user;
    }

    @Override
    public IMessage getLastMessage() {
        return message;
    }

    @Override
    public void setLastMessage(IMessage message) {
    }

    public boolean isOnline() {
        return Boolean.parseBoolean(user.getIsOnline());
    }

    @Override
    public int getUnreadCount() {
        return 0;
    }
}
