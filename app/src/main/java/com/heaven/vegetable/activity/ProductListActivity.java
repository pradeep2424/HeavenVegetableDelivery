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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.RecycleAdapterProductList;
import com.heaven.vegetable.interfaces.OnItemAddedToCart;
import com.heaven.vegetable.interfaces.OnRecyclerViewClickListener;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.CartObject;
import com.heaven.vegetable.model.CategoryObject;
import com.heaven.vegetable.model.ClientObject;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.sharedPreference.PrefManagerConfig;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.travijuu.numberpicker.library.Enums.ActionEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity implements OnRecyclerViewClickListener, OnItemAddedToCart {
    DialogLoadingIndicator progressIndicator;
    public RelativeLayout rlRootLayout;

    //    View viewToolbarAddresses;
//    ImageView ivBack;
    TextView tvToolbarTitle;

    private Toolbar toolbar;
    private PrefManagerConfig prefManagerConfig;

    String mobileNumber;
    String intentFlag;

    private int totalCartQuantity;
    private double totalCartPrice;

    private RecyclerView rvProductList;
    private RecycleAdapterProductList adapterProductList;
    private ArrayList<ProductObject> listProducts;

    private View viewViewCart;
    private TextView tvItemQuantity;
    private TextView tvTotalPrice;

    ClientObject clientObject;
    UserDetails userDetails;
    CategoryObject categoryObject;

    private final int REQUEST_CODE_PRODUCT_DETAILS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            categoryObject = (CategoryObject) bundle.getSerializable("CategoryObject");
        }

        initComponents();
        componentEvents();

//        getUserAddressList();
        getProductDetailsData();
//        setupRecyclerProductList();
    }

    private void initComponents() {
        clientObject = Application.clientObject;
        userDetails = Application.userDetails;

        progressIndicator = DialogLoadingIndicator.getInstance();

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

        viewViewCart = findViewById(R.id.view_bottomViewCart);
        tvItemQuantity = viewViewCart.findViewById(R.id.tv_itemQuantity);
        tvTotalPrice = viewViewCart.findViewById(R.id.tv_totalPrice);

//        viewToolbarAddresses = findViewById(R.id.view_toolbarProductList);
//        ivBack = viewToolbarAddresses.findViewById(R.id.iv_back);
//        tvToolbarTitle = viewToolbarAddresses.findViewById(R.id.tv_toolbarTitle);
//        tvToolbarTitle.setText(getResources().getString(R.string.profile_manage_addresses));
    }

    private void componentEvents() {
        viewViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "VIEW_CART");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    private void setupRecyclerProductList() {
//        getProductDummyData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setItemAnimator(new DefaultItemAnimator());

        adapterProductList = new RecycleAdapterProductList(this, listProducts);
        rvProductList.setAdapter(adapterProductList);
        adapterProductList.setClickListener(this);
        adapterProductList.setOnItemAddedToCart(this);

//        rvProductList.addItemDecoration(new SimpleDividerItemDecoration(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProductList.getContext(),
                layoutManager.getOrientation());
        rvProductList.addItemDecoration(dividerItemDecoration);

    }

