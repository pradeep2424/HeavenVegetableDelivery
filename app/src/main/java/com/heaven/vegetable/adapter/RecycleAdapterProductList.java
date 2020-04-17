package com.heaven.vegetable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.heaven.vegetable.R;
import com.heaven.vegetable.activity.ProductListActivity;
import com.heaven.vegetable.listeners.OnCuisineClickListener;
import com.heaven.vegetable.listeners.OnItemAddedToCart;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.ClientObject;
import com.heaven.vegetable.model.CuisineObject;
import com.heaven.vegetable.model.ProductObject;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.InternetConnection;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleAdapterProductList extends RecyclerView.Adapter<RecycleAdapterProductList.MyViewHolder> {
    ProductListActivity activity;

    private OnRecyclerViewClickListener clickListener;
    private OnItemAddedToCart onItemAddedToCart;

    private List<ProductObject> listProducts;

    DialogLoadingIndicator progressIndicator;

    public RecycleAdapterProductList(ProductListActivity activity, List<ProductObject> listProducts) {
        this.listProducts = listProducts;
        this.activity = activity;

        progressIndicator = DialogLoadingIndicator.getInstance();
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemAddedToCart(OnItemAddedToCart onItemAddedToCart) {
        this.onItemAddedToCart = onItemAddedToCart;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tvProductImage;
        TextView tvProductName;
        TextView tvProductCategory;
        TextView tvProductPrice;

        LinearLayout llAddItem;
        NumberPicker numberPickerItemQuantity;

//        LinearLayout ll250Gram;
//        LinearLayout ll500Gram;
//        LinearLayout ll1Kilo;

        public MyViewHolder(View view) {
            super(view);

            tvProductImage = view.findViewById(R.id.iv_productImage);
            tvProductName = view.findViewById(R.id.tv_productName);
            tvProductCategory = view.findViewById(R.id.tv_productCategory);
            tvProductPrice = view.findViewById(R.id.tv_ProductPrice);

            llAddItem = itemView.findViewById(R.id.ll_addButton);
            numberPickerItemQuantity = itemView.findViewById(R.id.numberPicker_quantity);

//            ll250Gram =  view.findViewById(R.id.ll_250Gram);
//            ll500Gram =  view.findViewById(R.id.ll_500Gram);
//            ll1Kilo =  view.findViewById(R.id.ll_1Kilo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_product_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ProductObject productObject = listProducts.get(position);

        Integer image[] = productObject.getProductImage();
        String name = productObject.getProductName();
        String category = productObject.getCategoryName();
        String price = activity.getResources().getString(R.string.rupees) + getFormattedNumberDouble(productObject.getPrice());

        holder.tvProductImage.setImageResource(image[2]);
        holder.tvProductName.setText(name);
        holder.tvProductCategory.setText(category);
        holder.tvProductPrice.setText(price);

        holder.llAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewHolderClickedItem = holder;

                if (onItemAddedToCart != null) {
                    addItemOrUpdateQuantity(holder, 1, position, ActionEnum.INCREMENT.toString());
                }
            }
        });

        holder.numberPickerItemQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
//                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ?
                        ActionEnum.INCREMENT.toString() : ActionEnum.DECREMENT.toString());
                String message = String.format("NumberPicker is %s to %d", actionText, value);

                addItemOrUpdateQuantity(holder, value, position, actionText);

            }
        });
    }

    public void showAddItemButton(MyViewHolder holder) {
        holder.numberPickerItemQuantity.setVisibility(View.GONE);
        holder.llAddItem.setVisibility(View.VISIBLE);
    }

    public void hideAddItemButton(MyViewHolder holder) {
//            show number picker
        holder.numberPickerItemQuantity.setVisibility(View.VISIBLE);
        holder.llAddItem.setVisibility(View.GONE);
    }

    private void addItemOrUpdateQuantity(MyViewHolder holder, final int quantity, final int position, final String incrementOrDecrement) {
        holder.numberPickerItemQuantity.setValue(quantity);
        if (quantity == 0) {
            showAddItemButton(holder);

        } else {
            hideAddItemButton(holder);
        }

        final ProductObject productObject = listProducts.get(position);
        addItemToCart(productObject, quantity, position, incrementOrDecrement);
    }

    private JsonObject createJsonCart(ProductObject productObject, int quantity) {
        double totalPrice;

        ClientObject clientObject = Application.clientObject;

//        if (clientObject.getTaxable()) {
//            double productPrice = dishObject.getPrice();
//            double cgst = dishObject.getCgst();
//            double sgst = dishObject.getCgst();
//
////            totalPrice = productPrice * ()
//        }

        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("ProductId", productObject.getProductID());
            postParam.addProperty("ProductName", productObject.getProductName());
            postParam.addProperty("ProductRate", productObject.getPrice());
            postParam.addProperty("ProductAmount", productObject.getPrice());
            postParam.addProperty("ProductSize", "Regular");
            postParam.addProperty("cartId", 0);
            postParam.addProperty("ProductQnty", quantity);
            postParam.addProperty("Taxableval", productObject.getPrice());    // doubt
            postParam.addProperty("CGST", productObject.getCgst());
            postParam.addProperty("SGST", productObject.getSgst());
            postParam.addProperty("DeliveryCharge", 30.00);
            postParam.addProperty("Userid", Application.userDetails.getUserID());
            postParam.addProperty("Clientid", clientObject.getRestaurantID());
            postParam.addProperty("TaxId", 0);
            postParam.addProperty("TotalAmount", productObject.getPrice());
            postParam.addProperty("HotelName", clientObject.getRestaurantName());
            postParam.addProperty("IsIncludeTax", true);
            postParam.addProperty("IsTaxApplicable", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }

    public void addItemToCart(final ProductObject productObject, final int quantity, final int position, final String incrementOrDecrement) {
        if (InternetConnection.checkConnection(activity)) {
            activity.showDialog();

            ApiInterface apiService = RetroClient.getApiService(activity);
            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(productObject, quantity));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            successOnAddToCart(quantity, position, incrementOrDecrement);

//                            if (onItemAddedToCart != null) {
//                                onItemAddedToCart.onItemChangedInCart(quantity, position, incrementOrDecrement);
//                            }

                        } else {
                            activity.showSnackbarErrorMsg(activity.getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    activity.dismissDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        activity.dismissDialog();
                        activity.showSnackbarErrorMsg(activity.getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(activity.rlRootLayout, activity.getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addItemToCart(productObject, quantity, position, incrementOrDecrement);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void successOnAddToCart(final int quantity, final int position, final String incrementOrDecrement) {
        if (onItemAddedToCart != null) {
            onItemAddedToCart.onItemChangedInCart(quantity, position, incrementOrDecrement);
        }
    }


    @Override
    public int getItemCount() {
        return listProducts.size();
    }

//    private String formatAmount(double amount) {
//        String amt;
//        DecimalFormat df = new DecimalFormat();
//        df.setDecimalSeparatorAlwaysShown(false);
//        amt = df.format(amount);
//
//        return amt;
//    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}


