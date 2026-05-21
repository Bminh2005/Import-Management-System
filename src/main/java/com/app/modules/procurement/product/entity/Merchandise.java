package com.app.modules.procurement.product.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Merchandise {
  private final StringProperty id = new SimpleStringProperty();
  private final StringProperty code = new SimpleStringProperty();
  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty unit = new SimpleStringProperty();
  private final StringProperty category = new SimpleStringProperty();
  private final StringProperty status = new SimpleStringProperty();
  private final StringProperty price = new SimpleStringProperty();
  private final StringProperty supplier = new SimpleStringProperty();
  private final StringProperty description = new SimpleStringProperty();

  public Merchandise(
      String id,
      String code,
      String name,
      String unit,
      String category,
      String status,
      String price,
      String supplier,
      String description) {
    this.id.set(id);
    this.code.set(code);
    this.name.set(name);
    this.unit.set(unit);
    this.category.set(category);
    this.status.set(status);
    this.price.set(price);
    this.supplier.set(supplier);
    this.description.set(description);
  }

  public String getId() {
    return id.get();
  }

  public String getCode() {
    return code.get();
  }

  public String getName() {
    return name.get();
  }

  public String getUnit() {
    return unit.get();
  }

  public String getCategory() {
    return category.get();
  }

  public String getStatus() {
    return status.get();
  }

  public String getPrice() {
    return price.get();
  }

  public String getSupplier() {
    return supplier.get();
  }

  public String getDescription() {
    return description.get();
  }

  public StringProperty codeProperty() {
    return code;
  }

  public StringProperty nameProperty() {
    return name;
  }

  public StringProperty unitProperty() {
    return unit;
  }

  public StringProperty categoryProperty() {
    return category;
  }

  public StringProperty statusProperty() {
    return status;
  }

  public boolean isActive() {
    return "active".equals(status.get());
  }

  public String getCategoryKey() {
    return switch (category.get()) {
      case "Điện tử" -> "electronics";
      case "Phụ kiện" -> "accessories";
      case "Nội thất" -> "furniture";
      default -> "other";
    };
  }
}
