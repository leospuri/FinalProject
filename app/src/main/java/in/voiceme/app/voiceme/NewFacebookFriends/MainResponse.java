package in.voiceme.app.voiceme.NewFacebookFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harishpc on 7/1/2017.
 */
public class MainResponse {
    @SerializedName("data") @Expose private List<Datum> data = null;
    @SerializedName("paging") @Expose private Paging paging;
    @SerializedName("summary") @Expose private Summary summary;

    public List<Datum> getData() {
        return data;
    }

    public Paging getPaging() {
        return paging;
    }

    public Summary getSummary() {
        return summary;
    }
}
