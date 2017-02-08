package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 2/8/2017.
 */

public class ReportResponse {


    @SerializedName("success") @Expose private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

}
