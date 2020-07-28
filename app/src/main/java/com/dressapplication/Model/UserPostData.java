package com.dressapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPostData {

@SerializedName("vid")
@Expose
private String vid;
@SerializedName("userId")
@Expose
private String userId;
@SerializedName("videoName")
@Expose
private Object videoName;
@SerializedName("videoThumImg")
@Expose
private String videoThumImg;
@SerializedName("videoDescribe")
@Expose
private String videoDescribe;
@SerializedName("commentStatus")
@Expose
private String commentStatus;
@SerializedName("postStatus")
@Expose
private String postStatus;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;

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

public Object getVideoName() {
return videoName;
}

public void setVideoName(Object videoName) {
this.videoName = videoName;
}

public String getVideoThumImg() {
return videoThumImg;
}

public void setVideoThumImg(String videoThumImg) {
this.videoThumImg = videoThumImg;
}

public String getVideoDescribe() {
return videoDescribe;
}

public void setVideoDescribe(String videoDescribe) {
this.videoDescribe = videoDescribe;
}

public String getCommentStatus() {
return commentStatus;
}

public void setCommentStatus(String commentStatus) {
this.commentStatus = commentStatus;
}

public String getPostStatus() {
return postStatus;
}

public void setPostStatus(String postStatus) {
this.postStatus = postStatus;
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