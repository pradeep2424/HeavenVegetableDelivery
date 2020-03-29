package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.listeners.OnUserMayLikedClickListener;
import com.heaven.vegetable.model.DishObject;

import java.util.List;

public class RecycleAdapterPopularItem extends RecyclerView.Adapter<RecycleAdapterPopularItem.MyViewHolder> {
    Context context;
    private OnUserMayLikedClickListener clickListener;

    private List<DishObject> listDish;

    public RecycleAdapterPopularItem(Context context, List<DishObject> listDish) {
        this.listDish = listDish;
        this.context = context;
    }

    public void setClickListener(OnUserMayLikedClickListener clickListener) {
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
                clickListener.onUserMayLikedClick(view, getAdapterPosition());
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
        DishObject movie = listDish.get(position);

        holder.tvItemName.setText(movie.getProductName());
        holder.ivItemIcon.setImageResource(Integer.parseInt(movie.getProductImage()));

//        Glide.with(context)
//                .load(R.drawable.resource_id)
//                .into(imageView);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                // You can pass your own memory cache implementation
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                .build();
//
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .build();
//
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config);
//        imageLoader.displayImage("drawable://" + movie.getImage(), holder.image, options);


    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }


}


