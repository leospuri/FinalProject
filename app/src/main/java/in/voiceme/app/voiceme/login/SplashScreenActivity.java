package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.DTO.ReportResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;

public class SplashScreenActivity extends BaseActivity {
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        token = FirebaseInstanceId.getInstance().getToken();

        postUsername();
        Intent intent = new Intent(this, DiscoverActivity.class);
        startActivity(intent);
        finish();
    }

    private void postUsername(){
        try {
            application.getWebService()
                    .save_token(MySharedPreferences.getUserId(preferences), token)
                    .retryWhen(new RetryWithDelay(3,2000))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<ReportResponse>() {
                        @Override
                        public void onNext(ReportResponse response) {


                            //Todo add network call for uploading profile_image file
                            //    startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
