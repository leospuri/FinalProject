package in.voiceme.app.voiceme.NotificationsPage;

public class NotificationPost {

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
}
