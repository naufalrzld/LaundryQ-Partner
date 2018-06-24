package com.motion.laundryq_partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.CategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> listCategory;

    public interface OnItemCheckListener {
        void onItemCheck(CategoryModel categoryModel);
        void onItemUpdate(CategoryModel categoryModel);
        void onItemUncheck(CategoryModel categoryModel);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public CategoryAdapter(Context context, @NonNull OnItemCheckListener onItemCheckListener) {
        this.context = context;
        this.onItemCheckListener = onItemCheckListener;
    }

    public void setData(List<CategoryModel> listCategory) {
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CategoryModel categoryModel = listCategory.get(position);
        holder.cbCategory.setText(categoryModel.getCategoryName());
        holder.cbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbCategory.isChecked()) {
                    holder.lytPrice.setVisibility(View.VISIBLE);
                    onItemCheckListener.onItemCheck(categoryModel);
                } else {
                    holder.lytPrice.setVisibility(View.GONE);
                    onItemCheckListener.onItemUncheck(categoryModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_category)
        CheckBox cbCategory;
        @BindView(R.id.lyt_price)
        LinearLayout lytPrice;
        @BindView(R.id.et_price)
        EditText etPrice;
        @BindView(R.id.rb_pcs)
        RadioButton rbPcs;
        @BindView(R.id.rb_kg)
        RadioButton rbKg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
