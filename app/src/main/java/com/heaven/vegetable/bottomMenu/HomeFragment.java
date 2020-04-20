package com.heaven.vegetable.bottomMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.activity.LocationGoogleMapActivity;
import com.heaven.vegetable.activity.ProductDetailsActivity;
import com.heaven.vegetable.activity.ProductListActivity;
import com.heaven.vegetable.adapter.PagerAdapterBanner;
import com.heaven.vegetable.adapter.RecycleAdapterPopularItem;
import com.heaven.vegetable.adapter.RecycleAdapterCategory;
import com.heaven.vegetable.fragments.FragmentFooter;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.listeners.OnPopularItemClickedListener;
import com.heaven.vegetable.listeners.TriggerTabChangeListener;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.BannerDetailsObject;
import com.heaven.vegetable.model.CategoryObject;
import com.heaven.vegetable.model.ClientObject;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.ConstantValues;
import com.heaven.vegetable.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements OnRecyclerViewClickListener,
        OnPopularItemClickedListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private DialogLoadingIndicator progressIndicator;
    View rootView;

    View viewToolbarLocation;
    TextView tvToolbarTitle;
    LinearLayout llToolbarLocation;
    LinearLayout llToolbarAddress;
//    LinearLayout llToolbarReferralPoints;

//    TextView tvReferralPoints;

    private SliderLayout imageSliderLayout;

    private ArrayList<ProductObject> listProductObject;
    private RecyclerView rvPopular;
    private RecycleAdapterPopularItem adapterDish;
//    Integer image[] = {R.mipmap.temp_paneer, R.mipmap.temp_order, R.mipmap.temp_ice_cream, R.mipmap.temp_paneer,
//            R.mipmap.temp_order, R.mipmap.temp_ice_cream, R.mipmap.temp_paneer, R.mipmap.temp_order};
//    String dish_name[] = {"Paratha", "Cheese", "Paneer", "Paneer", "Chiken", "Paneer Kopta", "Chiken", "Vegetable"};
//    String dish_type[] = {"Punjabi", "Maxican", "Punjabi", "Punjabi", "Non Veg", "Punjabi", "Non Veg", "Veg"};
//    double price[] = {250, 150, 200, 220, 350, 200, 100, 500};

    private ArrayList<CategoryObject> listCategoryObject;
    //    private TextView tvSeeMoreRestaurants;
    private RecyclerView rvVegetableCategories;
    private RecycleAdapterCategory adapterRestaurant;
//    Integer image2[] = {R.mipmap.temp_img1, R.mipmap.temp_img2, R.mipmap.temp_img3,
//            R.mipmap.temp_img4, R.mipmap.temp_img5, R.mipmap.temp_img6, R.mipmap.temp_img7};

    private HashMap<String, String> mapBannerDetails;

    private ViewPager viewPagerFooter;
    private PagerAdapterBanner pagerAdapterForFooter;
    private ArrayList<String> listFooterURL;

    TriggerTabChangeListener triggerTabChangeListener;

    private final int REQUEST_CODE_LOCATION = 99;
    private final int REQUEST_CODE_SEE_MORE_DISH = 100;
    private final int REQUEST_CODE_SEE_MORE_CUISINE = 101;
    private final int REQUEST_CODE_SEE_MORE_RESTAURANT = 102;
    private final int REQUEST_CODE_PRODUCT_LIST = 103;
    private final int REQUEST_CODE_PRODUCT_DETAILS = 104;

    int userID;
    int restaurantID;
    int zipCode;
//    double referralPoints;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        triggerTabChangeListener = (TriggerTabChangeListener) context;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurantID = Application.clientObject.getRestaurantID();
        userID = Application.userDetails.getUserID();

        if (Application.locationAddressData != null) {
            zipCode = Integer.parseInt(Application.locationAddressData.getAddressList().get(0).getPostalCode());
        }
//        zipCode = Application.userDetails.getZipCode();
//        referralPoints = Application.userDetails.getTotalReferralPoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents();
        componentEvents();
        setToolbarDetails();
//        setupViewPagerFooter();

//        setupRecyclerViewPopularItems();
//        setupSlidingImages();
//        setupRecyclerViewCategory();

