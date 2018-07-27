package com.motion.laundryq_partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private static final String SEPARATOR = ", ";
    private Context context;
    private List<OrderModel> orderList;

    private OnCardViewClicked onCardViewClicked;
    private OnButtonAcceptClicked onButtonAcceptClicked;
    private OnButtonDeclineClicked onButtonDeclineClicked;

    public OrderAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
    }

    public interface OnCardViewClicked {
        void onCardClick(OrderModel orderModel);
    }

    public interface OnButtonAcceptClicked {
        void onAccClick(String orderID, String laundryID);
    }

    public interface OnButtonDeclineClicked {
        void onDecClick(String orderID, String laundryID);
    }

    public void setOnCardViewClicked(OnCardViewClicked onCardViewClicked) {
        this.onCardViewClicked = onCardViewClicked;
    }

    public void setOnButtonAcceptClicked(OnButtonAcceptClicked onButtonAcceptClicked) {
        this.onButtonAcceptClicked = onButtonAcceptClicked;
    }

    public void setOnButtonDeclineClicked(OnButtonDeclineClicked onButtonDeclineClicked) {
        this.onButtonDeclineClicked = onButtonDeclineClicked;
    }

    public void setOrderList(List<OrderModel> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderModel orderModel = orderList.get(position);
        List<CategoryModel> categories = orderModel.getCategories();

        final String laundryID = orderModel.getLaundryID();

        StringBuilder stringBuilder = new StringBuilder();
        for (CategoryModel cm : categories) {
            stringBuilder.append(cm.getCategoryName());
            stringBuilder.append(SEPARATOR);
        }

        String category = stringBuilder.toString();
        category = category.substring(0, category.length() - SEPARATOR.length());

        final String orderID = orderModel.getOrderID();
        String datePickup = orderModel.getDatePickup();
        String dateDelivery = orderModel.getDateDelivery();
        String timePickup = orderModel.getTimePickup();
        String timeDelivery = orderModel.getTimeDelivery();

        holder.tvOrderID.setText(orderID);
        holder.tvCategoryName.setText(category);
        holder.tvDatePickup.setText(datePickup);
        holder.tvDateDelivery.setText(dateDelivery);
        holder.tvTimePickup.setText(timePickup);
        holder.tvTimeDelivery.setText(timeDelivery);
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardViewClicked.onCardClick(orderModel);
            }
        });
        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonDeclineClicked.onDecClick(orderID, laundryID);
            }
        });
        holder.btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonAcceptClicked.onAccClick(orderID, laundryID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_item)
        CardView cvItem;
        @BindView(R.id.tv_order_id)
        TextView tvOrderID;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_date_pickup)
        TextView tvDatePickup;
        @BindView(R.id.tv_date_delivery)
        TextView tvDateDelivery;
        @BindView(R.id.tv_time_pickup)
        TextView tvTimePickup;
        @BindView(R.id.tv_time_delivery)
        TextView tvTimeDelivery;
        @BindView(R.id.btn_decline)
        Button btnDecline;
        @BindView(R.id.btn_accept)
        Button btnAcc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
