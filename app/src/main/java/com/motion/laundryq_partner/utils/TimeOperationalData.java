package com.motion.laundryq_partner.utils;

import com.motion.laundryq_partner.model.TimeOperationModel;

import java.util.ArrayList;
import java.util.List;

public class TimeOperationalData {
    public static List<TimeOperationModel> setTimeOperational() {
        List<TimeOperationModel> list = new ArrayList<>();

        String days[] = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
        String timeOpen = "09.00";
        String timeClosed = "21.00";

        TimeOperationModel minggu = new TimeOperationModel(days[0], timeOpen, timeClosed);
        TimeOperationModel senin = new TimeOperationModel(days[1], timeOpen, timeClosed);
        TimeOperationModel selasa = new TimeOperationModel(days[2], timeOpen, timeClosed);
        TimeOperationModel rabu = new TimeOperationModel(days[3], timeOpen, timeClosed);
        TimeOperationModel kamis = new TimeOperationModel(days[4], timeOpen, timeClosed);
        TimeOperationModel jumat = new TimeOperationModel(days[5], timeOpen, timeClosed);
        TimeOperationModel sabtu = new TimeOperationModel(days[6], timeOpen, timeClosed);

        list.add(0, minggu);
        list.add(1, senin);
        list.add(2, selasa);
        list.add(3, rabu);
        list.add(4, kamis);
        list.add(5, jumat);
        list.add(6, sabtu);

        return list;
    }
}
