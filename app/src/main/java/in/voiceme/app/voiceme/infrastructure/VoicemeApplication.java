package in.voiceme.app.voiceme.infrastructure;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.digits.sdk.android.Digits;
import com.evernote.android.job.JobManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Bus;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import in.voiceme.app.voiceme.BuildConfig;
import in.voiceme.app.voiceme.loginV2.RefreshTokenJobCreator;
import in.voiceme.app.voiceme.services.ServiceFactory;
import in.voiceme.app.voiceme.services.WebService;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by Harish on 7/20/2016.
 */
public class VoicemeApplication extends Application {
    private static Auth auth;
    private static Bus bus;
    private static Context context;
    private WebService webService;
    private VoicemeApplication instance;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public VoicemeApplication() {
        bus = new Bus();
    }

    public static Context getContext() {
        return context;
    }

    public static Auth getAuth() {
        return auth;
    }

    public static Bus getBus() {
        return bus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //Fabric.with(this, new Crashlytics());
        auth = new Auth(this);
        FacebookSdk.sdkInitialize(this);
        webService = ServiceFactory.createRetrofitService(WebService.class);

        /*****************************************/
       Timber.plant(new Timber.DebugTree() {
            // Add the line number to the TAG
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });

        sAnalytics = GoogleAnalytics.getInstance(this);
        /******************************************/
   //     Fabric.with(this, new Crashlytics());
     //   Timber.plant(new ReleaseTree());


        context = getApplicationContext();
        /**
         *Creates a periodic job to refresh token
         */
        JobManager.create(this).addJobCreator(new RefreshTokenJobCreator());

     //   LeakCanary.install(this);

        Log.d(TAG, "Setting up StrictMode policy checking.");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        final TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY,
                BuildConfig.CONSUMER_SECRET);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Answers(), new TwitterCore(authConfig), new Digits.Builder().build())
                .debuggable(true)
                .build();

        Fabric.with(fabric);

        initDatabase();
    }

    synchronized public Tracker getDefaultTracker() {
        if (sTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        //    sTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return sTracker;
    }

    public WebService getWebService() {
        return webService;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initDatabase() {

        Realm.init(instance);

        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
