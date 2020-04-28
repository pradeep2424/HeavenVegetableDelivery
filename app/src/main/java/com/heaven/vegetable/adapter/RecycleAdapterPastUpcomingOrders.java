package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnPastOrderOptionsClickListener;
import com.heaven.vegetable.model.OrderDetailsObject;
import com.heaven.vegetable.model.OrderTimelineObject;
import com.heaven.vegetable.model.ProductObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapterPastUpcomingOrders extends RecyclerView.Adapter<RecycleAdapterPastUpcomingOrders.ViewHolder> {
    private Context context;
    private OnPastOrderOptionsClickListener clickListener;

    private ArrayList<OrderDetailsObject> modelArrayList;

    public RecycleAdapterPastUpcomingOrders(Context context, ArrayList<OrderDetailsObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    public void setClickListener(OnPastOrderOptionsClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderID;
        TextView tvOrderPrice;
        ImageView ivFoodImage;
        RecyclerView rvProductList;
        RecyclerView rvStatusTimeline;

        public ViewHolder(View itemView) {
            super(itemView);

            tvOrderID = itemView.findViewById(R.id.tv_orderID);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            ivFoodImage = itemView.findViewById(R.id.iv_foodImage);
            rvProductList = itemView.findViewById(R.id.recyclerView_productView);
            rvStatusTimeline = itemView.findViewById(R.id.recyclerView_status);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_past_upcomming_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetailsObject orderDetailsObject = modelArrayList.get(position);
        String formattedTotalAmount = getFormattedNumberDouble(orderDetailsObject.getTotalAmount());

//        holder.tvRestaurantName.setText(orderDetailsObject.getRestaurantName());
//        holder.tvRestaurantAddress.setText(orderDetailsObject.getUserAddress());

        holder.tvOrderID.setText(orderDetailsObject.getOrderID()+ "");
        holder.tvOrderPrice.setText("₹ " + formattedTotalAmount);

        if (orderDetailsObject.getListProducts() != null) {
            setupRecyclerViewProducts(holder.rvProductList, orderDetailsObject.getListProducts());
        }

        if (orderDetailsObject.getListOrderStatusTimeline() != null) {
            setupRecyclerViewStatus(holder.rvStatusTimeline, orderDetailsObject.getListOrderStatusTimeline());
        }
    }

    private void setupRecyclerViewProducts(RecyclerView recyclerView, ArrayList<ProductObject> listDish) {
        RecycleAdapterPastOrderProductView adapter = new RecycleAdapterPastOrderProductView(context, listDish);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setupRecyclerViewStatus(RecyclerView recyclerView, ArrayList<OrderTimelineObject> listStatus) {
        RecycleAdapterPastUpcomingStatusTimeline adapter = new RecycleAdapterPastUpcomingStatusTimeline(context, listStatus);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
