package in.voiceme.app.voiceme.NewFacebookFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harishpc on 7/2/2017.
 */
public class EmptyResponse {
    @SerializedName("data") @Expose private List<Datum> data = null;
    @SerializedName("summary") @Expose private Summary summary;

    public Summary getSummary() {
        return summary;
    }

    public List<Datum> getData() {
        return data;
    }
}
