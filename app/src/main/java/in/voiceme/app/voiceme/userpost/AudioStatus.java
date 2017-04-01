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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;

import java.io.File;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.RegisterActivity;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.utils.ActivityUtils.deleteAudioFile;

public class AudioStatus extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_RECORD_AUDIO = 0;
   // private static final String filepath = Environment.getExternalStorageDirectory().getPath() + "/" + "currentRecording.mp3";
    private static final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio"+".mp3";

  //  private String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio"+".mp3";

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

    private LinearLayout audio_category_back;
    private LinearLayout audio_feeling_back;
    private LinearLayout audio_text_status_back;
    private LinearLayout audio_record_back;
    private LinearLayout category_selected_audio;
    private LinearLayout selected_feeling_audio;
    private LinearLayout text_category_selected_audio;

    private LinearLayout selected_audio_file;

    private TextView choosen_audio;
    private TextView choosen_category;
    private TextView choosen_feeling;
    private TextView choosen_audio_written;

    private ImageView id_audio;
    private ImageView category_id;
    private ImageView id_status;
    private ImageView feeling_image;

  //  private FFmpeg ffmpeg;
    private ProgressDialog progressDialog;
  //  private int audioDuration;
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

        text_category_selected_audio = (LinearLayout) findViewById(R.id.text_category_selected_audio);
        selected_feeling_audio = (LinearLayout) findViewById(R.id.selected_feeling_audio);
        audio_category_back = (LinearLayout) findViewById(R.id.audio_category_back);
        audio_feeling_back = (LinearLayout) findViewById(R.id.audio_feeling_back);
        audio_text_status_back = (LinearLayout) findViewById(R.id.audio_text_status_back);
        audio_record_back = (LinearLayout) findViewById(R.id.audio_record_back);
        selected_audio_file = (LinearLayout) findViewById(R.id.selected_audio_file);
        category_selected_audio = (LinearLayout) findViewById(R.id.category_selected_audio);

        choosen_audio_written = (TextView) findViewById(R.id.choosen_audio_written);
        choosen_feeling = (TextView) findViewById(R.id.choosen_feeling);
        choosen_category = (TextView) findViewById(R.id.choosen_category);
        id_audio = (ImageView) findViewById(R.id.id_audio);
        category_id = (ImageView) findViewById(R.id.category_id);
        feeling_image = (ImageView) findViewById(R.id.feeling_image);
        id_status = (ImageView) findViewById(R.id.id_status);

        choosen_audio = (TextView) findViewById(R.id.choosen_audio);


        textView_category = (TextView) findViewById(R.id.textView_category);
        textView_feeling = (TextView) findViewById(R.id.textView_feeling);
        textView_status = (TextView) findViewById(R.id.textView_status);
        textView_record_button = (TextView) findViewById(R.id.recording_start_button);
        post_status = (Button) findViewById(R.id.button_post_audio_status);

        audio_category_back.setOnClickListener(this);
        audio_feeling_back.setOnClickListener(this);
        audio_text_status_back.setOnClickListener(this);
        audio_record_back.setOnClickListener(this);

        id_audio.setOnClickListener(this);
        category_id.setOnClickListener(this);
        feeling_image.setOnClickListener(this);
        id_status.setOnClickListener(this);
        post_status.setOnClickListener(this);

        textView_category.setOnClickListener(this);
        textView_feeling.setOnClickListener(this);
        textView_status.setOnClickListener(this);
        textView_record_button.setOnClickListener(this);

     //   ffmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
    }

    private void startRec() {
        int color = getResources().getColor(R.color.colorPrimaryDark);
        int requestCode = REQUEST_CODE;
        AndroidAudioRecorder.with(this)
                .setColor(color)
                .setRequestCode(requestCode)
                .record();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                audio_time = data.getExtras().getString("audioTime");
            //    Toast.makeText(this, "audio TIme: " + audio_time, Toast.LENGTH_SHORT).show();

           //     path.setText(data.getExtras().getString("path"));

                /************ Audio Received ******************** */
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();

                selected_audio_file.setVisibility(View.VISIBLE);
                choosen_audio.setText("Successful");
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 4) {
            if (resultCode == RESULT_OK) {

                category_selected_audio.setVisibility(View.VISIBLE);
                String result = data.getStringExtra("resultFromCategory");
                String textReturned = data.getStringExtra("resultFromCategory2");
                choosen_category.setText(textReturned);
                category = result;
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                selected_feeling_audio.setVisibility(View.VISIBLE);
                String result2 = data.getStringExtra("resultFromFeeling2");
                String result = data.getStringExtra("resultFromFeeling");
                choosen_feeling.setText(result2);
                feeling = result;

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                text_category_selected_audio.setVisibility(View.VISIBLE);
                String result = data.getStringExtra("resultFromStatus");
                choosen_audio_written.setText(result);
                textStatus = result;
            }
        }
    }

    private void recordActivity() {
        if (ActivityUtils.recordPermissionGranted(this)) {
            startRec();
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
                            postStatus();
                        }

                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();


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
                    .retryWhen(new RetryWithDelay(3,2000))
                    .subscribe(new BaseSubscriber<UserResponse>() {
                        @Override
                        public void onNext(UserResponse userResponse) {
                            Timber.e("UserResponse " + userResponse.getStatus() + "===" + userResponse.getMsg());
                            if (userResponse.getStatus() == 1) {
                                startActivity(new Intent(AudioStatus.this, DiscoverActivity.class));
                                deleteAudio();
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            try {
                                Timber.e(e.getMessage());
                           //     Toast.makeText(AudioStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }catch (Exception ex){
                                ex.printStackTrace();
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
                    startActivity(new Intent(AudioStatus.this, RegisterActivity.class));
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.audio_category_back ||
                view.getId() == R.id.category_id ||
                view.getId() == R.id.textView_category){
       //     processLoggedState(view);
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("AudioStatusActivity")
                    .setAction("Enter Category Page")
                    .build());
            // [END custom_event]
            Intent categoryIntent = new Intent(AudioStatus.this, CategoryActivity.class);
            startActivityForResult(categoryIntent, 4);

        } else if (view.getId() == R.id.audio_feeling_back ||
                view.getId() == R.id.feeling_image ||
                view.getId() == R.id.textView_feeling){

         //   processLoggedState(view);
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("AudioStatusActivity")
                    .setAction("Enter Feeling Page")
                    .build());
            // [END custom_event]
            Intent feelingIntent = new Intent(AudioStatus.this, FeelingActivity.class);
            startActivityForResult(feelingIntent, 2);


        } else if (view.getId() == R.id.audio_text_status_back ||
                view.getId() == R.id.id_status ||
                view.getId() == R.id.textView_status){
         //   processLoggedState(view);
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("AudioStatusActivity")
                    .setAction("Enter Text Status Page")
                    .build());
            // [END custom_event]
            Intent statusIntent = new Intent(AudioStatus.this, StatusActivity.class);
            startActivityForResult(statusIntent, 3);

        } else if (view.getId() == R.id.audio_record_back ||
                view.getId() == R.id.id_audio ||
                view.getId() == R.id.recording_start_button){
            recordActivity();
        } else if (view.getId() == R.id.button_post_audio_status){
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
    }
}
