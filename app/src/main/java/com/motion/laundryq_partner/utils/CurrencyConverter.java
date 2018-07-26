package com.motion.laundryq_partner.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyConverter {
    public static String toIDR(int nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        kursIndonesia.setMaximumFractionDigits(0);

        return kursIndonesia.format(nominal);
    }
}
