package com.dressapplication.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginModel {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("data")
@Expose
private List<Datum> data = null;

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private Object password;
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
        private Object otp;
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

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
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

        public Object getOtp() {
            return otp;
        }

        public void setOtp(Object otp) {
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

    }
}