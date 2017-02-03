package in.voiceme.app.voiceme.ActivityPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.LoginActivity;
import in.voiceme.app.voiceme.loginV2.AuthService;
import in.voiceme.app.voiceme.loginV2.RefreshTokenService;
import in.voiceme.app.voiceme.userpost.AudioStatus;
import in.voiceme.app.voiceme.userpost.TextStatus;
import timber.log.Timber;

public class MainActivity extends BaseActivity
        implements GoogleApiClient.OnConnectionFailedListener, Constants {
    private static Context applicationContext;
    FloatingActionButton textStatus;
    FloatingActionButton audioStatus;
    FloatingActionsMenu rightLabels;
    private AuthService authService;
    private RefreshTokenService refreshTokenService;
    private static ImageView play_button;
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        checkAuthStatus();

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Activity Page");
        setNavDrawer(new MainNavDrawer(this));

        applicationContext = this.getApplicationContext();

        Timber.v("Configuration");
        Timber.v("  > Amazon Identity Pool:          " + getString(R.string.aws_identity_pool));
        Timber.v("  > Google Service ID:             " + getString(R.string.google_server_client_id));
        Timber.v("  > Facebook Application ID:       " + getString(R.string.facebook_app_id));
        prefs = getSharedPreferences("Logged in or not", MODE_WORLD_WRITEABLE);
        isDemoMode = prefs.getBoolean("is this demo mode", false);
        if (!isDemoMode)
            checkAuthStatus();
        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        textStatus = (FloatingActionButton) findViewById(R.id.action_a);
        audioStatus = (FloatingActionButton) findViewById(R.id.action_b);
        play_button = (ImageView) findViewById(R.id.list_item_posts_play_button);

        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Toast.makeText(MainActivity.this, "Button 01", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, TextStatus.class));
                rightLabels.toggle();

            }
        });


        audioStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Toast.makeText(MainActivity.this, "button 02", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AudioStatus.class));
                rightLabels.toggle();
            }
        });

        /**
         * Initialize Facebook SDK
         */
        FacebookSdk.sdkInitialize(getApplicationContext());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        this.addPages(viewPager);

        // Give the PagerSlidingTabStrip the ViewPager
        SmartTabLayout tabsStrip = (SmartTabLayout) findViewById(R.id.tabs_main_activity);
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
                authService = new AuthService(MainActivity.this);
                authService.refreshToken(new Thread());

                scheduleTokenRefresh();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }

    //add all pages
    private void addPages(ViewPager pager) {
        MainActivityFragmentPagerAdapter adapter =
                new MainActivityFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new ActivityYourFeedFragment());
        adapter.addPage(new ActivityInteractionFragment());

        //set adapter to pager
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Implements GoogleApiClient.OnConnectionFailedListener */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format("Failed to connect to Google [error #%d, %s]...",
                connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        Timber.e(message);
    }

    private void scheduleTokenRefresh() {

        refreshTokenService = new RefreshTokenService(MainActivity.this, new Thread());

        refreshTokenService.schedulePeriodicJob();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshTokenService != null) refreshTokenService.cacelAll();
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
