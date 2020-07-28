package com.dressapplication.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendData {

    @SerializedName("Hashtag")
    @Expose
    private String hashtag;
    @SerializedName("postCount")
    @Expose
    private Integer postCount;
    @SerializedName("allPost")
    @Expose
    private List<AllPost> allPost = null;

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public List<AllPost> getAllPost() {
        return allPost;
    }

    public void setAllPost(List<AllPost> allPost) {
        this.allPost = allPost;
    }

}