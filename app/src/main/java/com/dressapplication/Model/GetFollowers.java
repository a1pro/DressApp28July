package com.dressapplication.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFollowers {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("data")
@Expose
private List<FollowersData> data = null;
@SerializedName("message")
@Expose
private Object message;

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

public List<FollowersData> getData() {
return data;
}

public void setData(List<FollowersData> data) {
this.data = data;
}

public Object getMessage() {
return message;
}

public void setMessage(Object message) {
this.message = message;
}

}