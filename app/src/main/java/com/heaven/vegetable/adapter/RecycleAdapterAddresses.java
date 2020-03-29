package com.heaven.vegetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven.vegetable.R;
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.AddressDetails;

import java.util.ArrayList;


/**
 * Created by Pradeep on 01-Sep-2019.
 */

public class RecycleAdapterAddresses extends RecyclerView.Adapter<RecycleAdapterAddresses.ViewHolder> {

    private Context context;
    private ArrayList<AddressDetails> listAddress;

    private OnRecyclerViewClickListener clickListener;

    public RecycleAdapterAddresses(Context context, ArrayList<AddressDetails> listAddress) {
        this.context = context;
        this.listAddress = listAddress;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvAddressTitle;
        TextView tvAddressText;
//        TextView tvEdit;
        TextView tvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvAddressTitle = itemView.findViewById(R.id.tv_addressTitle);
            tvAddressText = itemView.findViewById(R.id.tv_addressText);
//            tvEdit = itemView.findViewById(R.id.tv_editAddress);
            tvDelete = itemView.findViewById(R.id.tv_deleteAddress);

//            tvEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (clickListener != null) {
//                        clickListener.onEditAddress(view, getAdapterPosition());
//                    }
//                }
//            });

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClick(view, getAdapterPosition());
                    }
                }
            });

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            if (clickListener != null) {
//                clickListener.onClick(view, getAdapterPosition());
//            }
//        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_manage_addresses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddressDetails addressDetails = listAddress.get(position);

//        holder.ivIcon.setImageResource(addressDetails.getIcon());

        holder.tvAddressTitle.setText("Home");
        holder.tvAddressText.setText(addressDetails.getAddress());

    }

    @Override
    public int getItemCount() {
        return listAddress.size();
    }


}
