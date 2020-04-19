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
import com.heaven.vegetable.listeners.OnRecyclerViewClickListener;
import com.heaven.vegetable.model.CategoryObject;
import com.heaven.vegetable.model.UnitObject;

import java.util.ArrayList;

public class RecycleAdapterUnit extends RecyclerView.Adapter<RecycleAdapterUnit.ViewHolder> {
    Context context;
    private OnRecyclerViewClickListener clickListener;

    ArrayList<UnitObject> listUnits;

    public RecycleAdapterUnit(Context context, ArrayList<UnitObject> listUnits) {
        this.context = context;
        this.listUnits = listUnits;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llUnitMainLayout;
        TextView tvUnitTitle;
        TextView tvUnitSubTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            llUnitMainLayout = itemView.findViewById(R.id.ll_unitLayout);
            tvUnitTitle = itemView.findViewById(R.id.tv_unitTitle);
            tvUnitSubTitle = itemView.findViewById(R.id.tv_unitSubTitle);

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
    public RecycleAdapterUnit.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_unit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterUnit.ViewHolder holder, int position) {
        UnitObject unitObject = listUnits.get(position);
        holder.tvUnitTitle.setText(unitObject.getUnitName());

        if (unitObject.getIsChecked()) {
            holder.llUnitMainLayout.setBackground(context.getResources().getDrawable(R.drawable.rect2));

        } else {
            holder.llUnitMainLayout.setBackground(context.getResources().getDrawable(R.drawable.rect1));
        }
    }

    @Override
    public int getItemCount() {
        return listUnits.size();
    }
}
