package com.example.internet.adapter.pager;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.internet.fragment.BlankFragment;
import com.example.internet.fragment.home.TimelineFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class HomeTabPagerAdapter extends FragmentStateAdapter {


    public HomeTabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
//        mContext = context;
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            default:
                return new TimelineFragment();
//            default:
//                return new BlankFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}