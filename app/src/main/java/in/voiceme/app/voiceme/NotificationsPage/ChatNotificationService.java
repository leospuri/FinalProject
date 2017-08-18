package in.voiceme.app.voiceme.NotificationsPage;

import android.app.IntentService;
import android.content.Intent;

import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harishpc on 6/18/2017.
 */
public class ChatNotificationService extends IntentService {
    private Bus bus;
   // private Bus bus;

    public ChatNotificationService() {
        super("ChatNotificationService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    //    application = (VoicemeApplication)getApplicationContext();
    //    bus = application.getBus();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        bus = ((VoicemeApplication)getApplication()).getBus();

        bus.register(this);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public ChatNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ChatTextPojo chatMessage = intent.getExtras().getParcelable(Constants.CHAT_MESSAGE);
        bus.post(new Account.ChatMessageBusEvent(chatMessage));
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);

        super.onDestroy();
    }
}
