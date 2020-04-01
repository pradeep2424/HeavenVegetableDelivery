package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.listeners.OnCuisineClickListener;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.CuisineObject;
import com.heaven.vegetable.model.ProductObject;

import java.util.List;

public class RecycleAdapterProductList extends RecyclerView.Adapter<RecycleAdapterProductList.MyViewHolder> {
    Context context;
    private OnRecyclerViewClickListener clickListener;

    private List<ProductObject> listProducts;

    public RecycleAdapterProductList(Context context, List<ProductObject> listProducts) {
        this.listProducts = listProducts;
        this.context = context;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tvProductImage;
        TextView tvProductName;
        TextView tvProductCategory;
        TextView tvProductPrice;

        LinearLayout ll250Gram;
        LinearLayout ll500Gram;
        LinearLayout ll1Kilo;

        public MyViewHolder(View view) {
            super(view);

            tvProductImage =  view.findViewById(R.id.iv_productImage);
            tvProductName =  view.findViewById(R.id.tv_productName);
            tvProductCategory =  view.findViewById(R.id.tv_productCategory);
            tvProductPrice =  view.findViewById(R.id.tv_ProductPrice);

            ll250Gram =  view.findViewById(R.id.ll_250Gram);
            ll500Gram =  view.findViewById(R.id.ll_500Gram);
            ll1Kilo =  view.findViewById(R.id.ll_1Kilo);

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductObject productObject = listProducts.get(position);

//        String price = context.getResources().getString(R.string.rupees) + " " + productObject.getPrice();
//        holder.tvProductName.setText(productObject.getProductName());
//        holder.tvProductCategory.setText(productObject.getCategoryName());
//        holder.tvProductPrice.setText(price);
//        holder.tvProductImage.setImageResource(productObject.getProductImage());


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
        return listProducts.size();
    }
}


