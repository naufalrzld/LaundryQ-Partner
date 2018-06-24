package com.motion.laundryq_partner.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.TimeOperationModel;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private Context context;
    private List<TimeOperationModel> timeList;

    public interface OnItemCheckListener {
        void onItemCheck(TimeOperationModel timeOperationModel);
        void onItemUpdate(TimeOperationModel timeOperationModel);
        void onItemUncheck(TimeOperationModel timeOperationModel);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public DaysAdapter(Context context, @NonNull OnItemCheckListener onItemCheckListener) {
        this.context = context;
        this.onItemCheckListener = onItemCheckListener;
    }

    public void setData(List<TimeOperationModel> timeList) {
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_operational_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TimeOperationModel timeOperationModel = timeList.get(position);

        holder.cbDay.setText(timeOperationModel.getDay());
        holder.cbDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbDay.isChecked()) {
                    holder.lytTime.setVisibility(View.VISIBLE);
                    onItemCheckListener.onItemCheck(timeOperationModel);
                } else {
                    holder.lytTime.setVisibility(View.GONE);
                    onItemCheckListener.onItemUncheck(timeOperationModel);
                }
            }
        });
        holder.tvOpen.setText(timeOperationModel.getTimeOpen());
        holder.tvClosed.setText(timeOperationModel.getTimeClose());
        holder.tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        null,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(((Activity) context).getFragmentManager(), "TimeOpen");
                tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String timeOpen;
                        if (minute < 10) {
                            timeOpen = hourOfDay + ":" + "0" + minute;
                        } else {
                            timeOpen = hourOfDay + ":" + minute;
                        }
                        holder.tvOpen.setText(timeOpen);
                        timeOperationModel.setTimeOpen(timeOpen);
                        onItemCheckListener.onItemUpdate(timeOperationModel);
                    }
                });
            }
        });
        holder.tvClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        null,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(((Activity) context).getFragmentManager(), "TimeClosed");
                tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String timeClosed;
                        if (minute < 10) {
                            timeClosed = hourOfDay + ":" + "0" + minute;
                        } else {
                            timeClosed = hourOfDay + ":" + minute;
                        }
                        holder.tvClosed.setText(timeClosed);
                        timeOperationModel.setTimeClose(timeClosed);
                        onItemCheckListener.onItemUpdate(timeOperationModel);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_day)
        CheckBox cbDay;
        @BindView(R.id.lyt_time)
        LinearLayout lytTime;
        @BindView(R.id.tv_time_open)
        TextView tvOpen;
        @BindView(R.id.tv_time_closed)
        TextView tvClosed;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
