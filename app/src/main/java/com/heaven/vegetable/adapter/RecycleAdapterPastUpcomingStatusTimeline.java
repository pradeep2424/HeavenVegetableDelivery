package com.heaven.vegetable.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.heaven.vegetable.R;
import com.heaven.vegetable.interfaces.OnPastOrderOptionsClickListener;
import com.heaven.vegetable.model.OrderStatusEnum;
import com.heaven.vegetable.model.OrderTimelineObject;
import com.heaven.vegetable.utils.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapterPastUpcomingStatusTimeline extends RecyclerView.Adapter<RecycleAdapterPastUpcomingStatusTimeline.TimeLineViewHolder> {
    private Context context;
    private OnPastOrderOptionsClickListener clickListener;

    private ArrayList<OrderTimelineObject> listOrderStatus;

    public RecycleAdapterPastUpcomingStatusTimeline(Context context, ArrayList<OrderTimelineObject> listOrderStatus) {
        this.context = context;
        this.listOrderStatus = listOrderStatus;
    }

    public void setClickListener(OnPastOrderOptionsClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView timelineView;
        TextView tvTitle;
        TextView tvDate;
        TextView tvMessage;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);

            timelineView = itemView.findViewById(R.id.timeline);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvMessage = itemView.findViewById(R.id.tv_message);

            timelineView.initLine(viewType);

//            timelineView.setMarkerSize(Utils.dpToPx(15f));
//            timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//            timelineView.setMarkerInCenter(false);
//            timelineView.setMarkerPaddingBottom(Utils.dpToPx(0f));
//            timelineView.setMarkerPaddingTop(Utils.dpToPx(0f));
//            timelineView.setMarkerPaddingLeft(Utils.dpToPx(0f));
//            timelineView.setMarkerPaddingRight(Utils.dpToPx(0f));
////            timelineView.setLinePadding(Utils.dpToPx(2f));
//            timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), 1);
//            timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorAccent), 1);
//            timelineView.setLineStyle(TimelineView.LineStyle.NORMAL);
//            timelineView.setLineWidth(Utils.dpToPx(2f));
//            timelineView.setLineStyleDashLength(Utils.dpToPx(4f));
//            timelineView.setLineStyleDashGap(Utils.dpToPx(2f));
        }
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_order_status_timeline, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        OrderTimelineObject orderStatus = listOrderStatus.get(position);

        String title = orderStatus.getTitle();
        String date = orderStatus.getDate();
        String message = orderStatus.getMessage();
        int viewType = orderStatus.getViewType();
        OrderStatusEnum orderStatusEnum = orderStatus.getStatus();

        holder.tvTitle.setText(title);
        holder.tvDate.setText(date);
        holder.tvMessage.setText(message);

        setupStatusMarker(holder.timelineView, orderStatusEnum, viewType);
//        setupStatusLine(holder.timelineView, viewType);
    }

    private void setupStatusMarker(TimelineView timelineView, OrderStatusEnum orderStatusEnum, int viewType) {

        switch (orderStatusEnum) {
            case ACTIVE:
                Drawable drawable1 = ContextCompat.getDrawable(context, R.drawable.ic_marker_active);
                timelineView.setMarker(drawable1);

                setupStatusLine(timelineView, orderStatusEnum, viewType);

//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);

                break;

            case COMPLETED:

                Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.ic_marker);
                timelineView.setMarker(drawable2);

                setupStatusLine(timelineView, orderStatusEnum, viewType);

//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
                break;

            case INACTIVE:
                Drawable drawable3 = ContextCompat.getDrawable(context, R.drawable.ic_marker_inactive);
                timelineView.setMarker(drawable3);

                setupStatusLine(timelineView, orderStatusEnum, viewType);

//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.divider_dark));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
                break;

            default:
                Drawable drawable4 = ContextCompat.getDrawable(context, R.drawable.ic_marker_active);
                timelineView.setMarker(drawable4);

                setupStatusLine(timelineView, orderStatusEnum, viewType);

//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
        }
    }

    private void setupStatusLine(TimelineView timelineView, OrderStatusEnum orderStatusEnum, int viewType) {
        int color;
        if (orderStatusEnum == OrderStatusEnum.INACTIVE) {
            color = R.color.divider_dark;
        } else {
            color = R.color.colorPrimary;
        }

        timelineView.setMarkerColor(ContextCompat.getColor(context, color));
        timelineView.setStartLineColor(ContextCompat.getColor(context, color), viewType);
        timelineView.setEndLineColor(ContextCompat.getColor(context, color), viewType);

//        switch (viewType) {
//            case TimelineView.LineType.START:
//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//
//                break;
//
//            case TimelineView.LineType.NORMAL:
//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                break;
//
//            case TimelineView.LineType.END:
////                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.divider_dark));
////                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
////                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
//                break;
//
//            case TimelineView.LineType.ONLYONE:
////                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.divider_dark));
////                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
////                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
//                break;
//
//            default:
////                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
////                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
////                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//        }
    }


    @Override
    public int getItemCount() {
        return listOrderStatus.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
