package in.voiceme.app.voiceme.login;

import org.json.JSONObject;

/**
 * Created by multidots on 6/16/2016.
 * This class represents facebook user profile.
 */
public class FacebookUser {
    public String name;

    public String email;

    public String gender;
    /**
     * JSON response received. If you want to parse more fields.
     */
    public JSONObject response;

    public FacebookUser(String name, String email, String gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

}
