package com.dressapplication.utils;

import android.content.SharedPreferences;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by AQEEL on 2/15/2019.
 */

public class Variables {


    public static String device="android";

    public static int screen_width;
    public static int screen_height;

    public static String SelectedAudio_MP3 ="SelectedAudio.mp3";
    public static String SelectedAudio_AAC ="SelectedAudio.aac";

    public static String root= Environment.getExternalStorageDirectory().toString();


    public static int max_recording_duration=18000;
    public static int recording_duration=18000;

    public static String outputfile=root + "/output.mp4";
    public static String outputfile2=root + "/output2.mp4";
    public static String output_filter_file=root + "/output-filtered.mp4";

    public static String gallery_trimed_video=root + "/gallery_trimed_video.mp4";
    public static String gallery_resize_video=root + "/gallery_resize_video.mp4";

    public static String app_folder=root+"/DressApp/";

    public static SharedPreferences sharedPreferences;
    public static String pref_name="pref_name";
    public static String u_id="u_id";
    public static String u_name="u_name";
    public static String u_pic="u_pic";
    public static String f_name="f_name";
    public static String l_name="l_name";
    public static String gender="u_gender";
    public static String islogin="is_login";
    public static String device_token="device_token";



    public static String tag="DressApp";

    public static String Selected_sound_id="null";





    public static SimpleDateFormat df =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);

    public static SimpleDateFormat df2 =
            new SimpleDateFormat("dd-MM-yyyy HH:mmZZ", Locale.ENGLISH);




    public static String user_id;
    public static String user_name;
    public static String user_pic;


    public final static int permission_camera_code=786;
    public final static int permission_write_data=788;
    public final static int permission_Read_data=789;
    public final static int permission_Recording_audio=790;





    
    public static String domain="https://a1professionals.net/dressApp/";
    public static String base_url="https://a1professionals.net/dressApp/";


    public static String showAllVideos =domain+"api/getAllData";






}
