package com.fashionweb.controller.cart;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.Role;
import com.fashionweb.controller.FakeWeb;
import com.fashionweb.exception.DataAccessException;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.model.Cart;
import com.fashionweb.service.cart.CartService;
import com.fashionweb.service.shop.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit test CartServlet (giỏ hàng khách + khách đăng nhập). CartService/ProductService được giả bằng Mockito. */
@Tag("unit")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class CartServletTest {

    private MockedConstruction<CartService> cartConstruction;
    private MockedConstruction<ProductService> productConstruction;
    private CartService cartService;
    private CartServlet servlet;

    @BeforeEach
    void setUp() {
        cartConstruction = Mockito.mockConstruction(CartService.class);
        productConstruction = Mockito.mockConstruction(ProductService.class);
        servlet = new CartServlet();
        cartService = cartConstruction.constructed().get(0);
    }

    @AfterEach
    void tearDown() {
        cartConstruction.close();
        productConstruction.close();
    }

    private static FakeWeb post(String action) {
        return new FakeWeb().param("action", action);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> guestCart(FakeWeb web) {
        return (Map<Integer, Integer>) web.sessionAttrs.get(AppConstant.SESSION_GUEST_CART);
    }

    // ---------------------------------------------------------------- action không hợp lệ

    @ParameterizedTest(name = "[{index}] action=\"{0}\" -> 400")
    @DisplayName("TC-CART-01: action thiếu hoặc lạ thì trả 400, không đổi giỏ")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {"NULL", "''", "delete", "ADD", "clear"})
    void unknownActionIsBadRequest(String action) throws Exception {
        FakeWeb web = new FakeWeb();
        if (action != null) {
            web.param("action", action);
        }

        servlet.doPost(web.req, web.resp);

        verify(web.resp).sendError(400);
        verify(web.resp, never()).sendRedirect(Mockito.anyString());
        Mockito.verifyNoInteractions(cartService);
    }

    // ---------------------------------------------------------------- thêm vào giỏ

    @ParameterizedTest(name = "[{index}] variantId=\"{0}\" -> báo chọn màu và size")
    @DisplayName("TC-CART-02: variantId thiếu / 0 / âm / không phải số thì báo lỗi và không thêm")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {"NULL", "''", "0", "-1", "abc", "1.5"})
    void addWithInvalidVariant(String variantId) throws Exception {
        FakeWeb web = post("add").param("quantity", "1");
        if (variantId != null) {
            web.param("variantId", variantId);
        }

        servlet.doPost(web.req, web.resp);

        assertEquals("Vui lòng chọn màu và size.", web.sessionAttrs.get("flashMessage"));
        assertEquals("error", web.sessionAttrs.get("flashType"));
        verify(web.resp).sendRedirect("/saltair/cart");
        verify(cartService, never()).addGuestItem(any(), anyInt(), anyInt());
        verify(cartService, never()).addItem(anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("TC-CART-03: variantId sai thì quay lại trang sản phẩm (Referer cùng ứng dụng)")
    void invalidVariantGoesBackToReferer() throws Exception {
        FakeWeb web = post("add").param("variantId", "0")
                .header("Referer", "http://localhost:8080/saltair/product?slug=ao-polo");

        servlet.doPost(web.req, web.resp);

        verify(web.resp).sendRedirect("/saltair/product?slug=ao-polo");
    }

    @Test
    @DisplayName("TC-CART-04: Referer của trang web khác bị bỏ qua, quay về /cart")
    void foreignRefererIgnored() throws Exception {
        FakeWeb web = post("add").param("variantId", "0").header("Referer", "http://evil.com/saltair/product?slug=x");

        servlet.doPost(web.req, web.resp);

        verify(web.resp).sendRedirect("/saltair/cart");
    }

    @ParameterizedTest(name = "[{index}] quantity=\"{0}\" -> thêm {1}")
    @DisplayName("TC-CART-05: Số lượng sai định dạng hoặc rỗng thì dùng mặc định 1")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "NULL  | 1", "''    | 1", "abc   | 1", "3     | 3", "'  4 '| 4", "10    | 10", "-2    | -2"
    })
    void guestAddQuantityParsing(String quantity, int expected) throws Exception {
        FakeWeb web = post("add").param("variantId", "5");
        if (quantity != null) {
            web.param("quantity", quantity);
        }

        servlet.doPost(web.req, web.resp);

        verify(cartService).addGuestItem(guestCart(web), 5, expected);
        assertEquals("Đã thêm vào giỏ hàng.", web.sessionAttrs.get("flashMessage"));
        assertEquals("success", web.sessionAttrs.get("flashType"));
        verify(web.resp).sendRedirect("/saltair/cart");
    }

    @Test
    @DisplayName("TC-CART-06: Khách đã đăng nhập thêm vào giỏ trong DB, cập nhật số món trên menu")
    void loggedInAdd() throws Exception {
        when(cartService.countItems(9)).thenReturn(3);
        FakeWeb web = post("add").param("variantId", "5").param("quantity", "2")
                .loginAs(FakeWeb.user(9, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        verify(cartService).addItem(9, 5, 2);
        verify(cartService, never()).addGuestItem(any(), anyInt(), anyInt());
        assertEquals(3, web.sessionAttrs.get(AppConstant.SESSION_CART_COUNT));
    }

    @Test
    @DisplayName("TC-CART-07: Vượt tồn kho (Service ném ValidationException) -> flash lỗi, giỏ không đổi")
    void outOfStockShowsError() throws Exception {
        Mockito.doThrow(new ValidationException(null, "Chỉ còn 2 sản phẩm trong kho."))
                .when(cartService).addGuestItem(any(), anyInt(), anyInt());
        FakeWeb web = post("add").param("variantId", "5").param("quantity", "10");

        servlet.doPost(web.req, web.resp);

        assertEquals("Chỉ còn 2 sản phẩm trong kho.", web.sessionAttrs.get("flashMessage"));
        assertEquals("error", web.sessionAttrs.get("flashType"));
        verify(web.resp).sendRedirect("/saltair/cart");
    }

    @Test
    @DisplayName("TC-CART-08: Lỗi hệ thống (DataAccessException) không bị nuốt thành thông báo flash")
    void dataAccessExceptionPropagates() {
        Mockito.doThrow(new DataAccessException("DB down", null)).when(cartService).addItem(anyInt(), anyInt(), anyInt());
        FakeWeb web = post("add").param("variantId", "5").loginAs(FakeWeb.user(9, Role.CUSTOMER));

        DataAccessException ex = assertThrows(DataAccessException.class, () -> servlet.doPost(web.req, web.resp));
        assertEquals("DB down", ex.getMessage());
        assertEquals(null, web.sessionAttrs.get("flashMessage"));
    }

    // ---------------------------------------------------------------- cập nhật, xóa

    @ParameterizedTest(name = "[{index}] quantity=\"{0}\" -> Service nhận {1}")
    @DisplayName("TC-CART-09: Cập nhật số lượng (khách): 0 là xóa dòng, sai định dạng dùng mặc định 1")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {"0 | 0", "1 | 1", "7 | 7", "abc | 1", "NULL | 1"})
    void guestUpdateQuantity(String quantity, int expected) throws Exception {
        FakeWeb web = post("update").param("itemId", "5");
        if (quantity != null) {
            web.param("quantity", quantity);
        }

        servlet.doPost(web.req, web.resp);

        verify(cartService).updateGuestQuantity(guestCart(web), 5, expected);
        verify(web.resp).sendRedirect("/saltair/cart");
    }

    @Test
    @DisplayName("TC-CART-10: Khách đăng nhập cập nhật số lượng qua CartService theo userId")
    void loggedInUpdate() throws Exception {
        FakeWeb web = post("update").param("itemId", "4").param("quantity", "2").loginAs(FakeWeb.user(9, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        verify(cartService).updateQuantity(9, 4, 2);
    }

    @Test
    @DisplayName("TC-CART-11: Xóa dòng khỏi giỏ khách: Map session mất dòng, số món cập nhật")
    void guestRemove() throws Exception {
        FakeWeb web = post("remove").param("itemId", "5");
        web.sessionAttrs.put(AppConstant.SESSION_GUEST_CART, new java.util.LinkedHashMap<>(Map.of(5, 2, 6, 1)));

        servlet.doPost(web.req, web.resp);

        assertEquals(Map.of(6, 1), guestCart(web));
        assertEquals(1, web.sessionAttrs.get(AppConstant.SESSION_CART_COUNT));
        assertEquals("Đã xóa sản phẩm khỏi giỏ hàng.", web.sessionAttrs.get("flashMessage"));
    }

    @Test
    @DisplayName("TC-CART-12: Khách đăng nhập xóa dòng qua CartService")
    void loggedInRemove() throws Exception {
        FakeWeb web = post("remove").param("itemId", "4").loginAs(FakeWeb.user(9, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        verify(cartService).removeItem(9, 4);
    }

    // ---------------------------------------------------------------- ghi chú

    @ParameterizedTest(name = "[{index}] ghi chú dài {0} ký tự -> lưu {1} ký tự")
    @DisplayName("TC-CART-13: Biên 500 ký tự của ghi chú đơn hàng")
    @CsvSource({"0, 0", "1, 1", "499, 499", "500, 500", "501, 500", "2000, 500"})
    void noteLengthBoundary(int length, int expectedStored) throws Exception {
        FakeWeb web = post("note").param("note", "x".repeat(length));

        servlet.doPost(web.req, web.resp);

        assertEquals(expectedStored, ((String) web.sessionAttrs.get(AppConstant.SESSION_CART_NOTE)).length());
        verify(web.resp).sendRedirect("/saltair/cart");
    }

    @Test
    @DisplayName("TC-CART-14: Ghi chú được cắt khoảng trắng hai đầu; thiếu tham số thì lưu chuỗi rỗng")
    void noteTrimmed() throws Exception {
        FakeWeb trimmed = post("note").param("note", "   Giao giờ hành chính  ");
        servlet.doPost(trimmed.req, trimmed.resp);
        assertEquals("Giao giờ hành chính", trimmed.sessionAttrs.get(AppConstant.SESSION_CART_NOTE));

        FakeWeb missing = post("note");
        servlet.doPost(missing.req, missing.resp);
        assertEquals("", missing.sessionAttrs.get(AppConstant.SESSION_CART_NOTE));
    }

    // ---------------------------------------------------------------- xem giỏ

    @Test
    @DisplayName("TC-CART-15: GET /cart của khách: forward cart.jsp kèm cart, phí ship, gợi ý; session có số món")
    void getCartForGuest() throws Exception {
        Cart cart = new Cart();
        when(cartService.getGuestCart(any())).thenReturn(cart);
        FakeWeb web = new FakeWeb();

        servlet.doGet(web.req, web.resp);

        assertEquals("/WEB-INF/views/cart/cart.jsp", web.forwardedTo);
        assertEquals(cart, web.requestAttrs.get("cart"));
        assertEquals(AppConstant.SHIPPING_FEE, web.requestAttrs.get("shippingFee"));
        assertEquals(cart.getItemCount(), web.sessionAttrs.get(AppConstant.SESSION_CART_COUNT));
    }

    @Test
    @DisplayName("TC-CART-16: GET /cart của khách đăng nhập đọc giỏ trong DB theo userId")
    void getCartForUser() throws Exception {
        Cart cart = new Cart();
        when(cartService.getCart(9)).thenReturn(cart);
        FakeWeb web = new FakeWeb().loginAs(FakeWeb.user(9, Role.CUSTOMER));

        servlet.doGet(web.req, web.resp);

        verify(cartService).getCart(9);
        assertEquals(cart, web.requestAttrs.get("cart"));
    }
}
