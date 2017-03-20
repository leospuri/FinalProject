package in.voiceme.app.voiceme.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import in.voiceme.app.voiceme.R;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample5 extends AbstractStep {

    private TextView mAutofitOutput;
    private EditText text_status;
    private boolean yes = false;
    private KeyListener originalKeyListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_five, container, false);

        text_status = (EditText) v.findViewById(R.id.intro_edit_text_status);


        mAutofitOutput = (TextView) v.findViewById(R.id.intro_output_autofit);
        mAutofitOutput.setGravity(Gravity.CENTER);
        originalKeyListener = text_status.getKeyListener();
        text_status.setKeyListener(null);
        mAutofitOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restore key listener - this will make the field editable again.
                text_status.setKeyListener(originalKeyListener);
                // Focus the field.
                text_status.requestFocus();
                // Show soft keyboard for the user to enter the value.
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text_status, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        text_status = (EditText) v.findViewById(R.id.edit_text_status);

        editTextChangeListener();

        text_status.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If it loses focus...
                doesnotHaveFocus(hasFocus);
            }
        });

        return v;
    }

    private void doesnotHaveFocus(boolean hasFocus) {
        if (!hasFocus) {
            // Hide soft keyboard.
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        if (yes){
            return true;
        } else {
            Toast.makeText(getActivity(), "IsOptional is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        // get the Entered  message
        String status = text_status.getText().toString();
        StepFourInterface stepOneInterface = (StepFourInterface) getActivity();
        stepOneInterface.sendTextStatus(status);

    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        if (yes){
            return true;
        } else {
            Toast.makeText(getActivity(), "nextIf is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }

}
