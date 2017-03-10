package in.voiceme.app.voiceme.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_five, container, false);

        text_status = (EditText) v.findViewById(R.id.intro_edit_text_status);

        mAutofitOutput = (TextView) v.findViewById(R.id.intro_output_autofit);
        mAutofitOutput.setGravity(Gravity.CENTER);

        ((EditText) v.findViewById(R.id.intro_edit_text_status)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mAutofitOutput.setText(charSequence);
                yes = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        return v;
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