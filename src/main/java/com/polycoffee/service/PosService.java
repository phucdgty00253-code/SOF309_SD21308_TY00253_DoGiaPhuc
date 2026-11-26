package com.polycoffee.service;

import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.entity.Bill;
import com.polycoffee.entity.BillDetail;
import com.polycoffee.entity.BillStatus;
import com.polycoffee.entity.Drink;
import com.polycoffee.entity.User;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.ValidationException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/** Nghiệp vụ bán hàng tại quầy (POS). */
public class PosService {
    public static final int QUANTITY_MAX = 99;
    private static final DateTimeFormatter CODE_TIME = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    private static final SecureRandom RANDOM = new SecureRandom();

    private final BillDAO billDAO;
    private final BillDetailDAO billDetailDAO;
    private final DrinkDAO drinkDAO;
    private final BillService billService;

    public PosService(BillDAO billDAO, BillDetailDAO billDetailDAO, DrinkDAO drinkDAO) {
        this.billDAO = billDAO;
        this.billDetailDAO = billDetailDAO;
        this.drinkDAO = drinkDAO;
        this.billService = new BillService(billDAO, billDetailDAO);
    }

    /** Bấm "Thêm": chưa có hóa đơn thì tạo mới; có rồi thì thêm món hoặc tăng số lượng. Trả về billId. */
    public int addDrink(User user, int billId, int drinkId, Integer cardId) {
        Drink drink = drinkDAO.findById(drinkId);
        if (drink == null || !drink.isActive()) {
            throw new BusinessRuleException("Đồ uống không còn bán.");
        }
        if (billId <= 0) {
            Bill bill = new Bill(0, user.getId(), cardId, newCode(), new Date(), drink.getPrice(), BillStatus.WAITING);
            return billDAO.createWithDetail(bill, new BillDetail(0, 0, drinkId, 1, drink.getPrice()));
        }
        requireWaiting(billId, user);
        BillDetail detail = billDetailDAO.findByBillAndDrink(billId, drinkId);
        if (detail == null) {
            billDetailDAO.create(new BillDetail(0, billId, drinkId, 1, drink.getPrice()));
        } else {
            setQuantity(detail, detail.getQuantity() + 1);
        }
        billDAO.updateTotal(billId);
        return billId;
    }

    /** action: increase, decrease, remove. Số lượng về 0 thì xóa món khỏi hóa đơn. */
    public void changeQuantity(User user, int billId, int drinkId, String action) {
        requireWaiting(billId, user);
        BillDetail detail = billDetailDAO.findByBillAndDrink(billId, drinkId);
        if (detail == null) {
            throw new BusinessRuleException("Đồ uống không có trong hóa đơn.");
        }
        switch (action) {
            case "increase" -> setQuantity(detail, detail.getQuantity() + 1);
            case "decrease" -> setQuantity(detail, detail.getQuantity() - 1);
            case "remove" -> setQuantity(detail, 0);
            default -> throw new ValidationException("Thao tác không hợp lệ.");
        }
        billDAO.updateTotal(billId);
    }

    public void checkout(User user, int billId) {
        requireWaiting(billId, user);
        if (billDetailDAO.findByBillId(billId).isEmpty()) {
            throw new BusinessRuleException("Hóa đơn chưa có đồ uống.");
        }
        billService.changeStatus(billId, BillStatus.FINISH, user);
    }

    public void cancel(User user, int billId) {
        requireWaiting(billId, user);
        billService.changeStatus(billId, BillStatus.CANCEL, user);
    }

    private void requireWaiting(int billId, User user) {
        Bill bill = billService.findAccessible(billId, user);
        if (!BillStatus.WAITING.equals(bill.getStatus())) {
            throw new BusinessRuleException("Chỉ sửa được hóa đơn đang chờ thanh toán.");
        }
    }

    private void setQuantity(BillDetail detail, int quantity) {
        if (quantity > QUANTITY_MAX) {
            throw new BusinessRuleException("Mỗi món tối đa " + QUANTITY_MAX + " phần.");
        }
        if (quantity <= 0) {
            billDetailDAO.delete(detail.getId());
            return;
        }
        detail.setQuantity(quantity);
        billDetailDAO.update(detail);
    }

    private static String newCode() {
        return "HD" + LocalDateTime.now().format(CODE_TIME) + String.format("%04d", RANDOM.nextInt(10_000));
    }
}
