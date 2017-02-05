package in.voiceme.app.voiceme.DTO;

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

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getEmail() {
            return email;
        }

        public String getGender() {
            return gender;
        }

        public String getUserId() {
            return userId;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public int getAge() {
            return age;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getRrr() {
            return rrr;
        }
    }
}
