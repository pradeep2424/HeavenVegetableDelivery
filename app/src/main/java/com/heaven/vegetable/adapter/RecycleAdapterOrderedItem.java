package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnItemAddedToCart;
import com.heaven.vegetable.model.CartObject;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapterOrderedItem extends RecyclerView.Adapter<RecycleAdapterOrderedItem.ViewHolder> {

    Context context;

    private OnItemAddedToCart onItemAddedToCart;
    private ArrayList<CartObject> modelArrayList;

    private ViewHolder viewHolderClickedItem;

    public RecycleAdapterOrderedItem(Context context, ArrayList<CartObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public RecycleAdapterOrderedItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_past_completed_orders_product_view_item, parent, false);
        return new ViewHolder(view);
    }

    public void setOnItemAddedToCart(OnItemAddedToCart onItemAddedToCart) {
        this.onItemAddedToCart = onItemAddedToCart;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CartObject cartObject = modelArrayList.get(position);
        holder.tvFoodName.setText(cartObject.getProductName());
        holder.numberPickerQuantity.setValue(cartObject.getProductQuantity());

        double updatedPrice = getUpdateItemPrice(cartObject);
        String formattedPrice = getFormattedNumberDouble(updatedPrice);
        holder.tvFoodPrice.setText("₹ " + formattedPrice);

        holder.numberPickerQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
//                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ?
                        ActionEnum.INCREMENT.toString() :  ActionEnum.DECREMENT.toString());
                String message = String.format("NumberPicker is %s to %d", actionText, value);

                viewHolderClickedItem = holder;
                if (onItemAddedToCart != null) {
                    onItemAddedToCart.onItemChangedInCart(value, position, actionText);
                }
            }
        });
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    private double getUpdateItemPrice(CartObject cartObject) {
        double updatedPrice = cartObject.getProductRate() * cartObject.getProductQuantity();
        return updatedPrice;
    }

    public void updateCartItemQuantity(int quantity) {
        viewHolderClickedItem.numberPickerQuantity.setValue(quantity);
    }

    public void updateCartItemPrice(double price) {
        String formattedPrice = getFormattedNumberDouble(price);
        viewHolderClickedItem.tvFoodPrice.setText("₹ " + formattedPrice);
    }

    public void removeAt(int position) {
        modelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, modelArrayList.size());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;
        TextView tvFoodPrice;
        NumberPicker numberPickerQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_foodName);
            tvFoodPrice = itemView.findViewById(R.id.tv_foodPrice);
            numberPickerQuantity = itemView.findViewById(R.id.numberPicker_quantityLayout);
        }
    }
}
