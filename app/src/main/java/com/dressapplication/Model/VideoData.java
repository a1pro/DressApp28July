package com.dressapplication.Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoData {

    @SerializedName("vid")
    @Expose
    private String vid;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("videoName")
    @Expose
    private String videoName;
    @SerializedName("videoThumImg")
    @Expose
    private String videoThumImg;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("videoCommentsCount")
    @Expose
    private Integer videoCommentsCount;
    @SerializedName("videoDescribe")
    @Expose
    private String videoDescribe;
    @SerializedName("postStatus")
    @Expose
    private String postStatus;
    @SerializedName("videoComments")
    @Expose
    private List<VideoComment> videoComments = null;

    public String getVideoDescribe() {
        return videoDescribe;
    }

    public void setVideoDescribe(String videoDescribe) {
        this.videoDescribe = videoDescribe;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoThumImg() {
        return videoThumImg;
    }

    public void setVideoThumImg(String videoThumImg) {
        this.videoThumImg = videoThumImg;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getVideoCommentsCount() {
        return videoCommentsCount;
    }

    public void setVideoCommentsCount(Integer videoCommentsCount) {
        this.videoCommentsCount = videoCommentsCount;
    }

    public List<VideoComment> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(List<VideoComment> videoComments) {
        this.videoComments = videoComments;
    }

}