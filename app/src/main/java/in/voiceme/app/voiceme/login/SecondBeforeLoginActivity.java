package in.voiceme.app.voiceme.login;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

public class SecondBeforeLoginActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatusBar(false);

     //   addSlide(IntroFragment.newInstance("Welcome!", "Confess Anonymously via text or Audio Status.", R.drawable.splash_01, getResources().getColor(R.color.transparent)));
     //   addSlide(IntroFragment.newInstance("Secure Login", "We use Facebook and Gmail to prevent Hackers and Spammers.", R.drawable.splash_02, getResources().getColor(R.color.transparent)));
     //   addSlide(IntroFragment.newInstance("Diverse Culture", "Our Voice Me community has people from various backgrounds. Please Commit to respect each other.", R.drawable.splash_03, getResources().getColor(R.color.transparent)));
        addSlide(new BlankFragment01());
        addSlide(new BlankFragment02());
        addSlide(new BlankFragment03());
        addSlide(new BlankFragment());

        showSkipButton(false);
        setProgressButtonEnabled(false);

    }

}
