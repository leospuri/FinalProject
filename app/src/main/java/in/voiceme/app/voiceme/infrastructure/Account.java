package in.voiceme.app.voiceme.infrastructure;

import android.net.Uri;

import in.voiceme.app.voiceme.NotificationsPage.ChatTextPojo;

/**
 * Created by Harish on 7/25/2016.
 */
public final class Account {

    private Account() {

    }

    public static class UserResponse extends ServiceResponse {
        public int Id;
        public String AvatarUrl;
        public String UserNickName;
        public String Email;
        public String AuthToken;
        public String gender;
    }

    /* Login with Register with username and Paswword */

    public static class ChatMessageBusEvent {
        public ChatTextPojo messagePojo;

        public ChatMessageBusEvent(ChatTextPojo messagePojo) {
            this.messagePojo = messagePojo;
        }
    }

    /* Server returns the response for register login with username and passsword */
    public static class LoginWithUsernameResponse extends ServiceResponse {
    }


    /* login with local auth token that came from above register */
    public static class LoginWithLocalTokenRequest {
        public String AuthToken;

        public LoginWithLocalTokenRequest(String authToken) {
            AuthToken = authToken;
        }
    }

    /* login response with local auth token from register */
    public static class LoginWithLocalTokenResponse extends UserResponse {
    }


    /* login with Facebook and Google */
    public static class LoginWithExternalTokenRequest {
        public String Provider;
        public String Token;
        public String ClientId;

        public LoginWithExternalTokenRequest(String provider, String token) {
            Provider = provider;
            Token = token;
            ClientId = "android";
        }
    }

    /* UserResponse from Facebook and Google login */
    public static class LoginWithExternalTokenResponse extends UserResponse {
    }


    /* when we are register for account, we are registering a  new user and loggin in at the same time */
    public static class RegisterRequest {
        public String UserName;
        public String Email;
        public String Password;
        public String ClientId;

        public RegisterRequest(String userName, String email, String password) {
            UserName = userName;
            Email = email;
            Password = password;
            ClientId = "android";
        }
    }

    public static class RegisterResponse extends UserResponse {
    }

    /* registering with facebook and loggin in at the same time. all user information will come inside app and user's confirm */
    public static class RegisterWithExternalTokenRequest {
        public String UserName;
        public String Email;
        public String Provider;
        public String Token;
        public String ClientId;

        public RegisterWithExternalTokenRequest(String userName, String email, String provider, String token) {
            UserName = userName;
            Email = email;
            Provider = provider;
            Token = token;
            ClientId = "android";
        }
    }

    public static class RegisterWithExternalTokenResponse extends UserResponse {
    }


    public static class ChangeAvatarRequest {
        public Uri NewAvatarUri;

        public ChangeAvatarRequest(Uri newAvatarUri) {
            NewAvatarUri = newAvatarUri;
        }
    }

    public static class ChangeAvatarResponse extends ServiceResponse {
        public String avatarUrl;
    }

    public static class UpdateProfileRequest {
        public String UserNickName;
        public String Email;

        public UpdateProfileRequest(String userNickName, String email) {
            UserNickName = userNickName;
            Email = email;
        }
    }

    public static class UpdateProfileResponse extends ServiceResponse {
        public String displayName;
        public String Email;
    }

    public static class ChangePasswordRequest {
        public String CurrentPassword;
        public String NewPassword;
        public String ConfirmNewPassword;

        public ChangePasswordRequest(String currentPassword, String newPassword, String confirmNewPassword) {
            CurrentPassword = currentPassword;
            NewPassword = newPassword;
            this.ConfirmNewPassword = confirmNewPassword;
        }
    }

    public static class ChangePasswordResponse extends ServiceResponse {
    }

    public static class UserDetailsUpdatedEvent {
        public in.voiceme.app.voiceme.DTO.User User;

        public UserDetailsUpdatedEvent(in.voiceme.app.voiceme.DTO.User user) {
            User = user;
        }
    }

    public static class UpdateGcmRegistrationRequest {
        public String registrationId;

        public UpdateGcmRegistrationRequest(String registrationId) {
            this.registrationId = registrationId;
        }
    }

    public static class UpdateGcmRegistrationResponse extends ServiceResponse {

    }

    public static class GoogleAccessTokenCognito {
        public String accessToken;

        public GoogleAccessTokenCognito(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getToken() {
            return accessToken;
        }
    }

    public static class FacebookAccessTokenCognito {
        public String fbToken;

        public FacebookAccessTokenCognito(String fbToken) {
            this.fbToken = fbToken;
        }
    }


    public static class AmazonIdentity {
        public String amazonIdentity;

        public AmazonIdentity(String amazonIdentity) {
            this.amazonIdentity = amazonIdentity;
        }

        public String getAmazonIdentity() {
            return amazonIdentity;
        }
    }

    public static class chatData {
        public ChatTextPojo chatTextPojo;

        public chatData(ChatTextPojo chatTextPojo) {
            this.chatTextPojo = chatTextPojo;
        }
    }


}
