package com.heaven.vegetable.bottomMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.BuildConfig;
import com.heaven.vegetable.R;
import com.heaven.vegetable.activity.EditProfileActivity;
import com.heaven.vegetable.activity.ManageAddressesActivity;
import com.heaven.vegetable.adapter.RecycleAdapterProfile;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.main.GetStartedMobileNumberActivity;
import com.heaven.vegetable.main.MainActivity;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.model.ProfileObject;
import com.heaven.vegetable.model.CateogryObject;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.sharedPreference.PrefManagerConfig;
import com.heaven.vegetable.utils.Application;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements OnRecyclerViewClickListener {
    View rootView;
    MainActivity mainActivity;

    private View viewToolbar;
    TextView tvName;
    TextView tvEmail;
    TextView tvMobile;
    TextView tvEditProfile;

    private RecyclerView rvProfile;
    private LinearLayout llManageAddresses;

    private RecycleAdapterProfile adapterProfile;
    private ArrayList<ProfileObject> listProfile;

    private PrefManagerConfig prefManagerConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initComponents();
        componentEvents();
//        setUserInformation();
        setupRecyclerViewProfile();

        return rootView;
    }

    private void initComponents() {
        prefManagerConfig = new PrefManagerConfig(getActivity());

        rvProfile = rootView.findViewById(R.id.rv_profile);
        llManageAddresses = rootView.findViewById(R.id.ll_manageAddresses);
//        viewToolbar = rootView.findViewById(R.id.view_toolbar);

        tvEditProfile = rootView.findViewById(R.id.tv_editProfile);

//        tvName = rootView.findViewById(R.id.tv_name);
//        tvEmail = rootView.findViewById(R.id.tv_email);
//        tvMobile = rootView.findViewById(R.id.tv_mobile);
    }

    private void componentEvents() {
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        llManageAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageAddressesActivity.class);
                startActivity(intent);
            }
        });

//        viewToolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prefManagerConfig.clearPrefOnLogout();
//                Application.userDetails = new UserDetails();
//                Application.restaurantObject = new CateogryObject();
//                Application.productObject = new ProductObject();
//                Application.listCartItems.clear();
//
//                Intent intent = new Intent(getActivity(), GetStartedMobileNumberActivity.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
    }

//    private void setupToolbar() {
////        toolbar.setTitle("Your Fragment Title");
//        toolbar.setTitle("");
//        mainActivity.setSupportActionBar(toolbar);
//    }

    private void setUserInformation() {
        if (Application.userDetails != null && Application.userDetails.getFullName() != null) {
            tvName.setText(Application.userDetails.getFullName());
        }

        if (Application.userDetails != null && Application.userDetails.getEmail() != null) {
            tvEmail.setText(Application.userDetails.getEmail());

        } else {
            tvEmail.setVisibility(View.GONE);
        }

        if (Application.userDetails != null && Application.userDetails.getMobile() != null) {
            tvMobile.setText(Application.userDetails.getMobile());
        }
    }

    private void setupRecyclerViewProfile() {
        getProfileData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvProfile.setLayoutManager(layoutManager);
        rvProfile.setItemAnimator(new DefaultItemAnimator());


        adapterProfile = new RecycleAdapterProfile(getActivity(), listProfile);
        rvProfile.setAdapter(adapterProfile);
        adapterProfile.setClickListener(this);
    }

    private void getProfileData() {
//        String[] title = {getString(R.string.profile_payment_methods),
//                getString(R.string.profile_reward_credits),
//                getString(R.string.profile_settings),
//                getString(R.string.profile_invite_friends)};
//
//        Integer[] icon = {R.mipmap.profile_payment_method, R.mipmap.profile_reward_credits,
//                R.mipmap.profile_settings, R.mipmap.profile_invite_friends};

        String[] title = {getString(R.string.profile_payment_methods),
                getString(R.string.profile_reward_credits),
                getString(R.string.profile_invite_friends)};

        Integer[] icon = {R.mipmap.profile_payment_method, R.mipmap.profile_reward_credits,
                 R.mipmap.profile_invite_friends};


        listProfile = new ArrayList<>();
        for (int i = 0; i < icon.length; i++) {
            ProfileObject profileObject = new ProfileObject(title[i], icon[i]);
            listProfile.add(profileObject);
        }
    }

    private void shareApp() {
   /*
            https://play.google.com/store/apps/details?id=com.example.application
            &referrer=utm_source%3Dgoogle
            %26utm_medium%3Dcpc
            %26utm_term%3Drunning%252Bshoes
            %26utm_content%3Dlogolink
            %26utm_campaign%3Dspring_sale
    */

//        String shareMessage = "\nI recommend you this application\n\n";
//        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
//                +"&referrer=utm_source%3Dgoogle"
//                + "\n\n";

//        https://play.google.com/store/apps/details?id=com.miracle.dronam&referrer=utm_source%3D9665175415%26utm_medium%3Dronam


        String shareMessage = "\nI recommend you this application\n\n"
                + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                + "&referrer=utm_source=" + Application.userDetails.getMobile()
//                + "&referrer=utm_source%3D" + Application.userDetails.getMobile()
//                + "&referrer=" + Application.userDetails.getMobile()
//                + "%26utm_medium%3Dronam"
                + "\n\n";

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch (Exception e) {
            //e.toString();
        }


//        String shareMessage = "https://dronam.page.link/?" +
//                "link=https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +
//                "&apn=" + BuildConfig.APPLICATION_ID +
//                "&st=" + "I recommend you this application" +                     // &st – Title of refer link
//                "&sd=" + "Short description of Dronam" +                                     // &sd – Short description about refer link
//                "&si=" + "https://www.dronam.com/frontend-images/logo.png";           // &si – Image URL for refer link
//
//
//        // shorten the link
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                //.setLongLink(dynamicLink.getUri())
//                .setLongLink(Uri.parse(shareMessage))  // manually
//                .buildShortDynamicLink()
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                            Log.e("main ", "short link "+ shortLink.toString());
//                            // share app dialog
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
//                            intent.setType("text/plain");
//                            startActivity(intent);
//                        } else {
//                            // Error
//                            // ...
//                            Log.e("main", " error "+task.getException() );
//                        }
//                    }
//                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onClick(View view, int position) {
        switch (position) {
            case 0:
//                Intent intent0 = new Intent(getActivity(), PaymentMethodsActivity.class);
//                startActivity(intent0);
                break;

            case 1:
//                Intent intent1 = new Intent(getActivity(), RewardCreditsActivity.class);
//                startActivity(intent1);
                break;

            case 3:
                shareApp();
                break;
        }
    }
}
