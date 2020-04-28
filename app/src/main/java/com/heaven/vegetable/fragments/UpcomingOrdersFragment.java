package com.heaven.vegetable.fragments;

import android.icu.text.UFormat;
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

import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.adapter.RecycleAdapterPastUpcomingOrders;
import com.heaven.vegetable.interfaces.TimelineLineType;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.OrderDetailsObject;
import com.heaven.vegetable.model.OrderStatusEnum;
import com.heaven.vegetable.model.OrderTimelineObject;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.heaven.vegetable.utils.Utils;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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
    private ArrayList<OrderDetailsObject> listFormattedPastOrders;

    ArrayList<OrderTimelineObject> listDefaultStatus;

    private ArrayList<String> listOrderStatusTitle;
    private ArrayList<String> listOrderStatusSubtitle;

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

//        getAllOrderStatusTemplate();
        getAllOrderStatusTemplate();
        initOrderStatusArrays();
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

    private void initOrderStatusArrays() {
        listOrderStatusTitle = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.order_status_title)));
        listOrderStatusSubtitle = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.order_status_subtitle)));
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
        listFormattedPastOrders = new ArrayList<>();
        listFormattedPastOrders = formatPastOrderData();



        Collections.sort(listFormattedPastOrders, new Comparator<OrderDetailsObject>() {
            public int compare(OrderDetailsObject o1, OrderDetailsObject o2) {
                if (o1.getOrderDate() == null || o2.getOrderDate() == null)
                    return 0;
                return o2.getOrderDate().compareTo(o1.getOrderDate());
            }
        });

        adapterUpcomingOrders = new RecycleAdapterPastUpcomingOrders(getActivity(), listFormattedPastOrders);
//        adapterUpcomingOrders = new RecycleAdapterPastUpcomingOrders(getActivity(), listUpcomingOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvUpcomingOrders.setLayoutManager(layoutManager);
        rvUpcomingOrders.setItemAnimator(new DefaultItemAnimator());
        rvUpcomingOrders.setAdapter(adapterUpcomingOrders);

//        adapterUpcomingOrders.setClickListener(this);
    }

    private ArrayList<OrderDetailsObject> formatPastOrderData() {
        ArrayList<OrderDetailsObject> listPastOrders = new ArrayList<>();

        ArrayList<OrderDetailsObject> tempAllOrders = SerializationUtils.clone(listUpcomingOrders);

        for (int i = 0; i < listUpcomingOrders.size(); i++) {
            int orderNo = listUpcomingOrders.get(i).getOrderNumber();
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
                listUpcomingOrders.get(i).setListProducts(listProducts);

                OrderDetailsObject orderDetailsObject = SerializationUtils.clone(listUpcomingOrders.get(i));
//                updating status in list
                ArrayList<OrderTimelineObject> modifiedStatus = formatOrderStatusData(orderDetailsObject);
                orderDetailsObject.setListOrderStatusTimeline(modifiedStatus);

                listPastOrders.add(orderDetailsObject);
            }
        }

        return listPastOrders;
    }

    private ArrayList<OrderTimelineObject> formatOrderStatusData(OrderDetailsObject orderDetailsObject) {
        ArrayList<OrderTimelineObject> modifiedStatusList = new ArrayList<>();
        modifiedStatusList.addAll(SerializationUtils.clone(listDefaultStatus));

        int orderStatus = orderDetailsObject.getOrderStatus();
//        if order status is 2 then order is rejected
        if (orderStatus == 2) {
//            remove approved status from list
            modifiedStatusList.remove(1);


        } else {
//            remove rejected status from list
            modifiedStatusList.remove(2);

        }

        for (int i = 0; i < modifiedStatusList.size(); i++) {
            OrderTimelineObject orderStatsObj = modifiedStatusList.get(i);

            if (i < orderStatus) {
                orderStatsObj.setStatus(OrderStatusEnum.COMPLETED);

            } else if (i == orderStatus) {
                orderStatsObj.setStatus(OrderStatusEnum.ACTIVE);

            } else {
                orderStatsObj.setStatus(OrderStatusEnum.INACTIVE);
            }
        }
        return modifiedStatusList;
    }

