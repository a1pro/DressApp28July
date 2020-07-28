package com.dressapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowersData{

@SerializedName("id")
@Expose
private String id;
@SerializedName("followerBy")
@Expose
private String followerBy;
@SerializedName("followerTo")
@Expose
private String followerTo;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
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
private Object profileImage;
@SerializedName("profile_video")
@Expose
private Object profileVideo;
@SerializedName("address")
@Expose
private Object address;
@SerializedName("zipcode")
@Expose
private Object zipcode;
@SerializedName("city")
@Expose
private Object city;
@SerializedName("country")
@Expose
private Object country;
@SerializedName("no_bio_yet")
@Expose
private Object noBioYet;
@SerializedName("intagram")
@Expose
private Object intagram;
@SerializedName("youtube")
@Expose
private Object youtube;
@SerializedName("status")
@Expose
private String status;
@SerializedName("role")
@Expose
private String role;
@SerializedName("phone")
@Expose
private Object phone;
@SerializedName("social_login_type")
@Expose
private String socialLoginType;
@SerializedName("social_login_id")
@Expose
private Object socialLoginId;
@SerializedName("deviceId")
@Expose
private String deviceId;
@SerializedName("deviceType")
@Expose
private String deviceType;
@SerializedName("latitude")
@Expose
private Object latitude;
@SerializedName("longitude")
@Expose
private Object longitude;
@SerializedName("otp")
@Expose
private Object otp;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getFollowerBy() {
return followerBy;
}

public void setFollowerBy(String followerBy) {
this.followerBy = followerBy;
}

public String getFollowerTo() {
return followerTo;
}

public void setFollowerTo(String followerTo) {
this.followerTo = followerTo;
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

public Object getProfileImage() {
return profileImage;
}

public void setProfileImage(Object profileImage) {
this.profileImage = profileImage;
}

public Object getProfileVideo() {
return profileVideo;
}

public void setProfileVideo(Object profileVideo) {
this.profileVideo = profileVideo;
}

public Object getAddress() {
return address;
}

public void setAddress(Object address) {
this.address = address;
}

public Object getZipcode() {
return zipcode;
}

public void setZipcode(Object zipcode) {
this.zipcode = zipcode;
}

public Object getCity() {
return city;
}

public void setCity(Object city) {
this.city = city;
}

public Object getCountry() {
return country;
}

public void setCountry(Object country) {
this.country = country;
}

public Object getNoBioYet() {
return noBioYet;
}

public void setNoBioYet(Object noBioYet) {
this.noBioYet = noBioYet;
}

public Object getIntagram() {
return intagram;
}

public void setIntagram(Object intagram) {
this.intagram = intagram;
}

public Object getYoutube() {
return youtube;
}

public void setYoutube(Object youtube) {
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

public String getSocialLoginType() {
return socialLoginType;
}

public void setSocialLoginType(String socialLoginType) {
this.socialLoginType = socialLoginType;
}

public Object getSocialLoginId() {
return socialLoginId;
}

public void setSocialLoginId(Object socialLoginId) {
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

public Object getLatitude() {
return latitude;
}

public void setLatitude(Object latitude) {
this.latitude = latitude;
}

public Object getLongitude() {
return longitude;
}

public void setLongitude(Object longitude) {
this.longitude = longitude;
}

public Object getOtp() {
return otp;
}

public void setOtp(Object otp) {
this.otp = otp;
}

}