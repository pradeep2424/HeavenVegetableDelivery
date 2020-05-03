package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnPastOrderOptionsClickListener;
import com.heaven.vegetable.interfaces.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.OrderDetailsObject;
import com.heaven.vegetable.model.OrderTimelineObject;
import com.heaven.vegetable.model.ProductObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapterPastUpcomingOrders extends RecyclerView.Adapter<RecycleAdapterPastUpcomingOrders.ViewHolder> {
    private Context context;
    private ArrayList<OrderDetailsObject> modelArrayList;
    private OnRecyclerViewClickListener clickListener;

    public RecycleAdapterPastUpcomingOrders(Context context, ArrayList<OrderDetailsObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llCancelOrder;
        TextView tvOrderNo;
        TextView tvOrderPrice;
        ImageView ivFoodImage;
        RecyclerView rvProductList;
        RecyclerView rvStatusTimeline;

        public ViewHolder(View itemView) {
            super(itemView);

            llCancelOrder = itemView.findViewById(R.id.ll_cancelOrder);
            tvOrderNo = itemView.findViewById(R.id.tv_orderNo);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            ivFoodImage = itemView.findViewById(R.id.iv_foodImage);
            rvProductList = itemView.findViewById(R.id.recyclerView_productView);
            rvStatusTimeline = itemView.findViewById(R.id.recyclerView_status);

            llCancelOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
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
        int orderStatus = orderDetailsObject.getOrderStatus();
        String formattedTotalAmount = getFormattedNumberDouble(orderDetailsObject.getTotalAmount());

//        holder.tvRestaurantName.setText(orderDetailsObject.getRestaurantName());
//        holder.tvRestaurantAddress.setText(orderDetailsObject.getUserAddress());

        holder.tvOrderNo.setText("#" +orderDetailsObject.getOrderNumber());
        holder.tvOrderPrice.setText("₹ " + formattedTotalAmount);

        if (orderStatus < 1) {
            holder.llCancelOrder.setVisibility(View.VISIBLE);

        } else {
            holder.llCancelOrder.setVisibility(View.GONE);
        }

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

    public void hideCancelOrderButton() {

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
