package com.app.modules.site.catalog.entity;

import javafx.beans.property.*;

public class Site {
  private final StringProperty id = new SimpleStringProperty();
  private final StringProperty code = new SimpleStringProperty();
  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty warehouse = new SimpleStringProperty();
  private final StringProperty location = new SimpleStringProperty();
  private final StringProperty status = new SimpleStringProperty();
  private final IntegerProperty totalInventory = new SimpleIntegerProperty();

  public Site(
      String id,
      String code,
      String name,
      String warehouse,
      String location,
      String status,
      int totalInventory) {
    this.id.set(id);
    this.code.set(code);
    this.name.set(name);
    this.warehouse.set(warehouse);
    this.location.set(location);
    this.status.set(status);
    this.totalInventory.set(totalInventory);
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

  public String getWarehouse() {
    return warehouse.get();
  }

  public String getLocation() {
    return location.get();
  }

  public String getStatus() {
    return status.get();
  }

  public int getTotalInventory() {
    return totalInventory.get();
  }

  public StringProperty codeProperty() {
    return code;
  }

  public StringProperty nameProperty() {
    return name;
  }

  public boolean isActive() {
    return "active".equals(status.get());
  }
}
