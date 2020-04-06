package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heaven.vegetable.R;

import java.util.ArrayList;

public class RecycleAdapterRestaurantFoodPhotos extends RecyclerView.Adapter<RecycleAdapterRestaurantFoodPhotos.ViewHolder> {

    Context context;
    private ArrayList<Integer> modelArrayList;

    public RecycleAdapterRestaurantFoodPhotos(Context context, ArrayList<Integer> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public RecycleAdapterRestaurantFoodPhotos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurant_food_photos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleAdapterRestaurantFoodPhotos.ViewHolder holder, int position) {
//        DishObject dishObject = modelArrayList.get(position);
//        holder.ivFood.setImageResource(Integer.parseInt(dishObject.getProductImage()));

        int photoURL = modelArrayList.get(position);
        holder.ivFood.setImageResource(photoURL);
//        Glide.with(context).load(photoURL).into(holder.ivFood);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;

        public ViewHolder(View itemView) {
            super(itemView);

            ivFood = itemView.findViewById(R.id.iv_foodImage);
        }
    }
}
