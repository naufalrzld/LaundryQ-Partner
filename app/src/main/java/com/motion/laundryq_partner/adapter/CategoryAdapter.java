package com.motion.laundryq_partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.CategoryModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

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
        categoryModel.setCategoryPrice(0);
        categoryModel.setCategoryUnit("PCS");

        final Observable<String> priceStream = RxTextView.textChanges(holder.etPrice)
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                })
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());

        final Observer<String> priceObserver = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                if (s.length() == 0) {
                    s = "0";
                }
                categoryModel.setCategoryPrice(Integer.parseInt(s));
                onItemCheckListener.onItemUpdate(categoryModel);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        holder.cbCategory.setText(categoryModel.getCategoryName());
        holder.cbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbCategory.isChecked()) {
                    holder.lytPrice.setVisibility(View.VISIBLE);
                    onItemCheckListener.onItemCheck(categoryModel);
                    priceStream.subscribe(priceObserver);
                } else {
                    holder.lytPrice.setVisibility(View.GONE);
                    onItemCheckListener.onItemUncheck(categoryModel);
                }
            }
        });

        holder.rgCategoryUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.rb_pcs) {
                    categoryModel.setCategoryUnit("PCS");
                } else if (id == R.id.rb_kg) {
                    categoryModel.setCategoryUnit("KG");
                }
                onItemCheckListener.onItemUpdate(categoryModel);
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
        @BindView(R.id.rg_category_unit)
        RadioGroup rgCategoryUnit;
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
