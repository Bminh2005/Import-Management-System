package com.app.modules.procurement.product.repository;

import com.app.modules.procurement.product.entity.Merchandise;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Truy cập dữ liệu mặt hàng (mock — thay bằng DB sau). */
public class MerchandiseRepository {
  private static MerchandiseRepository instance;

  private final List<Merchandise> merchandise = new ArrayList<>();

  private MerchandiseRepository() {
    seed();
  }

  public static MerchandiseRepository getInstance() {
    if (instance == null) {
      instance = new MerchandiseRepository();
    }
    return instance;
  }

  private void seed() {
    merchandise.addAll(
        List.of(
            new Merchandise(
                "1",
                "MH001",
                "Laptop Dell XPS 13",
                "cái",
                "Điện tử",
                "active",
                "25,000,000 VND",
                "Dell Vietnam",
                "Laptop cao cấp cho doanh nghiệp"),
            new Merchandise(
                "2",
                "MH002",
                "Bàn phím cơ Keychron K2",
                "cái",
                "Phụ kiện",
                "active",
                "2,500,000 VND",
                "Keychron",
                "Bàn phím cơ không dây"),
            new Merchandise(
                "3",
                "MH003",
                "Chuột Logitech MX Master 3",
                "cái",
                "Phụ kiện",
                "active",
                "2,200,000 VND",
                "Logitech",
                "Chuột không dây cao cấp"),
            new Merchandise(
                "4",
                "MH004",
                "Màn hình LG UltraWide 34\"",
                "cái",
                "Điện tử",
                "inactive",
                "12,000,000 VND",
                "LG Electronics",
                "Màn hình siêu rộng cho đa nhiệm")));
  }

  public List<Merchandise> findAll() {
    return List.copyOf(merchandise);
  }

  public void insert(Merchandise item) {
    merchandise.add(item);
  }

  public int findMaxCodeSequence() {
    return merchandise.stream()
        .mapToInt(m -> Integer.parseInt(m.getCode().substring(2)))
        .max()
        .orElse(4);
  }

  public Optional<Merchandise> findById(String id) {
    return merchandise.stream().filter(m -> m.getId().equals(id)).findFirst();
  }
}
