package in.voiceme.app.voiceme.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import in.voiceme.app.voiceme.DTO.User;
import in.voiceme.app.voiceme.login.RegisterActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Harish on 7/20/2016.
 */
public class Auth {
    private final Context context;
    private final SharedPreferences preferences;
   // private final SharedPreferences preferences2;

    private User user;
    private String authToken;

    public Auth(Context context) {
        this.context = context;
        user = new User();
        preferences = context.getSharedPreferences(Constants.PREF_FILE, MODE_PRIVATE);
    //    preferences2 = context.getSharedPreferences(Constants.PREF_FILE, MODE_PRIVATE);
        authToken = preferences.getString(Constants.PREF_FILE, null);
    }

    public User getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREF_FILE, authToken);
        editor.apply();

    }

    public boolean hasAuthToken() {
        return authToken != null && !authToken.isEmpty();
    }

    public void logout() {
        setAuthToken(null);

        MySharedPreferences.wipe(preferences);

        if (MySharedPreferences.getUserId(preferences)!= null){
            Toast.makeText(context, "userID", Toast.LENGTH_LONG).show();
        }


        if (AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }

    /*    if(cognitoCachingCredentialsProvider != null){
            cognitoCachingCredentialsProvider.clear();
        }
        */

        /*
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }

*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        Intent loginIntent = new Intent(context, RegisterActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }
}
