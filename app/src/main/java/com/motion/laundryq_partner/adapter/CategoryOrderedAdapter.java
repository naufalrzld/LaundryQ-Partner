package com.motion.laundryq_partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.utils.CurrencyConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_INTENT_LIST_ORDER;

public class CategoryOrderedAdapter extends RecyclerView.Adapter<CategoryOrderedAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> categories;
    private int status;
    private OnButtonUpdateClicked onButtonUpdateClicked;

    public interface OnButtonUpdateClicked {
        void onButtonClicked(int status, int position);
    }

    public CategoryOrderedAdapter(Context context, int status) {
        this.context = context;
        categories = new ArrayList<>();
        this.status = status;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setOnButtonUpdateClicked(OnButtonUpdateClicked onButtonUpdateClicked) {
        this.onButtonUpdateClicked = onButtonUpdateClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_laundry_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CategoryModel cm = categories.get(position);

        String icon = cm.getIcon();
        String categoryName = cm.getCategoryName();
        int categoryPrice = cm.getCategoryPrice();
        int quantity = cm.getQuantity();
        int total = categoryPrice * quantity;
        String quantityString = quantity + " " + cm.getCategoryUnit();
        String statusMsg = "-";
        final int status = cm.getStatus();
        if (status == 0) {
            statusMsg = "Belum dicuci";
        } else if (status == 1) {
            statusMsg = "Dicuci";
        } else if (status == 2) {
            statusMsg = "Selesai";
        }

        if (this.status == KEY_INTENT_LIST_ORDER) {
            holder.lytStatus.setVisibility(View.GONE);
            holder.vBorder.setVisibility(View.GONE);
            holder.btnUpdateStatus.setVisibility(View.GONE);
        }

        holder.tvStatus.setText(statusMsg);
        Glide.with(context).load(icon).into(holder.imgCategory);
        holder.tvCategoryName.setText(categoryName);
        holder.tvQuantity.setText(quantityString);
        holder.tvTotal.setText(CurrencyConverter.toIDR(total));
        holder.btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                onButtonUpdateClicked.onButtonClicked(status, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lyt_status)
        LinearLayout lytStatus;
        @BindView(R.id.v_border)
        View vBorder;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.img_category)
        ImageView imgCategory;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.btn_update_status)
        Button btnUpdateStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
