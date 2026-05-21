package com.app.modules.site.catalog.repository;

import com.app.modules.site.catalog.entity.Site;
import com.app.modules.site.catalog.entity.SiteItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Truy cập dữ liệu Site (mock — thay bằng DB sau). */
public class SiteRepository {
  private static SiteRepository instance;

  private final List<Site> sites = new ArrayList<>();

  private SiteRepository() {
    seed();
  }

  public static SiteRepository getInstance() {
    if (instance == null) {
      instance = new SiteRepository();
    }
    return instance;
  }

  private void seed() {
    sites.addAll(
        List.of(
            new Site(
                "1",
                "SITE01",
                "Kho Hong Kong",
                "Hong Kong Central Warehouse",
                "Hong Kong",
                "active",
                15000),
            new Site(
                "2",
                "SITE02",
                "Kho Singapore",
                "Singapore Distribution Center",
                "Singapore",
                "active",
                12000),
            new Site(
                "3",
                "SITE03",
                "Kho Bangkok",
                "Bangkok Logistics Hub",
                "Bangkok, Thailand",
                "active",
                8500),
            new Site(
                "4",
                "SITE04",
                "Kho Shanghai",
                "Shanghai Regional Warehouse",
                "Shanghai, China",
                "active",
                20000),
            new Site(
                "5",
                "SITE05",
                "Kho Kuala Lumpur",
                "KL Storage Facility",
                "Kuala Lumpur, Malaysia",
                "inactive",
                3000)));
  }

  public List<Site> findAll() {
    return List.copyOf(sites);
  }

  public Optional<Site> findById(String id) {
    return sites.stream().filter(s -> s.getId().equals(id)).findFirst();
  }

  /** Mock: trả về danh sách mặt hàng tại site. */
  public List<SiteItem> findItemsBySiteId(String siteId) {
    return List.of(
        new SiteItem("MH001", "Laptop Dell XPS 13", "Máy tính", 10, "cái", 25000000, "2024-05-10"),
        new SiteItem("MH002", "Bàn phím cơ Keychron K2", "Phụ kiện", 25, "cái", 2700000, "2024-05-10"),
        new SiteItem("MH003", "Chuột Logitech MX Master 3", "Phụ kiện", 15, "cái", 2000000, "2024-05-09"),
        new SiteItem("MH004", "Màn hình LG UltraWide 34\"", "Màn hình", 8, "cái", 12000000, "2024-05-08"),
        new SiteItem("MH005", "Webcam Logitech C920", "Phụ kiện", 20, "cái", 1800000, "2024-05-10"),
        new SiteItem("MH006", "Tai nghe Sony WH-1000XM4", "Audio", 12, "cái", 7500000, "2024-05-09"));
  }
}
