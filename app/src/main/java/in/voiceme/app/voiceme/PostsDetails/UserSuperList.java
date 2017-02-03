package in.voiceme.app.voiceme.PostsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 1/3/2017.
 */

public class UserSuperList {

    @SerializedName("likes") @Expose private List<UserListModel> likes = null;
    @SerializedName("same") @Expose private List<UserListModel> same = null;
    @SerializedName("hug") @Expose private List<UserListModel> hug = null;
    @SerializedName("listen") @Expose private List<UserListModel> listen = null;

    public List<UserListModel> getLikes() {
        return likes;
    }

    public List<UserListModel> getSame() {
        return same;
    }

    public List<UserListModel> getHug() {
        return hug;
    }

    public List<UserListModel> getListen() {
        return listen;
    }

}
