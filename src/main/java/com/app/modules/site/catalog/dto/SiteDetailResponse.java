package com.app.modules.site.catalog.dto;

import com.app.modules.site.catalog.entity.SiteItem;
import java.util.List;

public class SiteDetailResponse {
  private final String siteId;
  private final String siteName;
  private final String address;
  private final String district;
  private final String city;
  private final String country;
  private final String contactPerson;
  private final String contactPhone;
  private final String contactEmail;
  private final long shippingCost;
  private final String estimatedDelivery;
  private final String status;
  private final double rating;
  private final int totalOrders;
  private final List<SiteItem> items;

  public SiteDetailResponse(
      String siteId,
      String siteName,
      String address,
      String district,
      String city,
      String country,
      String contactPerson,
      String contactPhone,
      String contactEmail,
      long shippingCost,
      String estimatedDelivery,
      String status,
      double rating,
      int totalOrders,
      List<SiteItem> items) {
    this.siteId = siteId;
    this.siteName = siteName;
    this.address = address;
    this.district = district;
    this.city = city;
    this.country = country;
    this.contactPerson = contactPerson;
    this.contactPhone = contactPhone;
    this.contactEmail = contactEmail;
    this.shippingCost = shippingCost;
    this.estimatedDelivery = estimatedDelivery;
    this.status = status;
    this.rating = rating;
    this.totalOrders = totalOrders;
    this.items = items;
  }

  public String getSiteId() {
    return siteId;
  }

  public String getSiteName() {
    return siteName;
  }

  public String getAddress() {
    return address;
  }

  public String getDistrict() {
    return district;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public long getShippingCost() {
    return shippingCost;
  }

  public String getEstimatedDelivery() {
    return estimatedDelivery;
  }

  public String getStatus() {
    return status;
  }

  public double getRating() {
    return rating;
  }

  public int getTotalOrders() {
    return totalOrders;
  }

  public List<SiteItem> getItems() {
    return items;
  }
}
