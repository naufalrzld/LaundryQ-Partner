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

public class CucianAdapter extends RecyclerView.Adapter<CucianAdapter.ViewHolder> {
    private static final String SEPARATOR = ", ";
    private Context context;
    private List<OrderModel> orderList;
    private OnButtonDetailClicked onButtonUpdateClicked;

    public interface OnButtonDetailClicked {
        void onButtonClick(OrderModel orderModel);
    }

    public CucianAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
    }

    public void setData(List<OrderModel> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public void setOnButtonDetailClicked(OnButtonDetailClicked onButtonDetailClicked) {
        this.onButtonUpdateClicked = onButtonDetailClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cucian_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderModel orderModel = orderList.get(position);
        List<CategoryModel> categories = orderModel.getCategories();

        StringBuilder stringBuilder = new StringBuilder();
        for (CategoryModel cm : categories) {
            stringBuilder.append(cm.getCategoryName());
            stringBuilder.append(SEPARATOR);
        }

        String category = stringBuilder.toString();
        category = category.substring(0, category.length() - SEPARATOR.length());

        String orderID = orderModel.getOrderID();
        String userID = orderModel.getUserID();

        int status = orderModel.getStatus();
        String statusMsg = "-";

        if (status == 1) {
            statusMsg = "Diterima";
        } else if (status == 3) {
            statusMsg = "Dicuci";
        } else if (status == 4) {
            statusMsg = "Cuci selesai";
        }

        holder.tvOrderID.setText(orderID);
        holder.tvCategoryName.setText(category);
        holder.tvName.setText(userID);
        holder.tvStatus.setText(statusMsg);
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonUpdateClicked.onButtonClick(orderModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_id)
        TextView tvOrderID;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.btn_detail)
        Button btnDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
