package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by harish on 3/2/2017.
 */

public class UserPojo implements IUser {

    @SerializedName("Id") @Expose private String id;
    @SerializedName("Name") @Expose private String name;
    @SerializedName("Avatar") @Expose private String avatar;
    @SerializedName("isOnline") @Expose private boolean isOnline;

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public boolean isOnline() {
        return isOnline;
    }

    public UserPojo(String id, String name, String avatar, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.isOnline = isOnline;
    }
}
