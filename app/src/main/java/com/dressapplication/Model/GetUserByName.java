package com.dressapplication.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserByName {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("postData")
@Expose
private List<UserPostData> postData = null;
@SerializedName("userData")
@Expose
private List<UserNameData> userData = null;

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

public List<UserPostData> getPostData() {
return postData;
}

public void setPostData(List<UserPostData> postData) {
this.postData = postData;
}

public List<UserNameData> getUserData() {
return userData;
}

public void setUserData(List<UserNameData> userData) {
this.userData = userData;
}

}