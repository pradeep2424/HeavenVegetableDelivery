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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.activity.LocationGoogleMapActivity;
import com.heaven.vegetable.activity.RestaurantDetailsActivity;
import com.heaven.vegetable.adapter.PagerAdapterBanner;
import com.heaven.vegetable.adapter.RecycleAdapterPopularItem;
import com.heaven.vegetable.adapter.RecycleAdapterCategory;
import com.heaven.vegetable.listeners.OnCuisineClickListener;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.listeners.OnUserMayLikedClickListener;
import com.heaven.vegetable.listeners.TriggerTabChangeListener;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.BannerDetailsObject;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.model.CateogryObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.ConstantValues;
import com.heaven.vegetable.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements OnRecyclerViewClickListener,
        OnUserMayLikedClickListener, OnCuisineClickListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private DialogLoadingIndicator progressIndicator;
    View rootView;

    View viewToolbarLocation;
    LinearLayout llToolbarLocation;
    LinearLayout llToolbarAddress;
    LinearLayout llToolbarReferralPoints;
    TextView tvToolbarTitle;
    TextView tvReferralPoints;

    private SliderLayout imageSliderLayout;

    private ArrayList<ProductObject> listProductObject;
    private RecyclerView rvPopular;
    private RecycleAdapterPopularItem adapterDish;
    Integer image[] = {R.mipmap.temp_paneer, R.mipmap.temp_order, R.mipmap.temp_ice_cream, R.mipmap.temp_paneer,
            R.mipmap.temp_order, R.mipmap.temp_ice_cream, R.mipmap.temp_paneer, R.mipmap.temp_order};
    String dish_name[] = {"Paratha", "Cheese", "Paneer", "Paneer", "Chiken", "Paneer Kopta", "Chiken", "Vegetable"};
    String dish_type[] = {"Punjabi", "Maxican", "Punjabi", "Punjabi", "Non Veg", "Punjabi", "Non Veg", "Veg"};
    double price[] = {250, 150, 200, 220, 350, 200, 100, 500};

    private ArrayList<CateogryObject> listRestaurantObject;
    //    private TextView tvSeeMoreRestaurants;
    private RecyclerView rvVegetableCategories;
    private RecycleAdapterCategory adapterRestaurant;
//    Integer image2[] = {R.mipmap.temp_img1, R.mipmap.temp_img2, R.mipmap.temp_img3,
//            R.mipmap.temp_img4, R.mipmap.temp_img5, R.mipmap.temp_img6, R.mipmap.temp_img7};

    private ViewPager viewPager;
    private ArrayList<BannerDetailsObject> listBannerDetails;
    private HashMap<String, String> mapBannerDetails;
    private PagerAdapterBanner pagerAdapterForBanner;

    TriggerTabChangeListener triggerTabChangeListener;

    private final int REQUEST_CODE_LOCATION = 99;
    private final int REQUEST_CODE_SEE_MORE_DISH = 100;
    private final int REQUEST_CODE_SEE_MORE_CUISINE = 101;
    private final int REQUEST_CODE_SEE_MORE_RESTAURANT = 102;
    private final int REQUEST_CODE_RESTAURANT_DETAILS = 103;

    int userID;
    int restaurantID;
    int zipCode;
    double referralPoints;

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

        restaurantID = Application.restaurantObject.getRestaurantID();
        userID = Application.userDetails.getUserID();
        zipCode = Application.userDetails.getZipCode();
        referralPoints = Application.userDetails.getTotalReferralPoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents();
        componentEvents();
        setToolbarDetails();
        setupViewPagerBanner();

        setupRecyclerPopular();
        setupSlidingImages();
        setupRecyclerViewRestaurant();

