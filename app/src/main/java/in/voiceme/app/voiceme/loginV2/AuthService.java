package in.voiceme.app.voiceme.loginV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.login.LoginActivity;
import in.voiceme.app.voiceme.login.RefreshTokenTask;
import in.voiceme.app.voiceme.login.account.AccountManager;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.FACEBOOK_LOGIN;
import static in.voiceme.app.voiceme.infrastructure.Constants.GOOGLE_LOGIN;
import static in.voiceme.app.voiceme.infrastructure.Constants.KEY_LAST_USED_PROVIDER;
import static in.voiceme.app.voiceme.infrastructure.Constants.PREF_FILE;

public class AuthService {

    private AccountManager manager;
    private SharedPreferences settings;
    private Context context;

    public AuthService(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(PREF_FILE, Activity.MODE_WORLD_WRITEABLE);
    }

    public void refreshToken(Runnable onCompletion) {

        if (manager == null) {
            manager = AccountManager.getInstance();
        }
        refreshCredentials(onCompletion, context);
    }

    /**
     * Refresh the Cognito credentials based on the last used provider and run onCompletion once
     * valid
     * credentials have been obtained
     *
     * @param onCompletion a runnable that will be executed once the credentials have been
     *                     successfully renewed
     */
    private void refreshCredentials(Runnable onCompletion, Context context) {

        String lastUsedProvider = settings.getString(KEY_LAST_USED_PROVIDER, null);

        // The below might produce an NPE, but should not. Left as is on purpose (would highlight a flow issue)
        if (lastUsedProvider.equals(GOOGLE_LOGIN)) {
            this.refreshGoogleCredentials(onCompletion, context);
        } else if (lastUsedProvider.equals(FACEBOOK_LOGIN)) {
            this.refreshFacebookCredentials(onCompletion, context);
        } else {
            Timber.e("Unknown previous identity provider " + lastUsedProvider);
        }
    }

    /**
     * Refresh cognito credentials based on identity token previously obtained from Google
     *
     * @param onCompletion a runnable that will be executed once the credentials have been
     *                     successfully renewed
     */
    private void refreshGoogleCredentials(final Runnable onCompletion, final Context context) {
        Timber.v("Will try to refresh Google token as session has expired...");

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                        context.getString(R.string.google_server_client_id)).build();

        GoogleApiClient googleApiClient =
                new GoogleApiClient.Builder(context).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        final OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (pendingResult.isDone()) {
            Timber.v("  -> got immediate result...");
            // There's immediate result available.
            GoogleSignInResult result = pendingResult.get();
            GoogleSignInAccount acct = result.getSignInAccount();
            String token = acct.getIdToken();
            Map<String, String> logins = new HashMap<>();
            logins.put(GOOGLE_LOGIN, token);

            Timber.v("  -> will refresh login for " + GOOGLE_LOGIN);

            // Refreshing session credentials (must be asynchronous)...
            new RefreshTokenTask(onCompletion).execute(logins);
        } else {

            // There's no immediate result ready, displays some progress indicator and waits for the
            // async callback.
            Timber.v("  -> got NO immediate result...");

            //this.setProgressBarIndeterminateVisibility(true);

            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {

                    if (result.isSuccess()) {

                        Timber.v("  -> got successful PENDING result...");
                        GoogleSignInAccount acct = result.getSignInAccount();
                        String token = acct.getIdToken();
                        Map<String, String> logins = new HashMap<>();
                        logins.put(GOOGLE_LOGIN, token);

                        Timber.v("  -> will refresh login for " + GOOGLE_LOGIN);

                        new RefreshTokenTask(onCompletion).execute(logins);
                    } else {
                        Timber.v("  -> got failed PENDING result: redirecting user to sign in...");
                        // It didn't work, we show the sign in, but things will get messy...
                        // TODO onCompletion is ignored (should be dealt with in onActivityResult)
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     * Refresh cognito credentials based on identity token previously obtained from Facebook
     *
     * @param onCompletion a runnable that will be executed once the credentials have been
     *                     successfully renewed
     */
    private void refreshFacebookCredentials(Runnable onCompletion, Context context) {

        Timber.v("Will try to refresh Facebook token as session has expired...");

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (!accessToken.isExpired()) {

            Timber.v(String.format("  -> Facebook token is not expired (expiration %s)...",
                    SimpleDateFormat.getInstance().format(accessToken.getExpires())));
            String token = accessToken.getToken();

            Map<String, String> logins = new HashMap<>();
            logins.put(FACEBOOK_LOGIN, token);

            Timber.v("  -> will refresh login for " + FACEBOOK_LOGIN);

            // Refreshing session credentials (must be asynchronous)...
            new RefreshTokenTask(onCompletion).execute(logins);

            // We use the opportunity to refresh the Facebook token
            Date now = new Date();
            if (accessToken.getLastRefresh().getTime() < (now.getTime() - 7 * 86400)) {
                Timber.v("  -> triggering async facebook token refresh");
                AccessToken.refreshCurrentAccessTokenAsync();
            }
        } else {

            Timber.v(String.format("  -> Facebook token is expired (expiration %s)...",
                    SimpleDateFormat.getInstance().format(accessToken.getExpires())));
            //User has to sign in to refresh an elapsed facebook token
            // TODO onCompletion is ignored (should be dealt with in onActivityResult)
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