//    private ArrayList<OrderTimelineObject> getAllOrderStatusTemplate(OrderDetailsObject orderObj) {
//        ArrayList<OrderTimelineObject> listOrderStatus = new ArrayList<>();
//
//        String orderDate = orderObj.getOrderDate();
//        int orderStatus = orderObj.getOrderStatus();
//        int maxStatus = listOrderStatusTitle.size() - 1;
//
////        if order status is 2 then order is rejected
//        if (orderStatus == 2) {
////            remove approved status from list
//            listOrderStatusTitle.remove(1);
//            listOrderStatusSubtitle.remove(1);
//
//        } else {
////            remove rejected status from list
//            listOrderStatusTitle.remove(2);
//            listOrderStatusSubtitle.remove(2);
//        }
//
//        for (int i = 0; i < listOrderStatusTitle.size(); i++) {
//            String title = listOrderStatusTitle.get(i);
//            String subTitle = listOrderStatusSubtitle.get(i);
//
//            OrderTimelineObject orderTimelineObj = new OrderTimelineObject();
//            orderTimelineObj.setTitle(title);
//            orderTimelineObj.setMessage(subTitle);
//
////            adding line for view
//            if (i == 0) {
//                orderTimelineObj.setDate(orderDate);
//                orderTimelineObj.setViewType(TimelineLineType.START);
//
//            } else if (i == maxStatus) {
//                orderTimelineObj.setViewType(TimelineLineType.END);
//
//            } else if (i == orderStatus) {
//                orderTimelineObj.setViewType(TimelineLineType.START);
//
//            } else {
//                orderTimelineObj.setViewType(TimelineLineType.NORMAL);
//            }
//
////            adding marker status
//            if (i < orderStatus) {
//                orderTimelineObj.setStatus(OrderStatusEnum.COMPLETED);
//
//            } else if (i == orderStatus) {
//                orderTimelineObj.setStatus(OrderStatusEnum.ACTIVE);
//
//            } else {
//                orderTimelineObj.setStatus(OrderStatusEnum.INACTIVE);
//            }
//
//
//            listOrderStatus.add(orderTimelineObj);
//        }
//
////        OrderTimelineObject orderTimelineObj1 = new OrderTimelineObject();
////        orderTimelineObj1.setTitle("Ordered");
//////        orderTimelineObj1.setDate("20th Apr 2020, 8:00 PM");
////        orderTimelineObj1.setMessage("Your order has been placed.");
////        orderTimelineObj1.setViewType(TimelineLineType.START);
////        orderTimelineObj1.setStatus(OrderStatusEnum.COMPLETED);
////
////        OrderTimelineObject orderTimelineObj2 = new OrderTimelineObject();
////        orderTimelineObj2.setTitle("Approved");
//////        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
////        orderTimelineObj2.setMessage("Your order has been approved.");
////        orderTimelineObj2.setViewType(TimelineLineType.NORMAL);
////        orderTimelineObj2.setStatus(OrderStatusEnum.COMPLETED);
////
////        OrderTimelineObject orderTimelineObj3 = new OrderTimelineObject();
////        orderTimelineObj3.setTitle("Packed");
//////        orderTimelineObj3.setDate("21st Apr 2020, 11:00 AM");
////        orderTimelineObj3.setMessage("Your item has been packed and waiting for delivery partner to arrive.");
////        orderTimelineObj3.setViewType(TimelineLineType.NORMAL);
////        orderTimelineObj3.setStatus(OrderStatusEnum.ACTIVE);
////
////        OrderTimelineObject orderTimelineObj4 = new OrderTimelineObject();
////        orderTimelineObj4.setTitle("Shipped");
//////        orderTimelineObj4.setDate("21th Apr 2020, 3:00 PM");
////        orderTimelineObj4.setMessage("Your items has been shipped");
////        orderTimelineObj4.setViewType(TimelineLineType.NORMAL);
////        orderTimelineObj4.setStatus(OrderStatusEnum.INACTIVE);
////
////        OrderTimelineObject orderTimelineObj5 = new OrderTimelineObject();
////        orderTimelineObj5.setTitle("Delivered");
//////        orderTimelineObj5.setDate("21th Apr 2020, 6:00 PM");
////        orderTimelineObj5.setMessage("Your item has been Delivered");
////        orderTimelineObj5.setViewType(TimelineLineType.END);
////        orderTimelineObj5.setStatus(OrderStatusEnum.INACTIVE);
////
////        listOrderStatus.add(orderTimelineObj1);
////        listOrderStatus.add(orderTimelineObj2);
////        listOrderStatus.add(orderTimelineObj3);
////        listOrderStatus.add(orderTimelineObj4);
////        listOrderStatus.add(orderTimelineObj5);
//
//        return listOrderStatus;
//    }


    private ArrayList<OrderTimelineObject> getAllOrderStatusTemplate() {
        listDefaultStatus = new ArrayList<>();

        OrderTimelineObject orderTimelineObj0 = new OrderTimelineObject();
        orderTimelineObj0.setTitle("Ordered");
//        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
        orderTimelineObj0.setMessage("Your order has been placed.");
        orderTimelineObj0.setViewType(TimelineLineType.START);
        orderTimelineObj0.setStatus(OrderStatusEnum.INACTIVE);

        OrderTimelineObject orderTimelineObj1 = new OrderTimelineObject();
        orderTimelineObj1.setTitle("Approved");
//        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
        orderTimelineObj1.setMessage("Your order has been approved.");
        orderTimelineObj1.setViewType(TimelineLineType.NORMAL);
        orderTimelineObj1.setStatus(OrderStatusEnum.INACTIVE);

        OrderTimelineObject orderTimelineObj2 = new OrderTimelineObject();
        orderTimelineObj2.setTitle("Rejected");
//        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
        orderTimelineObj2.setMessage("Your order has been rejected.");
        orderTimelineObj2.setViewType(TimelineLineType.NORMAL);
        orderTimelineObj2.setStatus(OrderStatusEnum.INACTIVE);


        OrderTimelineObject orderTimelineObj3 = new OrderTimelineObject();
        orderTimelineObj3.setTitle("Packed");
//        orderTimelineObj3.setDate("21st Apr 2020, 11:00 AM");
        orderTimelineObj3.setMessage("Your item has been packed and waiting for delivery partner to arrive.");
        orderTimelineObj3.setViewType(TimelineLineType.NORMAL);
        orderTimelineObj3.setStatus(OrderStatusEnum.INACTIVE);

        OrderTimelineObject orderTimelineObj4 = new OrderTimelineObject();
        orderTimelineObj4.setTitle("Shipped");
//        orderTimelineObj4.setDate("21th Apr 2020, 3:00 PM");
        orderTimelineObj4.setMessage("Your items has been shipped");
        orderTimelineObj4.setViewType(TimelineLineType.NORMAL);
        orderTimelineObj4.setStatus(OrderStatusEnum.INACTIVE);

        OrderTimelineObject orderTimelineObj5 = new OrderTimelineObject();
        orderTimelineObj5.setTitle("Delivered");
//        orderTimelineObj5.setDate("21th Apr 2020, 6:00 PM");
        orderTimelineObj5.setMessage("Your item has been Delivered");
        orderTimelineObj5.setViewType(TimelineLineType.END);
        orderTimelineObj5.setStatus(OrderStatusEnum.INACTIVE);

        listDefaultStatus.add(orderTimelineObj0);
        listDefaultStatus.add(orderTimelineObj1);
        listDefaultStatus.add(orderTimelineObj2);
        listDefaultStatus.add(orderTimelineObj3);
        listDefaultStatus.add(orderTimelineObj4);
        listDefaultStatus.add(orderTimelineObj5);

        return listDefaultStatus;
    }

