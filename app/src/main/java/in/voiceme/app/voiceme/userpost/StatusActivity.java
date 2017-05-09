package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class StatusActivity extends BaseActivity {
    private EditText text_status;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_status);
        getSupportActionBar().setTitle("Enter Status");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        Button button = (Button) findViewById(R.id.btn_status);
        text_status = (EditText) findViewById(R.id.edit_text_status);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);


                // get the Entered  message
                String status = text_status.getText().toString();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("resultFromStatus", status);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        text_status = (EditText) findViewById(R.id.edit_text_status);

    }


//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
