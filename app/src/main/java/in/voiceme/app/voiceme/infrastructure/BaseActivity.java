package in.voiceme.app.voiceme.infrastructure;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.PostsDetails.PostsDetailsActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.WasLoggedInInterface;
import in.voiceme.app.voiceme.l;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public abstract class BaseActivity extends AppCompatActivity implements WasLoggedInInterface {
    private static String TAG = MainActivity.class.getSimpleName();
    protected VoicemeApplication application;
    protected Toolbar toolbar;
    protected NavDrawer navDrawer;
    protected Bus bus;
    protected ActionScheduler scheduler;
    protected SharedPreferences preferences;
    private boolean isRegisterdWithBus;
    protected String givenContact;
    protected String givenFacebook;
    protected Tracker mTracker;
    public String id;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        application = (VoicemeApplication) getApplication();
        bus = application.getBus();
        scheduler = new ActionScheduler(application);
        preferences = getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);


      //  ActivityUtils.deleteAudioFile(this.getBaseContext())
    /*    File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        */

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            String idStr = uri.substring(uri.lastIndexOf('=') + 1);
            id = (idStr);
            Log.e("int value", ":======" + id);

            if (!id.equals("0")) {
                Intent intent = new Intent(BaseActivity.this, PostsDetailsActivity.class);
                intent.putExtra(Constants.POST_BACKGROUND, id);
                startActivity(intent);
            }
        }


        mTracker = application.getDefaultTracker();


        bus.register(this);
        isRegisterdWithBus = true;


        givenContact = preferences.getString(Constants.GET_CONTACT_NUMBER, null);
        givenFacebook = preferences.getString(Constants.FACEBOOK_ID, null);

        /**
         * Initialize Facebook SDK

         FacebookSdk.sdkInitialize(getApplicationContext());   */

        /**
         * Initializes the sync client. This must be call before you can use it. */
    }


    public boolean hasContactNumber() {
        return givenContact != null && !givenContact.isEmpty();
    }

    public boolean hasFacebook() {
        return givenFacebook != null && !givenFacebook.isEmpty();
    }

    public void setAuthToken(String givenContact) {
        this.givenContact = givenContact;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.GET_CONTACT_NUMBER, givenContact);
        editor.commit();
    }

    public ActionScheduler getScheduler() {
        return scheduler;
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduler.onResume();
    }

    protected boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        scheduler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isRegisterdWithBus) {
            bus.unregister(this);
            isRegisterdWithBus = false;
        }




        if (navDrawer != null) navDrawer.destroy();
    }

    @Override
    public void finish() {
        super.finish();

        if (isRegisterdWithBus) {
            bus.unregister(this);
            isRegisterdWithBus = false;
        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);

        toolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public void fadeOut(final FadeOutListener listener) {
        View rootView = findViewById(android.R.id.content);
        rootView.animate().alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onFadeOutEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).setDuration(300).start();
    }

    protected void setNavDrawer(NavDrawer drawer) {
        this.navDrawer = drawer;
        this.navDrawer.create();

        overridePendingTransition(0, 0);

        View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(450).start();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public VoicemeApplication getVoicemeApplication() {
        return application;
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

    public interface FadeOutListener {
        void onFadeOutEnd();
    }

    @Override
    public void onBackPressed(){
            super.onBackPressed();
    }
}
