package in.voiceme.app.voiceme.contactPage;

import android.content.Intent;
import android.os.Bundle;

import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public abstract class BaseContact extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasContactNumber()) {
            return;
        } else {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }



}
