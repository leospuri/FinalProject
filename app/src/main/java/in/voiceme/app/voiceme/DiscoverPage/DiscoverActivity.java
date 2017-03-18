package in.voiceme.app.voiceme.DiscoverPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.login.BeforeLoginActivity;
import in.voiceme.app.voiceme.loginV2.AuthService;
import in.voiceme.app.voiceme.loginV2.RefreshTokenService;
import in.voiceme.app.voiceme.userpost.AudioStatus;
import in.voiceme.app.voiceme.userpost.TextStatus;
import timber.log.Timber;

public class DiscoverActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, Constants {

    private static Context applicationContext;
    private static final int REQUEST_VIEW_MESSAGE = 1;
    FloatingActionButton textStatus;
    FloatingActionButton audioStatus;
    private AuthService authService;
    private RefreshTokenService refreshTokenService;
    FloatingActionsMenu rightLabels;

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

                startActivity(new Intent(DiscoverActivity.this, TextStatus.class));
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



                startActivity(new Intent(DiscoverActivity.this, AudioStatus.class));
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
                authService = new AuthService(DiscoverActivity.this);
                authService.refreshToken(new Thread());

                scheduleTokenRefresh();
            } else {
                startActivity(new Intent(this, BeforeLoginActivity.class));
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

    private void scheduleTokenRefresh() {

        refreshTokenService = new RefreshTokenService(DiscoverActivity.this, new Thread());

        refreshTokenService.schedulePeriodicJob();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshTokenService != null) refreshTokenService.cacelAll();
    }

    //add all pages
    private void addPages(ViewPager pager) {
        DiscoverActivityFragmentPagerAdapter adapter =
                new DiscoverActivityFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new DiscoverLatestFragment());
        adapter.addPage(new DiscoverPopularFragment());
        adapter.addPage(new DiscoverTrendingFragment());

        //set adapter to pager
        pager.setAdapter(adapter);
    }

//  @Override
//    public boolean processLoggedState(View viewPrm) {          if(this.mBaseLoginClass.isDemoMode(viewPrm))                      {Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show(); return true;} return false;
//
//  }
}
