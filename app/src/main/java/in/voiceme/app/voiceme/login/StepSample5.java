package in.voiceme.app.voiceme.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Constants;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample5 extends AbstractStep {
    private EditText text_status;
    private ProgressBar step5progressbar;
    private boolean yes = false;
    private RadioGroup radio_group_report;
    private RadioButton report_other;
    private String current_problem = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_five, container, false);

        text_status = (EditText) v.findViewById(R.id.intro_edit_text_status);
        step5progressbar = (ProgressBar) v.findViewById(R.id.step5progressbar);

        if (text_status.getVisibility() == View.VISIBLE){
            text_status.setVisibility(View.GONE);
        }

        radio_group_report = (RadioGroup) v.findViewById(R.id.radio_group_report);
        report_other = (RadioButton) v.findViewById(R.id.report_other);

        radio_group_report.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=group.getCheckedRadioButtonId();
                RadioButton rb=(RadioButton) v.findViewById(id);

                String radioText=rb.getText().toString();
                yes = true;
                current_problem = radioText;
                if (rb == report_other){
                    text_status.setVisibility(View.VISIBLE);
                }

            }
        });

        editTextChangeListener();

        return v;
    }


    private void editTextChangeListener() {
        text_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (text_status.getText().toString().trim().length() > 1500){
                    Toast.makeText(getActivity(), "Please Enter Status less than 1500 words", Toast.LENGTH_SHORT).show();
                    yes = false;
                } else {
                    yes = true;
                    current_problem = text_status.getText().toString();
                }


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
            return false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.SOCIAL_ID, current_problem);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            current_problem = savedInstanceState.getString(Constants.CATEGORY);
            text_status.setText(current_problem);
        }

    }

    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        // get the Entered  message

            step5progressbar.setVisibility(View.VISIBLE);
            String status = current_problem;
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
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must select one status to post!</b>";
    }

}