//        getUserLikeTopItems();

        getSliderDetails();
        getRestaurantData();
        getTopPopularItems();
        getCategoryData();
        getSlidingFooter();

        return rootView;
    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();
        viewToolbarLocation = rootView.findViewById(R.id.view_toolbarLocation);

        llToolbarLocation = viewToolbarLocation.findViewById(R.id.ll_toolbarLocation);
        llToolbarAddress = viewToolbarLocation.findViewById(R.id.ll_toolbarAddress);
        tvToolbarTitle = viewToolbarLocation.findViewById(R.id.tv_toolbarTitle);
//        llToolbarReferralPoints = viewToolbarLocation.findViewById(R.id.ll_toolbarReferralPoints);
//        tvReferralPoints = viewToolbarLocation.findViewById(R.id.tv_referralPoints);

        imageSliderLayout = (SliderLayout) rootView.findViewById(R.id.slider);

        rvPopular = rootView.findViewById(R.id.recyclerView_popular);
        rvVegetableCategories = rootView.findViewById(R.id.recyclerView_vegetableCategories);
        viewPagerFooter = rootView.findViewById(R.id.viewPagerFooter);

//        tvSeeMoreDish = (TextView) rootView.findViewById(R.id.tv_seeMoreDish);
//        tvSeeMoreCuisines = (TextView) rootView.findViewById(R.id.tv_seeMoreCuisine);
//        tvSeeMoreRestaurants = (TextView) rootView.findViewById(R.id.tv_seeMoreRestaurant);
    }

    private void componentEvents() {
//        llToolbarReferralPoints.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(getActivity(), RewardCreditsActivity.class);
////                startActivity(intent);
//            }
//        });

        llToolbarAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationGoogleMapActivity.class);
                intent.putExtra("CalledFrom", ConstantValues.ACTIVITY_ACTION_HOME);
                startActivityForResult(intent, REQUEST_CODE_LOCATION);

//                Intent intent = new VanillaPlacePicker.Builder(getActivity())
//                        .with(PickerType.MAP_WITH_AUTO_COMPLETE) // Select Picker type to enable autocompelte, map or both
//                        .withLocation(23.057582, 72.534458)
//                        .setPickerLanguage(PickerLanguage.HINDI) // Apply language to picker
//                        .setLocationRestriction(new LatLng(23.0558088,72.5325067), new LatLng(23.0587592,72.5357321)) // Restrict location bounds in map and autocomplete
//                        .setCountry("IN") // Only for Autocomplete
//
//                        /*
//                         * Configuration for Map UI
//                         */
//                        .setMapType(MapType.SATELLITE) // Choose map type (Only applicable for map screen)
//                        .setMapStyle(R.raw.style_json) // Containing the JSON style declaration for night-mode styling
//                        .setMapPinDrawable(android.R.drawable.ic_menu_mylocation) // To give custom pin image for map marker
//
//            .build();
//
//                startActivityForResult(intent, 50);
            }
        });
    }

    private void setupSlidingImages() {
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

        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.putAll(mapBannerDetails);

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            imageSliderLayout.addSlider(textSliderView);
        }
        imageSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSliderLayout.setCustomIndicator((PagerIndicator) rootView.findViewById(R.id.custom_indicator));
        imageSliderLayout.setCustomAnimation(new DescriptionAnimation());
        imageSliderLayout.setDuration(4000);
        imageSliderLayout.addOnPageChangeListener(this);
    }

    private void setupRecyclerViewPopularItems() {
//        getPopularItemsData();

        adapterDish = new RecycleAdapterPopularItem(getActivity(), listProductObject);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rvPopular.setLayoutManager(gridLayoutManager);
        rvPopular.setItemAnimator(new DefaultItemAnimator());
        rvPopular.setAdapter(adapterDish);
        adapterDish.setClickListener(this);
    }

