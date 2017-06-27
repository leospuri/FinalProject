package in.voiceme.app.voiceme.chat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

/**
 * Created by harishpc on 6/27/2017.
 */
public class ConstantOnlineRepeatService  extends Service {

    private MyThread mythread;
    public boolean isRunning = false;
    private static SharedPreferences userIdPreference;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userIdPreference = ((VoicemeApplication) getApplication()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);

        mythread  = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }

    class MyThread extends Thread{
        static final long DELAY = 58000;
        @Override
        public void run(){
            while(isRunning){
                try {
                    sendOnlineIndicator(MySharedPreferences.getUserId(userIdPreference));
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }


    protected void sendOnlineIndicator(String userId) {

        ((VoicemeApplication) getApplication()).getWebService()
                .sendOnline(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Sent Online Indicator");
                        // dialogInit();

                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }






}
