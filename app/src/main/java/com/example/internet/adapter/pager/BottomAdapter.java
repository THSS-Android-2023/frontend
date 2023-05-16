package com.example.internet.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.internet.activity.LoginActivity;
import com.example.internet.activity.RegisterActivity;
import com.example.internet.fragment.BlankFragment;
import com.example.internet.fragment.ChatFragment;
import com.example.internet.fragment.HomepageFragment;
import com.example.internet.fragment.InfoFragment;
import com.example.internet.fragment.home.TimelineFragment;

public class BottomAdapter extends FragmentStateAdapter {
    public BottomAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomepageFragment();
            case 1:
                return new TimelineFragment();
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