package in.voiceme.app.voiceme.contactPage;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import in.voiceme.app.voiceme.DTO.ContactAddResponse;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.ContactsHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public class ContactService extends IntentService {

    public static final String BROADCAST=
            "in.voiceme.app.voiceme.contactPage.ContactService.BROADCAST";
    private static Intent broadcast=new Intent(BROADCAST);


    SharedPreferences preferences;
    private final IBinder mBinder = new MyBinder();



    public class MyBinder extends Binder {
        ContactService getService(){
            return ContactService.this;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

       // SystemClock.sleep(5000);

    }

    public ContactService() {
        super("ContactService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Handler mainHandler = new Handler(this.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                getContacts();
            } // This is your code
        };
        mainHandler.post(myRunnable);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getContacts() {


        Observable.fromCallable(
                () -> ContactsHelper.readContacts(getContentResolver()))
                .doOnError(throwable -> Timber.d(throwable.getMessage()))
                .onErrorResumeNext(throwable -> {
                    Timber.d(throwable.getMessage());
                    return Observable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    try {
                        sendAllContacts(contacts.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // showContactsCompletedDialog(contacts);
                    Timber.d("comma separated contacts array %s", contacts.toString().replace("[", "").replace("]", ""));
                });
    }

    private void sendAllContacts(String contacts) throws Exception {
        ((VoicemeApplication)getApplication()).getWebService()
                .addAllContacts(MySharedPreferences.getUserId(preferences), contacts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<ContactAddResponse>() {
                    @Override
                    public void onNext(ContactAddResponse response) {
                        Timber.e("Got user details " + response.getInsertedRows().toString());
                        LocalBroadcastManager.getInstance(ContactService.this).sendBroadcast(broadcast);

                    }
                });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
