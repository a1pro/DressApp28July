package com.dressapplication.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dressapplication.ui.myProfile.LikeVideoFragment;
import com.dressapplication.ui.myProfile.ProfileVideoFragment;

public class ViewpagerAdapterProfile extends FragmentPagerAdapter {
    String Userid;
    public ViewpagerAdapterProfile(FragmentManager childFragmentManager,String Userid) {
        super(childFragmentManager);
        this.Userid=Userid;
    }





    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0)
        {
            fragment = new ProfileVideoFragment(Userid);
        }
        else if (position == 1)
        {
            fragment = new LikeVideoFragment(Userid);
        }

        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }

//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return false;
//    }

}
