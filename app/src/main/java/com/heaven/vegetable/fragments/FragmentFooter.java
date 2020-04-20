package com.heaven.vegetable.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.heaven.vegetable.R;

public class FragmentFooter extends Fragment {
    private View rootView;
    private ImageView ivImage;

    String imageURL;

//    public static FragmentFooter newInstance(int page) {
//        FragmentFooter fragmentFirst = new FragmentFooter();
//        Bundle args = new Bundle();
//        args.putInt("POSITION", page);
//        fragmentFirst.setArguments(args);
//        return fragmentFirst;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            imageURL = getArguments().getString("ImageURL");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_banner, container, false);

        init();
        return rootView;
    }

    private void init() {
        ivImage = rootView.findViewById(R.id.iv_image);
        Glide.with(getActivity()).load(imageURL).into(ivImage);

    }
}
