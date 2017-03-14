package in.voiceme.app.voiceme.userpost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;

import java.io.File;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.LoginActivity;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.utils.ActivityUtils.deleteAudioFile;

public class AudioStatus extends BaseActivity {
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final String filepath = Environment.getExternalStorageDirectory().getPath() + "/" + "currentRecording.mp3";

    public static final int REQUEST_CODE = 1;
    private String audio_time;

    private TextView textView_category;
    private TextView textView_feeling;
    private TextView textView_status;
    private TextView textView_record_button;
    private Button post_status;
    private String audioFileUrl;
    private String category;
    private String feeling;
    private String textStatus;
  //  private FFmpeg ffmpeg;
    private ProgressDialog progressDialog;
    private int audioDuration;
  //  private String convertAudioCommand = "-y -i " + AUDIO_FILE_PATH + " -ar 44100 -ac 2 -ab 64k -f mp3 " + CONVERTED_AUDIO_FILE_PATH;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_audio_status);
        getSupportActionBar().setTitle("Audio Status");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        textView_category = (TextView) findViewById(R.id.textView_category);
        textView_feeling = (TextView) findViewById(R.id.textView_feeling);
        textView_status = (TextView) findViewById(R.id.textView_status);
        textView_record_button = (TextView) findViewById(R.id.recording_start_button);
        post_status = (Button) findViewById(R.id.button_post_audio_status);

     //   ffmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);

        textView_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("AudioStatusActivity")
                        .setAction("Enter Category Page")
                        .build());
                // [END custom_event]
                Intent categoryIntent = new Intent(AudioStatus.this, Category2Activity.class);
                startActivityForResult(categoryIntent, 1);
            }
        });
        textView_feeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("AudioStatusActivity")
                        .setAction("Enter Feeling Page")
                        .build());
                // [END custom_event]
                Intent feelingIntent = new Intent(AudioStatus.this, FeelingActivity.class);
                startActivityForResult(feelingIntent, 2);
            }
        });
        textView_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("AudioStatusActivity")
                        .setAction("Enter Text Status Page")
                        .build());
                // [END custom_event]
                Intent statusIntent = new Intent(AudioStatus.this, StatusActivity.class);
                startActivityForResult(statusIntent, 3);
            }
        });


            post_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (processLoggedState(view)){
                    return;
                } else {
                    if (category == null || feeling == null || textStatus == null || audio_time == null) {
                        Toast.makeText(AudioStatus.this, "Please select all categories to Post Status", Toast.LENGTH_SHORT).show();
                    } else {
                        // [START custom_event]
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("AudioStatusActivity")
                                .setAction("Post Audio Status Page")
                                .build());
                        // [END custom_event]
                        // network call from retrofit
                        readAudioFileStorage();
                    }


                }
            }
        });

        textView_record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AudioStatus.this, AudioRecordingActivity.class);
                startActivityForResult(intent,4);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {

                audio_time = data.getExtras().getString("audioTime");

                /************ Audio Received ******************** */
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromCategory");
                category = result;
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromFeeling");
                feeling = result;

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromStatus");
                textStatus = result;
            }
        }
    }

    private void readAudioFileStorage() {
        if (ActivityUtils.isReadStoragePermission(this)) {
            uploadFile(Uri.parse(filepath));
        }
    }


    private void uploadFile(Uri fileUri) {
        File file = new File(String.valueOf(fileUri));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // finally, execute the request
        try {
            progressDialog.setMessage("uploading file...");
            progressDialog.show();
            application.getWebService()
                    .uploadFile(body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String response) {
                            Timber.d("file url " + response);
                            setAudioFileUrl(response);
                        }

                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();

                           postStatus();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void postStatus() {
        // network call from retrofit
        try {
            application.getWebService()
                    .postStatus(MySharedPreferences.getUserId(preferences),
                    textStatus, category, feeling, audioFileUrl, audio_time)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<UserResponse>() {
                        @Override
                        public void onNext(UserResponse userResponse) {
                            Timber.e("UserResponse " + userResponse.getStatus() + "===" + userResponse.getMsg());
                            if (userResponse.getStatus() == 1) {
                                startActivity(new Intent(AudioStatus.this, MainActivity.class));
                                deleteAudio();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAudio(){
        // check delete permissions for Android M
        deleteAudioFile(this);
        File fdelete = new File(filepath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + filepath);
            } else {
                System.out.println("file not Deleted :" + filepath);
            }
        }
    }

    private void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Reminder");
            alertDialog.setMessage("You cannot interact\nunless you logged in");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(AudioStatus.this, LoginActivity.class));
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }
}
