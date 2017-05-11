package in.voiceme.app.voiceme.userpost;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.utils.ActivityUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepSample6 extends AbstractStep {
    private boolean yes = false;
    public static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_step_sample6, container, false);

        recordActivity();

        return v;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == getResources().getInteger(R.integer.recorder_request)) {
                startRec();
            }
        }
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
        // get the Entered  messag

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
