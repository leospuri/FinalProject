package in.voiceme.app.voiceme.NotificationsPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.voiceme.app.voiceme.DTO.OnlyToken;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

/**
 * Created by Hitesh on 09-10-2016.
 */

public class FCMTokenGenerater extends FirebaseInstanceIdService {
    private VoicemeApplication application;
    private static SharedPreferences recyclerviewpreferences;

    @Override
    public void onTokenRefresh() {
        application = (VoicemeApplication) getApplication();
        recyclerviewpreferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token Is : ", refreshedToken);

        postToken(MySharedPreferences.getUserId(recyclerviewpreferences), refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
     //   sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }

    private void postToken(String id, String token){
        try {
            application.getWebService()
                    .onlyToken(id, token)
                    .retryWhen(new RetryWithDelay(3,2000))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
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
}
