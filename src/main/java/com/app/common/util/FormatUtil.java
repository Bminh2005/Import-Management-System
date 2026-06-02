package com.app.common.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtil {
    public static String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        // Định dạng: 0,000.00
        DecimalFormat formatter = new DecimalFormat("#,###.00", symbols);
        return formatter.format(price) + " VND";
    }
}
