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
import com.heaven.vegetable.model.ProfileObject;

import java.util.ArrayList;


/**
 * Created by Pradeep on 01-Sep-2019.
 */

public class RecycleAdapterProfile extends RecyclerView.Adapter<RecycleAdapterProfile.ViewHolder> {

    private Context context;
    private ArrayList<ProfileObject> profileObj;

    private OnRecyclerViewClickListener clickListener;

    public RecycleAdapterProfile(Context context, ArrayList<ProfileObject> profileObj) {
        this.context = context;
        this.profileObj = profileObj;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_profile,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(profileObj.get(position).getTitle());
        holder.ivIcon.setImageResource(profileObj.get(position).getIcon());
    }

    @Override
    public int getItemCount() {return profileObj.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            ivIcon = itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
