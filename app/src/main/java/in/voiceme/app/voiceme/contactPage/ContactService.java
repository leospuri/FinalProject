package in.voiceme.app.voiceme.contactPage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import in.voiceme.app.voiceme.DTO.ContactAddResponse;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.utils.ContactsHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public class ContactService extends Service {
    SharedPreferences preferences;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private final IBinder mBinder = new MyBinder();



    public class MyBinder extends Binder {
        ContactService getService(){
            return ContactService.this;
        }
    }

    public ContactService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(ContactService.this, "Service createed", Toast.LENGTH_SHORT).show();
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
                .subscribe(new BaseSubscriber<ContactAddResponse>() {
                    @Override
                    public void onNext(ContactAddResponse response) {
                        Timber.e("Got user details " + response.getInsertedRows().toString());
                        Toast.makeText(ContactService.this, "Sent All Contacts", Toast.LENGTH_SHORT).show();
                        ((VoicemeApplication)getApplication()).getAuth().getUser().setAllContacts(true);
                        showNotification();
                    }
                });
    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, ContactListActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Your Anonymous Page is ready")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Your Page is ready")
                .setContentText("Click to enter")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(ContactService.this, "Service destroyed", Toast.LENGTH_SHORT).show();
    }
}
