package com.poly.sof309.controllers.bill;

import com.polycoffee.service.BillService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TC_BILL_001 - Ma trận chuyển trạng thái hóa đơn
 *
 * <pre>
 * Quy tắc    : BR-BILL-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_001
 *
 * Luồng được gọi:
 *   Gọi thẳng service (hàm thuần)
 *    -> BillService.canTransition()
 *    -> BillService.canTransition
 *    -> giả lập (Mockito): (không cần)
 *
 * Mã được test: src/main/java/com/polycoffee/service/BillService.java
 * Dữ liệu    : 10 tổ hợp (từ, đến, vai trò)
 * Mong đợi   : Chỉ waiting->finish, waiting->cancel, và finish->cancel (quản lý) là hợp lệ
 * Chạy riêng : mvn test -Dtest='TC_BILL_001_*'
 * </pre>
 *
 * @see com.polycoffee.service.BillService#canTransition
 */
@Tag("TC_BILL_001")
@DisplayName("TC_BILL_001 - Ma trận chuyển trạng thái hóa đơn")
class TC_BILL_001_TransitionMatrixControllerTest extends BillControllerTest {

    @ParameterizedTest(name = "{0} -> {1}, quản lý={2}: {3}")
    @CsvSource({
            "waiting, finish,  false, true",
            "waiting, cancel,  false, true",
            "waiting, waiting, true,  false",
            "finish,  cancel,  false, false",
            "finish,  cancel,  true,  true",
            "finish,  waiting, true,  false",
            "finish,  finish,  true,  false",
            "cancel,  waiting, true,  false",
            "cancel,  finish,  true,  false",
            "cancel,  cancel,  true,  false"
    })
    void transitionMatrix(String from, String to, boolean manager, boolean allowed) {
        assertEquals(allowed, BillService.canTransition(from, to, manager));
    }
}
