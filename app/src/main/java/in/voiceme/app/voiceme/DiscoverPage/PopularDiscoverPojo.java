package in.voiceme.app.voiceme.DiscoverPage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 7/6/2017.
 */
public class PopularDiscoverPojo {
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("id")
    @Expose
    private String id;

    public String getPostId() {
        return postId;
    }

    public String getId() {
        return id;
    }
}
