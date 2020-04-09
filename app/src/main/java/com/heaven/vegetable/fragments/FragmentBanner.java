package com.heaven.vegetable.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.heaven.vegetable.R;

public class FragmentBanner extends Fragment {
    private View rootView;
    private ImageView ivImage;

    int position;

//    public static FragmentBanner newInstance(int page) {
//        FragmentBanner fragmentFirst = new FragmentBanner();
//        Bundle args = new Bundle();
//        args.putInt("POSITION", page);
//        fragmentFirst.setArguments(args);
//        return fragmentFirst;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("POSITION", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_banner, container, false);

        init();
        return rootView;
    }

    private void init() {
        ivImage = rootView.findViewById(R.id.iv_image);

//        switch (position) {
//            case 0:
//                ivImage.setImageResource(R.mipmap.temp_bottom_cover_1);
//                break;
//
//            case 1:
//                ivImage.setImageResource(R.mipmap.temp_bottom_cover_2);
//                break;
//
//            case 2:
//                ivImage.setImageResource(R.mipmap.temp_bottom_cover_3);
//                break;
//        }
    }
}
