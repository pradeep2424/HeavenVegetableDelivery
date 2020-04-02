package com.heaven.vegetable.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.RecycleAdapterAddresses;
import com.heaven.vegetable.adapter.RecycleAdapterProductList;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.AddressDetails;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.sharedPreference.PrefManagerConfig;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.heaven.vegetable.utils.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity implements OnRecyclerViewClickListener {
    private RelativeLayout rlRootLayout;

//    View viewToolbarAddresses;
//    ImageView ivBack;
    TextView tvToolbarTitle;

    private Toolbar toolbar;

    private PrefManagerConfig prefManagerConfig;
    String mobileNumber;

    private RecyclerView rvProductList;
    private RecycleAdapterProductList adapterProductList;
    private ArrayList<ProductObject> listProducts;

    private final int REQUEST_CODE_PRODUCT_DETAILS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        initComponents();
        componentEvents();

//        getUserAddressList();
        setupRecyclerAddresses();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolbarTitle =  findViewById(R.id.tv_toolbarTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText("Vegetables");

        prefManagerConfig = new PrefManagerConfig(this);
        mobileNumber = prefManagerConfig.getMobileNo();

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        rvProductList = findViewById(R.id.rv_productList);

//        viewToolbarAddresses = findViewById(R.id.view_toolbarProductList);
//        ivBack = viewToolbarAddresses.findViewById(R.id.iv_back);
//        tvToolbarTitle = viewToolbarAddresses.findViewById(R.id.tv_toolbarTitle);
//        tvToolbarTitle.setText(getResources().getString(R.string.profile_manage_addresses));
    }

    private void componentEvents() {
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    private void setupRecyclerAddresses() {
        getProductDummyData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setItemAnimator(new DefaultItemAnimator());

        adapterProductList = new RecycleAdapterProductList(this, listProducts);
        rvProductList.setAdapter(adapterProductList);
        adapterProductList.setClickListener(this);

//        rvProductList.addItemDecoration(new SimpleDividerItemDecoration(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProductList.getContext(),
                layoutManager.getOrientation());
        rvProductList.addItemDecoration(dividerItemDecoration);

    }

    private void getProductDummyData() {
        listProducts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ProductObject productObject = new ProductObject();
            productObject.setProductName("Vegetable Name " + i);
            productObject.setCategoryName("Vegetable Category " + i);
            productObject.setPrice(100);
            productObject.setProductImage("");

            listProducts.add(productObject);
        }


    }

    @Override
    public void onClick(View view, int position) {
        ProductObject productObject = listProducts.get(position);

        Intent intent = new Intent(ProductListActivity.this, ProductDetailsActivity.class);
        intent.putExtra("ProductObject", productObject);
        startActivityForResult(intent, REQUEST_CODE_PRODUCT_DETAILS);
    }

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
                            listProducts = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                int addressID = jsonObj.optInt("AddressId");
                                String address = jsonObj.getString("Address");
                                String addressType = jsonObj.optString("AddressType");
                                String mobileNo = jsonObj.optString("MobNo");
                                int zipCode = jsonObj.optInt("ZipCode");

//                                ProductObject productObject = new ProductObject();
//                                addressDetails.setAddressID(addressID);
//                                addressDetails.setAddress(address);
//                                addressDetails.setAddressType(addressType);
//                                addressDetails.setZipCode(zipCode);
//
//                                listProducts.add(addressDetails);
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

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
