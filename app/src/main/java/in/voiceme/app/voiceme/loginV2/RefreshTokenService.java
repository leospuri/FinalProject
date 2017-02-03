package in.voiceme.app.voiceme.loginV2;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by tejas on 12/26/16.
 */

public class RefreshTokenService extends Job {

    public static final String TAG = "job_refresh_token";
    private AuthService authService;
    private Context context;
    private Runnable runnable;

    public RefreshTokenService() {
    }

    public RefreshTokenService(Context context, Runnable runnable) {
        this.context = context;
        this.runnable = runnable;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        if (authService == null) {
            authService = new AuthService(context);
        }
        authService.refreshToken(runnable);
        return Result.SUCCESS;
    }

    public void schedulePeriodicJob() {
        int jobId =
                new JobRequest.Builder(RefreshTokenService.TAG).setPeriodic(TimeUnit.MINUTES.toMillis(55),
                        TimeUnit.MINUTES.toMillis(5)).setPersisted(true).build().schedule();
    }

    public void cancelJob(int jobId) {
        JobManager.instance().cancel(jobId);
    }

    public void cacelAll() {
        JobManager.instance().cancelAll();
    }
}
