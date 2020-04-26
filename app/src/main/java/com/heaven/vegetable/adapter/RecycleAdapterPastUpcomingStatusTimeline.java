package com.heaven.vegetable.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.heaven.vegetable.R;
import com.heaven.vegetable.listeners.OnPastOrderOptionsClickListener;
import com.heaven.vegetable.model.OrderDetailsObject;
import com.heaven.vegetable.model.OrderStatusEnum;
import com.heaven.vegetable.model.OrderTimelineObject;
import com.heaven.vegetable.model.ProductObject;
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

            timelineView.setMarkerSize(Utils.dpToPx(20f));
            timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
            timelineView.setMarkerInCenter(true);
            timelineView.setMarkerPaddingBottom(Utils.dpToPx(0f));
            timelineView.setMarkerPaddingTop(Utils.dpToPx(0f));
            timelineView.setMarkerPaddingLeft(Utils.dpToPx(0f));
            timelineView.setMarkerPaddingRight(Utils.dpToPx(0f));
            timelineView.setLinePadding(Utils.dpToPx(2f));
            timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), 0 );
            timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorAccent), 0 );
            timelineView.setLineStyle(TimelineView.LineStyle.NORMAL);
            timelineView.setLineWidth(Utils.dpToPx(2f));
            timelineView.setLineStyleDashLength(Utils.dpToPx(4f));
            timelineView.setLineStyleDashGap(Utils.dpToPx(2f));
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
        OrderStatusEnum orderStatusEnum = orderStatus.getStatus();

        holder.tvTitle.setText(title);
        holder.tvDate.setText(date);
        holder.tvMessage.setText(message);

        setupStatusLine(holder.timelineView, orderStatusEnum);
    }

    private void setupStatusLine(TimelineView timelineView, OrderStatusEnum orderStatusEnum) {
        int viewType = 1;


//        timelineView.setMarkerSize(); = dpToPx(20f),
//                markerColor = getColorCompat( R.color.material_grey_500),
//                markerInCenter = true,
//                markerLeftPadding = dpToPx(0f),
//                markerTopPadding = dpToPx(0f),
//                markerRightPadding = dpToPx(0f),
//                markerBottomPadding = dpToPx(0f),
//                linePadding = dpToPx(2f),
//                startLineColor = getColorCompat(R.color.colorAccent),
//                endLineColor = getColorCompat(R.color.colorAccent),
//                lineStyle = TimelineView.LineStyle.NORMAL,
//                lineWidth = dpToPx(2f),
//                lineDashWidth = dpToPx(4f),
//                lineDashGap = dpToPx(2f)


        switch (orderStatusEnum) {
            case ACTIVE:
                Drawable drawable1 = ContextCompat.getDrawable(context, R.drawable.ic_marker_active);
                timelineView.setMarker(drawable1);
//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);

                break;

            case COMPLETED:

                Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.ic_marker);
                timelineView.setMarker(drawable2);
//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
                break;

            case INACTIVE:
                Drawable drawable3 = ContextCompat.getDrawable(context, R.drawable.ic_marker_inactive);
                timelineView.setMarker(drawable3);
//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.divider_dark));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.divider_dark), viewType);
                break;

            default:
                Drawable drawable4 = ContextCompat.getDrawable(context, R.drawable.ic_marker_active);
                timelineView.setMarker(drawable4);
//                timelineView.setMarkerColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                timelineView.setStartLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
//                timelineView.setEndLineColor(ContextCompat.getColor(context, R.color.colorPrimary), viewType);
        }
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
