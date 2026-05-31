package com.app.modules.sales.request.createrequest.repository;

import com.app.common.exception.DatabaseOperationException;
import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLCreateRequestRepository implements ICreateRequestRepository {
    public List<MerchandiseDTO> getAllMerchandise(){
        List<MerchandiseDTO> merchandises = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {

            // Hàm isValid(timeout_in_seconds) để check kết nối có đang sống không
            if (conn != null && conn.isValid(5)) {
                System.out.println("✅ BƯỚC 1: Lấy Connection từ Pool THÀNH CÔNG!");

                // Chạy thử một câu truy vấn vô thưởng vô phạt
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT m.id, merchandise_name, unit, reference_price FROM \"MerchandiseDetail\" md JOIN \"Merchandise\" m ON md.merchandise_id = m.id")) {
                    while(rs.next()){
                        MerchandiseDTO m = new MerchandiseDTO(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
                        merchandises.add(m);
                    }
                }
            } else {
                throw new DatabaseOperationException("Failed connection with database");
            }
        } catch (Exception e) {
            throw new DatabaseOperationException("Cannot get All Merchandise Information",e);
        }
        return merchandises;
    }
}
