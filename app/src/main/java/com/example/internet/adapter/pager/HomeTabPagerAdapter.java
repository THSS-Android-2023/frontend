package com.example.internet.adapter.pager;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.internet.fragment.home.TimelineFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class HomeTabPagerAdapter extends FragmentStateAdapter {

    Context ctx;


    public HomeTabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        ctx = fragmentActivity;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return TimelineFragment.newInstance(TimelineFragment.NEWEST_PAGE, ctx);
            case 1:
                return TimelineFragment.newInstance(TimelineFragment.HOTTEST_PAGE, ctx);
            case 2:
                return TimelineFragment.newInstance(TimelineFragment.FOLLOWINGS_PAGE, ctx);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}