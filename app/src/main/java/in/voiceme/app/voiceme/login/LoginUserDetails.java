package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.DTO.ProfileAboutMe;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class LoginUserDetails extends BaseActivity implements View.OnClickListener {
    private Button button;

    private EditText username;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_details);
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        getSupportActionBar().setTitle("User Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        token = FirebaseInstanceId.getInstance().getToken();

        Toast.makeText(this, "Token " + token, Toast.LENGTH_SHORT).show();
        Timber.d(String.valueOf("token from fcm: " + token));
        // Log.d("Id Generated", token);
        username = (EditText) findViewById(R.id.login_start_username);

        button = (Button) findViewById(R.id.submit_user_data_button);
        button.setOnClickListener(this);
    }

    private void submitDataWithoutProfile() throws Exception {
        application.getWebService()
                .LoginUserName(MySharedPreferences.getSocialID(preferences), username.getText().toString(),
                       "", token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileAboutMe>() {
                    @Override
                    public void onNext(ProfileAboutMe response) {

                        MySharedPreferences.registerUsername(preferences, username.getText().toString());
                        //Todo add network call for uploading profile_image file
                   //    startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                    }
                });
    }

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.submit_user_data_button) {
            String ed_text = username.getText().toString().trim();
            if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) {
                Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    submitDataWithoutProfile();
                    startActivity(new Intent(this, IntroActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

//  @Override
//   public boolean processLoggedState(View viewPrm) {
//
//  }
}
