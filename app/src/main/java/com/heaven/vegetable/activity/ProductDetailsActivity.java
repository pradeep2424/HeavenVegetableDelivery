package com.heaven.vegetable.activity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.PagerAdapterSlidingProductImages;
import com.heaven.vegetable.listeners.OnItemAddedToCart;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.model.RestaurantObject;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class ProductDetailsActivity extends AppCompatActivity  {

public class ProductDetailsActivity extends AppCompatActivity {
    DialogLoadingIndicator progressIndicator;
    CoordinatorLayout clRootLayout;

    View viewToolbar;
    ImageView ivBack;

    String[] foodName = {"Navgrah Veg Restaurant", "Saroj Hotel", "Hotel Jewel of Chembur"};
    Integer[] foodImage = {R.mipmap.temp_order, R.mipmap.temp_order,
            R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order};

    private SliderLayout sliderLayoutProductImages;
    //    private CircleIndicator circleIndicator;
//    private PagerAdapterSlidingProductImages adapterSlidingImages;
    private ArrayList<Integer> listPhotos;

//    private View viewViewCart;
//    private TextView tvItemQuantity;
//    private TextView tvTotalPrice;

    private TextView tvProductName;
    private TextView tvProductCategory;
    private TextView tvProductPrice;

    private LinearLayout llViewCartLayout;
    private TextView tvProductTotalAmount;
    private TextView tvViewCartText;
    private TextView tvAddToCart;
    private NumberPicker numberPickerItemQuantity;

    private int totalCartQuantity;
    private double totalCartPrice;
    private double itemPackPrice;
    private int packSize;

    LinearLayout ll250Gram;
    LinearLayout ll500Gram;
    LinearLayout ll1Kilo;

    ProductObject productObject;
    UserDetails userDetails;
    RestaurantObject restaurantObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productObject = (ProductObject) bundle.getSerializable("ProductObject");
        }

        initComponents();
        componentEvents();
        setupSlidingImages();
        setupProductDetails();

//        setupRestaurantDetails();

//        setupRecyclerViewMenu();

//        getProductsPhotoGallery();
        getProductDetailsData();

        ll250Gram.performClick();
    }

    private void initComponents() {
        userDetails = Application.userDetails;
        restaurantObject = Application.restaurantObject;

        progressIndicator = DialogLoadingIndicator.getInstance();
        clRootLayout = findViewById(R.id.cl_rootLayout);
        viewToolbar = findViewById(R.id.view_toolbarRestaurantDetails);
        ivBack = viewToolbar.findViewById(R.id.iv_back);

        sliderLayoutProductImages = (SliderLayout) findViewById(R.id.sliderLayout_productImages);
//        circleIndicator =  findViewById(R.id.indicator);
        tvProductName = findViewById(R.id.tv_productName);
        tvProductCategory = findViewById(R.id.tv_productCategory);
        tvProductPrice = findViewById(R.id.tv_productPrice);

        llViewCartLayout = findViewById(R.id.ll_viewCartLayout);
        tvProductTotalAmount = findViewById(R.id.tv_productAmount);
        tvViewCartText = findViewById(R.id.tv_viewCartText);
        tvAddToCart = findViewById(R.id.tv_addToCart);
        numberPickerItemQuantity = findViewById(R.id.numberPicker_quantity);

//        viewViewCart = findViewById(R.id.view_bottomViewCart);
//        tvItemQuantity = viewViewCart.findViewById(R.id.tv_itemQuantity);
//        tvTotalPrice = viewViewCart.findViewById(R.id.tv_totalPrice);

        ll250Gram = findViewById(R.id.ll_250Gram);
        ll500Gram = findViewById(R.id.ll_500Gram);
        ll1Kilo = findViewById(R.id.ll_1Kilo);

//        viewPager = (ViewPager) findViewById(R.id.viewPager_slidingRestaurantImages);
    }

    private void componentEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        viewViewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("MESSAGE", "VIEW_CART");
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

        tvProductTotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "VIEW_CART");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemOrUpdateQuantity(1, ActionEnum.INCREMENT.toString());
            }
        });

        numberPickerItemQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
