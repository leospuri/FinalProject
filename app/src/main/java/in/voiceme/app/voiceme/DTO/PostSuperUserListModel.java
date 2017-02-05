package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 1/3/2017.
 */

public class PostSuperUserListModel {

    @SerializedName("likes") @Expose private List<PostUserListModel> likes = null;
    @SerializedName("same") @Expose private List<PostUserListModel> same = null;
    @SerializedName("hug") @Expose private List<PostUserListModel> hug = null;
    @SerializedName("listen") @Expose private List<PostUserListModel> listen = null;

    public List<PostUserListModel> getLikes() {
        return likes;
    }

    public List<PostUserListModel> getSame() {
        return same;
    }

    public List<PostUserListModel> getHug() {
        return hug;
    }

    public List<PostUserListModel> getListen() {
        return listen;
    }

}
