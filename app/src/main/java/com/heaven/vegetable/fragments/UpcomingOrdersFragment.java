package com.heaven.vegetable.fragments;

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
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.RecycleAdapterPastUpcomingOrders;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.OrderDetailsObject;
import com.heaven.vegetable.model.OrderStatusEnum;
import com.heaven.vegetable.model.OrderTimelineObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.heaven.vegetable.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpcomingOrdersFragment extends Fragment {
    DialogLoadingIndicator progressIndicator;
    View rootView;

    View viewEmptyUpcomingOrders;
    RelativeLayout rlUpcomingOrdersLayout;
    LinearLayout llBrowseMenu;

    private RecyclerView rvUpcomingOrders;
    private RecycleAdapterPastUpcomingOrders adapterUpcomingOrders;
    private ArrayList<OrderDetailsObject> listUpcomingOrders;
    private ArrayList<OrderTimelineObject> listOrderStatus = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_upcomming_orders, container, false);

        initComponents();
        componentEvents();
//        setupRecyclerViewUpcomingOrders();

        getDummyOrderStatus();
        getUpcomingOrderDetails();

        return rootView;
    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();

        rlUpcomingOrdersLayout = rootView.findViewById(R.id.rl_upcomingOrdersLayout);
        viewEmptyUpcomingOrders = rootView.findViewById(R.id.view_emptyUpcomingOrders);
        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);

        rvUpcomingOrders = rootView.findViewById(R.id.recyclerView_upcomingOrders);
    }

    private void componentEvents() {
    }

    private void showEmptyUpcomingOrdersLayout() {
        viewEmptyUpcomingOrders.setVisibility(View.VISIBLE);
        rlUpcomingOrdersLayout.setVisibility(View.GONE);
    }

    private void showUpcomingOrdersList() {
        viewEmptyUpcomingOrders.setVisibility(View.GONE);
        rlUpcomingOrdersLayout.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerViewUpcomingOrders() {
//        getTESTPastOrdersData();
//        listFormattedPastOrders = new ArrayList<>();
//        listFormattedPastOrders = formatPastOrderData();
//
//        Collections.sort(listFormattedPastOrders, new Comparator<OrderDetailsObject>() {
//            public int compare(OrderDetailsObject o1, OrderDetailsObject o2) {
//                if (o1.getOrderDate() == null || o2.getOrderDate() == null)
//                    return 0;
//                return o2.getOrderDate().compareTo(o1.getOrderDate());
//            }
//        });

//        adapterUpcomingOrders = new RecycleAdapterPastCompletedOrders(getActivity(), listFormattedPastOrders);
        adapterUpcomingOrders = new RecycleAdapterPastUpcomingOrders(getActivity(), listUpcomingOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvUpcomingOrders.setLayoutManager(layoutManager);
        rvUpcomingOrders.setItemAnimator(new DefaultItemAnimator());
        rvUpcomingOrders.setAdapter(adapterUpcomingOrders);

//        adapterUpcomingOrders.setClickListener(this);
    }

    private void getDummyOrderStatus() {
        OrderTimelineObject orderTimelineObj1 = new OrderTimelineObject();
        orderTimelineObj1.setTitle("Ordered");
        orderTimelineObj1.setDate("20th Apr 2020, 8:00 PM");
        orderTimelineObj1.setMessage("Your order has been placed.");
        orderTimelineObj1.setStatus(OrderStatusEnum.COMPLETED);

        OrderTimelineObject orderTimelineObj2 = new OrderTimelineObject();
        orderTimelineObj2.setTitle("Approved");
        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
        orderTimelineObj2.setMessage("Your order has been approved.");
        orderTimelineObj2.setStatus(OrderStatusEnum.COMPLETED);

        OrderTimelineObject orderTimelineObj3 = new OrderTimelineObject();
        orderTimelineObj3.setTitle("Packed");
        orderTimelineObj3.setDate("21st Apr 2020, 11:00 AM");
        orderTimelineObj3.setMessage("Your item has been packed and waiting for delivery partner to arrive.");
        orderTimelineObj3.setStatus(OrderStatusEnum.ACTIVE);

        OrderTimelineObject orderTimelineObj4 = new OrderTimelineObject();
        orderTimelineObj4.setTitle("Shipped");
        orderTimelineObj4.setDate("21th Apr 2020, 3:00 PM");
        orderTimelineObj4.setMessage("Your items has been shipped");
        orderTimelineObj4.setStatus(OrderStatusEnum.INACTIVE);

        OrderTimelineObject orderTimelineObj5 = new OrderTimelineObject();
        orderTimelineObj5.setTitle("Delivered");
        orderTimelineObj5.setDate("21th Apr 2020, 6:00 PM");
        orderTimelineObj5.setMessage("Your item has been Delivered");
        orderTimelineObj5.setStatus(OrderStatusEnum.INACTIVE);

        listOrderStatus.add(orderTimelineObj1);
        listOrderStatus.add(orderTimelineObj2);
        listOrderStatus.add(orderTimelineObj3);
        listOrderStatus.add(orderTimelineObj4);
        listOrderStatus.add(orderTimelineObj5);
    }

    private void getUpcomingOrderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {
//            showDialog();

            int userTypeID = Application.userDetails.getUserID();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getPendingOrders(userTypeID);
            Call<ResponseBody> call = apiService.getCompletedOrders(4);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listUpcomingOrders = new ArrayList<>();
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
                                    int orderStatus = jsonObj.optInt("OrderStatusEnum");
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
                                    orderObj.setListOrderStatusTimeline(listOrderStatus);

                                    listUpcomingOrders.add(orderObj);
                                }

                                showUpcomingOrdersList();
                                setupRecyclerViewUpcomingOrders();

                            } else {
                                showEmptyUpcomingOrdersLayout();
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

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
                            getUpcomingOrderDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
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
