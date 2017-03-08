package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.l;

public class FeelingActivity extends BaseActivity {
    private MaterialAnimatedSwitch happy_switch_button;
    private MaterialAnimatedSwitch relax_switch_button;
    private MaterialAnimatedSwitch angry_switch_button;
    private MaterialAnimatedSwitch sad_switch_button;
    private MaterialAnimatedSwitch bored_switch_button;
    private MaterialAnimatedSwitch disappointed_switch;
    private MaterialAnimatedSwitch loved_switch;
    private MaterialAnimatedSwitch sleepy_switch;
    private MaterialAnimatedSwitch flirty_switch;
    private MaterialAnimatedSwitch optimistic_switch;
    private MaterialAnimatedSwitch jealous_switch;
    private MaterialAnimatedSwitch sick_switch;
    private MaterialAnimatedSwitch tired_switch;
    private MaterialAnimatedSwitch sexy_switch;
    private MaterialAnimatedSwitch pranky_switch;
    private MaterialAnimatedSwitch frustrated_switch;

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
        disappointed_switch = (MaterialAnimatedSwitch) findViewById(R.id.disappointed_switch);
        loved_switch = (MaterialAnimatedSwitch) findViewById(R.id.loved_switch);
        sleepy_switch = (MaterialAnimatedSwitch) findViewById(R.id.sleepy_switch);
        flirty_switch = (MaterialAnimatedSwitch) findViewById(R.id.flirty_switch);
        optimistic_switch = (MaterialAnimatedSwitch) findViewById(R.id.optimistic_switch);
        jealous_switch = (MaterialAnimatedSwitch) findViewById(R.id.jealous_switch);
        sick_switch = (MaterialAnimatedSwitch) findViewById(R.id.sick_switch);
        tired_switch = (MaterialAnimatedSwitch) findViewById(R.id.tired_switch);
        sexy_switch = (MaterialAnimatedSwitch) findViewById(R.id.sexy_switch);
        pranky_switch = (MaterialAnimatedSwitch) findViewById(R.id.pranky_switch);
        frustrated_switch = (MaterialAnimatedSwitch) findViewById(R.id.frustrated_switch);

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
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        relax_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (relax_switch_button.isChecked()) {
                    setFeeling("3");
                    if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        angry_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (angry_switch_button.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });
        sad_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sad_switch_button.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });
        bored_switch_button.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (bored_switch_button.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });
        disappointed_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (disappointed_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        loved_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (loved_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        sleepy_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sleepy_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        flirty_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (flirty_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        optimistic_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (optimistic_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        jealous_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (jealous_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        sick_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sick_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        tired_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (tired_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        sexy_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (sexy_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {relax_switch_button.toggle();}
                    else if (angry_switch_button.isChecked()) {angry_switch_button.toggle();}
                    else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        pranky_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (pranky_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (frustrated_switch.isChecked()) {
                        frustrated_switch.toggle();
                    }
                }
            }
        });

        frustrated_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (frustrated_switch.isChecked()) {
                    setFeeling("3");
                    if (relax_switch_button.isChecked()) {
                        relax_switch_button.toggle();
                    } else if (angry_switch_button.isChecked()) {
                        angry_switch_button.toggle();
                    } else if (sad_switch_button.isChecked()) {
                        sad_switch_button.toggle();
                    } else if (bored_switch_button.isChecked()) {
                        bored_switch_button.toggle();
                    }else if (disappointed_switch.isChecked()) {
                        disappointed_switch.toggle();
                    }else if (loved_switch.isChecked()) {
                        loved_switch.toggle();
                    }else if (sleepy_switch.isChecked()) {
                        sleepy_switch.toggle();
                    }else if (flirty_switch.isChecked()) {
                        flirty_switch.toggle();
                    }else if (optimistic_switch.isChecked()) {
                        optimistic_switch.toggle();
                    }else if (jealous_switch.isChecked()) {
                        jealous_switch.toggle();
                    }else if (sick_switch.isChecked()) {
                        sick_switch.toggle();
                    }else if (tired_switch.isChecked()) {
                        tired_switch.toggle();
                    }else if (sexy_switch.isChecked()) {
                        sexy_switch.toggle();
                    }else if (happy_switch_button.isChecked()) {
                        happy_switch_button.toggle();
                    }else if (pranky_switch.isChecked()) {
                        pranky_switch.toggle();
                    }
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
}
