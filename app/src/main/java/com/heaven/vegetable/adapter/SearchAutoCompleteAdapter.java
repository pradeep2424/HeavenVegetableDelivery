package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.ProductObject;

import java.util.ArrayList;
import java.util.List;

public class SearchAutoCompleteAdapter extends RecyclerView.Adapter<SearchAutoCompleteAdapter.MyViewHolder> {
    Context context;

    private OnRecyclerViewClickListener clickListener;
    private List<ProductObject> listSearchedProducts;

    public SearchAutoCompleteAdapter(Context context, List<ProductObject> listSearchedProducts) {
        this.context = context;
        this.listSearchedProducts = listSearchedProducts;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProductName;
        private TextView tvProductCategory;
        private ImageView ivProductImage;
        private LinearLayout llRow;

        MyViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_productName);
            tvProductCategory = itemView.findViewById(R.id.tv_productCategory);
            ivProductImage = itemView.findViewById(R.id.iv_productImage);
            llRow = itemView.findViewById(R.id.ll_rootView);
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
                .inflate(R.layout.item_search_recycler_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ProductObject productObject = listSearchedProducts.get(position);

        ArrayList<String> listProductImages = productObject.getListProductImage();
        String name = productObject.getProductName();
        String category = productObject.getCategoryName();

        holder.tvProductName.setText(name);
        holder.tvProductCategory.setText(category);

        if (listProductImages != null && listProductImages.size() > 0) {
            String imageURL = listProductImages.get(0);
            Glide.with(context).load(imageURL).into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return listSearchedProducts.size();
    }

}
