package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 3/7/2017.
 */

public class AllPopularTagsPojo {

    @SerializedName("id") @Expose private String id;
    @SerializedName("name") @Expose private String name;
    @SerializedName("count") @Expose private String count;

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getId() {
        return id;
    }
}
