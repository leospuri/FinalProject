package in.voiceme.app.voiceme.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends AbstractResponse {
    public Info info;

    public class Info {
        String id;
        String name;
        String location;
        String email;
        String gender;
        @SerializedName("user_id")
        String userId;
        @SerializedName("dob")
        String dateOfBirth;
        int age;
        @SerializedName("imageurl")
        String imageUrl;
        String rrr;
    }
}
