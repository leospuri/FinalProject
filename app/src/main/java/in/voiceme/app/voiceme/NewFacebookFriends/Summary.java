package in.voiceme.app.voiceme.NewFacebookFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 7/1/2017.
 */
public class Summary {
    @SerializedName("total_count") @Expose private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }
}
