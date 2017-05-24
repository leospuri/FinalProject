package in.voiceme.app.voiceme.userpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.login.StepFourInterface;
import timber.log.Timber;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample5 extends AbstractStep {
    private EditText text_status;
    private ProgressBar step5progressbar;
    private boolean yes = false;

    Toolbar toolbar;
    private String current_problem = null;

    public static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.new_intro_step_five, container, false);

        text_status = (EditText) v.findViewById(R.id.intro_edit_text_status);
        step5progressbar = (ProgressBar) v.findViewById(R.id.step5progressbar);

        editTextChangeListener();

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = mStepper.getToolbar();
        if(toolbar==null){
            Timber.d("toolbar is null");
        }
        else{
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            try {
                actionBar.setHomeAsUpIndicator(R.mipmap.ic_ab_close);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }


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

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (text_status.getText().toString().trim().length() > 1000){
                    Toast.makeText(getActivity(), "Please Enter Status less than 1000 words", Toast.LENGTH_SHORT).show();
                    yes = false;
                } else {
                    current_problem = text_status.getText().toString();
                    yes = true;
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
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        // get the Entered  message
            step5progressbar.setVisibility(View.VISIBLE);
            String status = text_status.getText().toString().trim();
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
        return "<b>You must Enter your post within 1000 words!</b>";
    }

}
