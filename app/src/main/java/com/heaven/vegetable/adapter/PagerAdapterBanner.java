package com.heaven.vegetable.adapter;

import android.os.Bundle;

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
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                FragmentBanner tab1 = new FragmentBanner();
                bundle.putInt("POSITION", 1);
                tab1.setArguments(bundle);
                return tab1;

            case 1:
                FragmentBanner tab2 = new FragmentBanner();
                bundle.putInt("POSITION", 2);
                tab2.setArguments(bundle);
                return tab2;

            case 2:
                FragmentBanner tab3 = new FragmentBanner();
                bundle.putInt("POSITION", 3);
                tab3.setArguments(bundle);
                return tab3;

            default:
                return null;
        }

//        switch (position) {
//            case 0:
////                FragmentBanner tab1 = new FragmentBanner();
//
//                return new FragmentBanner().newInstance(0);
//
//            case 1:
////                FragmentBanner tab6 = new FragmentBanner();
//
//                return new FragmentBanner().newInstance(1);
//
//            case 2:
////                FragmentBanner tab2 = new FragmentBanner();
//
//                return new FragmentBanner().newInstance(3);
//
//
//            default:
//                return null;
//        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}