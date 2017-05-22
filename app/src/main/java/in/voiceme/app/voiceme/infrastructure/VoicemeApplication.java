package in.voiceme.app.voiceme.infrastructure;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Bus;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.one.EmojiOneProvider;

import net.danlew.android.joda.JodaTimeAndroid;

import in.voiceme.app.voiceme.BuildConfig;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.services.ServiceFactory;
import in.voiceme.app.voiceme.services.WebService;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

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

        try {
            webService = ServiceFactory.createRetrofitService(WebService.class);
        } catch (NullPointerException ex){
            Timber.e("Null Pointer exception");
        } catch (Exception ex){
            ex.printStackTrace();
        }





        EmojiManager.install(new EmojiOneProvider());

        /* ************************************** */
       Timber.plant(new Timber.DebugTree() {
            // Add the line number to the TAG
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });







        JodaTimeAndroid.init(this);
        FacebookSdk.sdkInitialize(this);
        Fresco.initialize(this);

        sAnalytics = GoogleAnalytics.getInstance(this);
        /* *****************************************/
   //     Fabric.with(this, new Crashlytics());
     //   Timber.plant(new ReleaseTree());


        context = getApplicationContext();
        /*
         *Creates a periodic job to refresh token
         */

     //   LeakCanary.install(this);

        Timber.d("Setting up StrictMode policy checking.");
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
                .kits(new Answers(), new Crashlytics(), new TwitterCore(authConfig), new Digits.Builder().build())
                .debuggable(true)
                .build();

        Fabric.with(fabric);

        // initDatabase();
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
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

    /*
    private void initDatabase() {

        Realm.init(instance);

        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    */


}
