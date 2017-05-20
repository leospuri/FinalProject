package in.voiceme.app.voiceme.userpost;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.login.StepTwoInterface;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepSample2 extends AbstractStep implements View.OnClickListener {
    private boolean sad;
    private boolean happy;
    private boolean sexy;
    private boolean tired;
    private boolean loved;
    private boolean flirty;
    private boolean sleepy;
    private boolean relax;
    private boolean angry;
    private boolean sick;
    private boolean bored;
    private boolean heartbroken;
    private boolean optimistic;
    private boolean blessed;
    private String current_feeling = null;

    private LinearLayout sad_emotion_back;
    private LinearLayout happy_emotion_back;
    private LinearLayout sexy_emotion_back;
    private LinearLayout tired_emotion_back;
    private LinearLayout loved_emotion_back;
    private LinearLayout flirty_emotion_back;
    private LinearLayout sleepy_emotion_back;
    private LinearLayout relax_emotion_back;
    private LinearLayout angry_emotion_back;
    private LinearLayout sick_emotion_back;
    private LinearLayout bored_emotion_back;
    private LinearLayout heartbroken_emotion_back;
    private LinearLayout optimistic_emotion_back;
    private LinearLayout blessed_emotion_back;

    private ImageView sad_emotion_icon;
    private ImageView happy_emotion_icon;
    private ImageView sexy_emotion_icon;
    private ImageView tired_emotion_icon;
    private ImageView loved_emotion_icon;
    private ImageView angry_emotion_icon;
    private ImageView flirty_emotion_icon;
    private ImageView sleepy_emotion_icon;
    private ImageView relax_emotion_icon;
    private ImageView sick_emotion_icon;
    private ImageView bored_emotion_icon;
    private ImageView heartbroken_emotion_icon;
    private ImageView optimistic_emotion_icon;
    private ImageView blessed_emotion_icon;

    private TextView sad_emotion_text;
    private TextView happy_emotion_text;
    private TextView sexy_emotion_text;
    private TextView tired_emotion_text;
    private TextView loved_emotion_text;
    private TextView angry_emotion_text;
    private TextView flirty_emotion_text;
    private TextView sleepy_emotion_text;
    private TextView relax_emotion_text;
    private TextView sick_emotion_text;
    private TextView bored_emotion_text;
    private TextView heartbroken_emotion_text;
    private TextView optimistic_emotion_text;
    private TextView blessed_emotion_text;

    private ProgressBar step5progressbar;
    private boolean yes = false;

    Toolbar toolbar;

    public static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_step_sample2, container, false);

        sad_emotion_back = (LinearLayout) v.findViewById(R.id.sad_emotion_back);
        happy_emotion_back = (LinearLayout) v.findViewById(R.id.happy_emotion_back);
        sexy_emotion_back = (LinearLayout) v.findViewById(R.id.sexy_emotion_back);
        tired_emotion_back = (LinearLayout) v.findViewById(R.id.tired_emotion_back);
        loved_emotion_back = (LinearLayout) v.findViewById(R.id.loved_emotion_back);
        angry_emotion_back = (LinearLayout) v.findViewById(R.id.angry_emotion_back);
        flirty_emotion_back = (LinearLayout) v.findViewById(R.id.flirty_emotion_back);
        sleepy_emotion_back = (LinearLayout) v.findViewById(R.id.sleepy_emotion_back);
        relax_emotion_back = (LinearLayout) v.findViewById(R.id.relax_emotion_back);
        sick_emotion_back = (LinearLayout) v.findViewById(R.id.sick_emotion_back);
        bored_emotion_back = (LinearLayout) v.findViewById(R.id.bored_emotion_back);
        heartbroken_emotion_back = (LinearLayout) v.findViewById(R.id.heartbroken_emotion_back);
        optimistic_emotion_back = (LinearLayout) v.findViewById(R.id.optimistic_emotion_back);
        blessed_emotion_back = (LinearLayout) v.findViewById(R.id.blessed_emotion_back);

        sad_emotion_icon = (ImageView) v.findViewById(R.id.sad_emotion_icon);
        happy_emotion_icon = (ImageView) v.findViewById(R.id.happy_emotion_icon);
        sexy_emotion_icon = (ImageView) v.findViewById(R.id.sexy_emotion_icon);
        tired_emotion_icon = (ImageView) v.findViewById(R.id.tired_emotion_icon);
        loved_emotion_icon = (ImageView) v.findViewById(R.id.loved_emotion_icon);
        angry_emotion_icon = (ImageView) v.findViewById(R.id.angry_emotion_icon);
        flirty_emotion_icon = (ImageView) v.findViewById(R.id.flirty_emotion_icon);
        sleepy_emotion_icon = (ImageView) v.findViewById(R.id.sleepy_emotion_icon);
        relax_emotion_icon = (ImageView) v.findViewById(R.id.relax_emotion_icon);
        sick_emotion_icon = (ImageView) v.findViewById(R.id.sick_emotion_icon);
        bored_emotion_icon = (ImageView) v.findViewById(R.id.bored_emotion_icon);
        heartbroken_emotion_icon = (ImageView) v.findViewById(R.id.heartbroken_emotion_icon);
        optimistic_emotion_icon = (ImageView) v.findViewById(R.id.optimistic_emotion_icon);
        blessed_emotion_icon = (ImageView) v.findViewById(R.id.blessed_emotion_icon);

        happy_emotion_text = (TextView) v.findViewById(R.id.happy_emotion_text);
        sad_emotion_text = (TextView) v.findViewById(R.id.sad_emotion_text);
        sexy_emotion_text = (TextView) v.findViewById(R.id.sexy_emotion_text);
        tired_emotion_text = (TextView) v.findViewById(R.id.tired_emotion_text);
        loved_emotion_text = (TextView) v.findViewById(R.id.loved_emotion_text);
        angry_emotion_text = (TextView) v.findViewById(R.id.angry_emotion_text);
        flirty_emotion_text = (TextView) v.findViewById(R.id.flirty_emotion_text);
        sleepy_emotion_text = (TextView) v.findViewById(R.id.sleepy_emotion_text);
        sick_emotion_text = (TextView) v.findViewById(R.id.sick_emotion_text);
        relax_emotion_text = (TextView) v.findViewById(R.id.relax_emotion_text);
        bored_emotion_text = (TextView) v.findViewById(R.id.bored_emotion_text);
        heartbroken_emotion_text = (TextView) v.findViewById(R.id.heartbroken_emotion_text);
        optimistic_emotion_text = (TextView) v.findViewById(R.id.optimistic_emotion_text);
        blessed_emotion_text = (TextView) v.findViewById(R.id.blessed_emotion_text);

        sad_emotion_back.setOnClickListener(this);
        happy_emotion_back.setOnClickListener(this);
        sexy_emotion_back.setOnClickListener(this);
        tired_emotion_back.setOnClickListener(this);
        loved_emotion_back.setOnClickListener(this);
        angry_emotion_back.setOnClickListener(this);
        flirty_emotion_back.setOnClickListener(this);
        sleepy_emotion_back.setOnClickListener(this);
        relax_emotion_back.setOnClickListener(this);
        sick_emotion_back.setOnClickListener(this);
        bored_emotion_back.setOnClickListener(this);
        heartbroken_emotion_back.setOnClickListener(this);
        optimistic_emotion_back.setOnClickListener(this);
        blessed_emotion_back.setOnClickListener(this);

        sad_emotion_icon.setOnClickListener(this);
        happy_emotion_icon.setOnClickListener(this);
        sexy_emotion_icon.setOnClickListener(this);
        tired_emotion_icon.setOnClickListener(this);
        loved_emotion_icon.setOnClickListener(this);
        angry_emotion_icon.setOnClickListener(this);
        flirty_emotion_icon.setOnClickListener(this);
        sleepy_emotion_icon.setOnClickListener(this);
        relax_emotion_icon.setOnClickListener(this);
        sick_emotion_icon.setOnClickListener(this);
        bored_emotion_icon.setOnClickListener(this);
        heartbroken_emotion_icon.setOnClickListener(this);
        optimistic_emotion_icon.setOnClickListener(this);
        blessed_emotion_icon.setOnClickListener(this);

        happy_emotion_text.setOnClickListener(this);
        sad_emotion_text.setOnClickListener(this);
        sexy_emotion_text.setOnClickListener(this);
        tired_emotion_text.setOnClickListener(this);
        loved_emotion_text.setOnClickListener(this);
        angry_emotion_text.setOnClickListener(this);
        flirty_emotion_text.setOnClickListener(this);
        sleepy_emotion_text.setOnClickListener(this);
        sick_emotion_text.setOnClickListener(this);
        relax_emotion_text.setOnClickListener(this);
        bored_emotion_text.setOnClickListener(this);
        heartbroken_emotion_text.setOnClickListener(this);
        optimistic_emotion_text.setOnClickListener(this);
        blessed_emotion_text.setOnClickListener(this);

        step5progressbar = (ProgressBar) v.findViewById(R.id.step5progressbar);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = mStepper.getToolbar();
        if(toolbar==null){
            Timber.d("toolbar is null");
        }
        else{
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            try {
                actionBar.setHomeAsUpIndicator(R.mipmap.ic_ab_close);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }


        }

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
        // get the Entered  message
    /*    if (feeling_selected_new.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter your status", Toast.LENGTH_LONG).show();
        } else {
            step5progressbar.setVisibility(View.VISIBLE);
            String status = feeling_selected_new.getText().toString();


            StepFourInterface stepOneInterface = (StepFourInterface) getActivity();
            stepOneInterface.sendTextStatus(status);
        } */

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
    public boolean nextIf() {
        if (yes){
            return true;
        } else {
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must select one Feeling!</b>";
    }

    private void removeChecked(){
        if (sad){
            sad = false;
            sad_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            sad_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));

        } else if (happy){
            happy = false;
            happy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            happy_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
        } else if (sexy){
            sexy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            sexy_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            sexy = false;
        } else if (tired){
            tired_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            tired_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            tired = false;
        } else if (loved){
            loved_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            loved_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            loved = false;
        } else if (flirty){
            flirty_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            flirty_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            flirty = false;
        } else if (sleepy){
            sleepy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            sleepy_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            sleepy = false;
        } else if (relax){
            relax_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            relax_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            relax = false;
        } else if (angry){
            angry_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            angry_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            angry = false;
        } else if (sick){
            sick_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            sick_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            sick = false;
        } else if (bored){
            bored_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            bored_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            bored = false;
        } else if (heartbroken){
            heartbroken_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            heartbroken_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            heartbroken = false;
        }else if (optimistic){
            optimistic_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            optimistic_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            optimistic = false;
        }else if (blessed){
            blessed_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            blessed_emotion_back.setBackground(getActivity().getResources().getDrawable(R.drawable.outline));
            blessed = false;
        }
    }

    @Override
    public void onClick(View v) {
       int id = v.getId();
        switch (id){
            case R.id.sad_emotion_back:
                sad_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("4");
                sad = true;
                break;
            case R.id.sad_emotion_icon:
                sad_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("4");
                sad = true;
                break;
            case R.id.sad_emotion_text:
                sad_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("4");
                sad = true;
                break;
            case R.id.blessed_emotion_back:
                blessed_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("14");
                blessed = true;
                break;
            case R.id.blessed_emotion_icon:
                blessed_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("14");
                blessed = true;
                break;
            case R.id.blessed_emotion_text:
                blessed_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("14");
                blessed = true;
                break;
            case R.id.optimistic_emotion_back:
                optimistic_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("13");
                optimistic = true;
                break;
            case R.id.optimistic_emotion_icon:
                optimistic_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("13");
                optimistic = true;
                break;
            case R.id.optimistic_emotion_text:
                optimistic_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("13");
                optimistic = true;
                break;
            case R.id.heartbroken_emotion_back:
                heartbroken_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("12");
                heartbroken = true;
                break;
            case R.id.heartbroken_emotion_icon:
                heartbroken_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("12");
                heartbroken = true;
                break;
            case R.id.heartbroken_emotion_text:
                heartbroken_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_brown_200));
                removeChecked();
                setFeeling("12");
                heartbroken = true;
                break;
            case R.id.happy_emotion_back:
                happy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("1");
                happy = true;
                break;
            case R.id.happy_emotion_icon:
                happy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("1");
                happy = true;
                break;
            case R.id.happy_emotion_text:
                happy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("1");
                happy = true;
                break;
            case R.id.sexy_emotion_back:
                sexy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("11");
                sexy = true;
                break;
            case R.id.sexy_emotion_icon:
                sexy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("11");
                sexy = true;
                break;
            case R.id.sexy_emotion_text:
                sexy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("11");
                sexy = true;
                break;
            case R.id.tired_emotion_back:
                tired_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("10");
                tired = true;
                break;
            case R.id.tired_emotion_icon:
                tired_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("10");
                tired = true;
                break;
            case R.id.tired_emotion_text:
                tired_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("10");
                tired = true;
                break;
            case R.id.loved_emotion_back:
                loved_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("6");
                loved = true;
                break;
            case R.id.loved_emotion_icon:
                loved_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("6");
                loved = true;
                break;
            case R.id.loved_emotion_text:
                loved_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("6");
                loved = true;
                break;
            case R.id.angry_emotion_back:
                angry_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("3");
                angry = true;
                break;
            case R.id.angry_emotion_icon:
                angry_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("3");
                angry = true;
                break;
            case R.id.angry_emotion_text:
                angry_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("3");
                angry = true;
                break;
            case R.id.flirty_emotion_back:
                flirty_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("8");
                flirty = true;
                break;
            case R.id.flirty_emotion_icon:
                flirty_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("8");
                flirty = true;
                break;
            case R.id.flirty_emotion_text:
                flirty_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("8");
                flirty = true;
                break;
            case R.id.sleepy_emotion_back:
                sleepy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("7");
                sleepy = true;
                break;
            case R.id.sleepy_emotion_icon:
                sleepy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("7");
                sleepy = true;
                break;
            case R.id.sleepy_emotion_text:
                sleepy_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("7");
                sleepy = true;
                break;
            case R.id.relax_emotion_back:
                relax_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("2");
                relax = true;
                break;
            case R.id.relax_emotion_icon:
                relax_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("2");
                relax = true;
                break;
            case R.id.relax_emotion_text:
                relax_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_blue_200));
                removeChecked();
                setFeeling("2");
                relax = true;
                break;
            case R.id.sick_emotion_back:
                sick_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("9");
                sick = true;
                break;
            case R.id.sick_emotion_icon:
                sick_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("9");
                sick = true;
                break;
            case R.id.sick_emotion_text:
                sick_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_red_200));
                removeChecked();
                setFeeling("9");
                sick = true;
                break;
            case R.id.bored_emotion_back:
                bored_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("5");
                bored = true;
                break;
            case R.id.bored_emotion_icon:
                bored_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("5");
                bored = true;
                break;
            case R.id.bored_emotion_text:
                bored_emotion_back.setBackgroundColor(getActivity().getResources().getColor(R.color.md_green_200));
                removeChecked();
                setFeeling("5");
                bored = true;
                break;


        }
    }
}