//        getSliderDetails();
//        getUserLikeTopItems();
//        getRestaurantData();

        return rootView;
    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();
        viewToolbarLocation = rootView.findViewById(R.id.view_toolbarLocation);

        llToolbarLocation = viewToolbarLocation.findViewById(R.id.ll_toolbarLocation);
        llToolbarAddress = viewToolbarLocation.findViewById(R.id.ll_toolbarAddress);
        llToolbarReferralPoints = viewToolbarLocation.findViewById(R.id.ll_toolbarReferralPoints);
        tvToolbarTitle = viewToolbarLocation.findViewById(R.id.tv_toolbarTitle);
        tvReferralPoints = viewToolbarLocation.findViewById(R.id.tv_referralPoints);

        imageSliderLayout = (SliderLayout) rootView.findViewById(R.id.slider);

        rvPopular = rootView.findViewById(R.id.recyclerView_popular);
        rvVegetableCategories = rootView.findViewById(R.id.recyclerView_vegetableCategories);
        viewPager = rootView.findViewById(R.id.viewpager);

//        tvSeeMoreDish = (TextView) rootView.findViewById(R.id.tv_seeMoreDish);
//        tvSeeMoreCuisines = (TextView) rootView.findViewById(R.id.tv_seeMoreCuisine);
//        tvSeeMoreRestaurants = (TextView) rootView.findViewById(R.id.tv_seeMoreRestaurant);
    }

    private void componentEvents() {
        llToolbarReferralPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), RewardCreditsActivity.class);
//                startActivity(intent);
            }
        });

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
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Best Offer", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

//        HashMap<String, Integer> url_maps = new HashMap<String, Integer>();
//        url_maps.put("50% off on Lunch", R.mipmap.temp_img1);
//        url_maps.put("Free delivery above 250", R.mipmap.temp_img2);
//        url_maps.put("House of Cards", R.mipmap.temp_img3);
//        url_maps.put("Game of Thrones", R.mipmap.temp_img4);

