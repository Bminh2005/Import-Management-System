package com.app.modules.procurement.product.service;

import com.app.modules.procurement.product.dto.AddMerchandiseDTO;
import com.app.modules.procurement.product.dto.MerchandiseTableRowDTO;
import com.app.modules.procurement.product.dto.MerchandiseTableRowDTO.RowType;
import com.app.modules.procurement.product.entity.Merchandise;
import com.app.modules.procurement.product.repository.MerchandiseRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MerchandiseService {
  private static MerchandiseService instance;

  private final MerchandiseRepository repository = MerchandiseRepository.getInstance();

  private MerchandiseService() {}

  public static MerchandiseService getInstance() {
    if (instance == null) {
      instance = new MerchandiseService();
    }
    return instance;
  }

  public String nextMerchandiseCode() {
    return String.format("MH%03d", repository.findMaxCodeSequence() + 1);
  }

  public List<Merchandise> searchMerchandise(String searchQuery, String categoryKey) {
    String q = searchQuery == null ? "" : searchQuery.toLowerCase();
    List<Merchandise> result = new ArrayList<>();
    for (Merchandise m : repository.findAll()) {
      if (!q.isBlank()
          && !m.getCode().toLowerCase().contains(q)
          && !m.getName().toLowerCase().contains(q)) {
        continue;
      }
      if (!"all".equals(categoryKey) && !categoryKey.equals(m.getCategoryKey())) {
        continue;
      }
      result.add(m);
    }
    return result;
  }

  public List<MerchandiseTableRowDTO> buildTableRows(
      List<Merchandise> items, Set<String> expandedIds) {
    List<MerchandiseTableRowDTO> rows = new ArrayList<>();
    for (Merchandise m : items) {
      rows.add(new MerchandiseTableRowDTO(m, RowType.MAIN));
      if (expandedIds.contains(m.getId())) {
        rows.add(new MerchandiseTableRowDTO(m, RowType.DETAIL));
      }
    }
    return rows;
  }

  public String buildPaginationSummary(int visibleCount) {
    return String.format("Hiển thị 1-%d trong 24 mặt hàng", Math.min(visibleCount, 4));
  }

  /** Validate và tạo mặt hàng mới. Trả về null nếu thành công, message lỗi nếu thất bại. */
  public String createMerchandise(AddMerchandiseDTO form) {
    if (form.getName().isBlank() || form.getUnit().isBlank() || form.getCategory() == null) {
      return "Thiếu trường bắt buộc";
    }
    String code = nextMerchandiseCode();
    String price = form.getPrice().isBlank() ? "" : form.getPrice() + " VND";
    repository.insert(
        new Merchandise(
            String.valueOf(System.currentTimeMillis()),
            code,
            form.getName(),
            form.getUnit(),
            form.getCategory(),
            "active",
            price,
            form.getSupplier(),
            form.getDescription()));
    return null;
  }
}
