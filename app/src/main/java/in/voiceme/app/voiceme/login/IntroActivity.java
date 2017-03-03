package in.voiceme.app.voiceme.login;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.redbooth.WelcomeCoordinatorLayout;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import rx.android.schedulers.AndroidSchedulers;

public class IntroActivity extends BaseActivity {
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    private WelcomeCoordinatorLayout coordinatorLayout;
    private MaterialAnimatedSwitch happy_switch_button;
    private MaterialAnimatedSwitch relax_switch_button;
    private MaterialAnimatedSwitch angry_switch_button;
    private MaterialAnimatedSwitch sad_switch_button;
    private MaterialAnimatedSwitch bored_switch_button;
    private EditText usernameEntered;
    private TextView usernameValidation;
    private boolean firstPageDone = false;
    private View firstPageNext;

    private String current_feeling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator_login);

        // initializeListeners();
        initializePages();
        initializeBackgroundTransitions();

        happy_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.login_happy_switch);
        relax_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.login_relaxed_switch);
        angry_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.login_angry_switch);
        sad_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.login_sad_switch);
        bored_switch_button = (MaterialAnimatedSwitch) findViewById(R.id.login_bored_switch);
        usernameEntered = (EditText) findViewById(R.id.username_intro);
        usernameValidation = (TextView) findViewById(R.id.check_username_status);
        firstPageNext = findViewById(R.id.first_page_next);

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



    private void initializePages() {
        coordinatorLayout.addPage(R.layout.start_01,
                R.layout.start_02,
                R.layout.start_03,
                R.layout.start_04);
    }

   /* private void initializeListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
            }
        });


    } */

    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();
        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.page1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.page2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.page3, getTheme());
        final int colorPage4 = ResourcesCompat.getColor(resources, R.color.page4, getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3, colorPage4);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    public void next(View view) {
        coordinatorLayout.setCurrentPage(1, true);
    }

    public void check(View view) {

        if (!usernameEntered.getText().toString().isEmpty()){
            try {
                getData(usernameEntered.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getData(String username) throws Exception {
        application.getWebService()
                .checkUsername(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        if (response.equals("false")){
                            usernameValidation.setVisibility(View.VISIBLE);
                            usernameValidation.setText("User name is available");
                            usernameValidation.setHighlightColor(getResources().getColor(R.color.md_green_A200));
                            firstPageDone = true;
                            firstPageNext.setVisibility(View.VISIBLE);
                        } else {
                            usernameValidation.setVisibility(View.VISIBLE);
                            usernameValidation.setText("User name is not available");
                            usernameValidation.setHighlightColor(getResources().getColor(R.color.md_red_A200));
                        }
                    }
                });
    }


}
