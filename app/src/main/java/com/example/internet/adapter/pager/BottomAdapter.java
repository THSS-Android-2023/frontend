package com.example.internet.adapter.pager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.internet.fragment.BlankFragment;
import com.example.internet.fragment.ChatFragment;
import com.example.internet.fragment.HomepageFragment;
import com.example.internet.fragment.InfoFragment;
import com.example.internet.fragment.TimelineFragment;

public class BottomAdapter extends FragmentStateAdapter {

    Context ctx;
    public BottomAdapter(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
        ctx = fragmentActivity;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomepageFragment();
            case 1:
                return TimelineFragment.newInstance(TimelineFragment.STARRED_PAGE, ctx);
            case 2:
                return new ChatFragment();
            case 3:
                return new InfoFragment();
            default:
                return new BlankFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}