package com.heaven.vegetable.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.heaven.vegetable.fragments.CompletedOrdersFragment;
import com.heaven.vegetable.fragments.UpcomingOrdersFragment;

public class PagerAdapterOrders extends FragmentStatePagerAdapter {
    int totalTabs;

    public PagerAdapterOrders(FragmentManager fm, int tabCount) {
        super(fm);
        this.totalTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UpcomingOrdersFragment();

            case 1:
                return new CompletedOrdersFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