//                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ?
                        ActionEnum.INCREMENT.toString() : ActionEnum.DECREMENT.toString());
                String message = String.format("NumberPicker is %s to %d", actionText, value);

                addItemOrUpdateQuantity(value, actionText);

            }
        });

        ll250Gram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                packSize = 0;
                calculateItemPackPrice();

                ll250Gram.setBackground(getResources().getDrawable(R.drawable.rect2));
                ll500Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll1Kilo.setBackground(getResources().getDrawable(R.drawable.rect1));
            }
        });

        ll500Gram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                packSize = 1;
                calculateItemPackPrice();

                ll250Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll500Gram.setBackground(getResources().getDrawable(R.drawable.rect2));
                ll1Kilo.setBackground(getResources().getDrawable(R.drawable.rect1));
            }
        });

        ll1Kilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                packSize = 2;
                calculateItemPackPrice();

                ll250Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll500Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll1Kilo.setBackground(getResources().getDrawable(R.drawable.rect2));
            }
        });
    }


    private void setupProductDetails() {
        if (productObject != null) {
            String formattedPrice = getString(R.string.rupees) + " " + formatAmount(productObject.getPrice());

            tvProductName.setText(productObject.getProductName());
            tvProductCategory.setText(productObject.getCategoryName());
            tvProductPrice.setText(formattedPrice);
        }
    }

    private void setupSlidingImages() {
        getPhotosData();

//        HashMap<String, String> url_maps = new HashMap<String, String>();
//        url_maps.put("Best Offer", "https://c4.wallpaperflare.com/wallpaper/563/7/828/peas-pod-food-wallpaper-preview.jpg");
//        url_maps.put("Big Bang Theory", "https://c4.wallpaperflare.com/wallpaper/755/380/1002/vegetables-paprika-food-tomatoes-mushroom-wallpaper-preview.jpg");
//        url_maps.put("House of Cards", "https://c4.wallpaperflare.com/wallpaper/401/129/264/food-vegetables-mushrooms-peppers-tomatoes-spoons-wallpaper-preview.jpg");
//        url_maps.put("Game of Thrones", "https://c4.wallpaperflare.com/wallpaper/53/520/935/food-vegetables-tomatoes-eggplant-wallpaper-preview.jpg");
//        url_maps.put("Game of Thrones", "https://c0.wallpaperflare.com/preview/218/234/443/asparagus-avocado-colorful-eating.jpg");

//        HashMap<String, Integer> url_maps = new HashMap<String, Integer>();
//        url_maps.put("50% off on Lunch", R.mipmap.temp_img1);
//        url_maps.put("Free delivery above 250", R.mipmap.temp_img2);
//        url_maps.put("House of Cards", R.mipmap.temp_img3);
//        url_maps.put("Game of Thrones", R.mipmap.temp_img4);

//        HashMap<String, String> url_maps = new HashMap<String, String>();
//        url_maps.putAll(mapBannerDetails);

//        for (String name : listPhotos.keySet()) {
        for (int i = 0; i < listPhotos.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
//                    .description(name)
                    .image(listPhotos.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
//                    .setOnSliderClickListener(this);

//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra", name);

            sliderLayoutProductImages.addSlider(textSliderView);
        }

        sliderLayoutProductImages.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        sliderLayoutProductImages.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayoutProductImages.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        sliderLayoutProductImages.setCustomAnimation(new DescriptionAnimation());
        sliderLayoutProductImages.setDuration(4000);
//        sliderLayoutProductImages.addOnPageChangeListener(this);
    }


//    private void setupSlidingProductImages() {
//        getPhotosData();
//
//        adapterSlidingImages = new PagerAdapterSlidingProductImages(this, listPhotos);
//
//        loginPagerAdapter = new FoodPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(loginPagerAdapter);
//        indicator.setViewPager(viewPager);
//        loginPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rvPhotos.setLayoutManager(layoutManager);
//        rvPhotos.setItemAnimator(new DefaultItemAnimator());
//        rvPhotos.setAdapter(adapterRestaurantPhotos);
//    }

    private void getPhotosData() {
        listPhotos = new ArrayList<>();

        Integer icons[] = productObject.getProductImage();
        listPhotos.addAll(new ArrayList<Integer>(Arrays.asList(icons)));
    }

    private void calculateItemPackPrice() {
        double itemPrice = productObject.getPrice();
        switch (packSize) {
            case 0:   // 250 gm
                itemPackPrice = itemPrice * 1;
                break;

            case 1:   // 500 gm
                itemPackPrice = itemPrice * 2;
                break;

            case 2:   // 1 KG
                itemPackPrice = itemPrice * 4;
                break;
        }

        String formatPackPrice = formatAmount(itemPackPrice);
        tvProductPrice.setText(getString(R.string.rupees) + formatPackPrice);
    }

    private void addItemOrUpdateQuantity(final int quantity, final String incrementOrDecrement) {
        numberPickerItemQuantity.setValue(quantity);
        if (quantity == 0) {
            showAddItemButton();

        } else {
            hideAddItemButton();
        }

        addItemToCart(quantity, productObject, incrementOrDecrement);
    }

    public void showAddItemButton() {
        numberPickerItemQuantity.setVisibility(View.GONE);
        tvAddToCart.setVisibility(View.VISIBLE);
    }

    public void hideAddItemButton() {
//            show number picker
        numberPickerItemQuantity.setVisibility(View.VISIBLE);
        tvAddToCart.setVisibility(View.GONE);
    }

    private void calculateViewCartDetails(String incrementOrDecrement) {
        int itemQuantity = 1;   // at a time 1 item can be added or removed
        if (incrementOrDecrement.equalsIgnoreCase(ActionEnum.INCREMENT.toString())) {
//            increment
            totalCartQuantity = totalCartQuantity + itemQuantity;
            double price = itemQuantity * itemPackPrice;
            totalCartPrice = totalCartPrice + price;

        } else {

            totalCartQuantity = totalCartQuantity - itemQuantity;
            double price = itemQuantity * itemPackPrice;
            totalCartPrice = totalCartPrice - price;
        }
    }

    private void updateViewCartStrip() {
        if (totalCartQuantity == 0) {
            removeBackgroundWhenNumberPickerVisHidden();

        } else {
            addBackgroundWhenNumberPickerVisible();

            String strPrice = formatAmount(totalCartPrice);
            String itemLabel = "";
            if (totalCartQuantity > 1) {
                itemLabel = getString(R.string.items);

            } else {
                itemLabel = getString(R.string.item);
            }

//            tvItemQuantity.setText(totalCartQuantity + " " + itemLabel);
            tvProductTotalAmount.setText(getString(R.string.rupees) + " " + strPrice);
        }
    }

    private void addBackgroundWhenNumberPickerVisible() {
        tvViewCartText.setVisibility(View.VISIBLE);
        tvProductTotalAmount.setText("");

        llViewCartLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_dark));
        tvAddToCart.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }

    private void removeBackgroundWhenNumberPickerVisHidden() {
        tvViewCartText.setVisibility(View.GONE);
        tvProductTotalAmount.setText(getString(R.string.view_cart));

        llViewCartLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        tvAddToCart.setBackgroundColor(ContextCompat.getColor(this, R.color.deep_orange));
    }

    private String formatAmount(double amount) {
        String amt;
        DecimalFormat df = new DecimalFormat();
        df.setDecimalSeparatorAlwaysShown(false);
        amt = df.format(amount);

        return amt;
    }

