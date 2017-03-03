package in.voiceme.app.voiceme.chat;

import android.os.Handler;

import com.squareup.otto.Bus;

import java.util.Random;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harish on 3/3/2017.
 */

public class BaseMessages {
    protected final Bus bus;
    protected final VoicemeApplication application;
    protected final Handler handler;
    protected final Random random;

    protected BaseMessages(VoicemeApplication application) {
        this.application = application;
        bus = application.getBus();
        handler = new Handler();
        random = new Random();
        bus.register(this);
    }
}
