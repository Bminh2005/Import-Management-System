package com.app.common.util;

/** Ghi log tạm cho các thao tác chưa triển khai backend. */
public final class ActionLog {

  private ActionLog() {}

  public static void stub(String functionDescription) {
    System.out.println(functionDescription);
  }
}
