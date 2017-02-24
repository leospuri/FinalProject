package in.voiceme.app.voiceme.login.account;

import android.os.Bundle;
import android.view.View;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class AfterLoginScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_screen);
        getSupportActionBar().setTitle("Enter Status");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });
    }
}
