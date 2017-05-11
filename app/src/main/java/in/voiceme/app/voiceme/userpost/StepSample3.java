package in.voiceme.app.voiceme.userpost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.login.StepTwoInterface;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample3 extends AbstractStep {
    private MaterialAnimatedSwitch happy_switch_button, relax_switch_button, angry_switch_button, sad_switch_button,
            bored_switch_button, loved_switch, sleepy_switch, flirty_switch, sick_switch, tired_switch, sexy_switch;

    private String current_feeling = null;
    private boolean yes = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_three, container, false);
        happy_switch_button = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_happy_switch);
        relax_switch_button = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_relaxed_switch);
        angry_switch_button = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_angry_switch);
        sad_switch_button = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_sad_switch);
        bored_switch_button = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_bored_switch);
        loved_switch = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_loved_switch);
        sleepy_switch = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_sleepy_switch);
        flirty_switch = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_flirty_switch);
        sick_switch = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_sick_switch);
        tired_switch = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_tired_switch);
        sexy_switch = (MaterialAnimatedSwitch) v.findViewById(R.id.intro_sexy_switch);

        happy_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (happy_switch_button.isChecked()) {
                    checkFeeling("1");
                    setFeeling("1");
                }
            }
        });

        relax_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (relax_switch_button.isChecked()) {
                    checkFeeling("2");
                    setFeeling("2");
                }
            }
        });

        angry_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (angry_switch_button.isChecked()) {
                    checkFeeling("3");
                    setFeeling("3");
                }
            }
        });

        sad_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sad_switch_button.isChecked()) {
                    checkFeeling("4");
                    setFeeling("4");
                }
            }
        });

        bored_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (bored_switch_button.isChecked()) {
                    checkFeeling("5");
                    setFeeling("5");
                }
            }
        });

        loved_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (loved_switch.isChecked()) {
                    checkFeeling("6");
                    setFeeling("6");
                }
            }
        });

        sleepy_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sleepy_switch.isChecked()) {
                    checkFeeling("7");
                    setFeeling("7");
                }
            }
        });

        flirty_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (flirty_switch.isChecked()) {
                    checkFeeling("8");
                    setFeeling("8");
                }
            }
        });

        sick_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sick_switch.isChecked()) {
                    checkFeeling("9");
                    setFeeling("9");
                }
            }
        });

        tired_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (tired_switch.isChecked()) {
                    checkFeeling("10");
                    setFeeling("10");
                }
            }
        });

        sexy_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sexy_switch.isChecked()) {
                    checkFeeling("11");
                    setFeeling("11");
                }
            }
        });

        return v;
    }

    private void checkFeeling(String value){
        if (!value.equals("1")){
            if (happy_switch_button.isChecked()) {
                happy_switch_button.toggle();
            }}
        if (!value.equals("2")){
            if (relax_switch_button.isChecked()) {
                relax_switch_button.toggle();
            }}

        if (!value.equals("3")){
            if (angry_switch_button.isChecked()) {
                angry_switch_button.toggle();
            }}
        if (!value.equals("4")){
            if (sad_switch_button.isChecked()) {
                sad_switch_button.toggle();
            }}
        if (!value.equals("5")){
            if (bored_switch_button.isChecked()) {
                bored_switch_button.toggle();
            }}
        if (!value.equals("6")){
            if (loved_switch.isChecked()) {
                loved_switch.toggle();
            }}
        if (!value.equals("7")){
            if (sleepy_switch.isChecked()) {
                sleepy_switch.toggle();
            }}
        if (!value.equals("8")){
            if (flirty_switch.isChecked()) {
                flirty_switch.toggle();
            }}
        if (!value.equals("9")){
            if (sick_switch.isChecked()) {
                sick_switch.toggle();
            }}
        if (!value.equals("10")){
            if (tired_switch.isChecked()) {
                tired_switch.toggle();
            }}
        if (!value.equals("11")){
            if (sexy_switch.isChecked()) {
                sexy_switch.toggle();
            }}
    }

    public void setFeeling(String feelingName) {
        yes = true;
        current_feeling = feelingName;
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
            return false;
        }
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        StepTwoInterface stepOneInterface = (StepTwoInterface) getActivity();
        stepOneInterface.sendFeeling(current_feeling);
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
    public boolean nextIf() {;
        if (yes){
            return true;
        } else {
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }


}
