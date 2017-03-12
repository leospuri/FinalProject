package in.voiceme.app.voiceme.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.R;
import timber.log.Timber;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample extends AbstractStep {
    private boolean yes = false;
    private String token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_one, container, false);
        token = FirebaseInstanceId.getInstance().getToken();

        Toast.makeText(getActivity(), "Token " + token, Toast.LENGTH_SHORT).show();
        Timber.d(String.valueOf("token from fcm: " + token));

        return v;
    }

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
        if (yes){
            return true;
        } else {
            Toast.makeText(getActivity(), "IsOptional is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        yes=true;
        StepzeroInterface stepOneInterface = (StepzeroInterface) getActivity();
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
        if (yes){
            return true;
        } else {
            Toast.makeText(getActivity(), "nextIf is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must agree!</b> <small>please respect other people!</small>";
    }

}
