package com.dressapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDatum {

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
private Object profileVideo;
@SerializedName("address")
@Expose
private String address;
@SerializedName("zipcode")
@Expose
private String zipcode;
@SerializedName("city")
@Expose
private String city;
@SerializedName("country")
@Expose
private String country;
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
private String phone;
@SerializedName("social_login_type")
@Expose
private String socialLoginType;
@SerializedName("social_login_id")
@Expose
private String socialLoginId;
@SerializedName("deviceId")
@Expose
private String deviceId;
@SerializedName("deviceType")
@Expose
private String deviceType;
@SerializedName("latitude")
@Expose
private String latitude;
@SerializedName("longitude")
@Expose
private String longitude;
@SerializedName("otp")
@Expose
private String otp;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("followersCount")
@Expose
private Integer followersCount;
@SerializedName("followingCount")
@Expose
private Integer followingCount;
@SerializedName("isfollowing")
@Expose
private String isfollowing;
@SerializedName("postLikeCount")
@Expose
private Integer postLikeCount;

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

public Object getProfileVideo() {
return profileVideo;
}

public void setProfileVideo(Object profileVideo) {
this.profileVideo = profileVideo;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getZipcode() {
return zipcode;
}

public void setZipcode(String zipcode) {
this.zipcode = zipcode;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getCountry() {
return country;
}

public void setCountry(String country) {
this.country = country;
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

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public String getSocialLoginType() {
return socialLoginType;
}

public void setSocialLoginType(String socialLoginType) {
this.socialLoginType = socialLoginType;
}

public String getSocialLoginId() {
return socialLoginId;
}

public void setSocialLoginId(String socialLoginId) {
this.socialLoginId = socialLoginId;
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

public String getLatitude() {
return latitude;
}

public void setLatitude(String latitude) {
this.latitude = latitude;
}

public String getLongitude() {
return longitude;
}

public void setLongitude(String longitude) {
this.longitude = longitude;
}

public String getOtp() {
return otp;
}

public void setOtp(String otp) {
this.otp = otp;
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

public Integer getFollowersCount() {
return followersCount;
}

public void setFollowersCount(Integer followersCount) {
this.followersCount = followersCount;
}

public Integer getFollowingCount() {
return followingCount;
}

public void setFollowingCount(Integer followingCount) {
this.followingCount = followingCount;
}

public String getIsfollowing() {
return isfollowing;
}

public void setIsfollowing(String isfollowing) {
this.isfollowing = isfollowing;
}

public Integer getPostLikeCount() {
return postLikeCount;
}

public void setPostLikeCount(Integer postLikeCount) {
this.postLikeCount = postLikeCount;
}

}