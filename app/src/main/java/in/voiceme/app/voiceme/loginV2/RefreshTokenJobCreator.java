package in.voiceme.app.voiceme.loginV2;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by tejas on 12/26/16.
 */

public class RefreshTokenJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case RefreshTokenService.TAG:
                return new RefreshTokenService(VoicemeApplication.getContext(), new Thread());
            default:
                return null;
        }
    }
}
