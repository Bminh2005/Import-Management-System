package com.app.modules.procurement.importorder;

import com.app.database.manager.DatabaseManager;
import com.app.modules.procurement.importorder.dto.AllocationProposalDTO;
import com.app.modules.procurement.importorder.repository.ImportOrderRepository;
import com.app.modules.procurement.importorder.service.ImportOrderService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class TestProcurementAllocation {

    public static void main(String[] args) {
        System.out.println("========== TESTING PROCUREMENT ALLOCATION ==========");
        
        // 1. Verify Database connection is working
        try (Connection conn = DatabaseManager.getConnection()) {
            if (conn != null && conn.isValid(5)) {
                System.out.println("Database connection established successfully!");
                
                // Print existing sites and inventory
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT s.site_name, si.merchandise_detail_id, si.quantity, si.price " +
                                                      "FROM \"SiteInventory\" si JOIN \"Site\" s ON si.site_id = s.id")) {
                    System.out.println("Current Site Inventories in DB:");
                    while (rs.next()) {
                        System.out.printf(" - Site: %s | Merchandise Detail ID: %d | Qty: %d | Price: %.2f%n",
                                rs.getString("site_name"),
                                rs.getLong("merchandise_detail_id"),
                                rs.getInt("quantity"),
                                rs.getDouble("price"));
                    }
                }
            } else {
                System.out.println("Failed to connect to database.");
                return;
            }
        } catch (Exception e) {
            System.err.println("Database Connection Error:");
            e.printStackTrace();
            return;
        }

        // 2. Initialize Service and Repository
        ImportOrderService service = new ImportOrderService();
        
        // 3. Get Pending Requests
        List<ImportOrderRepository.RequestSummary> pendingRequests = service.getPendingRequests();
        System.out.println("\nPending Requests in DB:");
        for (ImportOrderRepository.RequestSummary req : pendingRequests) {
            System.out.printf(" - ID: %d | Code: %s | CreatedBy: %s | DesiredDate: %s | Status: %s | ItemCount: %d%n",
                    req.id(), req.code(), req.createdBy(), req.desiredDate(), req.status(), req.itemCount());
        }

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending requests found in the database. Please seed the database first.");
            return;
        }

        // 4. Select the first pending request to test allocation (e.g. request_id = 3 or first in list)
        long testRequestId = pendingRequests.get(0).id();
        System.out.println("\nRunning Allocation Algorithm for Request ID: " + testRequestId);
        
        List<AllocationProposalDTO> proposals = service.proposeAllocation(testRequestId);
        System.out.println("Allocation Proposals Generated:");
        if (proposals.isEmpty()) {
            System.out.println(" - No proposals generated (possibly due to empty inventory).");
        } else {
            for (AllocationProposalDTO prop : proposals) {
                System.out.println(" - " + prop);
            }
            
            // 5. Test Transactional Confirmation (with user_id = 3 which is procurement_01 in seed)
            long testUserId = 3; 
            System.out.println("\nConfirming Allocation in Database...");
            boolean success = service.confirmAllocation(testRequestId, proposals, testUserId);
            if (success) {
                System.out.println("Allocation successfully confirmed and saved in the database!");
                
                // Let's verify by showing updated request status and created orders
                try (Connection conn = DatabaseManager.getConnection();
                     Statement stmt = conn.createStatement()) {
                    
                    try (ResultSet rs = stmt.executeQuery("SELECT id, status FROM \"ImportRequest\" WHERE id = " + testRequestId)) {
                        if (rs.next()) {
                            System.out.println("Updated Request Status: " + rs.getString("status"));
                        }
                    }
                    
                    try (ResultSet rs = stmt.executeQuery("SELECT o.id, o.site_id, o.expected_delivery_date, o.delivery, s.site_name " +
                                                          "FROM \"Order\" o JOIN \"Site\" s ON o.site_id = s.id WHERE o.request_id = " + testRequestId)) {
                        System.out.println("Created Purchase Orders:");
                        while (rs.next()) {
                            System.out.printf(" - Order ID: %d | Site: %s | Delivery: %s | Expected Date: %s%n",
                                    rs.getLong("id"),
                                    rs.getString("site_name"),
                                    rs.getString("delivery"),
                                    rs.getDate("expected_delivery_date"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to confirm allocation transaction.");
            }
        }
        
        System.out.println("\n========== TEST COMPLETE ==========");
    }
}
