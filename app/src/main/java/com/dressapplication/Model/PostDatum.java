package com.dressapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDatum {

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
    @SerializedName("videoDescribe")
    @Expose
    private String videoDescribe;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("loginUserIsLiked")
    @Expose
    private String loginUserIsLiked;

    @SerializedName("profile_image")
    @Expose
    private String profile_image;
    @SerializedName("username")
    @Expose
    private String username;

    public String getVideoDescribe() {
        return videoDescribe;
    }

    public void setVideoDescribe(String videoDescribe) {
        this.videoDescribe = videoDescribe;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLoginUserIsLiked() {
        return loginUserIsLiked;
    }

    public void setLoginUserIsLiked(String loginUserIsLiked) {
        this.loginUserIsLiked = loginUserIsLiked;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}