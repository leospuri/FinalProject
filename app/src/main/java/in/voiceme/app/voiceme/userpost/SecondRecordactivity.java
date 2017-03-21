package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.utils.ActivityUtils;

public class SecondRecordactivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    private Button start;
    private TextView size, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_recordactivity);
        size = (TextView) findViewById(R.id.size_tv);
        path = (TextView) findViewById(R.id.path_tv);
        start = (Button) findViewById(R.id.start_btn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                recordActivity();
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                size.setText(data.getExtras().getString("audioTime"));
                path.setText(data.getExtras().getString("path"));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void recordActivity() {
        if (ActivityUtils.recordPermissionGranted(this)) {
            startRec();
        }
    }

    private void startRec() {
        int color = getResources().getColor(R.color.colorPrimaryDark);
        int requestCode = REQUEST_CODE;
        AndroidAudioRecorder.with(this)
                .setColor(color)
                .setRequestCode(requestCode)
                .record();
    }
}
