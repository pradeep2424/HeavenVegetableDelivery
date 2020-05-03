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
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.model.OrderDetailsObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapterPastCompletedOrders extends RecyclerView.Adapter<RecycleAdapterPastCompletedOrders.ViewHolder> {
    private Context context;
    private OnPastOrderOptionsClickListener clickListener;

    private ArrayList<OrderDetailsObject> modelArrayList;

    public RecycleAdapterPastCompletedOrders(Context context, ArrayList<OrderDetailsObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    public void setClickListener(OnPastOrderOptionsClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        TextView tvRestaurantName;
//        TextView tvRestaurantAddress;
//        TextView tvRestaurantReviews;
        TextView tvOrderNo;
        TextView tvOrderStatus;
        TextView tvOrderDate;
        TextView tvOrderPrice;
        ImageView ivFoodImage;
        LinearLayout llReorder;
        RecyclerView rvProductList;

        public ViewHolder(View itemView) {
            super(itemView);

//            tvRestaurantName = itemView.findViewById(R.id.tv_restaurantName);
//            tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurantAddress);
//            tvRestaurantReviews = itemView.findViewById(R.id.tv_restaurantReview);
            tvOrderNo = itemView.findViewById(R.id.tv_orderNo);
            tvOrderStatus = itemView.findViewById(R.id.tv_orderStatusText);
            tvOrderDate = itemView.findViewById(R.id.tv_date);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            ivFoodImage = itemView.findViewById(R.id.iv_foodImage);
            llReorder = itemView.findViewById(R.id.ll_reorder);
            rvProductList = itemView.findViewById(R.id.recyclerView_productView);

            llReorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClickReorder(view, getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_past_completed_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetailsObject orderDetailsObject = modelArrayList.get(position);
        String formattedTotalAmount = getFormattedNumberDouble(orderDetailsObject.getTotalAmount());

        String strOrderStatus = getOrderStatus(orderDetailsObject.getOrderStatus());

        holder.tvOrderNo.setText("#" + orderDetailsObject.getOrderNumber());
        holder.tvOrderStatus.setText(strOrderStatus);
        holder.tvOrderDate.setText(orderDetailsObject.getOrderDate());
        holder.tvOrderPrice.setText("₹ " + formattedTotalAmount);

        setupRecyclerViewPastOrders(holder.rvProductList, orderDetailsObject.getListProducts());
    }

    private void setupRecyclerViewPastOrders(RecyclerView recyclerView, ArrayList<ProductObject> listDish) {
        RecycleAdapterPastOrderProductView adapter = new RecycleAdapterPastOrderProductView(context, listDish);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private String getOrderStatus(int orderStatusID) {
        String orderStatus = "";
        switch (orderStatusID) {
            case 2:
                orderStatus = context.getResources().getString(R.string.order_status_title_cancelled);
                break;

            case 5:
                orderStatus = context.getResources().getString(R.string.order_status_title_delivered);
                break;

            default:
                orderStatus = context.getResources().getString(R.string.order_status_title_delivered);
        }

        return orderStatus;
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
