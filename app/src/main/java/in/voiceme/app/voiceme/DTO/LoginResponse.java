package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends AbstractResponse {
    public Info info;

    public class Info {
        @SerializedName("id") @Expose private String id;
        @SerializedName("name") @Expose private String name;
        @SerializedName("user_id") @Expose private String userId;
        @SerializedName("present") @Expose private String present;
        @SerializedName("imageurl") @Expose private String imageurl;
        @SerializedName("contact") @Expose private String givenContact;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUserId() {
            return userId;
        }

        public String getPresent() {
            return present;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getGivenContact() {
            return givenContact;
        }
    }
}
