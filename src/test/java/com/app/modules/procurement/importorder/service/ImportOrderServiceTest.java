package com.app.modules.procurement.importorder.service;

import com.app.modules.procurement.importorder.dto.AllocationProposalDTO;
import com.app.modules.procurement.importorder.repository.ImportOrderRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImportOrderServiceTest {

    private final StubImportOrderRepository stubRepository = new StubImportOrderRepository();
    private final ImportOrderService service = new ImportOrderService(stubRepository);

    // --- Black-Box Test Cases ---

    @Test
    public void testBB1_RequestNotFound() {
        stubRepository.requestSummary = null;

        List<AllocationProposalDTO> proposals = service.proposeAllocation(999L);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void testBB2_EmptyRequestItems() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 0, "medium", "pending"
        );
        stubRepository.requestDetails = Collections.emptyList();

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void testBB3_NoSitesHaveStock() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = Collections.emptyList();

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void testBB4_SingleSiteFulfill() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(1, proposals.size());
        AllocationProposalDTO p = proposals.get(0);
        assertEquals(1L, p.getSiteId());
        assertEquals(5, p.getAllocatedQuantity());
        assertEquals("SHIP", p.getDeliveryType());
    }

    @Test
    public void testBB5_MultipleSitesNeeded() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 15, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0),
                new ImportOrderRepository.SiteInventoryInfo(2L, "Site B", 20.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(2, proposals.size());
        assertEquals(10, proposals.get(0).getAllocatedQuantity());
        assertEquals(5, proposals.get(1).getAllocatedQuantity());
    }

    @Test
    public void testBB6_ShipDeliveryFeasible() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(10).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals("SHIP", proposals.get(0).getDeliveryType());
    }

    @Test
    public void testBB7_ShipInfeasibleUseAir() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(3).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals("AIR", proposals.get(0).getDeliveryType());
    }

    @Test
    public void testBB8_SortingPriority_ShipOverAir() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(10).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
            new ImportOrderRepository.SiteInventoryInfo(1L, "Site A (Further, Ship Feasible)", 100.0, 5, 2, 10, 20000.0),
            new ImportOrderRepository.SiteInventoryInfo(2L, "Site B (Closer, Ship Infeasible)", 10.0, 12, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(1L, proposals.get(0).getSiteId());
    }

    @Test
    public void testBB9_SortingPriority_StockLevel() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
            new ImportOrderRepository.SiteInventoryInfo(2L, "Site B (Low Stock)", 10.0, 5, 2, 10, 20000.0),
            new ImportOrderRepository.SiteInventoryInfo(1L, "Site A (High Stock)", 20.0, 5, 2, 50, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(1L, proposals.get(0).getSiteId());
    }

    @Test
    public void testBB10_SortingPriority_Distance() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
            new ImportOrderRepository.SiteInventoryInfo(2L, "Site B (Farther)", 30.0, 5, 2, 10, 20000.0),
            new ImportOrderRepository.SiteInventoryInfo(1L, "Site A (Closer)", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(1L, proposals.get(0).getSiteId());
    }

    // --- White-Box Test Cases (C1 Coverage) ---

    @Test
    public void testWB1_NullRequest() {
        stubRepository.requestSummary = null;
        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void testWB2_InvalidDesiredDate() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), "invalid-date", "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertFalse(proposals.isEmpty());
        assertEquals("SHIP", proposals.get(0).getDeliveryType());
    }

    @Test
    public void testWB3_EmptySites() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = Collections.emptyList();

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void testWB4_SingleSiteShipFeasible() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(10).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(1, proposals.size());
        assertEquals("SHIP", proposals.get(0).getDeliveryType());
    }

    @Test
    public void testWB5_SingleSiteShipInfeasible() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(3).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(1, proposals.size());
        assertEquals("AIR", proposals.get(0).getDeliveryType());
    }

    @Test
    public void testWB6_SingleSiteSortingBranches() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(10).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 5, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A (Further, low stock)", 50.0, 5, 2, 10, 20000.0),
                new ImportOrderRepository.SiteInventoryInfo(2L, "Site B (Closer, high stock)", 10.0, 5, 2, 20, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(2L, proposals.get(0).getSiteId());
    }

    @Test
    public void testWB7_MultipleSitesFulfill() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 15, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0),
                new ImportOrderRepository.SiteInventoryInfo(2L, "Site B", 20.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(2, proposals.size());
        assertEquals(10, proposals.get(0).getAllocatedQuantity());
        assertEquals(5, proposals.get(1).getAllocatedQuantity());
    }

    @Test
    public void testWB8_MultipleSitesIncomplete() {
        stubRepository.requestSummary = new ImportOrderRepository.RequestSummary(
                1L, "REQ-001", LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), "admin", 1, "medium", "pending"
        );
        stubRepository.requestDetails = List.of(
                new ImportOrderRepository.RequestDetailItem(10L, 101L, "Laptop", 25, "cái", 20000.0)
        );
        stubRepository.sitesInventory = List.of(
                new ImportOrderRepository.SiteInventoryInfo(1L, "Site A", 10.0, 5, 2, 10, 20000.0),
                new ImportOrderRepository.SiteInventoryInfo(2L, "Site B", 20.0, 5, 2, 10, 20000.0)
        );

        List<AllocationProposalDTO> proposals = service.proposeAllocation(1L);
        assertEquals(2, proposals.size());
        assertEquals(10, proposals.get(0).getAllocatedQuantity());
        assertEquals(10, proposals.get(1).getAllocatedQuantity());
    }

    // --- Stub Repository Subclass ---

    static class StubImportOrderRepository extends ImportOrderRepository {
        ImportOrderRepository.RequestSummary requestSummary;
        List<ImportOrderRepository.RequestDetailItem> requestDetails = new ArrayList<>();
        List<ImportOrderRepository.SiteInventoryInfo> sitesInventory = new ArrayList<>();

        @Override
        public RequestSummary findRequestById(long id) {
            return requestSummary;
        }

        @Override
        public List<RequestDetailItem> getRequestDetails(long id) {
            return requestDetails;
        }

        @Override
        public List<SiteInventoryInfo> getSitesInventory(long merchandiseDetailId) {
            return new ArrayList<>(sitesInventory);
        }
    }
}
