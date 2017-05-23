package in.voiceme.app.voiceme.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.DTO.SuccessResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample2 extends AbstractStep {
    private EditText usernameText;
    private boolean yes = false;
    private String color;
    private String token;
    private Button checkUsername;
    private TextView usernameCheck;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_two, container, false);

        usernameText = (EditText) v.findViewById(R.id.intro_username);
        token = FirebaseInstanceId.getInstance().getToken();
        checkUsername = (Button) v.findViewById(R.id.check_user_name);
        usernameCheck = (TextView) v.findViewById(R.id.check_user_available);
        usernameCheck.setVisibility(View.GONE);

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                username = usernameText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (usernameText.getText().toString().trim().length() > 25){
                    Toast.makeText(getActivity(), "Please Enter shorter username", Toast.LENGTH_SHORT).show();
                    yes = false;
                } else {
                    yes = true;
                }

                // do nothing
            }
        });

        checkUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameCheck.setVisibility(View.VISIBLE);
                usernameCheck.setText("");
                if (!usernameText.getText().toString().trim().isEmpty()){
                    checkUsername(usernameText.getText().toString().trim());

                }
            }
        });

        Timber.d(String.valueOf("token from fcm: " + token));

        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.USERNAME, username);
        outState.putString(Constants.TOKEN, token);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            username = savedInstanceState.getString(Constants.USERNAME);
            token = savedInstanceState.getString(Constants.TOKEN);
        }

    }

    private void checkUsername(String username){
        try {
            ((VoicemeApplication)getActivity().getApplication()).getWebService()
                    .checkUsername(username)
                    .retryWhen(new RetryWithDelay(3,2000))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<SuccessResponse>() {
                        @Override
                        public void onNext(SuccessResponse response) {

                            if (response.getSuccess()){
                                usernameCheck.setText("Username already exists");
                                usernameCheck.setTextColor(getActivity().getResources().getColor(R.color.md_red_300));
                            } else {
                                usernameCheck.setText("Username is available");
                                usernameCheck.setTextColor(getActivity().getResources().getColor(R.color.md_green_300));
                                yes = true;
                            }
                            //    MySharedPreferences.registerUsername(preferences, usernameText);
                            //Todo add network call for uploading profile_image file
                            //    startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkUsername2(String username){
        try {
            ((VoicemeApplication)getActivity().getApplication()).getWebService()
                    .checkUsername(username)
                    .retryWhen(new RetryWithDelay(3,2000))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<SuccessResponse>() {
                        @Override
                        public void onNext(SuccessResponse response) {

                            if (response.getSuccess()){
                                usernameCheck.setText("Username already exists");
                                usernameCheck.setTextColor(getActivity().getResources().getColor(R.color.md_red_300));
                            } else {
                                usernameCheck.setText("Username is available");
                                usernameCheck.setTextColor(getActivity().getResources().getColor(R.color.md_green_300));
                                StepOneInterface stepOneInterface = (StepOneInterface) getActivity();
                                stepOneInterface.username(usernameText.getText().toString());
                                stepOneInterface.sendToken(token);
                                yes = true;
                            }
                            //    MySharedPreferences.registerUsername(preferences, usernameText);
                            //Todo add network call for uploading profile_image file
                            //    startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

   /* private void submitDataWithoutProfile() throws Exception {
        application.getWebService()
                .LoginUserName(MySharedPreferences.getSocialID(preferences), username.getText().toString(),
                        "", token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileAboutMe>() {
                    @Override
                    public void onNext(ProfileAboutMe response) {

                        MySharedPreferences.registerUsername(preferences, username.getText().toString());
                        //Todo add network call for uploading profile_image file
                        //    startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                    }
                }); */

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        if (!yes){
            if (!usernameText.getText().toString().trim().isEmpty()){
                checkUsername2(usernameText.getText().toString().trim());
            }
        }
        return yes;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {

            StepOneInterface stepOneInterface = (StepOneInterface) getActivity();
            stepOneInterface.username(usernameText.getText().toString());
            stepOneInterface.sendToken(token);

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
        return yes;

    }


    @Override
    public String error() {
        return "<b>Please Check Username Availability!</b>";
    }


}
