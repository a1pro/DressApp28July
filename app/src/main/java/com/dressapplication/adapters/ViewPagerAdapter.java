package com.dressapplication.adapters;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.dressapplication.Model.AllData;
import com.dressapplication.ui.home.ChildFragment;
import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int size;
    List<AllData> datalist;
    static SparseArray<Fragment> registeredFragments = new SparseArray<>();


    public ViewPagerAdapter(FragmentManager fm, int size, List<AllData> datalist) {
        super(fm);
        this.size = size;
        this.datalist = datalist;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public static Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {


        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();
        try {
            bundle.putString("videourl", datalist.get(position).getVideoName());
            bundle.putString("username", datalist.get(position).getUsername());
//            bundle.putString("VideoCommentCount",String.valueOf(datalist.get(position).getCommentCount()));
            bundle.putString("Videothumbnail",datalist.get(position).getVideoThumImg());
            bundle.putString("userid",datalist.get(position).getUserId());
            bundle.putString("Videoid",datalist.get(position).getVid());
            bundle.putString("isliked",datalist.get(position).getIsLiked());
            bundle.putString("likedcount",String.valueOf(datalist.get(position).getLikedCount()));
            bundle.putString("videoDescribe",datalist.get(position).getVideoDescribe());
            bundle.putString("userimage",datalist.get(position).getProfileImage());
            bundle.putString("lati",datalist.get(position).getLatitude());
            bundle.putString("long",datalist.get(position).getLongitude());

//            Log.e("ViewvideoDescribe",""+datalist.get(position).getUserId());

        }catch (Exception e){
            e.printStackTrace();
        }

        child.setArguments(bundle);
        return child;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

}
