package com.example.internet.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.internet.R;
import com.example.internet.adapter.pager.HomeTabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomepageFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TabLayout tabs = view.findViewById(R.id.tabs);
        Log.d("123", ""+(this.getActivity()));
        HomeTabPagerAdapter tabPagerAdapter = new HomeTabPagerAdapter((FragmentActivity) this.getActivity());
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPagerAdapter);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabs.getTabAt(position).select();
            }
        });
        return view;
    }
}
