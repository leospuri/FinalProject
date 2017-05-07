package in.voiceme.app.voiceme.login;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

import in.voiceme.app.voiceme.R;

public class SecondBeforeLoginActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatusBar(false);

        addSlide(IntroFragment.newInstance("Welcome!", "Confess Anonymously via text or Audio Status.", R.drawable.social, Color.parseColor("#5c6bc0")));
        addSlide(IntroFragment.newInstance("Secure Login", "We use Facebook and Gmail to prevent Hackers and Spammers.", R.drawable.hacker, Color.parseColor("#00bcd4")));
        addSlide(IntroFragment.newInstance("Diverse Culture", "Our Voice Me community has people from various backgrounds. Please Commit to respect each other.", R.drawable.social02, Color.parseColor("#4caf50")));
        addSlide(new BlankFragment());

        showSkipButton(false);
        setProgressButtonEnabled(false);

    }

}
