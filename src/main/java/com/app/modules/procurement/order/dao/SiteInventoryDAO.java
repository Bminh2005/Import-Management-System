package com.app.modules.procurement.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.app.database.manager.DatabaseManager;
import com.app.modules.procurement.order.model.SiteInventoryInfo;

public class SiteInventoryDAO {
    private static final String FIND_AVAILABLE_SITES_SQL = "SELECT si.site_id, s.site_name, s.site_address, "
            + "s.delivery_by_ship, s.delivery_by_air, si.quantity, si.price "
            + "FROM \"SiteInventory\" si "
            + "JOIN \"Site\" s ON si.site_id = s.id "
            + "WHERE si.merchandise_detail_id = ? AND si.quantity > 0 "
            + "ORDER BY s.delivery_by_ship ASC, si.quantity DESC";

    public List<SiteInventoryInfo> getAvailableSitesForItem(long merchandiseDetailId) {
        List<SiteInventoryInfo> sites = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_AVAILABLE_SITES_SQL)) {
            stmt.setLong(1, merchandiseDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SiteInventoryInfo info = new SiteInventoryInfo();
                    info.setSiteId(rs.getLong("site_id"));
                    info.setSiteName(rs.getString("site_name"));
                    info.setSiteAddress(rs.getString("site_address"));
                    info.setDeliveryByShip(rs.getLong("delivery_by_ship"));
                    info.setDeliveryByAir(rs.getLong("delivery_by_air"));
                    info.setAvailableQuantity(rs.getLong("quantity"));
                    info.setPrice(rs.getDouble("price"));
                    sites.add(info);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn SiteInventory", e);
        }
        normalizeScore(sites);
        sites.sort(Comparator.comparingInt(SiteInventoryInfo::getScore).reversed());
        return sites;
    }

    private void normalizeScore(List<SiteInventoryInfo> sites) {
        long maxQuantity = sites.stream()
                .mapToLong(SiteInventoryInfo::getAvailableQuantity)
                .max()
                .orElse(1);
        for (SiteInventoryInfo info : sites) {
            double shipValue = info.getDeliveryByShip() <= 0
                    ? 1.0
                    : 1.0 / info.getDeliveryByShip();
            double quantityScore = maxQuantity <= 0
                    ? 0
                    : (double) info.getAvailableQuantity() / maxQuantity;
            int score = (int) Math.round((shipValue * 50) + (quantityScore * 50));
            info.setScore(Math.min(100, Math.max(0, score)));
        }
    }
}
