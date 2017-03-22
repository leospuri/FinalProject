package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 2/26/2017.
 */

public class AllCategoryPojo {

    @SerializedName("name") @Expose private String name;
    @SerializedName("id") @Expose private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
