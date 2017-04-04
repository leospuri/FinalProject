package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.DTO.LoginResponse;
import in.voiceme.app.voiceme.DTO.OnlyToken;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class AfterRegisterActivity extends BaseActivity {
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        Intent intent =getIntent();
        LoginResponse loginResponse = (LoginResponse) intent.getParcelableExtra("registeruser");
        UserData(loginResponse);

        token = FirebaseInstanceId.getInstance().getToken();

        postToken(loginResponse.info.getUserId(), token);

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

                            Intent intent = new Intent(AfterRegisterActivity.this, DiscoverActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

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
}
