package in.voiceme.app.voiceme.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.userpost.TextStatus;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public class Intro2Activity extends DotStepper implements StepzeroInterface, StepOneInterface, StepTwoInterface, StepThreeInterface, StepFourInterface {

    private int i = 1;
    private String usernameText = null;
    private String feelingID = null;
    private String categoryID = null;
    private String textStatus = null;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1500);
        setTitle("Voiceme Community");
        addStep(createFragment(new StepSample()));
        addStep(createFragment(new StepSample2()));
        addStep(createFragment(new StepSample3()));
        addStep(createFragment(new StepSample4()));
        addStep(createFragment(new StepSample5()));

        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putInt("position", i++);
        fragment.setArguments(b);
        return fragment;
    }


    private void postStatus(){
        VoicemeApplication application = (VoicemeApplication)getApplication();
        SharedPreferences preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        try {
            application.getWebService().postStatus(MySharedPreferences.getUserId(preferences),
                    textStatus, categoryID, feelingID, "", "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<UserResponse>() {
                        @Override
                        public void onNext(UserResponse userResponse) {
                            Timber.e("UserResponse " + userResponse.getStatus() + "===" + userResponse.getMsg());
                            if (userResponse.getStatus() == 1) {
                                Toast.makeText(Intro2Activity.this, "Successfully posted status", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Intro2Activity.this, MainActivity.class));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onComplete() {
        super.onComplete();
        Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
       // startActivity(new Intent(this, MainActivity.class));
        postStatus();
    }

    @Override
    public void username(String name) {
        Toast.makeText(this, "username set: " + name, Toast.LENGTH_SHORT).show();
        usernameText = name;
    }

    @Override
    public void sendFeeling(String feelingID) {
        Toast.makeText(this, "Feeling ID: " + feelingID, Toast.LENGTH_SHORT).show();
        this.feelingID = feelingID;
    }

    @Override
    public void setCategory(String category) {
        Toast.makeText(this, "Feeling ID: " + category, Toast.LENGTH_SHORT).show();
        categoryID = category;
    }

    @Override
    public void sendTextStatus(String status) {
        Toast.makeText(this, "Feeling ID: " + status, Toast.LENGTH_SHORT).show();
        textStatus = status;
    }

    @Override
    public void sendToken(String token) {
        this.token = token;
    }
}
