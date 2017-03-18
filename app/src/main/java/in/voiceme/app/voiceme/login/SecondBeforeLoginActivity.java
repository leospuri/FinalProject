package in.voiceme.app.voiceme.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;

import in.voiceme.app.voiceme.R;

public class SecondBeforeLoginActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatusBar(false);

        addSlide(IntroFragment.newInstance("Welcome!", "Confess Anonymously via text or Audio Status.", R.drawable.intro_slide, Color.parseColor("#5c6bc0")));
        addSlide(IntroFragment.newInstance("Secure Login", "We use Facebook and Gmail to prevent Hackers and Spammers.", R.drawable.intro_slide, Color.parseColor("#00bcd4")));
        addSlide(IntroFragment.newInstance("Diverse Culture", "Our Voice Me community have people from various backgrounds. Please Commit to respect each other.", R.drawable.intro_slide, Color.parseColor("#4caf50")));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