//    private void addItemToLocal(DishObject dishObject, int quantity, String incrementOrDecrement) {
////        CartObject cartObject = new CartObject();
////        cartObject.setCgst(dishObject.getCgst());
////        cartObject.setRestaurantID(productObject.getRestaurantID());
////        cartObject.setDeliveryCharge(30);
////        cartObject.setRestaurantName(productObject.getRestaurantName());
////        cartObject.setIsIncludeTax(productObject.getIncludeTax());
////        cartObject.setIsTaxApplicable(productObject.getTaxable());
////        cartObject.setProductAmount(dishObject.getPrice());
////        cartObject.setProductID(dishObject.getProductID());
////        cartObject.setProductName(dishObject.getProductName());
////        cartObject.setProductQuantity(quantity);
////        cartObject.setProductRate(dishObject.getPrice());
////        cartObject.setProductSize("Regular");
////        cartObject.setSgst(dishObject.getSgst());
////        cartObject.setTaxID(dishObject.getTaxID());
////        cartObject.setTaxableVal(dishObject.getPrice());
////        cartObject.setTotalAmount(dishObject.getPrice());
////        cartObject.setUserID(Application.userDetails.getUserID());
////        cartObject.setCartID(Application.listCartItems.size());
////
////        boolean isItemAlreadyExist = false;
////        int newAddedProductID = dishObject.getProductID();
////        for (int i = 0; i < Application.listCartItems.size(); i++) {
////            int cartProductID = Application.listCartItems.get(i).getProductID();
////            if (cartProductID == newAddedProductID) {
////                isItemAlreadyExist = true;
////                Application.listCartItems.remove(i);
//////                Application.listCartItems.set(i, cartObject);
////            }
////        }
////
////        Application.listCartItems.add(cartObject);
////
//////        if (!isItemAlreadyExist) {
//////            Application.listCartItems.add(cartObject);
//////        }
////
////        calculateViewCartDetails(dishObject.getPrice(), incrementOrDecrement);
////        updateViewCartStrip();
////    }

    private void getProductDetailsData() {
        if (InternetConnection.checkConnection(this)) {
            showDialog();

            int userTypeID = Application.userDetails.getUserID();
            int restaurantID = productObject.getProductID();
            int foodTypeID = productObject.getFoodTypeID();
            int categoryID = productObject.getCategoryID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
//            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
//                            listDishProducts = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                double cgst = jsonObj.optDouble("CGST");
                                int categoryID = jsonObj.optInt("CategoryID");
                                String categoryName = jsonObj.optString("CategoryName");
                                String foodType = jsonObj.optString("FoodType");
                                int foodTypeID = jsonObj.optInt("FoodTypeId");
                                int dishID = jsonObj.optInt("HaveRuntimeRate");
                                String isDiscounted = jsonObj.optString("IsDiscounted");
                                double price = jsonObj.optDouble("Price");
                                String productDesc = jsonObj.optString("ProductDesc");
                                int productID = jsonObj.optInt("ProductId");
                                String productImage = jsonObj.optString("ProductImage");
                                String productName = jsonObj.optString("ProductName");
                                double sgst = jsonObj.optDouble("SGST");
                                int taxID = jsonObj.optInt("TaxID");
                                String taxName = jsonObj.optString("TaxName");

//                                DishObject dishObject = new DishObject();
//                                dishObject.setCgst(cgst);
//                                dishObject.setCategoryID(categoryID);
//                                dishObject.setCategoryName(categoryName);
//                                dishObject.setFoodType(foodType);
//                                dishObject.setFoodTypeID(foodTypeID);
//                                dishObject.setDishID(dishID);
//                                dishObject.setIsDiscounted(isDiscounted);
//                                dishObject.setPrice(price);
//                                dishObject.setProductDesc(productDesc);
//                                dishObject.setProductID(productID);
//                                dishObject.setProductImage(productImage);
//                                dishObject.setProductName(productName);
//                                dishObject.setSgst(sgst);
//                                dishObject.setTaxID(taxID);
//                                dishObject.setTaxName(taxName);
//
//                                listDishProducts.add(dishObject);
                            }

                            setupProductDetails();

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

            Snackbar.make(clRootLayout, getResources().getString(R.string.no_internet),
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


    private JsonObject createJsonCart(ProductObject productObject, int quantity) {
        double totalPrice;

        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("ProductId", productObject.getProductID());
            postParam.addProperty("ProductName", productObject.getProductName());
            postParam.addProperty("ProductRate", productObject.getPrice());
            postParam.addProperty("ProductAmount", productObject.getPrice());
            postParam.addProperty("ProductSize", "Regular");
            postParam.addProperty("cartId", 0);
            postParam.addProperty("ProductQnty", quantity);
            postParam.addProperty("Taxableval", productObject.getPrice());    // doubt
            postParam.addProperty("CGST", productObject.getCgst());
            postParam.addProperty("SGST", productObject.getSgst());
            postParam.addProperty("DeliveryCharge", 30.00);
            postParam.addProperty("Userid", userDetails.getUserID());
            postParam.addProperty("Clientid", restaurantObject.getRestaurantID());
            postParam.addProperty("TaxId", 0);
            postParam.addProperty("TotalAmount", productObject.getPrice());
            postParam.addProperty("HotelName", restaurantObject.getRestaurantName());
            postParam.addProperty("IsIncludeTax", true);
            postParam.addProperty("IsTaxApplicable", true);

//            postParam.addProperty("TotalAmount", dishObject.getPrice());
//            postParam.addProperty("HotelName", productObject.getRestaurantName());
//            postParam.addProperty("IsIncludeTax", productObject.getIncludeTax());
//            postParam.addProperty("IsTaxApplicable", productObject.getTaxable());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }


    public void addItemToCart(final int quantity, final ProductObject productObject, final String incrementOrDecrement) {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(productObject, quantity));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            calculateViewCartDetails(incrementOrDecrement);
                            updateViewCartStrip();
//                            showHideQuantityAndAddItemButton();

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

            Snackbar.make(clRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addItemToCart(quantity, productObject, incrementOrDecrement);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void showDialog() {
        progressIndicator.showProgress(ProductDetailsActivity.this);
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(clRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    public void showSnackBarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(clRootLayout, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }


//    private void setupViewPagerSlidingRestaurantImages()
//    {
//        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
//        loginPagerAdapter = new FoodPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(loginPagerAdapter);
//        indicator.setViewPager(viewPager);
//        loginPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
//    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        int totalItems = Application.listCartItems.size() + totalCartQuantity;

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "UPDATE_CART_COUNT");
        intent.putExtra("CART_ITEM_COUNT", totalItems);
        setResult(RESULT_OK, intent);
        finish();
    }
}
