package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 3/9/2017.
 */

public class NewCategoryAdded {

    @SerializedName("success") @Expose private Boolean success;
    @SerializedName("id") @Expose private String id;

    public Boolean getSuccess() {
        return success;
    }

    public String getId() {
        return id;
    }
}
