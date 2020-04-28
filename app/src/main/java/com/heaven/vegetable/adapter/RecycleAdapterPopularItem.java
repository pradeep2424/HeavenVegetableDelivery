package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnPopularItemClickedListener;
import com.heaven.vegetable.model.ProductObject;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterPopularItem extends RecyclerView.Adapter<RecycleAdapterPopularItem.MyViewHolder> {
    Context context;
    private OnPopularItemClickedListener clickListener;

    private List<ProductObject> listProducts;

    public RecycleAdapterPopularItem(Context context, List<ProductObject> listProducts) {
        this.listProducts = listProducts;
        this.context = context;
    }

    public void setClickListener(OnPopularItemClickedListener clickListener) {
        this.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemName;
        ImageView ivItemIcon;

        public MyViewHolder(View view) {
            super(view);

            tvItemName =  view.findViewById(R.id.tv_itemName);
            ivItemIcon =  view.findViewById(R.id.iv_itemIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onPopularItemClicked(view, getAdapterPosition());
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_popular_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductObject productObject = listProducts.get(position);

//        Integer icon[] = productObject.getProductImage();
        ArrayList<String> listProductImages = productObject.getListProductImage();

        holder.tvItemName.setText(productObject.getProductName());
//        holder.ivItemIcon.setImageResource(icon[1]);

        if (listProductImages != null && listProductImages.size() > 0) {
            String imageURL = listProductImages.get(0);
            Glide.with(context).load(imageURL).into(holder.ivItemIcon);
        }
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }


}


