package in.voiceme.app.voiceme.login;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.redbooth.WelcomeCoordinatorLayout;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class BeforeLoginActivity extends BaseActivity implements View.OnClickListener {
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    WelcomeCoordinatorLayout coordinatorLayout;
    private static final int REQUEST_REGISTER = 2;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);

        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.logincoordinator);

        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();

        registerButton = (Button) findViewById(R.id.activity_login_register);

        registerButton.setOnClickListener(this);
    }

    private void initializePages() {
        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout)findViewById(R.id.logincoordinator);
        coordinatorLayout.addPage(R.layout.start_01,
                R.layout.start_02,
                R.layout.start_03,
                R.layout.start_04);
    }

    private void initializeListeners() {
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
    }

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

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view == registerButton) {
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("BeforeLoginActivity")
                    .setAction("Clicked Register Button")
                    .build());
            // [END custom_event]
            // network call from retrofit
            startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_REGISTER);
        }
    }

    public void tryDemoOnClick(View viewPrm) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("BeforeLoginActivity")
                .setAction("Clicked Skip Button")
                .build());
        // [END custom_event]
        SharedPreferences prefsLcl = getSharedPreferences("Logged in or not", MODE_PRIVATE);
        prefsLcl.edit().putBoolean("is this demo mode", true).apply();
        startActivity(new Intent(this, AnonymousLogin.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_REGISTER) {
            finishLogin();
        }
    }

    private void finishLogin() {
        SharedPreferences prefsLcl = getSharedPreferences("Logged in or not", MODE_PRIVATE);
        prefsLcl.edit().putBoolean("is this demo mode", false).apply();
        if (secondPage()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        } else {
            startActivity(new Intent(this, IntroActivity.class));
            finish();
            return;
        }
    }
}
