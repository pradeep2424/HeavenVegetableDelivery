package com.heaven.vegetable.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.RecycleAdapterPastOrders;
import com.heaven.vegetable.listeners.OnPastOrderOptionsClickListener;
import com.heaven.vegetable.listeners.TriggerTabChangeListener;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.CartObject;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.model.OrderDetailsObject;
import com.heaven.vegetable.model.CateogryObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.heaven.vegetable.utils.Utils;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastOrdersFragment extends Fragment implements OnPastOrderOptionsClickListener {
    DialogLoadingIndicator progressIndicator;
    View rootView;

    View viewEmptyPastOrders;
    RelativeLayout rlPastOrdersLayout;
    LinearLayout llBrowseMenu;


    TriggerTabChangeListener triggerTabChangeListener;

//    String[] restaurantName  = {"Cocobolo Poolside Bar + Grill","Palm Beach Seafood Restaurant","Shin Minori Japanese Restaurant","Shin Minori Japanese Restaurant"};
//    String[] orderAddress = {"Chembur","Ghatkopar","Thane","Sion"};
//    String[] restaurantReviews ={"238 reviews","200 reviews","556 reviews","240 reviews"};
//    String[] orderDate = {"25 Nov 2017 10 : 30 AM","27 Nov 2017 10 : 30 AM","28 Nov 2017 10 : 30 AM","29 Nov 2017 10 : 30 AM"};
//    double[] orderPrice={199.00, 249.00, 149.00, 399.00};
//    Integer[] foodImage = {R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order};

    private RecyclerView rvPastOrders;
    private RecycleAdapterPastOrders adapterPastOrders;
    private ArrayList<OrderDetailsObject> listAllOrders;
    ArrayList<OrderDetailsObject> listFormattedPastOrders;

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
        rootView = inflater.inflate(R.layout.fragment_past_orders, container, false);

        initComponents();
        componentEvents();
//        setupRecyclerViewPastOrders();

        getPastOrderDetails();

        return rootView;
    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();
        rlPastOrdersLayout = rootView.findViewById(R.id.rl_pastOrdersLayout);
        viewEmptyPastOrders = rootView.findViewById(R.id.view_emptyPastOrders);
        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);

        rvPastOrders = rootView.findViewById(R.id.recyclerView_pastOrders);
    }

    private void componentEvents() {
        llBrowseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerTabChangeListener.setTab(0);
            }
        });

    }

    private void setupRecyclerViewPastOrders() {
//        getTESTPastOrdersData();
        listFormattedPastOrders = new ArrayList<>();
        listFormattedPastOrders = formatPastOrderData();

        Collections.sort(listFormattedPastOrders, new Comparator<OrderDetailsObject>() {
            public int compare(OrderDetailsObject o1, OrderDetailsObject o2) {
                if (o1.getOrderDate() == null || o2.getOrderDate() == null)
                    return 0;
                return o2.getOrderDate().compareTo(o1.getOrderDate());
            }
        });

        adapterPastOrders = new RecycleAdapterPastOrders(getActivity(), listFormattedPastOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPastOrders.setLayoutManager(layoutManager);
        rvPastOrders.setItemAnimator(new DefaultItemAnimator());
        rvPastOrders.setAdapter(adapterPastOrders);

        adapterPastOrders.setClickListener(this);
    }

//    private void getTESTPastOrdersData() {
//        listPastOrders = new ArrayList<>();
//
//        for (int i=0; i<restaurantName.length; i++){
////            OrderDetailsObject food7_model = new OrderDetailsObject(restaurantName[i], orderAddress[i],
////                    restaurantReviews[i], orderDate[i], orderPrice[i], foodImage[i]);
////            listPastOrders.add(food7_model);
//
//            OrderDetailsObject orderDetailsObject = new OrderDetailsObject();
//            orderDetailsObject.setRestaurantName(restaurantName[i]);
//            orderDetailsObject.setUserAddress(orderAddress[i]);
////            orderDetailsObject.setRestaurantReviews(restaurantReviews[i]);
//            orderDetailsObject.setOrderDate(orderDate[i]);
//            orderDetailsObject.setTotalAmount(orderPrice[i]);
////            orderDetailsObject.setDishImage(String.valueOf(foodImage[i]));
//
//            listPastOrders.add(orderDetailsObject);
//        }
//    }

//    private String convertJsonDate(String jsonDate) {
//        String dateTime = "";
//        try {
//            String timeString = jsonDate.substring(jsonDate.indexOf("(") + 1, jsonDate.indexOf(")"));
//            String[] timeSegments = timeString.split("\\+");
//            // May have to handle negative timezones
//            int timeZoneOffSet = Integer.valueOf(timeSegments[1]) * 36000; // (("0100" / 100) * 3600 * 1000)
//            long millis = Long.valueOf(timeSegments[0]);
//            Date date = new Date(millis + timeZoneOffSet);
//
//            //It Will Be in format 29-OCT-2014 2:26 PM
//            dateTime = new SimpleDateFormat("dd-MMM-yyyy h:mm tt").format(date);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return dateTime;
//    }

    private void showEmptyPastOrdersLayout() {
        viewEmptyPastOrders.setVisibility(View.VISIBLE);
        rlPastOrdersLayout.setVisibility(View.GONE);
    }

    private void showPastOrdersList() {
        viewEmptyPastOrders.setVisibility(View.GONE);
        rlPastOrdersLayout.setVisibility(View.VISIBLE);
    }

    private ArrayList<OrderDetailsObject> formatPastOrderData() {
        ArrayList<OrderDetailsObject> listPastOrders = new ArrayList<>();

        ArrayList<OrderDetailsObject> tempAllOrders = SerializationUtils.clone(listAllOrders);

        for (int i = 0; i < listAllOrders.size(); i++) {
            int orderNo = listAllOrders.get(i).getOrderNumber();
            int orderNoInList = 0;

            ArrayList<ProductObject> listProducts = new ArrayList<>();
            for (int j = 0; j < tempAllOrders.size(); ) {
                orderNoInList = tempAllOrders.get(j).getOrderNumber();

                if (orderNo == orderNoInList) {
                    ProductObject productObject = new ProductObject();

                    productObject.setProductID(tempAllOrders.get(j).getProductID());
                    productObject.setProductName(tempAllOrders.get(j).getProductName());
                    productObject.setProductQuantity(tempAllOrders.get(j).getProductQuantity());
                    productObject.setPrice(tempAllOrders.get(j).getProductRate());
                    productObject.setCgst(tempAllOrders.get(j).getCgst());
                    productObject.setSgst(tempAllOrders.get(j).getSgst());
                    productObject.setTaxID(tempAllOrders.get(j).getTaxID());

                    listProducts.add(productObject);
                    tempAllOrders.remove(j);
                    j = 0;
                } else {
                    j++;
                }
            }

            if (listProducts != null && listProducts.size() > 0) {
                listAllOrders.get(i).setListProducts(listProducts);

                OrderDetailsObject orderDetailsObject = SerializationUtils.clone(listAllOrders.get(i));
                listPastOrders.add(orderDetailsObject);
            }
        }

        return listPastOrders;
    }

    @Override
    public void onClickReceipt(View view, int position) {

    }

    @Override
    public void onClickReorder(View view, int position) {
        OrderDetailsObject orderDetailsObject = listFormattedPastOrders.get(position);
        ArrayList<ProductObject> listProducts = orderDetailsObject.getListProducts();

//        adding restaurant details to application class
        CateogryObject restaurantObject = new CateogryObject();
        restaurantObject.setRestaurantID(orderDetailsObject.getClientID());
        restaurantObject.setRestaurantName(orderDetailsObject.getRestaurantName());
        restaurantObject.setIncludeTax(orderDetailsObject.getIsIncludeTax());
        restaurantObject.setTaxable(orderDetailsObject.getIsTaxApplicable());
        Application.restaurantObject = restaurantObject;

        String mobileNo = Application.userDetails.getMobile();
        if (mobileNo != null) {
            addItemToCart(listProducts);
        } else {
            addItemToLocal(listProducts);
        }
    }


    private void getPastOrderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {
            showDialog();

            int userTypeID = Application.userDetails.getUserID();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getPastOrders(userTypeID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listAllOrders = new ArrayList<>();
//
                            JSONArray jsonArray = new JSONArray(responseString);

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                                    double cgst = jsonObj.optDouble("CGST");
                                    int restaurantID = jsonObj.optInt("Clientid");
                                    String orderDate = jsonObj.optString("OrderDate");
                                    int orderID = jsonObj.optInt("OrderId");
                                    int orderMode = jsonObj.optInt("OrderMode");
                                    int orderNumber = jsonObj.optInt("OrderNumber");
                                    boolean orderPaid = jsonObj.optBoolean("OrderPaid");     // @@@@@@
                                    int orderStatus = jsonObj.optInt("OrderStatus");
                                    int orderType = jsonObj.optInt("OrderType");
                                    int paymentID = jsonObj.optInt("PaymentId");
                                    int dishID = jsonObj.optInt("ProductId");
                                    String dishName = jsonObj.optString("ProductName");
                                    int dishQuantity = jsonObj.optInt("ProductQnty");
                                    double dishRate = jsonObj.optDouble("ProductRate");
                                    String rejectReason = jsonObj.optString("RejectReason");
                                    String restaurantName = jsonObj.optString("RestaurantName");
                                    double sgst = jsonObj.optDouble("SGST");
                                    int taxID = jsonObj.optInt("TaxId");
                                    double taxableVal = jsonObj.optDouble("Taxableval");
                                    double totalAmount = jsonObj.optDouble("TotalAmount");
                                    String userAddress = jsonObj.optString("UserAddress");
                                    int userID = jsonObj.optInt("Userid");
                                    boolean isIncludeTax = jsonObj.getBoolean("IsIncludeTax");
                                    boolean isTaxApplicable = jsonObj.getBoolean("IsTaxApplicable");

                                    String convertedOrderDate = Utils.convertJsonDate(orderDate);

                                    OrderDetailsObject orderObj = new OrderDetailsObject();
                                    orderObj.setCgst(cgst);
                                    orderObj.setClientID(restaurantID);
                                    orderObj.setOrderDate(convertedOrderDate);
                                    orderObj.setOrderID(orderID);
                                    orderObj.setOrderMode(orderMode);
                                    orderObj.setOrderNumber(orderNumber);
                                    orderObj.setOrderStatus(orderStatus);
                                    orderObj.setOrderType(orderType);
                                    orderObj.setPaymentID(paymentID);
                                    orderObj.setProductID(dishID);
                                    orderObj.setProductName(dishName);
                                    orderObj.setProductQuantity(dishQuantity);
                                    orderObj.setProductRate(dishRate);
                                    orderObj.setRejectReason(rejectReason);
                                    orderObj.setRestaurantName(restaurantName);
                                    orderObj.setSgst(sgst);
                                    orderObj.setTaxID(taxID);
                                    orderObj.setTaxableVal(taxableVal);
                                    orderObj.setTotalAmount(totalAmount);
                                    orderObj.setUserAddress(userAddress);
                                    orderObj.setUserID(userID);
                                    orderObj.setIsIncludeTax(isIncludeTax);
                                    orderObj.setIsTaxApplicable(isTaxApplicable);

                                    listAllOrders.add(orderObj);
                                }

//                            getTESTPastOrdersData();
                                showPastOrdersList();
                                setupRecyclerViewPastOrders();

                            } else {
                                showEmptyPastOrdersLayout();
                            }


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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getPastOrderDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private JsonObject createJsonCart(ProductObject productObject) {
        CateogryObject restaurantObject = Application.restaurantObject;

        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("ProductId", productObject.getProductID());
            postParam.addProperty("ProductName", productObject.getProductName());
            postParam.addProperty("ProductRate", productObject.getPrice());
            postParam.addProperty("ProductAmount", productObject.getPrice());
            postParam.addProperty("ProductSize", "Regular");
            postParam.addProperty("cartId", 0);
            postParam.addProperty("ProductQnty", productObject.getProductQuantity());
            postParam.addProperty("Taxableval", productObject.getPrice());    // doubt
            postParam.addProperty("CGST", productObject.getCgst());
            postParam.addProperty("SGST", productObject.getSgst());
            postParam.addProperty("HotelName", restaurantObject.getRestaurantName());
            postParam.addProperty("IsIncludeTax", restaurantObject.getIncludeTax());
            postParam.addProperty("IsTaxApplicable", restaurantObject.getTaxable());
            postParam.addProperty("DeliveryCharge", 20);
            postParam.addProperty("Userid", Application.userDetails.getUserID());
            postParam.addProperty("Clientid", restaurantObject.getRestaurantID());
            postParam.addProperty("TotalAmount", productObject.getPrice());
            postParam.addProperty("TaxId", 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }

    public void addItemToCart(final ArrayList<ProductObject> listProducts) {
        if (InternetConnection.checkConnection(getActivity())) {
            showDialog();

            for (int i = 0; i < listProducts.size(); i++) {
                final int currentIndex = i;
                final ProductObject productObject = listProducts.get(i);

                ApiInterface apiService = RetroClient.getApiService(getActivity());
                Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(productObject));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            int statusCode = response.code();

                            if (response.isSuccessful()) {
                                String responseString = response.body().string();

                                successOnAddToCart(currentIndex, listProducts);

//                                if (currentIndex == listProducts.size() - 1) {
//                                    triggerTabChangeListener.setTab(1);
////                                    triggerTabChangeListener.setTab(2);
//                                }

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
                            dismissDialog();
                            showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                            Log.e("Error onFailure : ", t.toString());
                            t.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addItemToCart(listProducts);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void addItemToLocal(final ArrayList<ProductObject> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            final int currentIndex = i;
            ProductObject productObject = listProducts.get(i);

            CartObject cartObject = new CartObject();
            cartObject.setCgst(productObject.getCgst());
            cartObject.setRestaurantID(Application.restaurantObject.getRestaurantID());
            cartObject.setDeliveryCharge(30);
            cartObject.setRestaurantName(Application.restaurantObject.getRestaurantName());
            cartObject.setIsIncludeTax(Application.restaurantObject.getIncludeTax());
            cartObject.setIsTaxApplicable(Application.restaurantObject.getTaxable());
            cartObject.setProductAmount(productObject.getPrice());
            cartObject.setProductID(productObject.getProductID());
            cartObject.setProductName(productObject.getProductName());
            cartObject.setProductQuantity(productObject.getProductQuantity());
            cartObject.setProductRate(productObject.getPrice());
            cartObject.setProductSize("Regular");
            cartObject.setSgst(productObject.getSgst());
            cartObject.setTaxID(productObject.getTaxID());
            cartObject.setTaxableVal(productObject.getPrice());
            cartObject.setTotalAmount(productObject.getPrice());
            cartObject.setUserID(Application.userDetails.getUserID());
            cartObject.setCartID(Application.listCartItems.size());
            Application.listCartItems.add(cartObject);

            successOnAddToCart(currentIndex, listProducts);
        }
    }


    private void successOnAddToCart(int currentIndex, ArrayList<ProductObject> listProducts) {
        if (currentIndex == listProducts.size() - 1) {
            triggerTabChangeListener.setTab(1);
            dismissDialog();

//                                    triggerTabChangeListener.setTab(2);
        }
    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    public void showDialog() {
        progressIndicator.showProgress(getActivity());
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }
}

