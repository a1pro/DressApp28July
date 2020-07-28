package com.dressapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SinglevideoCommentData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("videoOwnerId")
    @Expose
    private String videoOwnerId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userComments")
    @Expose
    private String userComments;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("CommnetedUserName")
    @Expose
    private String commnetedUserName;
    @SerializedName("CommnetedUseremail")
    @Expose
    private String commnetedUseremail;
    @SerializedName("CommnetedUserProfileIm")
    @Expose
    private String commnetedUserProfileIm;
    @SerializedName("islikedCommnet")
    @Expose
    private String islikedCommnet;
    @SerializedName("likCommnetCount")
    @Expose
    private int likCommnetCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoOwnerId() {
        return videoOwnerId;
    }

    public void setVideoOwnerId(String videoOwnerId) {
        this.videoOwnerId = videoOwnerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
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

    public String getCommnetedUserName() {
        return commnetedUserName;
    }

    public void setCommnetedUserName(String commnetedUserName) {
        this.commnetedUserName = commnetedUserName;
    }

    public String getCommnetedUseremail() {
        return commnetedUseremail;
    }

    public void setCommnetedUseremail(String commnetedUseremail) {
        this.commnetedUseremail = commnetedUseremail;
    }

    public String getCommnetedUserProfileIm() {
        return commnetedUserProfileIm;
    }

    public void setCommnetedUserProfileIm(String commnetedUserProfileIm) {
        this.commnetedUserProfileIm = commnetedUserProfileIm;
    }

    public String getIslikedCommnet() {
        return islikedCommnet;
    }

    public void setIslikedCommnet(String islikedCommnet) {
        this.islikedCommnet = islikedCommnet;
    }

    public int getLikCommnetCount() {
        return likCommnetCount;
    }

    public void setLikCommnetCount(int likCommnetCount) {
        this.likCommnetCount = likCommnetCount;
    }
}
