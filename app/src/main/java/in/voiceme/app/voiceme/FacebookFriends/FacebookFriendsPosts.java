package in.voiceme.app.voiceme.FacebookFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import timber.log.Timber;

public class FacebookFriendsPosts extends BaseActivity implements MainView, View.OnClickListener {
    private MainPresenter mainPresenter;
    private LoginButton btnFBLogin;
    private Button btnShowFriendsList;
    private Button btnLoginFBLoginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_friends_posts);

        mainPresenter = new MainPresenter(this);

    }

    @Override
    public void initializeFBSdk() {
        //init the callback manager
        callbackManager = CallbackManager.Factory.create();
        btnFBLogin.registerCallback(callbackManager, fbLoginCallback);
        LoginManager.getInstance().registerCallback(callbackManager, fbLoginCallback);
    }

    @Override
    public void initializeView() {
        btnFBLogin = (LoginButton) findViewById(R.id.fbButton);
        btnShowFriendsList = (Button) findViewById(R.id.btn_showFriendsList);
        btnLoginFBLoginManager = (Button) findViewById(R.id.but_LoginFBLoginManager);
        btnShowFriendsList.setOnClickListener(this);
        btnLoginFBLoginManager.setOnClickListener(this);
    }

    @Override
    public void showFriendsList() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showFBLoginResult(AccessToken fbAccessToken) {
        btnShowFriendsList.setVisibility(View.VISIBLE);
        Timber.d("Success Login" + "\n"+
                "User = " + fbAccessToken.getUserId() + "\n" +
                "Token = " + fbAccessToken.getToken());

    }

    @Override
    public void loginUsingFBManager() {
        //"user_friends" this will return only the common friends using this app
        LoginManager.getInstance().logInWithReadPermissions(FacebookFriendsPosts.this,
                Arrays.asList("public_profile", "user_friends", "email"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //to pass Results to your facebook callbackManager
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_showFriendsList:
                mainPresenter.onShowFriendsListButtonClicked();
                break;
            case R.id.but_LoginFBLoginManager:
                mainPresenter.onLoginUsingFBManagerClicked();
                break;
        }
    }


    private final FacebookCallback fbLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            mainPresenter.onFBLoginSuccess(loginResult);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };




}
