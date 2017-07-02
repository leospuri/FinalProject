package in.voiceme.app.voiceme.NewFacebookFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NewFacebookFriends extends BaseActivity {
    // Facebook
    private LoginButton facebookSignInBtn;
    private CallbackManager callbackManager;
    //   private ProgressBar progressBar;

    private String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_facebook_friends);

        facebookSignInBtn = (LoginButton) this.findViewById(R.id.facebook_friends_login);

        this.setUpFacebookSignIn();


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

        facebookSignInBtn.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        facebookSignInBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                NewFacebookFriends.this.handleFacebookLogin(loginResult);

            }

            @Override
            public void onCancel() {
                Timber.v("User cancelled log in with Facebook");
            }

            @Override
            public void onError(FacebookException error) {
                NewFacebookFriends.this.handleFacebookError(error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11234) {
            // We are coming back from the Google login activity

        } else {
            // We are coming back from the Facebook login activity
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookError(FacebookException error) {

        String message = String.format("Failed to authenticate against Facebook %s - \"%s\"",
                error.getClass().getSimpleName(), error.getLocalizedMessage());
        Timber.e(error,message );
    }

    private void handleFacebookLogin(LoginResult loginResult) {

        Timber.v( "Successfully logged in with Facebook...");
    //    Timber.v("Access Token " + loginResult.getAccessToken().toString());
        try {
            getFriendList(loginResult.getAccessToken().getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getFriendList(String accessToken) throws Exception {
        application.getWebService()
                .getFriendsFirst(accessToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<MainResponse>() {
                    @Override
                    public void onNext(MainResponse response) {
                        Timber.d(response.getSummary().getTotalCount().toString());

                        if (response.getData().size()==25){
                            try {
                                getFriendListPart2(response, accessToken);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Timber.d("Finished");
                        }

                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e.getMessage());
                            //   Toast.makeText(ChangeProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void repeatNetWork(MainResponse response, String token){
        try {
            getFriendListPart2(response, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFriendListPart2(MainResponse response, String token) throws Exception {
        application.getWebService()
                .getFriends(token, "25", response.getPaging().getCursors().getAfter())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<MainResponse>() {
                    @Override
                    public void onNext(MainResponse response) {

                        if (response.getData().size()==25){
                            repeatNetWork(response, token);

                        } else {
                            Timber.d("Finished");
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e.getMessage());
                            //   Toast.makeText(ChangeProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

}
