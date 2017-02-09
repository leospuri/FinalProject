package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class AnonymousLogin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_login);
    }

    public void EnterApp(View view) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("AnonymousLogin")
                .setAction("Anonymous Login enter")
                .build());
        // [END custom_event]
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
