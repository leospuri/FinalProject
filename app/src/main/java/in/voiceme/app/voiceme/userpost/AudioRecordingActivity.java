package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.utils.ActivityUtils;

public class AudioRecordingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AudioFxActivity";

    private static final float VISUALIZER_HEIGHT_DIP = 360f;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder myAudioRecorder;
    private TextView timer;
    private String time;
    public boolean isContinue = true;
    private int maxDuration = 120 * 1000;

    private static final String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + "currentRecording.mp3";
    private int currentDuration = 0;
    private int second = 0;
    private int minute = 0;
    private String timePlay;
    private boolean isListen = false;

    private ImageView play;
    private ImageView stop;
    private ImageView record;
    private ImageView done;
    private ImageView pause;
    private ImageView cancel;



    Handler handler = new Handler();
    Runnable updateThread = new Runnable() {
        public void run() {
            handler.postDelayed(updateThread, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recording);
        getSupportActionBar().setTitle("Audio Recoding");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // filePath = Environment.getExternalStorageDirectory() + "/currentRecording.mp3";

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        timer = (TextView) findViewById(R.id.timer_tv);
        play = (ImageView) findViewById(R.id.play);
        record = (ImageView) findViewById(R.id.record);
        stop = (ImageView) findViewById(R.id.stop);
        done = (ImageView) findViewById(R.id.done);
        pause = (ImageView) findViewById(R.id.pause);
        cancel = (ImageView) findViewById(R.id.cancel_recording);

        initialiseAudio();  // IOException=- set data source failed.

        handler.post(updateThread);

        play.setOnClickListener(this);
        play.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        done.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initialiseAudio() {

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        setVolumeControlStream(AudioManager.STREAM_SYSTEM);
                        if (mMediaPlayer.isPlaying()) {
                            play.setVisibility(View.VISIBLE);
                            pause.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void recordActivity() {
        if (ActivityUtils.recordPermissionGranted(this)) {
            start();
        }
    }

    private void readFromStorage() {
        if (ActivityUtils.isReadStoragePermission(this)) {
            listenPlay();
        }
    }

    private void readAudioFileStorage() {
        if (ActivityUtils.isReadStoragePermission(this)) {
            done();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && mMediaPlayer != null) {
            handler.removeCallbacks(updateThread);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void listen() {
        if (!mMediaPlayer.isPlaying()) {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
            mMediaPlayer.start();
        } else {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play) {
            play.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            readFromStorage();
        } else if(v.getId() == R.id.record){
            record.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            recordActivity();
        } else if (v.getId() == R.id.stop){
            stop.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            stopRecording();
        } else if (v.getId() == R.id.done){
            readAudioFileStorage();
        } else if (v.getId() == R.id.pause){
            listenStop();
            done.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        } else if(v.getId() == R.id.cancel_recording){
            Intent intent = new Intent(AudioRecordingActivity.this, AudioStatus.class);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            return;
        }
    }

    public void start() {
        // startChange();
        final long start = System.currentTimeMillis();
        final Handler handler = new Handler();
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setOutputFile(filePath);
        myAudioRecorder.setMaxDuration(maxDuration);

        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (isContinue) {
                            timerHandler();
                            if (System.currentTimeMillis() - start >= maxDuration) {
                                stopRecording();
                            }
                            handler.postDelayed(this, 1000);
                        }
                    }
                });
    }

    private void stopRecording() {
        isContinue = false;
        //	stopChange();
        done.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;

    }

    private void listenPlay() {
        isListen = true;
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        minute = 0;
        second = 0;
        final long start = System.currentTimeMillis();
        final Handler handler = new Handler();
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (isListen) {
                            timerHandlerPlay();
                            if (System.currentTimeMillis() - start >= (currentDuration - 1) * 1000) {
                                listenStop();
                            }
                            handler.postDelayed(this, 1000);
                        }
                    }
                });
    }

    private void listenStop() {
        isListen = false;
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    private void done() {
        Intent intent = new Intent(AudioRecordingActivity.this, AudioStatus.class);
        intent.putExtra("path", filePath);
        intent.putExtra("audioTime", time);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void timerHandler() {
        if (second == 59) {
            second = 0;
            minute++;
        } else {
            second++;
        }
        currentDuration++;
        time = String.format("%02d : %02d", minute, second);
        timer.setText(time);
    }

    private void timerHandlerPlay() {
        if (second == 59) {
            second = 0;
            minute++;
        } else {
            second++;
        }
        timePlay = String.format("%02d : %02d", minute, second);
        timer.setText(timePlay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
