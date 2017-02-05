package in.voiceme.app.voiceme.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PostsModel {

    @SerializedName("id_posts") @Expose private String idPosts;
    @SerializedName("id_user_name") @Expose private String idUserName;
    @SerializedName("post_time") @Expose private String postTime;
    @SerializedName("text_status") @Expose private String textStatus;
    @SerializedName("audio_duration") @Expose private String audioDuration;
    @SerializedName("audio_file_link") @Expose private String audioFileLink;
    @SerializedName("user_nic_name") @Expose private String userNicName;
    @SerializedName("avatar_pics") @Expose private String avatarPics;
    @SerializedName("emotions") @Expose private String emotions;
    @SerializedName("category") @Expose private String category;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("same")
    @Expose
    private Integer same;
    @SerializedName("hug")
    @Expose
    private Integer hug;
    @SerializedName("listen")
    @Expose
    private Integer listen;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("user_like") @Expose private Boolean userLike;
    @SerializedName("user_Same") @Expose private Boolean userSame;
    @SerializedName("user_Huge") @Expose private Boolean userHuge;
    @SerializedName("user_Listen") @Expose private Boolean userListen;

    public Boolean getUserLike() {
        return userLike;
    }

    public void setUserLike(Boolean userLike) {
        this.userLike = userLike;
    }

    public Boolean getUserSame() {
        return userSame;
    }

    public void setUserSame(Boolean userSame) {
        this.userSame = userSame;
    }

    public Boolean getUserHuge() {
        return userHuge;
    }

    public void setUserHuge(Boolean userHuge) {
        this.userHuge = userHuge;
    }

    public Boolean getUserListen() {
        return userListen;
    }

    public void setUserListen(Boolean userListen) {
        this.userListen = userListen;
    }

    public String getIdPosts() {
        return idPosts;
    }

    public void setIdPosts(String idPosts) {
        this.idPosts = idPosts;
    }

    public String getIdUserName() {
        return idUserName;
    }

    public void setIdUserName(String idUserName) {
        this.idUserName = idUserName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(String textStatus) {
        this.textStatus = textStatus;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getAudioFileLink() {
        return audioFileLink;
    }

    public void setAudioFileLink(String audioFileLink) {
        this.audioFileLink = audioFileLink;
    }

    public String getUserNicName() {
        return userNicName;
    }

    public void setUserNicName(String userNicName) {
        this.userNicName = userNicName;
    }

    public String getAvatarPics() {
        return avatarPics;
    }

    public void setAvatarPics(String avatarPics) {
        this.avatarPics = avatarPics;
    }

    public String getEmotions() {
        return emotions;
    }

    public void setEmotions(String emotions) {
        this.emotions = emotions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getSame() {
        return same;
    }

    public void setSame(Integer same) {
        this.same = same;
    }

    public Integer getHug() {
        return hug;
    }

    public void setHug(Integer hug) {
        this.hug = hug;
    }

    public Integer getListen() {
        return listen;
    }

    public void setListen(Integer listen) {
        this.listen = listen;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }
}