//    private ArrayList<OrderTimelineObject> getAllOrderStatusTemplate(OrderDetailsObject orderObj) {
//        listDefaultStatus = new ArrayList<>();
//
//        String orderDate = orderObj.getOrderDate();
//        int orderStatus = orderObj.getOrderStatus();
//
//        OrderTimelineObject orderTimelineObj0 = new OrderTimelineObject();
//        orderTimelineObj0.setTitle("Ordered");
////        orderTimelineObj1.setDate("20th Apr 2020, 8:00 PM");
//        orderTimelineObj0.setMessage("Your order has been placed.");
//        orderTimelineObj0.setViewType(TimelineLineType.START);
//        orderTimelineObj0.setStatus(OrderStatusEnum.INACTIVE);
//
//        OrderTimelineObject orderTimelineObj1 = new OrderTimelineObject();
//        orderTimelineObj1.setTitle("Approved");
////        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
//        orderTimelineObj1.setMessage("Your order has been approved.");
//        orderTimelineObj1.setViewType(TimelineLineType.NORMAL);
//        orderTimelineObj1.setStatus(OrderStatusEnum.INACTIVE);
//
//        OrderTimelineObject orderTimelineObj2 = new OrderTimelineObject();
//        orderTimelineObj2.setTitle("Rejected");
////        orderTimelineObj2.setDate("20th Apr 2020, 11:00 PM");
//        orderTimelineObj2.setMessage("Your order has been rejected.");
//        orderTimelineObj2.setViewType(TimelineLineType.NORMAL);
//        orderTimelineObj2.setStatus(OrderStatusEnum.INACTIVE);
//
//
//        OrderTimelineObject orderTimelineObj3 = new OrderTimelineObject();
//        orderTimelineObj3.setTitle("Packed");
////        orderTimelineObj3.setDate("21st Apr 2020, 11:00 AM");
//        orderTimelineObj3.setMessage("Your item has been packed and waiting for delivery partner to arrive.");
//        orderTimelineObj3.setViewType(TimelineLineType.NORMAL);
//        orderTimelineObj3.setStatus(OrderStatusEnum.INACTIVE);
//
//        OrderTimelineObject orderTimelineObj4 = new OrderTimelineObject();
//        orderTimelineObj4.setTitle("Shipped");
////        orderTimelineObj4.setDate("21th Apr 2020, 3:00 PM");
//        orderTimelineObj4.setMessage("Your items has been shipped");
//        orderTimelineObj4.setViewType(TimelineLineType.NORMAL);
//        orderTimelineObj4.setStatus(OrderStatusEnum.INACTIVE);
//
//        OrderTimelineObject orderTimelineObj5 = new OrderTimelineObject();
//        orderTimelineObj5.setTitle("Delivered");
////        orderTimelineObj5.setDate("21th Apr 2020, 6:00 PM");
//        orderTimelineObj5.setMessage("Your item has been Delivered");
//        orderTimelineObj5.setViewType(TimelineLineType.END);
//        orderTimelineObj5.setStatus(OrderStatusEnum.INACTIVE);
//
//        listDefaultStatus.add(orderTimelineObj1);
//        listDefaultStatus.add(orderTimelineObj2);
//        listDefaultStatus.add(orderTimelineObj3);
//        listDefaultStatus.add(orderTimelineObj4);
//        listDefaultStatus.add(orderTimelineObj5);
//
//        return listDefaultStatus;
//    }

    private void getUpcomingOrderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {
//            showDialog();

            int userTypeID = Application.userDetails.getUserID();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getPendingOrders(userTypeID);
//            Call<ResponseBody> call = apiService.getPendingOrders(4);
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
                                    boolean orderPaid = jsonObj.optBoolean("OrderPaid");
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

//                                    ArrayList<OrderTimelineObject> listOrderStatus = getAllOrderStatusTemplate(orderObj);
//                                    orderObj.setListOrderStatusTimeline(listOrderStatus);

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
