package in.voiceme.app.voiceme.login;

import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class IntroActivity extends BaseActivity {
    /*
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
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

*/

}
