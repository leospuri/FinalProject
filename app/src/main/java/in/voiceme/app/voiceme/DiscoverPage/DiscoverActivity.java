package in.voiceme.app.voiceme.DiscoverPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.robertsimoes.shareable.Shareable;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.ConstantOnlineRepeatService;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.login.SecondBeforeLoginActivity;
import in.voiceme.app.voiceme.userpost.NewAudioStatusActivity;
import in.voiceme.app.voiceme.userpost.NewTextStatusActivity;
import timber.log.Timber;

public class DiscoverActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, Constants {

    private static Context applicationContext;
    private static final int REQUEST_VIEW_MESSAGE = 1;
    FloatingActionButton textStatus;
    FloatingActionButton audioStatus;
    FloatingActionsMenu rightLabels;
    private final String share_url = "https://play.google.com/store/apps/details?id=in.voiceme.app.voiceme";
    private final Uri share_image = Uri.parse("https://s3-us-west-2.amazonaws.com/voice-me-images/Untitled-3.jpg");

    /**
     * saves and shows state: if the user in demo mode or not .
     */
    private SharedPreferences prefs;
    /**
     * Is true if  the user is  in demo mode.
     * Otherwise is  false
     */
    private boolean isDemoMode;

    public static Context getStaticApplicationContext() {
        return applicationContext;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_discover);
        getSupportActionBar().setTitle("Discover");
        setNavDrawer(new MainNavDrawer(this));

        applicationContext = this.getApplicationContext();
        textStatus = (FloatingActionButton) findViewById(R.id.action_a);
        audioStatus = (FloatingActionButton) findViewById(R.id.action_b);
        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        startService(new Intent(this, ConstantOnlineRepeatService.class));


        prefs = getSharedPreferences("Logged in or not", MODE_PRIVATE);
        isDemoMode = prefs.getBoolean("is this demo mode", false);
        if (!isDemoMode)
            checkAuthStatus();

        // ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // viewPager.setAdapter(new DiscoverActivityFragmentPagerAdapter(getSupportFragmentManager()));

        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);

                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("PostStatus")
                        .setAction("Text Post Clicked")
                        .build());
                // [END custom_event]

                startActivity(new Intent(DiscoverActivity.this, NewTextStatusActivity.class));
                rightLabels.toggle();

            }
        });


        audioStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);

                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("PostStatus")
                        .setAction("Audio Post Clicked")
                        .build());
                // [END custom_event]



                startActivity(new Intent(DiscoverActivity.this, NewAudioStatusActivity.class));
                rightLabels.toggle();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_discover_viewpager);
        this.addPages(viewPager);

        // Give the PagerSlidingTabStrip the ViewPager
        SmartTabLayout tabsStrip = (SmartTabLayout) findViewById(R.id.activity_discover_tab_layout);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    //quits demo mode
    public void onBackPressed() {
        if (isDemoMode)
            prefs.edit().putBoolean("is this demo mode", false).apply();
        super.onBackPressed();
    }

    private void checkAuthStatus() {
        if (!application.getAuth().getUser().isLoggedIn()) {
            if (application.getAuth().hasAuthToken()) {
                return;
            } else {
                startActivity(new Intent(this, SecondBeforeLoginActivity.class));
                finish();
            }
        }
    }


    /* Implements GoogleApiClient.OnConnectionFailedListener */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format("Failed to connect to Google [error #%d, %s]...",
                connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        Timber.e(message);
    }

  /*  private void scheduleTokenRefresh() {

        refreshTokenService = new RefreshTokenService(DiscoverActivity.this, new Thread());

        refreshTokenService.schedulePeriodicJob();
    }
    */

  /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshTokenService != null) refreshTokenService.cacelAll();
    }
    */

    //add all pages
    private void addPages(ViewPager pager) {
        DiscoverActivityFragmentPagerAdapter adapter =
                new DiscoverActivityFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new DiscoverLatestFragment());
        adapter.addPage(new DiscoverTrendingFragment());
        adapter.addPage(new DiscoverFeaturedFragment());

        //set adapter to pager
        pager.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 191 && resultCode == Activity.RESULT_OK){

            Intent intent = data;
            String countryCode = intent.getStringExtra(ShareActivity.RESULT_CONTRYCODE);
            String shareMessage = intent.getStringExtra(Constants.SHARE_MESSAGE);
            switch (countryCode){
                case "1":
                    facebook(shareMessage);
                    return;
                case "2":
                    twitter(shareMessage);
                    return;
                case "3":
                    plus(shareMessage);
                    return;
                case "4":
                    linkedin(shareMessage);
                    return;
                case "5":
                    startActivity(Intent.createChooser(sharedIntentMaker(shareMessage), "Choose an app"));
                    return;
            }
        }
    }

    private Intent sharedIntentMaker(String message){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message + " You can download from \" +\n" +
                "                                \"https://play.google.com/store/apps/details?id=in.voiceme.app.voiceme");
        return shareIntent;
    }

    private void facebook(String message) {
        Shareable shareInstance = new Shareable.Builder(this)
                .message(message)
             //   .image(share_image)
                .socialChannel(Shareable.Builder.FACEBOOK)
                .url(share_url)
                .build();
        shareInstance.share();
    }

    private void linkedin(String message) {
        Shareable shareInstance = new Shareable.Builder(this)
                .message(message)
             //   .image(share_image)
                .socialChannel(Shareable.Builder.LINKED_IN)
                .url(share_url)
                .build();
        shareInstance.share();
    }

    private void twitter(String message) {
        Shareable shareInstance = new Shareable.Builder(this)
                .message(message)
                .socialChannel(Shareable.Builder.TWITTER)
            //    .image(share_image)
                .url(share_url)
                .build();
        shareInstance.share();
    }

    private void plus(String message) {
        Shareable shareInstance = new Shareable.Builder(this)
                .message(message)
              //  .image(share_image)
                .socialChannel(Shareable.Builder.GOOGLE_PLUS)
                .url(share_url)
                .build();
        shareInstance.share();
    }



//  @Override
//    public boolean processLoggedState(View viewPrm) {          if(this.mBaseLoginClass.isDemoMode(viewPrm))                      {Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show(); return true;} return false;
//
//  }
}
