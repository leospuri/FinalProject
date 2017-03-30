package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import in.voiceme.app.voiceme.DTO.LoginResponse;
import in.voiceme.app.voiceme.DTO.OnlyToken;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class RegisterActivity extends BaseActivity
        implements GoogleApiClient.OnConnectionFailedListener, Constants {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    //Activity requests
    private static final int GOOGLE_SIGN_IN = 1;
    private static String TAG = RegisterActivity.class.getSimpleName();
    // Google
    private SignInButton googleSignInBtn;
    private GoogleApiClient googleApiClient;
    private ImageView go_back;

    // Facebook
    private LoginButton facebookSignInBtn;
    private CallbackManager callbackManager;
 //   private ProgressBar progressBar;

    private String token;

    /* Implements GoogleApiClient.OnConnectionFailedListener */

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format("Failed to connect to Google [error #%d, %s]...",
                connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        Log.e(TAG, message);
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_register);
        go_back = (ImageView) findViewById(R.id.go_back);

        googleSignInBtn = (SignInButton) this.findViewById(R.id.signin_with_google_btn);
        facebookSignInBtn = (LoginButton) this.findViewById(R.id.signin_with_facebook_btn);


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, SecondBeforeLoginActivity.class));
            }
        });

        this.setUpGoogleSignIn();

        this.setUpFacebookSignIn();

        // initFacebookLogin();
        //   initGoogleLogin();

        //   outputCognitoCredentials();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            // We are coming back from the Google login activity
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            this.handleGoogleSignInResult(result);
        } else {
            // We are coming back from the Facebook login activity
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Configures (or hides) the Facebook sign in button
     */
    private void setUpFacebookSignIn() {

        String facebookAppId = getString(R.string.FACEBOOK_APP_ID);

        // The Facebook application ID must be defined in res/values/configuration_strings.xml
        if (facebookAppId.isEmpty()) {
            facebookSignInBtn.setVisibility(View.GONE);
       //     findViewById(R.id.signin_no_facebook_signin_txt).setVisibility(View.VISIBLE);
            return;
        }

        callbackManager = CallbackManager.Factory.create();

        facebookSignInBtn.setReadPermissions(Arrays.asList("email", "public_profile"));
        facebookSignInBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                RegisterActivity.this.handleFacebookLogin(loginResult);

            }

            @Override
            public void onCancel() {
                Log.v(TAG, "User cancelled log in with Facebook");
            }

            @Override
            public void onError(FacebookException error) {
                RegisterActivity.this.handleFacebookError(error);
            }
        });
    }

    /**
     * Configures (or hides) the Google sign in button
     */
    private void setUpGoogleSignIn() {

        String serverClientId = getString(R.string.google_server_client_id);

        // The Google server client ID must be defined in res/values/configuration_strings.xml
   /*     if (serverClientId.isEmpty()) {
            googleSignInBtn.setVisibility(View.GONE);
            findViewById(R.id.signin_no_google_signin_txt).setVisibility(View.VISIBLE);
            return;
        } */

        // We configure the Google Sign in button
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                        serverClientId).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignInBtn.setSize(SignInButton.SIZE_WIDE);

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Log.v(TAG, "Signing in with Google...");

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                RegisterActivity.this.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
            }
        });
    }

    /**
     * Handle Google sign in result
     */
    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("RegisterActivity")
                    .setAction("Successful google login")
                    .build());
            // [END custom_event]

            Log.v(TAG, "Successfully logged in with Google...");
            // We can request some Cognito Credentials
         //   GoogleSignInAccount acct = result.getSignInAccount();
      //      Map<String, String> logins = new HashMap<>();
      //      logins.put(GOOGLE_LOGIN, acct.getIdToken());

      //      Log.v(TAG, String.format("Google token <<<\n%s\n>>>", logins.get(GOOGLE_LOGIN)));

            // The identity must be created asynchronously
     //       new CreateIdentityTask(this).execute(logins);
      //      application.getAuth().getUser().setLoggedIn(true);

            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                Timber.d(String.valueOf("Display name : " + result.getSignInAccount().getDisplayName()));
                Timber.d(String.valueOf("Id : " + result.getSignInAccount().getId()));
                Timber.d(String.valueOf("Email : " + result.getSignInAccount().getEmail()));
                Timber.d(String.valueOf("Photo url : " + result.getSignInAccount().getPhotoUrl()));
            }

            try {
                getData(result.getSignInAccount().getDisplayName(),
                        result.getSignInAccount().getEmail(), result.getSignInAccount().getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
            ///////////////////////////////////////////////////////////

        } else {

            Log.w(TAG, String.format("Failed to authenticate against Google #%d - %s",
                    result.getStatus().getStatusCode(), result.getStatus().getStatusMessage()));
        }
    }


    private void getData(String name, String email, String userId) throws Exception {
        application.getWebService()
                .login(name, email, userId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<LoginResponse>() {
                    @Override
                    public void onNext(LoginResponse response) {
                        UserData(response);
                        application.getAuth().setAuthToken("token");
                        SharedPreferences prefsLcl = application.getSharedPreferences("Logged in or not", MODE_PRIVATE);
                        prefsLcl.edit().putBoolean("is this demo mode", false).apply();

                        if (response.info.getPresent().equals("yes")){
                            token = FirebaseInstanceId.getInstance().getToken();
                            application.getAuth().getUser().setLoggedIn(true);
                            postToken(response.info.getUserId(), token);
                            Intent intent = new Intent(RegisterActivity.this, DiscoverActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            application.getAuth().getUser().setLoggedIn(true);
                            Intent intent = new Intent(RegisterActivity.this, IntroActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
    }

    private void postToken(String id, String token){
        try {
            application.getWebService()
                    .onlyToken(id, token)
                    .retryWhen(new RetryWithDelay(3,2000))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<OnlyToken>() {
                        @Override
                        public void onNext(OnlyToken response) {

                        //    MySharedPreferences.registerUsername(preferences, usernameText);
                            //Todo add network call for uploading profile_image file
                            //    startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void UserData(LoginResponse response) {
        MySharedPreferences.registerUserId(preferences, response.info.getId());
        MySharedPreferences.registerUsername(preferences, response.info.getName());
        MySharedPreferences.registerImageUrl(preferences, response.info.getImageurl());
        MySharedPreferences.registerSocialID(preferences, response.info.getUserId());

        Timber.d("the user ID is " + response.info.getId());

        Timber.e("Successfully entered the value inside SharedPreferences");
    }

    /**
     * Handle a Facebook login error
     *
     * @param error the error that should be handled.
     */
    private void handleFacebookError(FacebookException error) {

        String message = String.format("Failed to authenticate against Facebook %s - \"%s\"",
                error.getClass().getSimpleName(), error.getLocalizedMessage());
        Log.e(TAG, message, error);
    }

    /**
     * Handle a Facebook login success
     *
     * @param loginResult the successful login result
     */
    private void handleFacebookLogin(LoginResult loginResult) {


        Log.v(TAG, "Successfully logged in with Facebook...");

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("RegisterActivity")
                .setAction("Successful facebook login")
                .build());
        // [END custom_event]

        final Map<String, String> logins = new HashMap<>();
    //    logins.put(FACEBOOK_LOGIN, AccessToken.getCurrentAccessToken().getToken());
    //    Log.v(TAG, String.format("Facebook token <<<\n%s\n>>>", logins.get(FACEBOOK_LOGIN)));

        GraphRequest graphRequest = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.i(TAG, response.getError().getErrorMessage());
                        } else {
                            String email = me.optString("email");
                            String id = me.optString("id");
                            String gender = me.optString("gender");
                            String first_name = me.optString("first_name");
                            String last_name = me.optString("last_name");
                            String age_range = me.optString("age_range");


                            Log.i("email", "-->" + email);
                            Log.i("id", "-->" + id);
                            Log.i("gender", "-->" + gender);
                            Log.i("first_name", "-->" + first_name);
                            Log.i("last_name", "-->" + last_name);
                            Log.i("age_range", "-->" + age_range);

                            try {
                                getData(String.valueOf(first_name + " " + last_name), email, id);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // send email and id to your web server
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,age_range");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

        // The identity must be created asynchronously
      //  new CreateIdentityTask(this).execute(logins);

        setResult(RESULT_OK);
        finish();
    }

    /**
     * <h2>refreshCredentialsProvider</h2>
     * <p>This calls the refresh() method for the AWS Credentials Provider on a background thread.
     * This should be called after updating login information (such as calling setLogins()).</p>
     */





    /*
    // -- Facebook SDK Related Methods
    private void initFacebookLogin() {
        if (mFacebookCallbackManager == null) {
            mFacebookCallbackManager = CallbackManager.Factory.create();
        }

        LoginButton fbLogin = (LoginButton) findViewById(R.id.fbLogin);
        fbLogin.setReadPermissions(Arrays.asList("public_profile", "email"));

        fbLogin.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "fbLogin onSuccess");
                Log.i(TAG, "accessToken: " + loginResult.getAccessToken().getToken());

                addFacebookLoginToCognito(loginResult.getAccessToken());

                application.getAuth().setAuthToken(loginResult.getAccessToken().toString());
                application.getAuth().getUser().setLoggedIn(true);
                setResult(RESULT_OK);
                finishLogin();
            }


            @Override
            public void onCancel() {
                Log.i(TAG, "fbLogin onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "FacebookException: " + e.toString());
            }
        });

    } */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void loginQuick(View view) {
        application.getAuth().getUser().setLoggedIn(true);
        setResult(RESULT_OK);
        finish();
    }
    */



}
