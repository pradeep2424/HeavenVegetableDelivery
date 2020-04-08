package com.heaven.vegetable.activity;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.PagerAdapterSlidingProductImages;
import com.heaven.vegetable.listeners.OnItemAddedToCart;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.travijuu.numberpicker.library.Enums.ActionEnum;

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

public class ProductDetailsActivity extends AppCompatActivity implements OnItemAddedToCart {
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

    private View viewViewCart;
    private TextView tvItemQuantity;
    private TextView tvTotalPrice;

    private TextView tvRestaurantName;
    private TextView tvRestaurantReviews;
    private RatingBar ratingBarReviews;

    private int totalCartQuantity;
    private double totalCartPrice;

    LinearLayout ll250Gram;
    LinearLayout ll500Gram;
    LinearLayout ll1Kilo;

    ProductObject productObject;

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

//        setupRestaurantDetails();

//        setupRecyclerViewMenu();

//        getProductsPhotoGallery();
        getProductDetailsData();
    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();

        clRootLayout = findViewById(R.id.cl_rootLayout);
        viewToolbar = findViewById(R.id.view_toolbarRestaurantDetails);
        ivBack = viewToolbar.findViewById(R.id.iv_back);

        sliderLayoutProductImages = (SliderLayout) findViewById(R.id.sliderLayout_productImages);
//        circleIndicator =  findViewById(R.id.indicator);
        tvRestaurantName = findViewById(R.id.tv_restaurantName);
        tvRestaurantReviews = findViewById(R.id.tv_restaurantReviews);
        ratingBarReviews = findViewById(R.id.ratingBar_restaurantReviews);

        viewViewCart = findViewById(R.id.view_bottomViewCart);
        tvItemQuantity = viewViewCart.findViewById(R.id.tv_itemQuantity);
        tvTotalPrice = viewViewCart.findViewById(R.id.tv_totalPrice);

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

        viewViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "VIEW_CART");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ll250Gram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll250Gram.setBackground(getResources().getDrawable(R.drawable.rect2));
                ll500Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll1Kilo.setBackground(getResources().getDrawable(R.drawable.rect1));
            }
        });

        ll500Gram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll250Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll500Gram.setBackground(getResources().getDrawable(R.drawable.rect2));
                ll1Kilo.setBackground(getResources().getDrawable(R.drawable.rect1));
            }
        });

        ll1Kilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll250Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll500Gram.setBackground(getResources().getDrawable(R.drawable.rect1));
                ll1Kilo.setBackground(getResources().getDrawable(R.drawable.rect2));
            }
        });
    }

    private void setupRestaurantDetails() {
        if (productObject != null) {
            tvRestaurantName.setText(productObject.getProductName());
//            tvRestaurantReviews.setText(productObject.ge());
//            ratingBarReviews.setText(productObject.getRestaurantName());
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
            for(int i = 0; i < listPhotos.size(); i ++) {
            TextSliderView textSliderView = new TextSliderView(this);
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
        sliderLayoutProductImages.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
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

    @Override
    public void onItemChangedInCart(int quantity, int position, String incrementOrDecrement) {
//        DishObject dishObject = listDishProducts.get(position);
//        Application.dishObject = dishObject;
//
////        calculateViewCartDetails(dishObject.getPrice(), incrementOrDecrement);
////        updateViewCartStrip();
//
//        String mobileNo = Application.userDetails.getMobile();
//        if (mobileNo != null) {
//            calculateViewCartDetails(dishObject.getPrice(), incrementOrDecrement);
//            updateViewCartStrip();
//
//        } else {
//            addItemToLocal(dishObject, quantity, incrementOrDecrement);
//        }

    }

//    private void addItemToLocal(DishObject dishObject, int quantity, String incrementOrDecrement) {
//        CartObject cartObject = new CartObject();
//        cartObject.setCgst(dishObject.getCgst());
//        cartObject.setRestaurantID(productObject.getRestaurantID());
//        cartObject.setDeliveryCharge(30);
//        cartObject.setRestaurantName(productObject.getRestaurantName());
//        cartObject.setIsIncludeTax(productObject.getIncludeTax());
//        cartObject.setIsTaxApplicable(productObject.getTaxable());
//        cartObject.setProductAmount(dishObject.getPrice());
//        cartObject.setProductID(dishObject.getProductID());
//        cartObject.setProductName(dishObject.getProductName());
//        cartObject.setProductQuantity(quantity);
//        cartObject.setProductRate(dishObject.getPrice());
//        cartObject.setProductSize("Regular");
//        cartObject.setSgst(dishObject.getSgst());
//        cartObject.setTaxID(dishObject.getTaxID());
//        cartObject.setTaxableVal(dishObject.getPrice());
//        cartObject.setTotalAmount(dishObject.getPrice());
//        cartObject.setUserID(Application.userDetails.getUserID());
//        cartObject.setCartID(Application.listCartItems.size());
//
//        boolean isItemAlreadyExist = false;
//        int newAddedProductID = dishObject.getProductID();
//        for (int i = 0; i < Application.listCartItems.size(); i++) {
//            int cartProductID = Application.listCartItems.get(i).getProductID();
//            if (cartProductID == newAddedProductID) {
//                isItemAlreadyExist = true;
//                Application.listCartItems.remove(i);
////                Application.listCartItems.set(i, cartObject);
//            }
//        }
//
//        Application.listCartItems.add(cartObject);
//
////        if (!isItemAlreadyExist) {
////            Application.listCartItems.add(cartObject);
////        }
//
//        calculateViewCartDetails(dishObject.getPrice(), incrementOrDecrement);
//        updateViewCartStrip();
//    }

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

//                            setupRecyclerViewProducts();

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


//    private JsonObject createJsonCart(DishObject dishObject, int quantity) {
//        double totalPrice;
//
//        ProductObject productObject = Application.productObject;
//
//        if (productObject.getTaxable()) {
//            double productPrice = dishObject.getPrice();
//            double cgst = dishObject.getCgst();
//            double sgst = dishObject.getCgst();
//
////            totalPrice = productPrice * ()
//        }
//
//        JsonObject postParam = new JsonObject();
//
//        try {
//            postParam.addProperty("ProductId", dishObject.getProductID());
//            postParam.addProperty("ProductName", dishObject.getProductName());
//            postParam.addProperty("ProductRate", dishObject.getPrice());
//            postParam.addProperty("ProductAmount", dishObject.getPrice());
//            postParam.addProperty("ProductSize", "Regular");
//            postParam.addProperty("cartId", 0);
//            postParam.addProperty("ProductQnty", quantity);
//            postParam.addProperty("Taxableval", dishObject.getPrice());    // doubt
//            postParam.addProperty("CGST", dishObject.getCgst());
//            postParam.addProperty("SGST", dishObject.getSgst());
//            postParam.addProperty("HotelName", productObject.getRestaurantName());
//            postParam.addProperty("IsIncludeTax", productObject.getIncludeTax());
//            postParam.addProperty("IsTaxApplicable", productObject.getTaxable());
//            postParam.addProperty("DeliveryCharge", 30.00);
//            postParam.addProperty("Userid", Application.userDetails.getUserID());
//            postParam.addProperty("Clientid", productObject.getRestaurantID());
//            postParam.addProperty("TotalAmount", dishObject.getPrice());
//            postParam.addProperty("TaxId", 0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return postParam;
//    }
//
//
//    public void addItemToCart(final int quantity, final DishObject dishObject, final String incrementOrDecrement) {
//        if (InternetConnection.checkConnection(this)) {
//
//            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(dishObject, quantity));
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//
//                        if (response.isSuccessful()) {
//                            String responseString = response.body().string();
//
////                            Application.cartObject = dishObject;
//
//
//                            calculateViewCartDetails(quantity, dishObject.getPrice(), incrementOrDecrement);
//                            updateViewCartStrip();
//                            adapterRestaurantMenu.showHideQuantityAndAddItemButton();
//
////                            listCartDish = new ArrayList<>();
//
////                            ada
//
////                            JSONArray jsonArray = new JSONArray(responseString);
////                            for (int i = 0; i < jsonArray.length(); i++) {
////                                JSONObject jsonObj = jsonArray.getJSONObject(i);
////
////                                String dishID = jsonObj.optString("ProductId");
////                                String dishName = jsonObj.optString("ProductName");
////                                String dishDescription = jsonObj.optString("ProductDesc");
////                                String dishImage = jsonObj.optString("ProductImage");
////                                String dishPrice = jsonObj.optString("Price");
////
////                                DishObject dishObject = new DishObject();
////                                dishObject.setDishID(dishID);
////                                dishObject.setDishName(dishName);
////                                dishObject.setDishDescription(dishDescription);
////                                dishObject.setDishImage(dishImage);
////                                dishObject.setDishPrice(dishPrice);
////
////                                listCartDish.add(dishObject);
////                            }
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            addItemToCart(quantity, dishObject, incrementOrDecrement);
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

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
