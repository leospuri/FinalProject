package in.voiceme.app.voiceme.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import in.voiceme.app.voiceme.R;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample extends AbstractStep implements View.OnClickListener {
    private Button accept;
    private boolean yes = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_one, container, false);
        accept = (Button) v.findViewById(R.id.intro01_accept);



        accept.setOnClickListener(this);

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


    @Override
    public void onClick(View view) {
        yes = true;
        onNext();
    }
}
