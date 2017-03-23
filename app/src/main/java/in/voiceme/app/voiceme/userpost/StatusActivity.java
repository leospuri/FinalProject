package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class StatusActivity extends BaseActivity {
    private TextView mAutofitOutput;
    private EditText text_status;
    private KeyListener originalKeyListener;

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

        originalKeyListener = text_status.getKeyListener();
        text_status.setKeyListener(null);

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

        mAutofitOutput = (TextView) findViewById(R.id.output_autofit);
        mAutofitOutput.setGravity(Gravity.CENTER);
        mAutofitOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restore key listener - this will make the field editable again.
                text_status.setKeyListener(originalKeyListener);
                // Focus the field.
                text_status.requestFocus();
                // Show soft keyboard for the user to enter the value.
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text_status, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        text_status = (EditText) findViewById(R.id.edit_text_status);

        editTextChangeListener();

        text_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restore key listener - this will make the field editable again.
                text_status.setKeyListener(originalKeyListener);
                // Focus the field.
                text_status.requestFocus();
                // Show soft keyboard for the user to enter the value.
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text_status, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        text_status.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If it loses focus...
                doesnotHaveFocus(hasFocus);
            }
        });

    }

    private void doesnotHaveFocus(boolean hasFocus) {
        if (!hasFocus) {
            // Hide soft keyboard.
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(text_status.getWindowToken(), 0);
            // Make it non-editable again.
            text_status.setKeyListener(null);
        }
    }


    private void editTextChangeListener() {
        text_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mAutofitOutput.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
