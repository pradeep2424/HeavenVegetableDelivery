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
import com.heaven.vegetable.listeners.OnPastOrderOptionsClickListener;
import com.heaven.vegetable.model.DishObject;
import com.heaven.vegetable.model.OrderDetailsObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapterPastOrders extends RecyclerView.Adapter<RecycleAdapterPastOrders.ViewHolder> {
    private Context context;
    private OnPastOrderOptionsClickListener clickListener;

    private ArrayList<OrderDetailsObject> modelArrayList;

    public RecycleAdapterPastOrders(Context context, ArrayList<OrderDetailsObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    public void setClickListener(OnPastOrderOptionsClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRestaurantName;
        TextView tvRestaurantAddress;
        TextView tvRestaurantReviews;
        TextView tvOrderDate;
        TextView tvOrderPrice;
        ImageView ivFoodImage;
        LinearLayout llReorder;
        RecyclerView rvProductList;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRestaurantName = itemView.findViewById(R.id.tv_restaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurantAddress);
            tvRestaurantReviews = itemView.findViewById(R.id.tv_restaurantReview);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_past_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetailsObject orderDetailsObject = modelArrayList.get(position);
        String formattedTotalAmount = getFormattedNumberDouble(orderDetailsObject.getTotalAmount());

        holder.tvRestaurantName.setText(orderDetailsObject.getRestaurantName());
        holder.tvRestaurantAddress.setText(orderDetailsObject.getUserAddress());
//        holder.tvRestaurantReviews.setText(orderDetailsObject.getRestaurantReviews());
        holder.tvOrderDate.setText(orderDetailsObject.getOrderDate());
        holder.tvOrderPrice.setText("₹ " + formattedTotalAmount);
//        holder.ivFoodImage.setImageResource(Integer.parseInt(orderDetailsObject.getDishImage()));

        setupRecyclerViewPastOrders(holder.rvProductList, orderDetailsObject.getListProducts());
    }

    private void setupRecyclerViewPastOrders(RecyclerView recyclerView, ArrayList<DishObject> listDish) {
        RecycleAdapterPastOrderProductView  adapter = new RecycleAdapterPastOrderProductView(context, listDish);
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
