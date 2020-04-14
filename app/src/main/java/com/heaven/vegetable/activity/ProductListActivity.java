package com.heaven.vegetable.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
        tvToolbarTitle = findViewById(R.id.tv_toolbarTitle);

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

        Integer broccoli[] = {R.mipmap.temp_broccoli_1, R.mipmap.temp_broccoli_2, R.mipmap.temp_broccoli_3};
        Integer cabbage[] = {R.mipmap.temp_cabbage_1, R.mipmap.temp_cabbage_2, R.mipmap.temp_cabbage_3};
        Integer capsicum[] = {R.mipmap.temp_capsicum_1, R.mipmap.temp_capsicum_2, R.mipmap.temp_capsicum_3,
                R.mipmap.temp_capsicum_4, R.mipmap.temp_capsicum_5};
        Integer carrots[] = {R.mipmap.temp_carrots_1, R.mipmap.temp_carrots_2, R.mipmap.temp_carrots_3};
        Integer chilli[] = {R.mipmap.temp_chilli_1, R.mipmap.temp_chilli_2, R.mipmap.temp_chilli_3, R.mipmap.temp_chilli_4};
        Integer lemon[] = {R.mipmap.temp_lemon_1, R.mipmap.temp_lemon_2, R.mipmap.temp_lemon_3, R.mipmap.temp_lemon_4};
        Integer melons[] = {R.mipmap.temp_melons_1, R.mipmap.temp_melons_2, R.mipmap.temp_melons_3, R.mipmap.temp_melons_4};
        Integer potato[] = {R.mipmap.temp_potato_1, R.mipmap.temp_potato_2, R.mipmap.temp_potato_3, R.mipmap.temp_potato_4};
        Integer tomato[] = {R.mipmap.temp_tomato_1, R.mipmap.temp_tomato_2, R.mipmap.temp_tomato_3, R.mipmap.temp_tomato_4};

        ProductObject productObject = new ProductObject();
        productObject.setProductName("Broccoli");
        productObject.setProductImage(broccoli);
        productObject.setCategoryName("Vegetable");
        productObject.setPrice(40);

        ProductObject productObject1 = new ProductObject();
        productObject1.setProductName("Cabbage");
        productObject1.setProductImage(cabbage);
        productObject1.setCategoryName("Leaf Vegetable");
        productObject1.setPrice(30);

        ProductObject productObject2 = new ProductObject();
        productObject2.setProductName("Capsicum");
        productObject2.setProductImage(capsicum);
        productObject2.setCategoryName("Green Vegetable");
        productObject2.setPrice(50);

        ProductObject productObject3 = new ProductObject();
        productObject3.setProductName("Carrots");
        productObject3.setProductImage(carrots);
        productObject3.setCategoryName("Fruit Vegetable");
        productObject3.setPrice(35);

        ProductObject productObject4 = new ProductObject();
        productObject4.setProductName("Chili");
        productObject4.setProductImage(chilli);
        productObject4.setCategoryName("Green Vegetable");
        productObject4.setPrice(25);

        ProductObject productObject5 = new ProductObject();
        productObject5.setProductName("Lemon");
        productObject5.setProductImage(lemon);
        productObject5.setCategoryName("Fruit Vegetable");
        productObject5.setPrice(15);

        ProductObject productObject6 = new ProductObject();
        productObject6.setProductName("Potato");
        productObject6.setProductImage(potato);
        productObject6.setCategoryName("Root Vegetable");
        productObject6.setPrice(55);

        ProductObject productObject7 = new ProductObject();
        productObject7.setProductName("Tomato");
        productObject7.setProductImage(tomato);
        productObject7.setCategoryName("Fruit Vegetable");
        productObject7.setPrice(45);

        ProductObject productObject8 = new ProductObject();
        productObject8.setProductName("Water Melons");
        productObject8.setProductImage(melons);
        productObject8.setCategoryName("Fruit");
        productObject8.setPrice(80);

        listProducts.add(productObject);
        listProducts.add(productObject1);
        listProducts.add(productObject2);
        listProducts.add(productObject3);
        listProducts.add(productObject4);
        listProducts.add(productObject5);
        listProducts.add(productObject6);
        listProducts.add(productObject7);
        listProducts.add(productObject8);


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == REQUEST_CODE_PRODUCT_DETAILS) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                String flag = data.getExtras().getString("MESSAGE");

                Intent intent = new Intent();
                intent.putExtra("MESSAGE", flag);
                setResult(RESULT_OK, intent);
                finish();

//                if (flag.equalsIgnoreCase("VIEW_CART")) {
//                    triggerTabChangeListener.setTab(1);
//
//                } else if (flag.equalsIgnoreCase("UPDATE_CART_COUNT")) {
//                    int noOfItems = data.getExtras().getInt("CART_ITEM_COUNT");
//                    triggerTabChangeListener.setBadgeCount(noOfItems);
//                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
