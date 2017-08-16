package in.voiceme.app.voiceme.login;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import de.hdodenhof.circleimageview.CircleImageView;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Constants;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepSample1 extends AbstractStep implements AdapterView.OnItemSelectedListener{
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
    private boolean yes = false;

    Toolbar toolbar;

    public static final int REQUEST_CODE = 1;

    private galleryAdapter adapter;
    private MediaPlayer mp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_step_sample1Intro, container, false);

        sad_emotion_back = (LinearLayout) v.findViewById(R.id.sad_emotion_back_intro);
        happy_emotion_back = (LinearLayout) v.findViewById(R.id.happy_emotion_back_intro);
        sexy_emotion_back = (LinearLayout) v.findViewById(R.id.sexy_emotion_back_intro);
        tired_emotion_back = (LinearLayout) v.findViewById(R.id.tired_emotion_back_intro);
        loved_emotion_back = (LinearLayout) v.findViewById(R.id.loved_emotion_back_intro);
        angry_emotion_back = (LinearLayout) v.findViewById(R.id.angry_emotion_back_intro);
        flirty_emotion_back = (LinearLayout) v.findViewById(R.id.flirty_emotion_back_intro);
        sleepy_emotion_back = (LinearLayout) v.findViewById(R.id.sleepy_emotion_back_intro);
        relax_emotion_back = (LinearLayout) v.findViewById(R.id.relax_emotion_back_intro);
        sick_emotion_back = (LinearLayout) v.findViewById(R.id.sick_emotion_back_intro);
        bored_emotion_back = (LinearLayout) v.findViewById(R.id.bored_emotion_back_intro);
        heartbroken_emotion_back = (LinearLayout) v.findViewById(R.id.heartbroken_emotion_back_intro);
        optimistic_emotion_back = (LinearLayout) v.findViewById(R.id.optimistic_emotion_back_intro);
        blessed_emotion_back = (LinearLayout) v.findViewById(R.id.blessed_emotion_back_intro);

        sad_emotion_icon = (ImageView) v.findViewById(R.id.sad_emotion_icon_intro);
        happy_emotion_icon = (ImageView) v.findViewById(R.id.happy_emotion_icon_intro);
        sexy_emotion_icon = (ImageView) v.findViewById(R.id.sexy_emotion_icon_intro);
        tired_emotion_icon = (ImageView) v.findViewById(R.id.tired_emotion_icon_intro);
        loved_emotion_icon = (ImageView) v.findViewById(R.id.loved_emotion_icon_intro);
        angry_emotion_icon = (ImageView) v.findViewById(R.id.angry_emotion_icon_intro);
        flirty_emotion_icon = (ImageView) v.findViewById(R.id.flirty_emotion_icon_intro);
        sleepy_emotion_icon = (ImageView) v.findViewById(R.id.sleepy_emotion_icon_intro);
        relax_emotion_icon = (ImageView) v.findViewById(R.id.relax_emotion_icon_intro);
        sick_emotion_icon = (ImageView) v.findViewById(R.id.sick_emotion_icon_intro);
        bored_emotion_icon = (ImageView) v.findViewById(R.id.bored_emotion_icon_intro);
        heartbroken_emotion_icon = (ImageView) v.findViewById(R.id.heartbroken_emotion_icon_intro);
        optimistic_emotion_icon = (ImageView) v.findViewById(R.id.optimistic_emotion_icon_intro);
        blessed_emotion_icon = (ImageView) v.findViewById(R.id.blessed_emotion_icon_intro);

        happy_emotion_text = (TextView) v.findViewById(R.id.happy_emotion_text_intro);
        sad_emotion_text = (TextView) v.findViewById(R.id.sad_emotion_text_intro);
        sexy_emotion_text = (TextView) v.findViewById(R.id.sexy_emotion_text_intro);
        tired_emotion_text = (TextView) v.findViewById(R.id.tired_emotion_text_intro);
        loved_emotion_text = (TextView) v.findViewById(R.id.loved_emotion_text_intro);
        angry_emotion_text = (TextView) v.findViewById(R.id.angry_emotion_text_intro);
        flirty_emotion_text = (TextView) v.findViewById(R.id.flirty_emotion_text_intro);
        sleepy_emotion_text = (TextView) v.findViewById(R.id.sleepy_emotion_text_intro);
        sick_emotion_text = (TextView) v.findViewById(R.id.sick_emotion_text_intro);
        relax_emotion_text = (TextView) v.findViewById(R.id.relax_emotion_text_intro);
        bored_emotion_text = (TextView) v.findViewById(R.id.bored_emotion_text_intro);
        heartbroken_emotion_text = (TextView) v.findViewById(R.id.heartbroken_emotion_text_intro);
        optimistic_emotion_text = (TextView) v.findViewById(R.id.optimistic_emotion_text_intro);
        blessed_emotion_text = (TextView) v.findViewById(R.id.blessed_emotion_text_intro);

        Gallery gallery=(Gallery)v.findViewById(R.id.gallery_intro);
        adapter=new galleryAdapter(getActivity());
        gallery.setAdapter(adapter);
        gallery.setSpacing(40);
        gallery.setOnItemSelectedListener(this);

     /*   gallery.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                mp.start();
            }
        });
        */



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
        mp = MediaPlayer.create(getActivity(), R.raw. bg_music);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

    public void setFeeling(int position) {
        yes = true;
      //  current_feeling = feelingName;
        switch (position){
            case 0:
                current_feeling = "4";
                break;
            case 1:
                current_feeling = "14";
                break;
            case 2:
                current_feeling = "13";
                break;
            case 3:
                current_feeling = "12";
                break;
            case 4:
                current_feeling = "1";
                break;
            case 5:
                current_feeling = "11";
                break;
            case 6:
                current_feeling = "10";
                break;
            case 7:
                current_feeling = "6";
                break;
            case 8:
                current_feeling = "3";
                break;
            case 9:
                current_feeling = "8";
                break;
            case 10:
                current_feeling = "7";
                break;
            case 11:
                current_feeling = "2";
                break;
            case 12:
                current_feeling = "9";
                break;
            case 13:
                current_feeling = "5";
                break;
        }
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.EMOTION, current_feeling);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            current_feeling = savedInstanceState.getString(Constants.EMOTION);
            mp = MediaPlayer.create(getActivity(), R.raw. bg_music);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        adapter.setSelectItem(position);

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }




    private class galleryAdapter extends BaseAdapter{

        Context mContext;
        private int selectItem;
        private int drawable1[]=new int[]
                {       R.drawable.sad_after,
                        R.drawable.blessed,
                        R.drawable.optimistic,
                        R.drawable.heartbroken,
                        R.drawable.emoji_after, // happy
                        R.drawable.sexy,
                        R.drawable.tired,
                        R.drawable.loved,
                        R.drawable.angry_after,
                        R.drawable.flirty,
                        R.drawable.sleepy,
                        R.drawable.relax_after,
                        R.drawable.sick,
                        R.drawable.bored_after
                        };

        private String[] txt = new String [] {"Sad","Blessed", "Optimistic","Heartbroke","Happy","Sexy", "Tired", "Loved", "Angry", "Flirty",
                "Sleepy", "Relaxed", "Sick", "Bored"};

        public galleryAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return drawable1.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelectItem(int selectItem) {

            if (this.selectItem != selectItem) {
                this.selectItem = selectItem;
                notifyDataSetChanged();
                mp.start();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v;
            if (convertView == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, parent, false);
            }
            else {
                v = convertView;
            }

            CircleImageView img = (CircleImageView) v.findViewById(R.id.img);
            TextView txt_name = (TextView)v.findViewById(R.id.txt_name);

            img.setImageResource(drawable1[position%drawable1.length]);

            txt_name.setText(txt[position%txt.length]);


            if(selectItem==position){

                ViewGroup.LayoutParams params = img.getLayoutParams();
                params.width = 155;
                params.height = 170;
                img.setLayoutParams(params);
                txt_name.setVisibility(View.VISIBLE);
                setFeeling(position);


            }
            else{
                ViewGroup.LayoutParams params = img.getLayoutParams();
                params.width = 65;
                params.height = 80;
                img.setLayoutParams(params);
                txt_name.setVisibility(View.GONE);
            }
            return v;
        }
    }
}
