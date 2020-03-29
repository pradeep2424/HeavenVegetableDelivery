package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.listeners.OnCuisineClickListener;
import com.heaven.vegetable.model.CuisineObject;

import java.util.List;

public class RecycleAdapterCuisine extends RecyclerView.Adapter<RecycleAdapterCuisine.MyViewHolder> {
    Context context;
    private OnCuisineClickListener clickListener;

    private List<CuisineObject> listCuisine;

    public RecycleAdapterCuisine(Context context, List<CuisineObject> listCuisine) {
        this.listCuisine = listCuisine;
        this.context = context;
    }

    public void setClickListener(OnCuisineClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView cuisineName;
        TextView price;
        TextView city;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            cuisineName = (TextView) view.findViewById(R.id.tv_cuisine_name);
            price = (TextView) view.findViewById(R.id.tv_price);
            city = (TextView) view.findViewById(R.id.tv_city);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onCuisineClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_cuisine, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CuisineObject cuisineObject = listCuisine.get(position);
        holder.cuisineName.setText(cuisineObject.getCuisineName());
        holder.price.setText(cuisineObject.getPrice());
        holder.city.setText(cuisineObject.getCity());
        holder.image.setImageResource(cuisineObject.getImage());


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
//        imageLoader.displayImage("drawable://"+ movie.getImage(),holder.image, options );

    }

    @Override
    public int getItemCount() {
        return listCuisine.size();
    }
}


