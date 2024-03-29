package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.CategoryObject;

import java.util.ArrayList;

public class RecycleAdapterCategory extends RecyclerView.Adapter<RecycleAdapterCategory.ViewHolder> {
    Context context;
    private OnRecyclerViewClickListener clickListener;

    ArrayList<CategoryObject> listCategory;

    public RecycleAdapterCategory(Context context, ArrayList<CategoryObject> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        public ViewHolder(View itemView) {
            super(itemView);

            ivCategoryImage = itemView.findViewById(R.id.iv_categoryImage);
            tvCategoryName = itemView.findViewById(R.id.tv_categoryName);

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
    public RecycleAdapterCategory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterCategory.ViewHolder holder, int position) {
        CategoryObject cateogryObject = listCategory.get(position);

        holder.tvCategoryName.setText(cateogryObject.getCategoryName());
//        holder.ivCategoryImage.setImageResource(Integer.parseInt(cateogryObject.getCategoryImage()));

        Glide.with(context).load(cateogryObject.getCategoryImage()).into(holder.ivCategoryImage);
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }
}
