package com.app.modules.sales.request.createrequest.repository;

import com.app.common.entity.RequestDetail;
import com.app.common.exception.DatabaseOperationException;
import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                     ResultSet rs = stmt.executeQuery("SELECT m.id, md.id, merchandise_name, unit, reference_price FROM \"MerchandiseDetail\" md JOIN \"Merchandise\" m ON md.merchandise_id = m.id")) {
                    while(rs.next()){
                        MerchandiseDTO m = new MerchandiseDTO(rs.getLong(1), rs.getLong(2),rs.getString(3), rs.getString(4), rs.getDouble(5));
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

    @Override
    public long createRequest(List<RequestDetail> merchandiseList) {
        String createRequestSql = """
        INSERT INTO "ImportRequest"(user_id,created_at, status)
        VALUES (1, CURRENT_TIMESTAMP, 'PENDING')
        RETURNING id
        """;

        String createDetailSql = """
        INSERT INTO "RequestDetail"
        (request_id, merchandise_detail_id, quantity, desired_date)
        VALUES (?, ?, ?, ?)
        """;
        try (Connection conn = DatabaseManager.getConnection()) {
            if (conn != null && conn.isValid(5)) {
                conn.setAutoCommit(false);

                try {

                    long requestId;

                    // Tạo Request
                    try (PreparedStatement ps =
                                 conn.prepareStatement(createRequestSql)) {

                        ResultSet rs = ps.executeQuery();

                        if (!rs.next()) {
                            throw new DatabaseOperationException(
                                    "Cannot create request"
                            );
                        }

                        requestId = rs.getLong("id");
                    }

                    // Tạo các RequestDetail
                    try (PreparedStatement ps =
                                 conn.prepareStatement(createDetailSql)) {

                        for (RequestDetail detail : merchandiseList) {

                            ps.setLong(1, requestId);
                            ps.setLong(2, detail.getMerchandise_detail_id());
                            ps.setInt(3, detail.getQuantity());
                            ps.setDate(4, java.sql.Date.valueOf(detail.getDesired_date())
                            );
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                    conn.commit();
                    return requestId;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            }
            else{
                throw new DatabaseOperationException("Failed connection with database");
            }
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed connection with database", e);
        }
    }
}
