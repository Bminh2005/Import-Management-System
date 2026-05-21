package com.app.modules.site.catalog.service;

import com.app.common.util.CurrencyUtil;
import com.app.modules.site.catalog.dto.SiteCardDTO;
import com.app.modules.site.catalog.dto.SiteCategorySectionDTO;
import com.app.modules.site.catalog.dto.SiteDetailPageDTO;
import com.app.modules.site.catalog.dto.SiteDetailResponse;
import com.app.modules.site.catalog.dto.SiteInfoRowDTO;
import com.app.modules.site.catalog.dto.SiteItemRowDTO;
import com.app.modules.site.catalog.entity.Site;
import com.app.modules.site.catalog.entity.SiteItem;
import com.app.modules.site.catalog.repository.SiteRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SiteService {
  private static SiteService instance;

  private final SiteRepository repository = SiteRepository.getInstance();

  private SiteService() {}

  public static SiteService getInstance() {
    if (instance == null) {
      instance = new SiteService();
    }
    return instance;
  }

  public List<SiteCardDTO> searchSiteCards(String searchQuery, String statusFilter) {
    String q = searchQuery == null ? "" : searchQuery.toLowerCase();
    List<SiteCardDTO> cards = new ArrayList<>();
    for (Site site : repository.findAll()) {
      if (!matchesStatus(site, statusFilter) || !matchesSearch(site, q)) {
        continue;
      }
      cards.add(toSiteCardDTO(site));
    }
    return cards;
  }

  public SiteDetailPageDTO getSiteDetailPage(String siteId) {
    Site site =
        repository.findById(siteId).orElse(repository.findAll().get(0));
    SiteDetailResponse detail = buildSiteDetailResponse(site);
    return toSiteDetailPageDTO(detail);
  }

  private SiteDetailResponse buildSiteDetailResponse(Site site) {
    List<SiteItem> items = repository.findItemsBySiteId(site.getId());
    return new SiteDetailResponse(
        site.getCode(),
        "Site A - Hà Nội",
        "Tầng 5, Tòa nhà ABC, Số 10 Lê Duẩn",
        "Hoàn Kiếm",
        "Hà Nội",
        "Việt Nam",
        "Nguyễn Văn An",
        "024-3942-1234",
        "an.nguyen@siteA.com",
        500000,
        "2-3 ngày",
        site.getStatus(),
        4.5,
        245,
        items);
  }

  private SiteCardDTO toSiteCardDTO(Site site) {
    SiteCardDTO dto = new SiteCardDTO();
    dto.setSiteId(site.getId());
    dto.setCode(site.getCode());
    dto.setName(site.getName());
    dto.setWarehouse(site.getWarehouse());
    dto.setLocation(site.getLocation());
    dto.setActive(site.isActive());
    dto.setStatusLabel(site.isActive() ? "Hoạt động" : "Không hoạt động");
    dto.setInventoryText(String.format("%,d mặt hàng", site.getTotalInventory()));
    return dto;
  }

  private SiteDetailPageDTO toSiteDetailPageDTO(SiteDetailResponse site) {
    SiteDetailPageDTO dto = new SiteDetailPageDTO();
    dto.setSiteId(site.getSiteId());
    dto.setPageTitle("Chi tiết Site: " + site.getSiteName());
    dto.setStatusLabel(resolveStatusLabel(site.getStatus()));
    dto.setStatusStyleClass(
        "active".equals(site.getStatus()) ? "badge-active" : "badge-inactive");
    dto.setShippingCostText(CurrencyUtil.format(site.getShippingCost()));
    dto.setDeliveryText(site.getEstimatedDelivery());
    dto.setRatingText(String.valueOf(site.getRating()));
    dto.setTotalOrdersText(String.valueOf(site.getTotalOrders()));
    dto.setAvailableItemsText(String.valueOf(site.getItems().size()));
    dto.setTotalItemsSummary("Tổng: " + site.getItems().size() + " mặt hàng");

    long totalValue =
        site.getItems().stream()
            .mapToLong(i -> (long) i.getAvailableQuantity() * i.getUnitPrice())
            .sum();
    dto.setInventoryValueText(CurrencyUtil.format(totalValue));

    dto.getInfoRows()
        .addAll(
            List.of(
                infoRow(
                    "📍",
                    "ĐỊA CHỈ",
                    site.getAddress(),
                    site.getDistrict() + ", " + site.getCity() + ", " + site.getCountry(),
                    false,
                    true),
                infoRow("👤", "NGƯỜI LIÊN HỆ", site.getContactPerson(), null, false, false),
                infoRow("📞", "SỐ ĐIỆN THOẠI", site.getContactPhone(), null, true, false),
                infoRow("✉", "EMAIL", site.getContactEmail(), null, true, false)));

    Map<String, List<SiteItem>> grouped = new LinkedHashMap<>();
    for (SiteItem item : site.getItems()) {
      grouped.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
    }
    for (Map.Entry<String, List<SiteItem>> entry : grouped.entrySet()) {
      SiteCategorySectionDTO section = new SiteCategorySectionDTO();
      section.setCategoryName(entry.getKey());
      section.setItemCountText(entry.getValue().size() + " mặt hàng");
      for (SiteItem item : entry.getValue()) {
        SiteItemRowDTO row = new SiteItemRowDTO();
        row.setCode(item.getCode());
        row.setName(item.getName());
        row.setQuantityText(item.getAvailableQuantity() + " " + item.getUnit());
        row.setUnitPriceText(CurrencyUtil.format(item.getUnitPrice()));
        row.setLastUpdated(item.getLastUpdated());
        section.getItems().add(row);
      }
      dto.getCategorySections().add(section);
    }
    return dto;
  }

  private SiteInfoRowDTO infoRow(
      String icon, String label, String primary, String secondary, boolean link, boolean sep) {
    SiteInfoRowDTO dto = new SiteInfoRowDTO();
    dto.setIcon(icon);
    dto.setLabel(label);
    dto.setPrimaryText(primary);
    dto.setSecondaryText(secondary);
    dto.setLinkStyle(link);
    dto.setShowSeparatorAfter(sep);
    return dto;
  }

  private boolean matchesStatus(Site site, String statusFilter) {
    return "all".equals(statusFilter) || statusFilter.equals(site.getStatus());
  }

  private boolean matchesSearch(Site site, String q) {
    if (q.isBlank()) {
      return true;
    }
    return site.getCode().toLowerCase().contains(q)
        || site.getName().toLowerCase().contains(q)
        || site.getLocation().toLowerCase().contains(q);
  }

  private String resolveStatusLabel(String status) {
    return switch (status) {
      case "active" -> "Đang hoạt động";
      case "inactive" -> "Ngưng hoạt động";
      case "maintenance" -> "Bảo trì";
      default -> status;
    };
  }
}
