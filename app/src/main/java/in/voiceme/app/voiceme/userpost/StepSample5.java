package in.voiceme.app.voiceme.userpost;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.login.StepFourInterface;
import in.voiceme.app.voiceme.utils.ActivityUtils;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample5 extends AbstractStep {
    private EditText text_status;
    private ProgressBar step5progressbar;
    private boolean yes = false;

    public static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_five, container, false);

        text_status = (EditText) v.findViewById(R.id.intro_edit_text_status);
        step5progressbar = (ProgressBar) v.findViewById(R.id.step5progressbar);

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
                yes = true;
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
        if (text_status.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter your status", Toast.LENGTH_LONG).show();
        } else {
            step5progressbar.setVisibility(View.VISIBLE);
            String status = text_status.getText().toString();
            StepFourInterface stepOneInterface = (StepFourInterface) getActivity();
            stepOneInterface.sendTextStatus(status);
            checkIfAlreadyhavePermission();
        }


    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            recordActivity();
            return false;

        }
    }

    private void recordActivity() {
        if (ActivityUtils.recordPermissionGranted(getActivity())) {
            startRec();
        }
    }

    private void startRec() {
        int color = getResources().getColor(R.color.colorPrimaryDark);
        int requestCode = REQUEST_CODE;
        AndroidAudioRecorder.with(getActivity())
                .setColor(color)
                .setRequestCode(requestCode)
                .record();
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
