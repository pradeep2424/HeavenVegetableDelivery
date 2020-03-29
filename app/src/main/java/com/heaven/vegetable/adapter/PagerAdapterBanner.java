package com.heaven.vegetable.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.heaven.vegetable.fragments.FragmentBanner;

public class PagerAdapterBanner extends FragmentStatePagerAdapter {
    public PagerAdapterBanner(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentBanner tab1 = new FragmentBanner();
                return tab1;

            case 1:
                FragmentBanner tab6 = new FragmentBanner();
                return tab6;


            case 2:
                FragmentBanner tab2 = new FragmentBanner();
                return tab2;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}