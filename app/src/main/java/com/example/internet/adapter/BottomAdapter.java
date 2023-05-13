package com.example.internet.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.internet.activity.LoginActivity;
import com.example.internet.activity.RegisterActivity;
import com.example.internet.fragment.BlankFragment;

public class BottomAdapter extends FragmentStateAdapter {
    public BottomAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BlankFragment();
            default:
                return new BlankFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}