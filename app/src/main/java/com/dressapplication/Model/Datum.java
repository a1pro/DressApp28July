package com.dressapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

@SerializedName("id")
@Expose
private String id;
@SerializedName("username")
@Expose
private String username;
@SerializedName("dob")
@Expose
private String dob;
@SerializedName("email")
@Expose
private String email;
@SerializedName("password")
@Expose
private String password;
@SerializedName("profile_image")
@Expose
private String profileImage;
@SerializedName("profile_video")
@Expose
private String profileVideo;
@SerializedName("no_bio_yet")
@Expose
private String noBioYet;
@SerializedName("intagram")
@Expose
private String intagram;
@SerializedName("youtube")
@Expose
private String youtube;
@SerializedName("status")
@Expose
private String status;
@SerializedName("role")
@Expose
private String role;
@SerializedName("phone")
@Expose
private Object phone;
@SerializedName("deviceId")
@Expose
private String deviceId;
@SerializedName("deviceType")
@Expose
private String deviceType;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public String getDob() {
return dob;
}

public void setDob(String dob) {
this.dob = dob;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getProfileImage() {
return profileImage;
}

public void setProfileImage(String profileImage) {
this.profileImage = profileImage;
}

public String getProfileVideo() {
return profileVideo;
}

public void setProfileVideo(String profileVideo) {
this.profileVideo = profileVideo;
}

public String getNoBioYet() {
return noBioYet;
}

public void setNoBioYet(String noBioYet) {
this.noBioYet = noBioYet;
}

public String getIntagram() {
return intagram;
}

public void setIntagram(String intagram) {
this.intagram = intagram;
}

public String getYoutube() {
return youtube;
}

public void setYoutube(String youtube) {
this.youtube = youtube;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getRole() {
return role;
}

public void setRole(String role) {
this.role = role;
}

public Object getPhone() {
return phone;
}

public void setPhone(Object phone) {
this.phone = phone;
}

public String getDeviceId() {
return deviceId;
}

public void setDeviceId(String deviceId) {
this.deviceId = deviceId;
}

public String getDeviceType() {
return deviceType;
}

public void setDeviceType(String deviceType) {
this.deviceType = deviceType;
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