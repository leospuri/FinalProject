package in.voiceme.app.voiceme.chat;

import in.voiceme.app.voiceme.chat.fixtures.MessagesListFixtures;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harish on 3/2/2017.
 */

public class BaseChat {

    public static void register(VoicemeApplication application) {
        new MessagesListFixtures(application);
    }

}
