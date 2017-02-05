package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 1/4/2017.
 */

public class ContactAddResponse {

    @SerializedName("inserted_rows") @Expose private Integer insertedRows;

    public Integer getInsertedRows() {
        return insertedRows;
    }

    @Override
    public String toString() {
        return String.valueOf(insertedRows);
    }
}
