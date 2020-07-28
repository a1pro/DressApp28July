package com.dressapplication.Interface;

import com.dressapplication.Model.ChatMessage;
import com.dressapplication.Model.GetAllData;
import com.dressapplication.Model.GetAlldataWithComment;
import com.dressapplication.Model.GetFollowers;
import com.dressapplication.Model.GetFollowing;
import com.dressapplication.Model.GetFriendList;
import com.dressapplication.Model.GetHashTag;
import com.dressapplication.Model.GetProfileModel;
import com.dressapplication.Model.GetSingleVideoComment;
import com.dressapplication.Model.GetUserByName;
import com.dressapplication.Model.LikedVideos;
import com.dressapplication.Model.LoginData;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.Model.SetPasswordModel;
import com.dressapplication.Model.TrendingHashTag;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {


    @FormUrlEncoded
    @POST("api/login")
    Call<LoginData> LoginApi(@Field("email") String email,
                             @Field("password") String password,
                             @Field("deviceType") String deviceType,
                             @Field("deviceId") String deviceId);


    @GET("api/getAllVideosWithComments")
    Call<GetAlldataWithComment> getAlldatawithComment();

    @FormUrlEncoded
    @POST("api/getSingleVedioComments")
    Call<GetSingleVideoComment> GetSingleVideocomment(@Field("userId") String userId,
                                                      @Field("videoId") String videoId,
                                                      @Field("loginUserId") String loginUserId);



    @FormUrlEncoded
    @POST("api/getAllData")
    Call<GetAllData> getAllData(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/register")
    Call<ResponseData> RegisterApi(@Field("username") String username,
                                   @Field("email") String email,
                                   @Field("dob") String dob,
                                   @Field("password") String password,
                                   @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("api/addComments")
    Call<ResponseData> AddcommentApi(@Field("videoId") String videoId,
                                     @Field("videoOwnerId") String videoOwnerId,
                                     @Field("userId") String userId,
                                     @Field("userComments") String userComments);

    @FormUrlEncoded
    @POST("api/addlikeOnVideo")
    Call<ResponseData> AddLikeOnVideo(@Field("videoId") String videoId,
                                      @Field("videoOwnerId") String videoOwnerId,
                                      @Field("userId") String userId,
                                      @Field("likeStatus") String likeStatus);


    @Multipart
    @POST("api/uploadOurStory")
    Call<ResponseData> uploadStory(
            @Part("userId") RequestBody userId,
            @Part("commentStatus") RequestBody commentStatus,
            @Part("postStatus") RequestBody postStatus,
            @Part("videoDescribe") RequestBody videoDescribe,
            @Part MultipartBody.Part addVideo,
            @Part MultipartBody.Part videoThumImg);

    @FormUrlEncoded
    @POST("api/getUserPorfile")
    Call<GetProfileModel> GetProfile(@Field("userId") String userId,
                                     @Field("loginUserId") String loginUserId);


    @Multipart
    @POST("api/profileEdit")
    Call<ResponseData> EditProfile(
            @Part("userId") RequestBody userId,
            @Part("no_bio_yet") RequestBody no_bio_yet,
            @Part("intagram") RequestBody intagram,
            @Part("youtube") RequestBody youtube,
            @Part MultipartBody.Part profile_image);


    @FormUrlEncoded
    @POST("api/followUnfollow")
    Call<ResponseData> FollowUnfollow(@Field("followerBy") String followerBy,
                                      @Field("followerTo") String followerTo);

    @FormUrlEncoded
    @POST("api/getLikedVideoByMe")
    Call<LikedVideos> getVideolikeByMe(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("api/getOurFollowers")
    Call<GetFollowers> getFollowers(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("api/getOurFollowings")
    Call<GetFollowing> GetFollowing(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("api/likeOnComment")
    Call<ResponseData> AddLikeOnComment(@Field("userId") String userId,
                                        @Field("commentId") String commentId,
                                        @Field("commentOwnerId") String commentOwnerId);


    @FormUrlEncoded
    @POST("api/getHashTagVideoData")
    Call<GetHashTag> getHashTagVideoData(@Field("hashtagwords") String hashtagwords);

    @FormUrlEncoded
    @POST("api/getUserPorfileByUserName")
    Call<GetUserByName> getUsername(@Field("username") String username);


    @GET("api/getVideoDataWithHashTag")
    Call<TrendingHashTag> getTrendHash();

    @FormUrlEncoded
    @POST("api/deleteAccount")
    Call<ResponseData> deleteAccount(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/userLogout")
    Call<ResponseData> logout(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/socialLogin")
    Call<LoginData> socialLogin(@Field("email") String email,
                                @Field("username") String username,
                                @Field("social_login_id") String social_login_id,
                                @Field("social_login_type") String social_login_type,
                                @Field("deviceType") String deviceType,
                                @Field("deviceId") String deviceId,
                                @Field("latitude") String latitude,
                                @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST("api/getOurFriendsListNew")
    Call<GetFriendList> GetFriendList(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/getChatMessage")
    Call<ChatMessage> GetChatMessage(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/addChatThread")
    Call<ResponseData> AddChat(@Field("senderId") String senderId,
                               @Field("receiverId") String receiverId);


    @FormUrlEncoded
    @POST("api/updateLastMessage")
    Call<ResponseData> LastMessage(@Field("senderId") String senderId,
                                   @Field("receiverId") String receiverId,
                                   @Field("message") String message);


    @FormUrlEncoded
    @POST("api/manageAccount")
    Call<ResponseData> manageAccount(@Field("phone") String phone,
                                     @Field("address") String address,
                                     @Field("longitude") String longitude,
                                     @Field("latitude") String latitude,
                                     @Field("country") String country,
                                     @Field("city") String city,
                                     @Field("zipcode") String zipcode,
                                     @Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/changePassword")
    Call<ResponseData> changePassword(@Field("password") String password,
                                      @Field("confirm_password") String confirm_password,
                                      @Field("oldPassword") String oldPassword,
                                      @Field("userId") String userId);


    @FormUrlEncoded
    @POST("api/reportUser")
    Call<ResponseData> reportUser(@Field("videoId") String videoId,
                                  @Field("videoOwnerid") String videoOwnerid,
                                  @Field("title") String title,
                                  @Field("content") String content);

    @FormUrlEncoded
    @POST("api/signUpWithPhone")
    Call<SetPasswordModel> setPhone(@Field("phone") String phone,
                                    @Field("password") String password,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude);

}
