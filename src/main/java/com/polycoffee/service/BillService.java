package com.polycoffee.service;

import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
import com.polycoffee.dto.Page;
import com.polycoffee.entity.Bill;
import com.polycoffee.entity.BillDetail;
import com.polycoffee.entity.BillStatus;
import com.polycoffee.entity.User;
import com.polycoffee.exception.AccessDeniedException;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

/** Xem hóa đơn và chuyển trạng thái. */
public class BillService {
    private final BillDAO billDAO;
    private final BillDetailDAO billDetailDAO;

    public BillService(BillDAO billDAO, BillDetailDAO billDetailDAO) {
        this.billDAO = billDAO;
        this.billDetailDAO = billDetailDAO;
    }

    /**
     * Quy tắc chuyển trạng thái (slide 6): waiting -> finish, waiting -> cancel,
     * finish -> cancel chỉ dành cho quản lý.
     */
    public static boolean canTransition(String from, String to, boolean manager) {
        if (BillStatus.WAITING.equals(from)) {
            return BillStatus.FINISH.equals(to) || BillStatus.CANCEL.equals(to);
        }
        return manager && BillStatus.FINISH.equals(from) && BillStatus.CANCEL.equals(to);
    }

    /** userId = null để xem mọi hóa đơn (quản lý). */
    public Page<Bill> search(Integer userId, LocalDate from, LocalDate to, int page) {
        Validator.range(from, to);
        int total = billDAO.count(userId, from, to);
        int current = Page.clamp(page, total);
        return new Page<>(billDAO.search(userId, from, to, Page.offset(current), Page.SIZE), current, total);
    }

    /** Nhân viên chỉ truy cập được hóa đơn của mình; quản lý truy cập được tất cả. */
    public Bill findAccessible(int billId, User user) {
        Bill bill = billDAO.findById(billId);
        if (bill == null) {
            throw new NotFoundException("Không tìm thấy hóa đơn.");
        }
        if (!user.isRole() && bill.getUserId() != user.getId()) {
            throw new AccessDeniedException("Bạn không có quyền truy cập hóa đơn này.");
        }
        return bill;
    }

    public List<BillDetail> details(int billId) {
        return billDetailDAO.findByBillId(billId);
    }

    public void changeStatus(int billId, String to, User user) {
        Bill bill = findAccessible(billId, user);
        if (!canTransition(bill.getStatus(), to, user.isRole())) {
            throw new BusinessRuleException("Không thể chuyển hóa đơn ở trạng thái hiện tại.");
        }
        if (billDAO.updateStatus(billId, bill.getStatus(), to) == 0) {
            throw new BusinessRuleException("Hóa đơn vừa được người khác cập nhật. Hãy tải lại trang.");
        }
    }
}
