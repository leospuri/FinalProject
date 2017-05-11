package in.voiceme.app.voiceme.userpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.login.StepFourInterface;
import in.voiceme.app.voiceme.login.StepThreeInterface;
import in.voiceme.app.voiceme.login.StepTwoInterface;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public class NewTextStatusActivity extends DotStepper implements StepTwoInterface, StepThreeInterface, StepFourInterface {

    private int i = 1;
    private String usernameText = null;
    private String feelingID = null;
    private String categoryID = null;
    private String textStatus = null;
    private ProgressDialog loading = null;
    private SharedPreferences preferences = null;
    private VoicemeApplication application = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (VoicemeApplication)getApplication();
        preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        setErrorTimeout(1500);
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Loading");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        setTitle("Post Text Status");
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

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        AbstractStep step = this.mSteps.getCurrent();

        if (count < 5) {
            step.onPrevious();
            //additional code
        } else if (count == 0){
            super.onBackPressed();
        }
    }

    private void postStatus(){
        try {
            application.getWebService().postStatus(MySharedPreferences.getUserId(preferences),
                    textStatus, categoryID, feelingID, "", "")
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
            Toast.makeText(NewTextStatusActivity.this, "Successfully posted status", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewTextStatusActivity.this, DiscoverActivity.class);
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
        postStatus();
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
