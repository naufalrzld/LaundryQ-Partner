package com.motion.laundryq_partner.utils;

import com.motion.laundryq_partner.model.TimeOperationalModel;

import java.util.ArrayList;
import java.util.List;

public class TimeOperationalData {
    public static List<TimeOperationalModel> getTimeOperational() {
        List<TimeOperationalModel> list = new ArrayList<>();

        String days[] = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
        String timeOpen = "09:00";
        String timeClosed = "21:00";

        TimeOperationalModel minggu = new TimeOperationalModel(days[0], 0, timeOpen, timeClosed);
        TimeOperationalModel senin = new TimeOperationalModel(days[1], 1, timeOpen, timeClosed);
        TimeOperationalModel selasa = new TimeOperationalModel(days[2], 2, timeOpen, timeClosed);
        TimeOperationalModel rabu = new TimeOperationalModel(days[3], 3, timeOpen, timeClosed);
        TimeOperationalModel kamis = new TimeOperationalModel(days[4], 4, timeOpen, timeClosed);
        TimeOperationalModel jumat = new TimeOperationalModel(days[5], 5, timeOpen, timeClosed);
        TimeOperationalModel sabtu = new TimeOperationalModel(days[6], 6, timeOpen, timeClosed);

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
