package com.app.modules.procurement.importorder.service;

import com.app.database.manager.DatabaseManager;
import com.app.modules.procurement.importorder.dto.AllocationProposalDTO;
import com.app.modules.procurement.importorder.repository.ImportOrderRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ImportOrderService {

    private final ImportOrderRepository repository;

    public ImportOrderService() {
        this.repository = new ImportOrderRepository();
    }

    public List<ImportOrderRepository.RequestSummary> getPendingRequests() {
        return repository.findPendingRequests();
    }

    public ImportOrderRepository.RequestSummary getRequestById(long id) {
        return repository.findRequestById(id);
    }

    public List<ImportOrderRepository.RequestDetailItem> getRequestDetails(long id) {
        return repository.getRequestDetails(id);
    }

    /**
     * Allocation algorithm based on usecase: UC001.
     * Prioritize ship (cost-effective), then large inventory, then least number of sites.
     */
    public List<AllocationProposalDTO> proposeAllocation(long requestId) {
        List<AllocationProposalDTO> proposals = new ArrayList<>();

        ImportOrderRepository.RequestSummary request = repository.findRequestById(requestId);
        if (request == null) {
            return proposals;
        }

        LocalDate tempDesiredDate = LocalDate.now().plusDays(30); // fallback default desired date
        if (request.desiredDate() != null && !request.desiredDate().isBlank()) {
            try {
                tempDesiredDate = LocalDate.parse(request.desiredDate());
            } catch (Exception e) {
                // Ignore parse error, use default fallback
            }
        }
        final LocalDate desiredLocalDate = tempDesiredDate;

        List<ImportOrderRepository.RequestDetailItem> items = repository.getRequestDetails(requestId);
        LocalDate today = LocalDate.now();

        for (ImportOrderRepository.RequestDetailItem item : items) {
            int neededQty = item.quantity();
            long mdId = item.merchandiseDetailId();

            List<ImportOrderRepository.SiteInventoryInfo> sites = repository.getSitesInventory(mdId);
            if (sites.isEmpty()) {
                // No site has this item in stock (Alternative flow 6a: Không tìm thấy Site phù hợp)
                continue;
            }

            // Step 1: Check shipping feasibility for each site
            // List of sites that can fulfill the entire quantity
            List<ImportOrderRepository.SiteInventoryInfo> singleSites = new ArrayList<>();
            for (ImportOrderRepository.SiteInventoryInfo site : sites) {
                if (site.availableQuantity() >= neededQty) {
                    singleSites.add(site);
                }
            }

            if (!singleSites.isEmpty()) {
                // Single site can fulfill (Least sites = 1)
                // Sort single sites to choose the best one
                singleSites.sort((s1, s2) -> {
                    // Check if delivery by ship meets desired date
                    boolean s1ShipOk = today.plusDays(s1.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(s1.deliveryByShip()).isEqual(desiredLocalDate);
                    boolean s2ShipOk = today.plusDays(s2.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(s2.deliveryByShip()).isEqual(desiredLocalDate);

                    if (s1ShipOk != s2ShipOk) {
                        return s1ShipOk ? -1 : 1; // Prioritize ship delivery
                    }
                    // Prioritize larger inventory
                    if (s1.availableQuantity() != s2.availableQuantity()) {
                        return Integer.compare(s2.availableQuantity(), s1.availableQuantity());
                    }
                    // Prioritize shorter distance
                    return Double.compare(s1.siteDistance(), s2.siteDistance());
                });

                ImportOrderRepository.SiteInventoryInfo bestSite = singleSites.get(0);
                boolean shipOk = today.plusDays(bestSite.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(bestSite.deliveryByShip()).isEqual(desiredLocalDate);
                String delivery = shipOk ? "SHIP" : "AIR";
                long deliveryDays = shipOk ? bestSite.deliveryByShip() : bestSite.deliveryByAir();
                String expectedDate = today.plusDays(deliveryDays).toString();

                proposals.add(new AllocationProposalDTO(
                        bestSite.siteId(),
                        bestSite.siteName(),
                        mdId,
                        item.merchandiseName(),
                        item.unit(),
                        neededQty,
                        delivery,
                        expectedDate,
                        bestSite.price()
                ));
            } else {
                // Need multiple sites (Alternative flow 10a)
                // Sort all sites to pick the best sequence of sites
                sites.sort((s1, s2) -> {
                    boolean s1ShipOk = today.plusDays(s1.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(s1.deliveryByShip()).isEqual(desiredLocalDate);
                    boolean s2ShipOk = today.plusDays(s2.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(s2.deliveryByShip()).isEqual(desiredLocalDate);

                    if (s1ShipOk != s2ShipOk) {
                        return s1ShipOk ? -1 : 1;
                    }
                    if (s1.availableQuantity() != s2.availableQuantity()) {
                        return Integer.compare(s2.availableQuantity(), s1.availableQuantity());
                    }
                    return Double.compare(s1.siteDistance(), s2.siteDistance());
                });

                int remainingNeeded = neededQty;
                for (ImportOrderRepository.SiteInventoryInfo site : sites) {
                    if (remainingNeeded <= 0) break;

                    int allocated = Math.min(remainingNeeded, site.availableQuantity());
                    boolean shipOk = today.plusDays(site.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(site.deliveryByShip()).isEqual(desiredLocalDate);
                    String delivery = shipOk ? "SHIP" : "AIR";
                    long deliveryDays = shipOk ? site.deliveryByShip() : site.deliveryByAir();
                    String expectedDate = today.plusDays(deliveryDays).toString();

                    proposals.add(new AllocationProposalDTO(
                            site.siteId(),
                            site.siteName(),
                            mdId,
                            item.merchandiseName(),
                            item.unit(),
                            allocated,
                            delivery,
                            expectedDate,
                            site.price()
                    ));

                    remainingNeeded -= allocated;
                }

                if (remainingNeeded > 0) {
                    System.out.println("Warning: Incomplete allocation for item: " + item.merchandiseName() + ", missing quantity: " + remainingNeeded);
                }
            }
        }

        return proposals;
    }

    /**
     * Confirms the allocation and writes Orders + Details to PostgreSQL in a single TRANSACTION.
     */
    public boolean confirmAllocation(long requestId, List<AllocationProposalDTO> proposals, long userId) {
        if (proposals == null || proposals.isEmpty()) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // Group proposals by (siteId, deliveryType) to create one Order per unique combination
            Map<String, List<AllocationProposalDTO>> grouped = new HashMap<>();
            for (AllocationProposalDTO prop : proposals) {
                String key = prop.getSiteId() + "_" + prop.getDeliveryType();
                grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(prop);
            }

            for (Map.Entry<String, List<AllocationProposalDTO>> entry : grouped.entrySet()) {
                List<AllocationProposalDTO> groupProps = entry.getValue();
                AllocationProposalDTO firstProp = groupProps.get(0);

                long siteId = firstProp.getSiteId();
                String deliveryType = firstProp.getDeliveryType();

                // Find the maximum expected delivery date in the group to set as the Order expected_delivery_date
                String maxExpectedDate = firstProp.getExpectedDeliveryDate();
                for (AllocationProposalDTO prop : groupProps) {
                    if (prop.getExpectedDeliveryDate().compareTo(maxExpectedDate) > 0) {
                        maxExpectedDate = prop.getExpectedDeliveryDate();
                    }
                }

                // 1. Create the Order
                long orderId = repository.createOrder(conn, siteId, maxExpectedDate, userId, requestId, deliveryType);

                // 2. Create OrderDetails & deduct inventory
                for (AllocationProposalDTO prop : groupProps) {
                    repository.createOrderDetail(conn, orderId, prop.getMerchandiseDetailId(), prop.getAllocatedQuantity());
                    repository.deductSiteInventory(conn, siteId, prop.getMerchandiseDetailId(), prop.getAllocatedQuantity());
                }
            }

            // 3. Update the ImportRequest status to PROCESSED
            repository.updateRequestStatus(conn, requestId, "PROCESSED");

            conn.commit(); // Commit Transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback Transaction
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
