package com.app.modules.procurement.product.dto;

import com.app.modules.procurement.product.entity.Merchandise;

public class MerchandiseTableRowDTO {
  public enum RowType {
    MAIN,
    DETAIL
  }

  private final RowType type;
  private final Merchandise merchandise;

  public MerchandiseTableRowDTO(Merchandise merchandise, RowType type) {
    this.merchandise = merchandise;
    this.type = type;
  }

  public RowType getType() {
    return type;
  }

  public Merchandise getMerchandise() {
    return merchandise;
  }

  public boolean isDetail() {
    return type == RowType.DETAIL;
  }
}
