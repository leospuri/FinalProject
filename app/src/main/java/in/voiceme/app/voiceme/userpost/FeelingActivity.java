package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.l;

public class FeelingActivity extends BaseActivity {
    private MaterialAnimatedSwitch happy_switch_button, relax_switch_button, angry_switch_button, sad_switch_button,
            bored_switch_button, loved_switch, sleepy_switch, flirty_switch, sick_switch, tired_switch, sexy_switch;

    private MaterialAnimatedSwitch firstSwitch = null;

    private String current_feeling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling);
        getSupportActionBar().setTitle("Choose Feeling");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        happy_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.happy_switch);
        relax_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.relaxed_switch);
        angry_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.angry_switch);
        sad_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.sad_switch);
        bored_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.bored_switch);
        loved_switch = (MaterialAnimatedSwitch) findViewById(R.id.loved_switch);
        sleepy_switch = (MaterialAnimatedSwitch) findViewById(R.id.sleepy_switch);
        flirty_switch = (MaterialAnimatedSwitch) findViewById(R.id.flirty_switch);
        sick_switch = (MaterialAnimatedSwitch) findViewById(R.id.sick_switch);
        tired_switch = (MaterialAnimatedSwitch) findViewById(R.id.tired_switch);
        sexy_switch = (MaterialAnimatedSwitch) findViewById(R.id.sexy_switch);

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

    }

    public void setFeeling(String feelingName) {
        current_feeling = feelingName;
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }

    private void setCurrent_feeling(MaterialAnimatedSwitch feeling){
        if (firstSwitch == null){
            firstSwitch = feeling;
        } else {
            firstSwitch.toggle();
            firstSwitch = feeling;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categorymenu, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.category_menu) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("resultFromFeeling", current_feeling);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
