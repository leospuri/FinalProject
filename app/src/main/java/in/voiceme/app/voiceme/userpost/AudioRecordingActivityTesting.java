package in.voiceme.app.voiceme.userpost;

/**
 * Created by harish on 2/20/2017.
 */

public class AudioRecordingActivityTesting {

    /* ************************ Recording **********************/

    /*

    package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import in.voiceme.app.voiceme.BuildConfig;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import timber.log.Timber;

public class AudioRecordingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AudioFxActivity";

    private static final float VISUALIZER_HEIGHT_DIP = 360f;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder myAudioRecorder;
    private TextView timer;
    private String time;
    public boolean isContinue = true;
    private int maxDuration = 120 * 1000;
   // private File audioFile;

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
    private String mCurrentAudioPath;



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

        createInitialFile();
     //   createAudioFile();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        timer = (TextView) findViewById(R.id.timer_tv);
        play = (ImageView) findViewById(R.id.play);
        record = (ImageView) findViewById(R.id.record);
        stop = (ImageView) findViewById(R.id.stop);
        done = (ImageView) findViewById(R.id.done);
        pause = (ImageView) findViewById(R.id.pause);
        cancel = (ImageView) findViewById(R.id.cancel_recording);

      //  initialiseAudio();  // IOException=- set data source failed.

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

    private Uri createFile(){
        File audioFile = null;
        Uri audioUrlTaken = null;
        try {
            audioFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (audioFile != null) {
            //  Uri photoURI = Uri.fromFile(createImageFile());
            try {
                Uri audioUrl = FileProvider.getUriForFile(AudioRecordingActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                audioUrlTaken = audioUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //     takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
       //     startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }


        //initialiseRecord(audioUrlTaken);

        return audioUrlTaken;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
     //   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
     //   String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName = "currentRecording";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Audio");
        if (!storageDir.exists()){
            storageDir.mkdir();
            Timber.e("directory created");
        } else {
            Toast.makeText(this, "Cannot create directory", Toast.LENGTH_SHORT).show();
        }
        File audio = File.createTempFile(
                imageFileName,  /* prefix */
            //    ".mp3",         /* suffix */
   // storageDir      /* directory */
    //    );

    // Save a file: path for use with ACTION_VIEW intents
    /*
    mCurrentAudioPath = "file:" + audio.getAbsolutePath();
        return audio;
}

    private void initialiseAudio(Uri audioPath) {

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(audioPath.getPath());
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

    private void createInitialFile() {
        if (ActivityUtils.deleteAudioFile(this)) {
            try {
                createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Cannot create audio recording permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createAudioFile() {
        if (ActivityUtils.deleteAudioFile(this)) {
            createFile();
        }
    }

    private void stopRecordingPerm() {
        if (ActivityUtils.deleteAudioFile(this)) {
            stopRecording();
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

            readFromStorage();
        } else if(v.getId() == R.id.record){
            recordActivity();
        } else if (v.getId() == R.id.stop){

            stopRecordingPerm();
        } else if (v.getId() == R.id.done){
            readAudioFileStorage();
        } else if (v.getId() == R.id.pause){
            listenStop();

        } else if(v.getId() == R.id.cancel_recording){
            Intent intent = new Intent(AudioRecordingActivity.this, AudioStatus.class);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            return;
        }
    }

    public void start() {
        // startChange();
        //   File file = new File(createFile().getPath());


        final long start = System.currentTimeMillis();
        final Handler handler = new Handler();
        record.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);

        File audioFile = null;
        Uri audioUrlTaken = null;
        try {
            audioFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (audioFile != null) {
            //  Uri photoURI = Uri.fromFile(createImageFile());
            try {
                Uri audioUrl = FileProvider.getUriForFile(AudioRecordingActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                audioUrlTaken = audioUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //     takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            //     startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

        initialiseAudio(audioUrlTaken);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setMaxDuration(maxDuration);
        myAudioRecorder.setOutputFile(String.valueOf(audioUrlTaken));


        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, "Illegalexception", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
        }
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (isContinue) {
                            timerHandler();
                            if (System.currentTimeMillis() - start >= maxDuration) {
                                stopRecordingPerm();
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
        play.setVisibility(View.GONE);
        done.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        isListen = true;
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(createFile().getPath());
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
        done.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
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

     *********************End of Recording activity*/

    /* blog post recording code **************************** */

    /*
    package in.voiceme.app.voiceme.userpost;

/**
 * Created by harish on 2/20/2017.
 */

 //   public class Test {

    /*
    final MediaRecorder recorder = new MediaRecorder();
    final String path;

    /**
     * Creates a new audio recording at the given path (relative to root of SD card).
     */

    /*
    public AudioRecorder(String path) {
        this.path = sanitizePath(path);
    }

    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
    }

    /**
     * Starts a new recording.
     */

    /*
    public void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
            throw new IOException("SD Card is not mounted.  It is " + state + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    /**
     * Stops a recording that has been previously started.
     */

    /*
    public void stop() throws IOException {
        recorder.stop();
        recorder.release();
    }

    //}


    ********************** end of blog post recording ***************** */

    /* *************** Manifest code *******************************

    <application
        android:name=".infrastructure.VoicemeApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="in.voiceme.app.voiceme.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"/>
        </provider>
        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/FACEBOOK_APP_ID" />
     */

    /* ******************** external path ************************

     <?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="external_files" path="."/>
</paths>

      */
}
