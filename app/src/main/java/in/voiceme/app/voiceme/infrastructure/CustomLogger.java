package in.voiceme.app.voiceme.infrastructure;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.digits.sdk.android.DigitsEventLogger;
import com.digits.sdk.android.events.ContactsPermissionForDigitsImpressionDetails;
import com.digits.sdk.android.events.DigitsEventDetails;
import com.digits.sdk.android.events.LogoutEventDetails;

/**
 * Created by harish on 2/9/2017.
 */

public class CustomLogger extends DigitsEventLogger {

    @Override
    public void loginBegin(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("LoginBegin")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void phoneNumberImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("PhoneNumberImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void phoneNumberSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("PhoneNumberSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void phoneNumberSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("PhoneNumberSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void confirmationCodeImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("ConfirmationCodeImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void confirmationCodeSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("ConfirmationCodeSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void confirmationCodeSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("ConfirmationCodeSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void twoFactorPinImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("TwoFactorPinImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void twoFactorPinSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("TwoFactorPinSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void twoFactorPinSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("TwoFactorPinSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void emailImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("EmailImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void emailSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("EmailSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void emailSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("EmailSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void failureImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("FailureImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void failureRetryClick(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("FailureRetryClick")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void failureDismissClick(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("FailureDismissClick")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void loginSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("LoginSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void loginFailure(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("LoginFailure")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void logout(LogoutEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Logout")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country));
    }

    @Override
    public void contactsPermissionForDigitsImpression(
            ContactsPermissionForDigitsImpressionDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("ContactsPermissionForDigitsImpression"));
    }

}
