package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class FeelingActivity extends BaseActivity {
    private MaterialAnimatedSwitch happy_switch_button;
    private MaterialAnimatedSwitch relax_switch_button;
    private MaterialAnimatedSwitch angry_switch_button;
    private MaterialAnimatedSwitch sad_switch_button;
    private MaterialAnimatedSwitch bored_switch_button;

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

        Button button = (Button) findViewById(R.id.btn_feeling);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // get the Entered  message
                Intent returnIntent = new Intent();
                returnIntent.putExtra("resultFromFeeling", current_feeling);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

        happy_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.happy_switch);
        relax_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.relaxed_switch);
        angry_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.angry_switch);
        sad_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.sad_switch);
        bored_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.bored_switch);

        happy_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (happy_switch_button.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }
                }
            }
        });

        relax_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (relax_switch_button.isChecked()) {
                    setFeeling("2");
                    if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }
                }
            }
        });

        angry_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (angry_switch_button.isChecked()) {
                    setFeeling("1");
                    if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }
                }
            }
        });
        sad_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sad_switch_button.isChecked()) {
                    setFeeling("4");
                    if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }
                }
            }
        });
        bored_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (bored_switch_button.isChecked()) {
                    setFeeling("5");
                    if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    }
                }
            }
        });

    }

    public void setFeeling(String feelingName) {
        current_feeling = feelingName;
    }

//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
