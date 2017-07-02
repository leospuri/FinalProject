package in.voiceme.app.voiceme.NewFacebookFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 7/1/2017.
 */
public class Datum {
    @SerializedName("name") @Expose private String name;
    @SerializedName("id") @Expose private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
