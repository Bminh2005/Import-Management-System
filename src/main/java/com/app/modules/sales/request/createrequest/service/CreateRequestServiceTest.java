package com.app.modules.sales.request.createrequest.service;

import com.app.common.exception.BusinessException;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CreateRequestServiceTest {

    private CreateRequestService service;
    private CreateImportItemModel validItem;

    @BeforeEach
    public void setUp() {
        service = new CreateRequestService();

        // Tạo sẵn một Model hợp lệ dùng làm base cho các Test Case
        validItem = new CreateImportItemModel(1, "1", null, 50, "Cái", 2,LocalDate.now().plusDays(2));
        validItem.setItemCode("1");
        validItem.setQuantity(50);
        validItem.setExpectedDate(LocalDate.now().plusDays(2)); // Ngày mốt
    }

    // ==============================================================
    // PHẦN 1: KIỂM THỬ HỘP TRẮNG (ĐỘ ĐO C1 - BAO PHỦ NHÁNH)
    // ==============================================================

    @Test
    @DisplayName("TC_WB_01: Dữ liệu null (Nhánh 1 True)")
    public void testValidate_ItemIsNull_ThrowsException() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(null);
        });
        assertEquals("Dữ liệu mặt hàng không được để trống.", exception.getMessage());
    }

    @Test
    @DisplayName("TC_WB_02: ID mặt hàng <= 0 (Nhánh 2 True)")
    public void testValidate_InvalidId_ThrowsException() {
        validItem.setMerchandiseDetailId(0L); // Set ID = 0 để bắt lỗi

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(validItem);
        });
        assertEquals("Mã mặt hàng không hợp lệ.", exception.getMessage());
    }

    @Test
    @DisplayName("TC_WB_03: Quantity <= 0 (Nhánh 3 True)")
    public void testValidate_InvalidQuantity_ThrowsException() {
        validItem.setQuantity(0); // Set ID = 0 để bắt lỗi

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(validItem);
        });
        assertEquals("Số lượng nhập phải lớn hơn 0.", exception.getMessage());
    }

    @Test
    @DisplayName("TC_WB_03: ExpectedDate là null (Nhánh 4 True)")
    public void testValidate_DateIsNull_ThrowsException() {
        validItem.setExpectedDate(null); // Bỏ trống ngày

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(validItem);
        });
        assertEquals("Ngày mong muốn nhận hàng không được để trống.", exception.getMessage());
    }

    @Test
    @DisplayName("TC_WB_04: ExpectedDate là ngày hôm nay (Nhánh 5 True)")
    public void testValidate_DateIsToday_ThrowsException() {
        validItem.setExpectedDate(LocalDate.now()); // Set ngày hôm nay

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(validItem);
        });
        assertEquals("Ngày mong muốn nhận hàng phải từ ngày mai trở đi.", exception.getMessage());
    }

    // ==============================================================
    // PHẦN 2: KIỂM THỬ HỘP ĐEN (PHÂN TÍCH GIÁ TRỊ BIÊN CHO QUANTITY)
    // Biên hợp lệ: 1 <= quantity <= 10000
    // ==============================================================

    @Test
    @DisplayName("TC_BB_01: Quantity = 0 - Biên dưới lỗi (Nhánh 3 True)")
    public void testValidate_QuantityIsZero_ThrowsException() {
        validItem.setQuantity(0);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(validItem);
        });
        assertEquals("Số lượng nhập phải lớn hơn 0.", exception.getMessage());
    }

    @Test
    @DisplayName("TC_BB_02: Quantity = 1 - Biên dưới hợp lệ (All False)")
    public void testValidate_QuantityIsOne_Success() {
        validItem.setQuantity(1);

        // Không ném ra ngoại lệ nào là Pass
        assertDoesNotThrow(() -> {
            service.validateImportItem(validItem);
        });
    }

    @Test
    @DisplayName("TC_BB_03: Quantity = -10 - Không hợp lệ (All False)")
    public void testValidate_QuantityIsMinusTen_Success() {
        validItem.setQuantity(-10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.validateImportItem(validItem);
        });
        assertEquals("Số lượng nhập phải lớn hơn 0.", exception.getMessage());
    }

    @Test
    @DisplayName("TC_BB_04: Quantity = 100 - Hợp lệ (All False)")
    public void testValidate_QuantityIsHundred_Success() {
        validItem.setQuantity(100);

        // Không ném ra ngoại lệ nào là Pass
        assertDoesNotThrow(() -> {
            service.validateImportItem(validItem);
        });
    }

    // ==============================================================
    // PHẦN 3: HAPPY PATH (DỮ LIỆU CHUẨN)
    // ==============================================================

    @Test
    @DisplayName("TC_HP_01: Dữ liệu hoàn toàn hợp lệ (Luồng đi qua tất cả nhánh False)")
    public void testValidate_ValidItem_Success() {
        // validItem đã được setup chuẩn từ @BeforeEach
        assertDoesNotThrow(() -> {
            service.validateImportItem(validItem);
        });
    }
}