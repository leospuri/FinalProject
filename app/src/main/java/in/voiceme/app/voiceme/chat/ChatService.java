package in.voiceme.app.voiceme.chat;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by harishpc on 6/9/2017.
 */
public class ChatService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ChatService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
