package in.voiceme.app.voiceme.NotificationsPage;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass public class NotificationPost extends RealmObject {

  private Long sentTime;
  private String senderName;
  private String senderId;
  private String activity;
  private String postId;
  private String senderAvatar;
  private String receiverId;

  Long getSentTime() {
    return sentTime;
  }

  public void setSentTime(Long sentTime) {
    this.sentTime = sentTime;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
  }

  String getSenderAvatar() {
    return senderAvatar;
  }

  public void setSenderAvatar(String senderAvatar) {
    this.senderAvatar = senderAvatar;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }

  String initWithNotification() {

    StringBuilder text = new StringBuilder();

    switch (getActivity()) {
      case "1": //LIKE
        text.append(getSenderName()).append(" liked your post").append(" ").append(postId);
        break;
      case "2": //HUG
        text.append(getSenderName()).append(" hugged your post").append(" ").append(postId);
        break;
      case "3": //SAME
        text.append(getSenderName()).append(" same your post").append(" ").append(postId);
        break;
      case "follow":
        text.append(getSenderName()).append(" followed your post").append(" ").append(postId);
        break;
      case "unfollow":
        text.append(getSenderName()).append(" un-followed your post").append(" ").append(postId);
    }

    return text.toString();
  }
}
