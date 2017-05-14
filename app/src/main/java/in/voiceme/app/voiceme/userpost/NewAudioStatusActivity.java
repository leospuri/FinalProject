package in.voiceme.app.voiceme.userpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.io.File;

import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.login.StepFourInterface;
import in.voiceme.app.voiceme.login.StepThreeInterface;
import in.voiceme.app.voiceme.login.StepTwoInterface;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public class NewAudioStatusActivity extends DotStepper implements StepTwoInterface, StepThreeInterface, StepFourInterface {

    private int i = 1;
    private String usernameText = null;
    private String feelingID = null;
    private String categoryID = null;
    private String textStatus = null;
    private ProgressDialog loading = null;
    private SharedPreferences preferences = null;
    private VoicemeApplication application = null;
    private String audioFileUrl;
    private String audio_time;
    private ProgressDialog progressDialog;
    private static final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio"+".mp3";

    public static final int REQUEST_CODE = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (VoicemeApplication)getApplication();
        preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        setErrorTimeout(1500);
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Loading");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     //   ViewSwitcher switcher_layout = (ViewSwitcher)findViewById(com.github.fcannizzaro.materialstepper.R.id.stepSwitcher);
    //    switcher_layout.setBackgroundColor(getResources().getColor(R.color.emotion_color));


        setTitle("Post Text Status");
        addStep(createFragment(new StepSample2()));
        addStep(createFragment(new StepSample4()));
        addStep(createFragment(new StepSample5()));
        addStep(createFragment(new StepSample6()));

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putInt("position", i++);
        fragment.setArguments(b);
        return fragment;
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
                    .subscribeOn(Schedulers.io())
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

    private void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    private void postStatus(){
        try {
            application.getWebService().postStatus(MySharedPreferences.getUserId(preferences),
                    textStatus, categoryID, feelingID, audioFileUrl, audio_time)
                    .retryWhen(new RetryWithDelay(3,2000))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<UserResponse>() {
                        @Override
                        public void onNext(UserResponse userResponse) {
                            Timber.e("UserResponse " + userResponse.getStatus() + "===" + userResponse.getMsg());
                            finishLogin(userResponse);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishLogin(UserResponse userResponse) {
        if (userResponse.getStatus() == 1) {
            Toast.makeText(NewAudioStatusActivity.this, "Successfully posted status", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewAudioStatusActivity.this, DiscoverActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        //    Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(this, MainActivity.class));

        loading.show();
        readAudioFileStorage();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {



       //     textview_audio_success.setVisibility(View.VISIBLE);
            audio_time = data.getExtras().getString("audioTime");
      //      textview_ask_audio_perm.setText("Recording done");
            //    Toast.makeText(this, "audio TIme: " + audio_time, Toast.LENGTH_SHORT).show();

            //     path.setText(data.getExtras().getString("path"));

            /************ Audio Received ******************** */

            Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
        //    textview_audio_success.setText("Successful");

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendFeeling(String feelingID) {
        this.feelingID = feelingID;
    }

    @Override
    public void setCategory(String category) {
        categoryID = category;
    }

    @Override
    public void sendTextStatus(String status) {
        textStatus = status;
    }
}
