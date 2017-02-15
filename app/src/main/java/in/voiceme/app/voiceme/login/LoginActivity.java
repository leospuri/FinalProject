package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_REGISTER = 2;


    private final String DEFAULT_SHAREDPREFERENCES_NAME = "com.amazonaws.android.auth";

    private View registerButton;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_login);

        registerButton = findViewById(R.id.activity_login_register);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view == registerButton) {
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("LoginActivity")
                    .setAction("Clicked Register Button")
                    .build());
            // [END custom_event]
            // network call from retrofit
            startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_REGISTER);
        }
    }

    public void tryDemoOnClick(View viewPrm) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("LoginActivity")
                .setAction("Clicked Skip Button")
                .build());
        // [END custom_event]
        SharedPreferences prefsLcl = getSharedPreferences("Logged in or not", MODE_PRIVATE);
        prefsLcl.edit().putBoolean("is this demo mode", true).apply();
        startActivity(new Intent(this, AnonymousLogin.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_REGISTER) {
            finishLogin();
        }
    }

    private void finishLogin() {
        SharedPreferences cognitoPref = getSharedPreferences(DEFAULT_SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        if (cognitoPref != null){
            cognitoPref.edit().clear().apply();
        }

        SharedPreferences prefsLcl = getSharedPreferences("Logged in or not", MODE_PRIVATE);
        prefsLcl.edit().putBoolean("is this demo mode", false).apply();
        startActivity(new Intent(this, LoginUserDetails.class));
        finish();
    }



//  @Override
//   public boolean processLoggedState(View viewPrm) {
//
//  }
}
