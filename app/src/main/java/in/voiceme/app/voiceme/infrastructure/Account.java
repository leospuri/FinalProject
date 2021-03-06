package in.voiceme.app.voiceme.infrastructure;

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
    public static class sendCommentReply {
        public String id_post_comments;
        public String id_post_user_name;
        public String message;

        public sendCommentReply(String id_post_comments, String id_post_user_name, String message) {
            this.id_post_comments = id_post_comments;
            this.id_post_user_name = id_post_user_name;
            this.message = message;
        }
    }

    public static class sendLikeUserId {
        public String id_user;
        public String post_id_user;

        public sendLikeUserId(String id_user, String post_id_user) {
            this.id_user = id_user;
            this.post_id_user = post_id_user;
        }
    }

    public static class sendCommentLike {
        public String id_post_comment;
        public String id_user_name;
        public String like;

        public sendCommentLike(String id_post_comment, String id_user_name, String like) {
            this.id_post_comment = id_post_comment;
            this.id_user_name = id_user_name;
            this.like = like;
        }
    }

    public static class sendCommentReplyLike {
        public String id_post_comment_reply;
        public String post_comment_reply_id;

        public sendCommentReplyLike(String id_post_comment_reply, String post_comment_reply_id) {
            this.id_post_comment_reply = id_post_comment_reply;
            this.post_comment_reply_id = post_comment_reply_id;
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