//    private void getProductDummyData() {
//        listProducts = new ArrayList<>();
//
//        Integer broccoli[] = {R.mipmap.temp_broccoli_1, R.mipmap.temp_broccoli_2, R.mipmap.temp_broccoli_3};
//        Integer cabbage[] = {R.mipmap.temp_cabbage_1, R.mipmap.temp_cabbage_2, R.mipmap.temp_cabbage_3};
//        Integer capsicum[] = {R.mipmap.temp_capsicum_1, R.mipmap.temp_capsicum_2, R.mipmap.temp_capsicum_3,
//                R.mipmap.temp_capsicum_4, R.mipmap.temp_capsicum_5};
//        Integer carrots[] = {R.mipmap.temp_carrots_1, R.mipmap.temp_carrots_2, R.mipmap.temp_carrots_3};
//        Integer chilli[] = {R.mipmap.temp_chilli_1, R.mipmap.temp_chilli_2, R.mipmap.temp_chilli_3, R.mipmap.temp_chilli_4};
//        Integer lemon[] = {R.mipmap.temp_lemon_1, R.mipmap.temp_lemon_2, R.mipmap.temp_lemon_3, R.mipmap.temp_lemon_4};
//        Integer melons[] = {R.mipmap.temp_melons_1, R.mipmap.temp_melons_2, R.mipmap.temp_melons_3, R.mipmap.temp_melons_4};
//        Integer potato[] = {R.mipmap.temp_potato_1, R.mipmap.temp_potato_2, R.mipmap.temp_potato_3, R.mipmap.temp_potato_4};
//        Integer tomato[] = {R.mipmap.temp_tomato_1, R.mipmap.temp_tomato_2, R.mipmap.temp_tomato_3, R.mipmap.temp_tomato_4};
//
//        ProductObject productObject = new ProductObject();
//        productObject.setProductName("Broccoli");
//        productObject.setProductImage(broccoli);
//        productObject.setCategoryName("Vegetable");
//        productObject.setPrice(40);
//
//        ProductObject productObject1 = new ProductObject();
//        productObject1.setProductName("Cabbage");
//        productObject1.setProductImage(cabbage);
//        productObject1.setCategoryName("Leaf Vegetable");
//        productObject1.setPrice(30);
//
//        ProductObject productObject2 = new ProductObject();
//        productObject2.setProductName("Capsicum");
//        productObject2.setProductImage(capsicum);
//        productObject2.setCategoryName("Green Vegetable");
//        productObject2.setPrice(50);
//
//        ProductObject productObject3 = new ProductObject();
//        productObject3.setProductName("Carrots");
//        productObject3.setProductImage(carrots);
//        productObject3.setCategoryName("Fruit Vegetable");
//        productObject3.setPrice(35);
//
//        ProductObject productObject4 = new ProductObject();
//        productObject4.setProductName("Chili");
//        productObject4.setProductImage(chilli);
//        productObject4.setCategoryName("Green Vegetable");
//        productObject4.setPrice(25);
//
//        ProductObject productObject5 = new ProductObject();
//        productObject5.setProductName("Lemon");
//        productObject5.setProductImage(lemon);
//        productObject5.setCategoryName("Fruit Vegetable");
//        productObject5.setPrice(15);
//
//        ProductObject productObject6 = new ProductObject();
//        productObject6.setProductName("Potato");
//        productObject6.setProductImage(potato);
//        productObject6.setCategoryName("Root Vegetable");
//        productObject6.setPrice(55);
//
//        ProductObject productObject7 = new ProductObject();
//        productObject7.setProductName("Tomato");
//        productObject7.setProductImage(tomato);
//        productObject7.setCategoryName("Fruit Vegetable");
//        productObject7.setPrice(45);
//
//        ProductObject productObject8 = new ProductObject();
//        productObject8.setProductName("Water Melons");
//        productObject8.setProductImage(melons);
//        productObject8.setCategoryName("Fruit");
//        productObject8.setPrice(80);
//
//        listProducts.add(productObject);
//        listProducts.add(productObject1);
//        listProducts.add(productObject2);
//        listProducts.add(productObject3);
//        listProducts.add(productObject4);
//        listProducts.add(productObject5);
//        listProducts.add(productObject6);
//        listProducts.add(productObject7);
//        listProducts.add(productObject8);
//
//
//    }

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

                            setupRecyclerProductList();

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


    public void showDialog() {
        progressIndicator.showProgress(ProductListActivity.this);
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    public void showSnackBarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }


    private void calculateViewCartDetails(double itemPrice, String incrementOrDecrement) {
        int itemQuantity = 1;   // at a time 1 item can be added or removed
        if (incrementOrDecrement.equalsIgnoreCase(ActionEnum.INCREMENT.toString())) {
//            increment
            totalCartQuantity = totalCartQuantity + itemQuantity;
            double price = itemQuantity * itemPrice;
            totalCartPrice = totalCartPrice + price;

        } else {

            totalCartQuantity = totalCartQuantity - itemQuantity;
            double price = itemQuantity * itemPrice;
            totalCartPrice = totalCartPrice - price;
        }
    }


    private void updateViewCartStrip() {
        if (totalCartQuantity == 0) {
            viewViewCart.setVisibility(View.GONE);

        } else {
            viewViewCart.setVisibility(View.VISIBLE);

            String strPrice = formatAmount(totalCartPrice);
            String itemLabel = "";
            if (totalCartQuantity > 1) {
                itemLabel = getString(R.string.items);

            } else {
                itemLabel = getString(R.string.item);
            }

            tvItemQuantity.setText(totalCartQuantity + " " + itemLabel);
            tvTotalPrice.setText(getString(R.string.rupees) + " " + strPrice);
        }
    }

    private String formatAmount(double amount) {
        String amt;
        DecimalFormat df = new DecimalFormat();
        df.setDecimalSeparatorAlwaysShown(false);
        amt = df.format(amount);

        return amt;
    }

    private void addItemToLocal(ProductObject dishObject, int quantity, String incrementOrDecrement) {
        CartObject cartObject = new CartObject();
        cartObject.setCgst(dishObject.getCgst());
        cartObject.setClientID(clientObject.getRestaurantID());
        cartObject.setDeliveryCharge(30);
        cartObject.setRestaurantName(clientObject.getRestaurantName());
        cartObject.setIsIncludeTax(clientObject.getIncludeTax());
        cartObject.setIsTaxApplicable(clientObject.getTaxable());
        cartObject.setProductAmount(dishObject.getPrice());
        cartObject.setProductID(dishObject.getProductID());
        cartObject.setProductName(dishObject.getProductName());
        cartObject.setProductQuantity(quantity);
        cartObject.setProductRate(dishObject.getPrice());
        cartObject.setProductSize("Regular");
        cartObject.setSgst(dishObject.getSgst());
        cartObject.setTaxID(dishObject.getTaxID());
        cartObject.setTaxableVal(dishObject.getPrice());
        cartObject.setTotalAmount(dishObject.getPrice());
        cartObject.setUserID(Application.userDetails.getUserID());
        cartObject.setCartID(Application.listCartItems.size());
        cartObject.setUnitID(dishObject.getUnitID());

        boolean isItemAlreadyExist = false;
        int newAddedProductID = dishObject.getProductID();
        for (int i = 0; i < Application.listCartItems.size(); i++) {
            int cartProductID = Application.listCartItems.get(i).getProductID();
            if (cartProductID == newAddedProductID) {
                isItemAlreadyExist = true;
                Application.listCartItems.remove(i);
//                Application.listCartItems.set(i, cartObject);
            }
        }

        Application.listCartItems.add(cartObject);

//        if (!isItemAlreadyExist) {
//            Application.listCartItems.add(cartObject);
//        }

        calculateViewCartDetails(dishObject.getPrice(), incrementOrDecrement);
        updateViewCartStrip();
    }


    private void getProductDetailsData() {
        if (InternetConnection.checkConnection(this)) {
            showDialog();

            int userTypeID = userDetails.getUserID();
            int clientID = clientObject.getRestaurantID();
            int foodTypeID = 0;
            int categoryID = categoryObject.getCategoryID();
//            int categoryID = productObject.getCategoryID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, clientID, foodTypeID, categoryID);
//            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
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

                                int categoryID = jsonObj.optInt("CategoryID");
                                String categoryName = jsonObj.optString("CategoryName");
                                String foodType = jsonObj.optString("FoodType");
                                int foodTypeID = jsonObj.optInt("FoodTypeId");
                                String group = jsonObj.optString("Group");
                                int groupID = jsonObj.optInt("GroupId");
                                double price = jsonObj.optDouble("Price");
                                String productDesc = jsonObj.optString("ProductDesc");
                                int productID = jsonObj.optInt("ProductId");
                                String productImage = jsonObj.optString("PhotoPath");
                                String productName = jsonObj.optString("ProductName");
                                String unit = jsonObj.optString("Unit");
                                int unitID = jsonObj.optInt("UnitId");

//                                double cgst = jsonObj.optDouble("CGST");
//                                int haveRuntimeRate = jsonObj.optInt("HaveRuntimeRate");
//                                String isDiscounted = jsonObj.optString("IsDiscounted");
//                                double sgst = jsonObj.optDouble("SGST");
//                                int taxID = jsonObj.optInt("TaxID");
//                                String taxName = jsonObj.optString("TaxName");

                                ArrayList<String> listProdImages = new ArrayList<>();
                                listProdImages.add(productImage);

                                ProductObject productObject = new ProductObject();
                                productObject.setProductID(productID);
                                productObject.setProductName(productName);
                                productObject.setProductDescription(productDesc);
                                productObject.setListProductImage(listProdImages);
                                productObject.setPrice(price);
                                productObject.setCategoryID(categoryID);
                                productObject.setCategoryName(categoryName);
                                productObject.setFoodTypeID(foodTypeID);
                                productObject.setFoodType(foodType);
                                productObject.setGroupID(groupID);
                                productObject.setGroup(group);
                                productObject.setUnitID(unitID);
                                productObject.setUnit(unit);

                                listProducts.add(productObject);
                            }

                            setupRecyclerProductList();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dismissDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        dismissDialog();
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
                            getProductDetailsData();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    @Override
    public void onItemChangedInCart(int quantity, int position, String incrementOrDecrement) {
        ProductObject productObject = listProducts.get(position);
        Application.productObject = productObject;

//        calculateViewCartDetails(dishObject.getPrice(), incrementOrDecrement);
//        updateViewCartStrip();

        String mobileNo = Application.userDetails.getMobile();
        if (mobileNo != null) {
            calculateViewCartDetails(productObject.getPrice(), incrementOrDecrement);
            updateViewCartStrip();

        } else {
            addItemToLocal(productObject, quantity, incrementOrDecrement);
        }
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

                intentFlag = data.getExtras().getString("MESSAGE");
                totalCartQuantity = data.getExtras().getInt("CART_ITEM_COUNT");

                if (intentFlag.equalsIgnoreCase("VIEW_CART")) {
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "VIEW_CART");
                    setResult(RESULT_OK, intent);
                    finish();
                }
//                else if (flag.equalsIgnoreCase("UPDATE_CART_COUNT")) {
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
//        super.onBackPressed();

        int totalItems;
        if (intentFlag != null) {    // back from product details page
            totalItems = totalCartQuantity;

        } else {
            totalItems = Application.listCartItems.size() + totalCartQuantity;
        }

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "UPDATE_CART_COUNT");
        intent.putExtra("CART_ITEM_COUNT", totalItems);
        setResult(RESULT_OK, intent);
        finish();
    }
}
