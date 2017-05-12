package in.voiceme.app.voiceme.userpost;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepSample6 extends AbstractStep implements View.OnClickListener {
    private boolean yes = false;
    private TextView textview_ask_audio_perm;
    private TextView textview_audio_success;
    private Button request_record_audio_perm;
    Toolbar toolbar;

    public static final int REQUEST_CODE = 5000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_step_sample6, container, false);

        textview_ask_audio_perm = (TextView) v.findViewById(R.id.textview_ask_audio_perm);
        textview_audio_success = (TextView) v.findViewById(R.id.textview_audio_success);
        request_record_audio_perm = (Button) v.findViewById(R.id.request_record_audio_perm);

        request_record_audio_perm.setOnClickListener(this);
        textview_audio_success.setVisibility(View.GONE);

        checkIfAlreadyhavePermission();

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

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            textview_ask_audio_perm.setText("Click below to record audio");
            return true;
        } else {
            textview_ask_audio_perm.setText("You need to give permission to Record Audio");
            return false;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

                textview_audio_success.setVisibility(View.VISIBLE);

                textview_ask_audio_perm.setText("Recording done");
                //    Toast.makeText(this, "audio TIme: " + audio_time, Toast.LENGTH_SHORT).show();

                //     path.setText(data.getExtras().getString("path"));

                /************ Audio Received ******************** */

                Toast.makeText(getActivity(), "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
                textview_audio_success.setText("Successful");

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Audio was not recorded", Toast.LENGTH_SHORT).show();
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
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.request_record_audio_perm){
            recordActivity();
            textview_ask_audio_perm.setText("Click Below to Record Again.");
        }
    }
}
