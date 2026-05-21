package com.app.common.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtil {
  private static final NumberFormat VND =
      NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

  private CurrencyUtil() {}

  public static String format(long value) {
    return VND.format(value);
  }
}
