package com.heaven.vegetable.bottomMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.activity.ProductDetailsActivity;
import com.heaven.vegetable.adapter.SearchAutoCompleteAdapter;
import com.heaven.vegetable.interfaces.OnRecyclerViewClickListener;
import com.heaven.vegetable.interfaces.TriggerTabChangeListener;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.InternetConnection;
import com.heaven.vegetable.utils.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements OnRecyclerViewClickListener {
    private View rootView;
    private View viewToolbar;
    private SearchView searchView;

    private RecyclerView rvSearchItems;
    private SearchAutoCompleteAdapter adapterSearch;
    private ArrayList<ProductObject> listProductObject = new ArrayList<>();

    TriggerTabChangeListener triggerTabChangeListener;

    private final int REQUEST_CODE_PRODUCT_DETAILS = 100;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        triggerTabChangeListener = (TriggerTabChangeListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        init();
        events();
        setupSearchProduct();
        return rootView;
    }

    private void init() {
        viewToolbar = rootView.findViewById(R.id.view_toolbar);
        searchView = viewToolbar.findViewById(R.id.searchView);
        rvSearchItems = rootView.findViewById(R.id.rv_productItems);
    }

    private void events() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    listProductObject.clear();
                    adapterSearch.notifyDataSetChanged();

                } else if (!TextUtils.isEmpty(query) && query.length() > 2) {
                    getSearchItems(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    listProductObject.clear();
                    adapterSearch.notifyDataSetChanged();

                } else if (!TextUtils.isEmpty(newText) && newText.length() > 2) {
                    getSearchItems(newText);
                }
                return true;
            }
        });
    }

    private void setupSearchProduct() {
        adapterSearch = new SearchAutoCompleteAdapter(getActivity(), listProductObject);
        rvSearchItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSearchItems.setAdapter(adapterSearch);

        rvSearchItems.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


        adapterSearch.setClickListener(this);
        adapterSearch.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        ProductObject productObject = listProductObject.get(position);
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra("ProductObject", productObject);
        startActivityForResult(intent, REQUEST_CODE_PRODUCT_DETAILS);
    }

    private void getSearchItems(final String searchText) {
        if (InternetConnection.checkConnection(getActivity())) {
//            showDialog();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getSearchItem(searchText);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            listProductObject.clear();

                            String responseString = response.body().string();

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

                            adapterSearch.notifyDataSetChanged();

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
                            getSearchItems(searchText);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == REQUEST_CODE_PRODUCT_DETAILS) {
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
