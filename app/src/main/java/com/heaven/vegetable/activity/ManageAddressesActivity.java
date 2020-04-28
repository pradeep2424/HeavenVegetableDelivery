package com.heaven.vegetable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.RecycleAdapterAddresses;
import com.heaven.vegetable.interfaces.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.AddressDetails;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.sharedPreference.PrefManagerConfig;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageAddressesActivity extends AppCompatActivity implements OnRecyclerViewClickListener {
    RelativeLayout rlRootLayout;
    RecyclerView rvAddresses;

    View viewToolbarAddresses;
    ImageView ivBack;
    TextView tvToolbarTitle;

    private PrefManagerConfig prefManagerConfig;
    String mobileNumber;

    private RecycleAdapterAddresses adapterAddresses;
    private ArrayList<AddressDetails> listAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_addresses);

        initComponents();
        componentEvents();

        getUserAddressList();
//        setupRecyclerAddresses();
    }

    private void initComponents() {
        prefManagerConfig = new PrefManagerConfig(this);
        mobileNumber = prefManagerConfig.getMobileNo();

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        rvAddresses = findViewById(R.id.rv_manageAddresses);

        viewToolbarAddresses = findViewById(R.id.view_toolbarAddresses);
        ivBack = viewToolbarAddresses.findViewById(R.id.iv_back);
        tvToolbarTitle = viewToolbarAddresses.findViewById(R.id.tv_toolbarTitle);
        tvToolbarTitle.setText(getResources().getString(R.string.profile_manage_addresses));
    }

    private void componentEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setupRecyclerAddresses() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvAddresses.setLayoutManager(layoutManager);
        rvAddresses.setItemAnimator(new DefaultItemAnimator());

        adapterAddresses = new RecycleAdapterAddresses(this, listAddress);
        rvAddresses.setAdapter(adapterAddresses);
        adapterAddresses.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        AddressDetails addressDetails = listAddress.get(position);
        deleteUserAddress(position, addressDetails);
    }

    //    private void getProfileData() {
//        String[] title = {getString(R.string.profile_payment_methods),
//                getString(R.string.profile_reward_credits),
//                getString(R.string.profile_settings),
//                getString(R.string.profile_invite_friends)};
//
//        Integer[] icon = {R.mipmap.profile_payment_method, R.mipmap.profile_reward_credits,
//                R.mipmap.profile_settings, R.mipmap.profile_invite_friends};
//
//        listAddress = new ArrayList<>();
//        for (int i = 0; i < icon.length; i++) {
//            ProfileObject profileObject = new ProfileObject(title[i], icon[i]);
//            list.add(profileObject);
//        }
//    }

    private void getUserAddressList() {
        if (InternetConnection.checkConnection(this)) {
            String mobileNo = Application.userDetails.getMobile();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getUserAddress(mobileNo);
//            Call<ResponseBody> call = apiService.getUserAddress(mobileNumber);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listAddress = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                int addressID = jsonObj.optInt("AddressId");
                                String address = jsonObj.getString("Address");
                                String addressType = jsonObj.optString("AddressType");
                                String mobileNo = jsonObj.optString("MobNo");
                                int zipCode = jsonObj.optInt("ZipCode");

                                AddressDetails addressDetails = new AddressDetails();
                                addressDetails.setAddressID(addressID);
                                addressDetails.setAddress(address);
                                addressDetails.setAddressType(addressType);
                                addressDetails.setZipCode(zipCode);

                                listAddress.add(addressDetails);
                            }

                            setupRecyclerAddresses();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getUserAddressList();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void deleteUserAddress(final int position, final AddressDetails addressDetails) {
        if (InternetConnection.checkConnection(this)) {
            int addressID = addressDetails.getAddressID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.deleteUserAddress(addressID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            listAddress.remove(position);
                            adapterAddresses.notifyDataSetChanged();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteUserAddress(position, addressDetails);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

//
//    @Override
//    public void onEditAddress(View view, int position) {
//
//    }
//
//    @Override
//    public void onDeleteAddress(View view, int position) {
//        AddressDetails addressDetails = listAddress.get(position);
//        deleteUserAddress(position, addressDetails);
//    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