//    private void getPopularItemsData() {
//        listProductObject = new ArrayList<>();
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
//        Integer strawberry[] = {R.mipmap.temp_strawberry_1, R.mipmap.temp_strawberry_2, R.mipmap.temp_strawberry_3};
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
//        ProductObject productObject9 = new ProductObject();
//        productObject9.setProductName("Strawberry");
//        productObject9.setProductImage(strawberry);
//        productObject9.setCategoryName("Fruit");
//        productObject9.setPrice(250);
//
//        listProductObject.add(productObject);
//        listProductObject.add(productObject1);
//        listProductObject.add(productObject2);
//        listProductObject.add(productObject3);
//        listProductObject.add(productObject4);
//        listProductObject.add(productObject5);
//        listProductObject.add(productObject6);
//        listProductObject.add(productObject7);
//        listProductObject.add(productObject8);
//        listProductObject.add(productObject9);
//
////        for (int i = 0; i < image.length; i++) {
//////            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
////            ProductObject productObject = new ProductObject();
////            productObject.setProductName(dish_name[i]);
////            productObject.setProductImage(String.valueOf(image[i]));
////            productObject.setCategoryName(dish_type[i]);
////            productObject.setPrice(price[i]);
////            listProductObject.add(productObject);
////        }
//    }

    private void setupRecyclerViewCategory() {
//        getCategoryDummyData();

        adapterRestaurant = new RecycleAdapterCategory(getActivity(), listCategoryObject);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvVegetableCategories.setLayoutManager(gridLayoutManager);
        rvVegetableCategories.setItemAnimator(new DefaultItemAnimator());
        rvVegetableCategories.setAdapter(adapterRestaurant);
        adapterRestaurant.setClickListener(this);
    }

    private void getCategoryDummyData() {
        listCategoryObject = new ArrayList<>();

        CategoryObject categoryObject1 = new CategoryObject();
        categoryObject1.setCategoryName("Vegetables");
        categoryObject1.setCategoryImage(String.valueOf(R.mipmap.temp_cabbage_1));

        CategoryObject categoryObject2 = new CategoryObject();
        categoryObject2.setCategoryName("Fresh Fruits");
        categoryObject2.setCategoryImage(String.valueOf(R.mipmap.temp_capsicum_1));

        CategoryObject categoryObject3 = new CategoryObject();
        categoryObject3.setCategoryName("Organic");
        categoryObject3.setCategoryImage(String.valueOf(R.mipmap.temp_carrots_1));

        CategoryObject categoryObject4 = new CategoryObject();
        categoryObject4.setCategoryName("Herbs & Seasoning");
        categoryObject4.setCategoryImage(String.valueOf(R.mipmap.temp_chilli_1));

        CategoryObject categoryObject5 = new CategoryObject();
        categoryObject5.setCategoryName("Exotic");
        categoryObject5.setCategoryImage(String.valueOf(R.mipmap.temp_lemon_1));

        CategoryObject categoryObject6 = new CategoryObject();
        categoryObject6.setCategoryName("Seasonal");
        categoryObject6.setCategoryImage(String.valueOf(R.mipmap.temp_melons_1));

        CategoryObject categoryObject7 = new CategoryObject();
        categoryObject7.setCategoryName("Flowers");
        categoryObject7.setCategoryImage(String.valueOf(R.mipmap.temp_potato_1));

        CategoryObject categoryObject8 = new CategoryObject();
        categoryObject8.setCategoryName("Cut Fruits");
        categoryObject8.setCategoryImage(String.valueOf(R.mipmap.temp_tomato_1));

        CategoryObject categoryObject9 = new CategoryObject();
        categoryObject9.setCategoryName("Organic Fruits");
        categoryObject9.setCategoryImage(String.valueOf(R.mipmap.temp_strawberry_1));

        CategoryObject categoryObject10 = new CategoryObject();
        categoryObject10.setCategoryName("Cuts & Sprouts");
        categoryObject10.setCategoryImage(String.valueOf(R.mipmap.temp_vegetable));

        listCategoryObject.add(categoryObject1);
        listCategoryObject.add(categoryObject2);
        listCategoryObject.add(categoryObject3);
        listCategoryObject.add(categoryObject4);
        listCategoryObject.add(categoryObject5);
        listCategoryObject.add(categoryObject6);
        listCategoryObject.add(categoryObject7);
        listCategoryObject.add(categoryObject8);
        listCategoryObject.add(categoryObject9);
        listCategoryObject.add(categoryObject10);


//        for (int i = 0; i < image.length; i++) {
//            CategoryObject cateogryObject = new CategoryObject();
//            cateogryObject.setCategoryName("Category " + 1);
//            cateogryObject.setCategoryImage(String.valueOf(image[i]));
//            listCategoryObject.add(cateogryObject);
//        }
    }


    private void setupViewPagerFooter(ArrayList<Fragment> fragments, ArrayList<String> listFooterURL) {
        viewPagerFooter.setVisibility(View.VISIBLE);

        pagerAdapterForFooter = new PagerAdapterBanner(getActivity(), getFragmentManager(), fragments, listFooterURL);
        viewPagerFooter.setAdapter(pagerAdapterForFooter);
    }

    private void setToolbarDetails() {
        if (Application.locationAddressData != null) {
            tvToolbarTitle.setText(Application.locationAddressData.getAddressList().get(0).getSubLocality());
        } else {
            tvToolbarTitle.setText(getString(R.string.set_location));
        }

//        if (referralPoints > 0) {
////            double referralPoints = Application.userDetails.getTotalReferralPoints();
//            String formattedPoints = getFormattedNumberDouble(referralPoints)
//                    .concat(" ")
//                    .concat(getString(R.string.rupees));
//            tvReferralPoints.setText(formattedPoints);
//
//        } else {
//            tvReferralPoints.setText("0"
//                    .concat(" ")
//                    .concat(getString(R.string.rupees)));
//        }
    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    private void getRestaurantData() {
        if (InternetConnection.checkConnection(getActivity())) {
//            showDialog();
            String strZipCode = String.valueOf(zipCode);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getRestaurantDetails("416004");
            Call<ResponseBody> call = apiService.getCompanyDetails(strZipCode);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                int categoryID = jsonObj.optInt("CategoryId");
                                String categoryName = jsonObj.optString("CategoryName");
                                int restaurantID = jsonObj.optInt("ClientId");
                                String restaurantName = jsonObj.optString("RestaurantName");
                                String restaurantAddress = jsonObj.optString("ClientAddress");
                                String openTime = jsonObj.optString("OpentTime");
                                String closeTime = jsonObj.optString("CloseTime");
                                String contact = jsonObj.optString("Contact");
                                String description = jsonObj.optString("Description");
                                String longitude = jsonObj.optString("Langitude");
                                String latitude = jsonObj.optString("Latitude");
                                String rating = jsonObj.optString("Rating", "4.5");
                                int foodTypeID = jsonObj.optInt("FoodTypeId");
                                String foodTypeName = jsonObj.optString("FoodTypeName");
                                String logo = jsonObj.optString("Logo");
                                String taxID = jsonObj.optString("TaxId");
                                boolean taxable = Boolean.parseBoolean(jsonObj.optString("Taxable"));
                                boolean includeTax = Boolean.parseBoolean(jsonObj.optString("IncludeTax"));

                                ClientObject clientObject = new ClientObject();
                                clientObject.setCategoryID(categoryID);
                                clientObject.setCategoryName(categoryName);
                                clientObject.setRestaurantID(restaurantID);
                                clientObject.setRestaurantName(restaurantName);
                                clientObject.setRestaurantAddress(restaurantAddress);
                                clientObject.setOpenTime(openTime);
                                clientObject.setCloseTime(closeTime);
                                clientObject.setContact(contact);
                                clientObject.setDescription(description);
                                clientObject.setLongitude(longitude);
                                clientObject.setLatitude(latitude);
                                clientObject.setRating(rating);
                                clientObject.setFoodTypeID(foodTypeID);
                                clientObject.setFoodTypeName(foodTypeName);
                                clientObject.setLogo(logo);
                                clientObject.setTaxID(taxID);
                                clientObject.setTaxable(taxable);
                                clientObject.setIncludeTax(includeTax);

                                Application.clientObject = clientObject;
//                                listCategoryObject.add(categoryObject);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        getSliderDetails();
//                        setupRecyclerViewRestaurant();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    dismissDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
//                        dismissDialog();
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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getRestaurantData();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void getSliderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {
            int clientID = Application.clientObject.getRestaurantID();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getSlidingPhotoDetails(clientID, ConstantValues.SLIDER_BANNER);   // 0 for sliding photos
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            mapBannerDetails = new HashMap<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                String photoURL = jsonObj.optString("PhotoData");
                                String title = jsonObj.optString("Text");

//                                BannerDetailsObject bannerDetails = new BannerDetailsObject();
//                                bannerDetails.setPhotoURL(photoURL);
//                                bannerDetails.setTitle(title);

                                mapBannerDetails.put(title, photoURL);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        setupSlidingImages();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSliderDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void getSlidingFooter() {
        if (InternetConnection.checkConnection(getActivity())) {
            int clientID = Application.clientObject.getRestaurantID();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getSlidingPhotoDetails(clientID, ConstantValues.SLIDER_FOOTER);   // 0 for sliding photos
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ArrayList<String> listFooterURL = new ArrayList<>();
                    ArrayList<Fragment> fragments = new ArrayList<>();

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseString);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                String photoURL = jsonObj.optString("PhotoData");
//                                String title = jsonObj.optString("Text");

                                Bundle b = new Bundle();
                                b.putString("ImageURL", photoURL);
                                fragments.add(Fragment.instantiate(getActivity(), FragmentFooter.class.getName(), b));

                                listFooterURL.add(photoURL);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        if ( listFooterURL.size() != 0 && fragments.size() != 0) {
                            setupViewPagerFooter(fragments, listFooterURL);

                        } else {
                            viewPagerFooter.setVisibility(View.GONE);
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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSlidingFooter();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    private void getTopPopularItems() {
        if (InternetConnection.checkConnection(getActivity())) {
//            showDialog();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getTopProducts();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listProductObject = new ArrayList<>();

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

                                listProductObject.add(productObject);
                            }

                            setupRecyclerViewPopularItems();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

//                        setupRecyclerViewRestaurant();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    dismissDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
//                        dismissDialog();
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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getTopPopularItems();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void getCategoryData() {
        if (InternetConnection.checkConnection(getActivity())) {
            showDialog();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getCategory();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listCategoryObject = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                int categoryID = jsonObj.optInt("CategoryId");
                                String categoryName = jsonObj.optString("CategoryName");
                                String categoryImage = jsonObj.optString("PhotoPath");

                                CategoryObject cateogryObject = new CategoryObject();
                                cateogryObject.setCategoryID(categoryID);
                                cateogryObject.setCategoryName(categoryName);
                                cateogryObject.setCategoryImage(categoryImage);

                                listCategoryObject.add(cateogryObject);
                            }

                            setupRecyclerViewCategory();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

//                        setupRecyclerViewRestaurant();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getRestaurantData();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


//    private void getUserLikeTopItems() {
//        if (InternetConnection.checkConnection(getActivity())) {
//
//            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getUserLikeTopItems();
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//
//                        if (response.isSuccessful()) {
//                            String responseString = response.body().string();
//                            mapBannerDetails = new HashMap<>();
//
//                            JSONArray jsonArray = new JSONArray(responseString);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                                String photoURL = jsonObj.optString("PhotoData");
//                                String title = jsonObj.optString("Text");
//
//                                for (int ii = 0; i < image.length; i++) {
////            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
//                                    ProductObject productObject = new ProductObject();
//                                    productObject.setProductName(dish_name[i]);
//                                    productObject.setProductImage(String.valueOf(image[i]));
//                                    productObject.setCategoryName(dish_type[i]);
//                                    listProductObject.add(productObject);
//                                }
//
//                                mapBannerDetails.put(title, photoURL);
//                            }
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//                        setupRecyclerPopular();
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
//            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getSliderDetails();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

    private String getFormattedNumber(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    public void showDialog() {
        progressIndicator.showProgress(getActivity());
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }

//    @Override
//    public void onCuisineClick(View view, int position) {
//        if (listCategoryObject.size() > 0) {
//            CategoryObject categoryObject = listCategoryObject.get(0);
//            Intent intent = new Intent(getActivity(), ProductListActivity.class);
//            intent.putExtra("ClientObject", categoryObject);
//            startActivityForResult(intent, REQUEST_CODE_PRODUCT_LIST);
//        }
//    }

    @Override
    public void onPopularItemClicked(View view, int position) {
        ProductObject productObject = listProductObject.get(position);
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra("ProductObject", productObject);
        startActivityForResult(intent, REQUEST_CODE_PRODUCT_DETAILS);
    }

    @Override
    public void onClick(View view, int position) {
        CategoryObject categoryObject = listCategoryObject.get(position);
        Application.categoryObject = categoryObject;

        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra("CategoryObject", categoryObject);
        startActivityForResult(intent, REQUEST_CODE_PRODUCT_LIST);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        imageSliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == Activity.RESULT_OK && data != null) {

            }

        } else if (requestCode == REQUEST_CODE_SEE_MORE_CUISINE) {
            if (resultCode == Activity.RESULT_OK && data != null) {

            }
        } else if (requestCode == REQUEST_CODE_PRODUCT_LIST || requestCode == REQUEST_CODE_PRODUCT_DETAILS) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                String flag = data.getExtras().getString("MESSAGE");
                if (flag.equalsIgnoreCase("VIEW_CART")) {
                    triggerTabChangeListener.setTab(2);
//                    triggerTabChangeListener.setTab(2);

                } else if (flag.equalsIgnoreCase("UPDATE_CART_COUNT")) {
                    int noOfItems = data.getExtras().getInt("CART_ITEM_COUNT");
                    triggerTabChangeListener.setBadgeCount(noOfItems);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
