package in.voiceme.app.voiceme.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import in.voiceme.app.voiceme.ProfilePage.User;
import in.voiceme.app.voiceme.login.SecondBeforeLoginActivity;
import in.voiceme.app.voiceme.login.account.AccountManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Harish on 7/20/2016.
 */
public class Auth {
    private static final String AUTH_PREFERENCES = "AUTH_PREFERENCES";
    private static final String AUTH_PREFERENCES_TOKEN = "AUTH_PREFERENCES_TOKEN";
    private static final String AK_KEY = "accessKey";
    private final String DEFAULT_SHAREDPREFERENCES_NAME = "com.amazonaws.android.auth";
    private final Context context;
    private final SharedPreferences preferences;

    private User user;
    private String authToken;

    public Auth(Context context) {
        this.context = context;
        user = new User();
        preferences = context.getSharedPreferences(Constants.PREF_FILE, MODE_PRIVATE);
        authToken = preferences.getString(Constants.KEY_PROVIDER_TOKEN, null);
    }

    public User getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean hasAuthToken() {
        return authToken != null && !authToken.isEmpty();
    }

    public void logout() {
        setAuthToken(null);
        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = AccountManager.getInstance().getCredentialsProvider();

        MySharedPreferences.wipe(preferences);


        if (AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }

        if(cognitoCachingCredentialsProvider != null){
            cognitoCachingCredentialsProvider.clear();
        }

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

        Intent loginIntent = new Intent(context, SecondBeforeLoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }
}
