package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;

public class Intro2Activity extends DotStepper implements StepOneInterface, StepTwoInterface, StepThreeInterface, StepFourInterface {

    private int i = 1;
    private String usernameText = null;
    private String feelingID = null;
    private String categoryID = null;
    private String textStatus = null;

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



    @Override
    public void onComplete() {
        super.onComplete();
        Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
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
}