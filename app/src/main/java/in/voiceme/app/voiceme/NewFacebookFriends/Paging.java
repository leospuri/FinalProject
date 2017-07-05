package in.voiceme.app.voiceme.NewFacebookFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harishpc on 7/1/2017.
 */
public class Paging {
    @SerializedName("cursors") @Expose private Cursors cursors;
    @SerializedName("next") @Expose private String next;
    @SerializedName("previous") @Expose private String previous;

    public Cursors getCursors() {
        return cursors;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }
}
