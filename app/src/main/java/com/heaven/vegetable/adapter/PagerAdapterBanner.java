package com.heaven.vegetable.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapterBanner extends FragmentStatePagerAdapter {
    public static int pos = 0;

    private ArrayList<Fragment> myFragments;
    private ArrayList<String> images;
    private Context context;

    public PagerAdapterBanner(Context c, FragmentManager fragmentManager, ArrayList<Fragment> myFrags, ArrayList<String> images) {
        super(fragmentManager);
        myFragments = myFrags;
        this.images = images;
        this.context = c;
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        setPos(position);
        return images.get(position);
    }

    public static int getPos() {
        return pos;
    }

    public void add(Class<Fragment> c, String title, Bundle b) {
        myFragments.add(Fragment.instantiate(context, c.getName(), b));
        images.add(title);
    }

    public static void setPos(int pos) {
        PagerAdapterBanner.pos = pos;
    }
}


//package com.heaven.vegetable.adapter;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//import com.heaven.vegetable.fragments.FragmentFooter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PagerAdapterBanner extends FragmentStatePagerAdapter {
//    private List<Fragment> myFragments;
//    private ArrayList<String> images;
//    private Context context;
//
//    public PagerAdapterBanner(FragmentManager fm) {
//        super(fm);
//
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        Bundle bundle = new Bundle();
//
//        switch (position) {
//            case 0:
//                FragmentFooter tab1 = new FragmentFooter();
//                bundle.putInt("POSITION", 1);
//                tab1.setArguments(bundle);
//                return tab1;
//
//            case 1:
//                FragmentFooter tab2 = new FragmentFooter();
//                bundle.putInt("POSITION", 2);
//                tab2.setArguments(bundle);
//                return tab2;
//
//            case 2:
//                FragmentFooter tab3 = new FragmentFooter();
//                bundle.putInt("POSITION", 3);
//                tab3.setArguments(bundle);
//                return tab3;
//
//            default:
//                return null;
//        }
//
////        switch (position) {
////            case 0:
//////                FragmentFooter tab1 = new FragmentFooter();
////
////                return new FragmentFooter().newInstance(0);
////
////            case 1:
//////                FragmentFooter tab6 = new FragmentFooter();
////
////                return new FragmentFooter().newInstance(1);
////
////            case 2:
//////                FragmentFooter tab2 = new FragmentFooter();
////
////                return new FragmentFooter().newInstance(3);
////
////
////            default:
////                return null;
////        }
//
//    }
//
//    @Override
//    public int getCount() {
//        return 3;
//    }
//}