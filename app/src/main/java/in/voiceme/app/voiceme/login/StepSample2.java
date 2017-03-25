package in.voiceme.app.voiceme.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.R;
import timber.log.Timber;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample2 extends AbstractStep {
    private EditText usernameText;
    private TextView mAutofitOutput;
    private boolean yes = false;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_two, container, false);
        usernameText = (EditText) v.findViewById(R.id.intro_username);
        token = FirebaseInstanceId.getInstance().getToken();

        Timber.d(String.valueOf("token from fcm: " + token));

        mAutofitOutput = (TextView) v.findViewById(R.id.intro_output_autofitpage2);
        mAutofitOutput.setGravity(Gravity.CENTER);
        mAutofitOutput.setVisibility(View.GONE);

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAutofitOutput.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                yes = true;
                mAutofitOutput.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
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
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
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
        return "<b>You must enter username!</b>";
    }


}