//        HashMap<String, String> url_maps = new HashMap<String, String>();
//        url_maps.putAll(mapBannerDetails);

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
        imageSliderLayout.setCustomAnimation(new DescriptionAnimation());
        imageSliderLayout.setDuration(4000);
        imageSliderLayout.addOnPageChangeListener(this);
    }

    private void setupRecyclerPopular() {
        getPopularItemsData();

        adapterDish = new RecycleAdapterPopularItem(getActivity(), listProductObject);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rvPopular.setLayoutManager(gridLayoutManager);
        rvPopular.setItemAnimator(new DefaultItemAnimator());
        rvPopular.setAdapter(adapterDish);
        adapterDish.setClickListener(this);
    }

    private void getPopularItemsData() {
        listProductObject = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
//            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
            ProductObject productObject = new ProductObject();
            productObject.setProductName(dish_name[i]);
            productObject.setProductImage(String.valueOf(image[i]));
            productObject.setCategoryName(dish_type[i]);
            productObject.setPrice(price[i]);
            listProductObject.add(productObject);
        }
    }

    private void setupRecyclerViewRestaurant() {
        getCategoryData();

        adapterRestaurant = new RecycleAdapterCategory(getActivity(), listRestaurantObject);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvVegetableCategories.setLayoutManager(gridLayoutManager);
        rvVegetableCategories.setItemAnimator(new DefaultItemAnimator());
        rvVegetableCategories.setAdapter(adapterRestaurant);
        adapterRestaurant.setClickListener(this);
    }

    private void getCategoryData() {
        listRestaurantObject = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            CateogryObject cateogryObject = new CateogryObject();
            cateogryObject.setCategoryName("Category " + 1);
            cateogryObject.setCategoryImage(String.valueOf(image[i]));
            listRestaurantObject.add(cateogryObject);
        }
    }


    private void setupViewPagerBanner() {
        pagerAdapterForBanner = new PagerAdapterBanner(getFragmentManager());
        viewPager.setAdapter(pagerAdapterForBanner);
    }

    private void setToolbarDetails() {
        if (Application.locationAddressData != null) {
            tvToolbarTitle.setText(Application.locationAddressData.getAddressList().get(0).getSubLocality());
        } else {
            tvToolbarTitle.setText(getString(R.string.set_location));
        }

        if (referralPoints > 0) {
//            double referralPoints = Application.userDetails.getTotalReferralPoints();
            String formattedPoints = getFormattedNumberDouble(referralPoints)
                    .concat(" ")
                    .concat(getString(R.string.rupees));
            tvReferralPoints.setText(formattedPoints);

        } else {
            tvReferralPoints.setText("0"
                    .concat(" ")
                    .concat(getString(R.string.rupees)));
        }
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
            showDialog();

            String strZipCode = String.valueOf(zipCode);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getRestaurantDetails("416004");
            Call<ResponseBody> call = apiService.getRestaurantDetails(strZipCode);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listRestaurantObject = new ArrayList<>();

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

                                CateogryObject restaurantObject = new CateogryObject();
                                restaurantObject.setCategoryID(categoryID);
                                restaurantObject.setCategoryName(categoryName);
                                restaurantObject.setRestaurantID(restaurantID);
                                restaurantObject.setRestaurantName(restaurantName);
                                restaurantObject.setRestaurantAddress(restaurantAddress);
                                restaurantObject.setOpenTime(openTime);
                                restaurantObject.setCloseTime(closeTime);
                                restaurantObject.setContact(contact);
                                restaurantObject.setDescription(description);
                                restaurantObject.setLongitude(longitude);
                                restaurantObject.setLatitude(latitude);
                                restaurantObject.setRating(rating);
                                restaurantObject.setFoodTypeID(foodTypeID);
                                restaurantObject.setFoodTypeName(foodTypeName);
                                restaurantObject.setLogo(logo);
                                restaurantObject.setTaxID(taxID);
                                restaurantObject.setTaxable(taxable);
                                restaurantObject.setIncludeTax(includeTax);

                                listRestaurantObject.add(restaurantObject);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        setupRecyclerViewRestaurant();

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

    private void getSliderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getSlidingPhotoDetails(0, ConstantValues.SLIDER_BANNER);   // 0 for sliding photos
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

//                        setupSlidingImages();

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

    private void getUserLikeTopItems() {
        if (InternetConnection.checkConnection(getActivity())) {

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getUserLikeTopItems();
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

                                for (int ii = 0; i < image.length; i++) {
//            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
                                    ProductObject productObject = new ProductObject();
                                    productObject.setProductName(dish_name[i]);
                                    productObject.setProductImage(String.valueOf(image[i]));
                                    productObject.setCategoryName(dish_type[i]);
                                    listProductObject.add(productObject);
                                }

                                mapBannerDetails.put(title, photoURL);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        setupRecyclerPopular();

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

    @Override
    public void onCuisineClick(View view, int position) {
        if (listRestaurantObject.size() > 0) {
            CateogryObject restaurantObject = listRestaurantObject.get(0);
            Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
            intent.putExtra("RestaurantObject", restaurantObject);
            startActivityForResult(intent, REQUEST_CODE_RESTAURANT_DETAILS);
        }
    }

    @Override
    public void onUserMayLikedClick(View view, int position) {
        if (listRestaurantObject.size() > 0) {
            CateogryObject restaurantObject = listRestaurantObject.get(0);
            Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
            intent.putExtra("RestaurantObject", restaurantObject);
            startActivityForResult(intent, REQUEST_CODE_RESTAURANT_DETAILS);
        }
    }


    @Override
    public void onClick(View view, int position) {
        CateogryObject restaurantObject = listRestaurantObject.get(position);
        Application.restaurantObject = restaurantObject;

        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtra("RestaurantObject", restaurantObject);
        startActivityForResult(intent, REQUEST_CODE_RESTAURANT_DETAILS);
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
        } else if (requestCode == REQUEST_CODE_RESTAURANT_DETAILS) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                String flag = data.getExtras().getString("MESSAGE");
                if (flag.equalsIgnoreCase("VIEW_CART")) {
                    triggerTabChangeListener.setTab(1);
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
