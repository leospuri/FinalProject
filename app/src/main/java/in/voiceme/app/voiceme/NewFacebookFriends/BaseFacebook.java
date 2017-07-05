package in.voiceme.app.voiceme.NewFacebookFriends;

import android.content.Intent;
import android.os.Bundle;

import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public abstract class BaseFacebook extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasFacebook()) {
            return;
        } else {
            Intent intent = new Intent(this, NewFacebookFriends.class);
            startActivity(intent);
            finish();
            return;
        }

    }



}
