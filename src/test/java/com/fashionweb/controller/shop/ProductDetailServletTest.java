package com.fashionweb.controller.shop;

import com.fashionweb.constant.Role;
import com.fashionweb.controller.FakeWeb;
import com.fashionweb.exception.DataAccessException;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.model.Product;
import com.fashionweb.model.Review;
import com.fashionweb.service.account.AccountService;
import com.fashionweb.service.order.ReviewService;
import com.fashionweb.service.shop.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit test ProductDetailServlet: trang chi tiết (GET) và đánh giá sản phẩm (POST). */
@Tag("unit")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class ProductDetailServletTest {

    private static final String DETAIL_JSP = "/WEB-INF/views/shop/product-detail.jsp";

    private MockedConstruction<ProductService> productConstruction;
    private MockedConstruction<ReviewService> reviewConstruction;
    private MockedConstruction<AccountService> accountConstruction;
    private ProductService productService;
    private ReviewService reviewService;
    private ProductDetailServlet servlet;
    private Product product;

    @BeforeEach
    void setUp() {
        productConstruction = Mockito.mockConstruction(ProductService.class);
        reviewConstruction = Mockito.mockConstruction(ReviewService.class);
        accountConstruction = Mockito.mockConstruction(AccountService.class);
        servlet = new ProductDetailServlet();
        productService = productConstruction.constructed().get(0);
        reviewService = reviewConstruction.constructed().get(0);
        product = new Product();
        product.setId(11);
        product.setSlug("ao-polo");
        product.setName("Áo polo");
        when(productService.findBySlug("ao-polo")).thenReturn(product);
        when(productService.galleryImages(product)).thenReturn(List.of("a.jpg"));
    }

    @AfterEach
    void tearDown() {
        productConstruction.close();
        reviewConstruction.close();
        accountConstruction.close();
    }

    private static Review review(int rating) {
        Review r = new Review();
        r.setRating(rating);
        return r;
    }

    // ---------------------------------------------------------------- GET

    @ParameterizedTest(name = "[{index}] slug=\"{0}\" -> 404")
    @DisplayName("TC-PDET-01: Slug thiếu hoặc không có sản phẩm thì trả 404")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {"NULL", "''", "khong-ton-tai"})
    void unknownSlugIs404(String slug) throws Exception {
        FakeWeb web = new FakeWeb();
        if (slug != null) {
            web.param("slug", slug);
        }

        servlet.doGet(web.req, web.resp);

        verify(web.resp).sendError(404);
        assertNull(web.forwardedTo);
    }

    @Test
    @DisplayName("TC-PDET-02: Có sản phẩm thì forward product-detail.jsp kèm đủ thuộc tính")
    void found() throws Exception {
        FakeWeb web = new FakeWeb().param("slug", "ao-polo");

        servlet.doGet(web.req, web.resp);

        assertEquals(DETAIL_JSP, web.forwardedTo);
        assertEquals(product, web.requestAttrs.get("product"));
        assertEquals(List.of("a.jpg"), web.requestAttrs.get("galleryImages"));
        assertEquals(30000L, web.requestAttrs.get("shippingFee"));
        assertEquals(false, web.requestAttrs.get("canReview"));
        assertEquals(0, web.requestAttrs.get("reviewTotal"));
        assertEquals(0.0, web.requestAttrs.get("reviewAvg"));
    }

    @Test
    @DisplayName("TC-PDET-03: Thống kê sao: đếm theo từng mức, điểm trung bình làm tròn 1 chữ số")
    void ratingBreakdown() throws Exception {
        when(reviewService.listVisibleByProduct(11)).thenReturn(List.of(review(5), review(4), review(4)));
        FakeWeb web = new FakeWeb().param("slug", "ao-polo");

        servlet.doGet(web.req, web.resp);

        assertArrayEquals(new int[]{0, 0, 0, 0, 2, 1}, (int[]) web.requestAttrs.get("ratingBreakdown"));
        assertEquals(3, web.requestAttrs.get("reviewTotal"));
        assertEquals(4.3, web.requestAttrs.get("reviewAvg"));
    }

    @ParameterizedTest(name = "[{index}] sao lưu trong DB = {0} -> tính như {1} sao")
    @DisplayName("TC-PDET-04: Số sao ngoài 1..5 được kẹp về biên")
    @CsvSource({"0, 1", "-3, 1", "1, 1", "5, 5", "9, 5"})
    void ratingClamped(int stored, int counted) throws Exception {
        when(reviewService.listVisibleByProduct(11)).thenReturn(List.of(review(stored)));
        FakeWeb web = new FakeWeb().param("slug", "ao-polo");

        servlet.doGet(web.req, web.resp);

        int[] breakdown = (int[]) web.requestAttrs.get("ratingBreakdown");
        assertEquals(1, breakdown[counted]);
        assertEquals((double) counted, web.requestAttrs.get("reviewAvg"));
    }

    @Test
    @DisplayName("TC-PDET-05: Chỉ khách (CUSTOMER) có đơn hoàn thành mới được đánh giá; Admin thì không")
    void canReviewOnlyForCustomers() throws Exception {
        when(reviewService.canReview(5, 11)).thenReturn(true);
        when(reviewService.canReview(1, 11)).thenReturn(true);

        FakeWeb customer = new FakeWeb().param("slug", "ao-polo").loginAs(FakeWeb.user(5, Role.CUSTOMER));
        servlet.doGet(customer.req, customer.resp);
        assertEquals(true, customer.requestAttrs.get("canReview"));

        FakeWeb admin = new FakeWeb().param("slug", "ao-polo").loginAs(FakeWeb.user(1, Role.ADMIN));
        servlet.doGet(admin.req, admin.resp);
        assertEquals(false, admin.requestAttrs.get("canReview"));
    }

    // ---------------------------------------------------------------- POST

    @Test
    @DisplayName("TC-PDET-06: Chưa đăng nhập mà gửi đánh giá thì chuyển sang /login kèm redirect về sản phẩm")
    void postRequiresLogin() throws Exception {
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "review");

        servlet.doPost(web.req, web.resp);

        verify(web.resp).sendRedirect("/saltair/login?redirect=%2Fproduct%3Fslug%3Dao-polo");
        Mockito.verifyNoInteractions(reviewService);
    }

    @ParameterizedTest(name = "[{index}] action=\"{0}\" -> 400")
    @DisplayName("TC-PDET-07: action thiếu hoặc lạ thì trả 400")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {"NULL", "''", "hack", "REVIEW"})
    void badAction(String action) throws Exception {
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").loginAs(FakeWeb.user(5, Role.CUSTOMER));
        if (action != null) {
            web.param("action", action);
        }

        servlet.doPost(web.req, web.resp);

        verify(web.resp).sendError(400);
        verify(reviewService, never()).save(any());
    }

    @ParameterizedTest(name = "[{index}] rating=\"{0}\" -> Service nhận {1}")
    @DisplayName("TC-PDET-08: Điểm sao sai định dạng được đọc thành 0 để Service báo lỗi")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {"5 | 5", "1 | 1", "0 | 0", "abc | 0", "'' | 0", "NULL | 0", "4.5 | 0"})
    void ratingParsing(String rating, int expected) throws Exception {
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "review").param("productId", "11")
                .param("comment", "Vải mát, mặc rất thoải mái").loginAs(FakeWeb.user(5, Role.CUSTOMER));
        if (rating != null) {
            web.param("rating", rating);
        }

        servlet.doPost(web.req, web.resp);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewService).save(captor.capture());
        assertEquals(expected, captor.getValue().getRating());
        assertEquals(5, captor.getValue().getUserId());
        assertEquals(11, captor.getValue().getProductId());
    }

    @ParameterizedTest(name = "[{index}] đã đánh giá trước đó={0} -> \"{1}\"")
    @DisplayName("TC-PDET-09: Thêm mới và sửa đánh giá hiện hai thông báo khác nhau, rồi redirect về #reviews")
    @CsvSource(delimiter = '|', value = {
            "false | Cảm ơn bạn đã đánh giá sản phẩm!",
            "true  | Đã cập nhật đánh giá của bạn."
    })
    void saveSuccess(boolean updated, String message) throws Exception {
        when(reviewService.save(any())).thenReturn(updated);
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "review").param("productId", "11")
                .param("rating", "5").param("comment", "Vải mát, mặc rất thoải mái")
                .loginAs(FakeWeb.user(5, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        assertEquals(message, web.sessionAttrs.get("flashMessage"));
        assertEquals("success", web.sessionAttrs.get("flashType"));
        verify(web.resp).sendRedirect("/saltair/product?slug=ao-polo#reviews");
    }

    @Test
    @DisplayName("TC-PDET-10: Lỗi nhập liệu theo ô thì hiện lại trang chi tiết kèm errors, không redirect")
    void fieldErrorsShowDetailAgain() throws Exception {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("comment", "Nhận xét phải từ 10 đến 1000 ký tự.");
        when(reviewService.save(any())).thenThrow(new ValidationException(errors));
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "review").param("productId", "11")
                .param("rating", "5").param("comment", "ngắn").loginAs(FakeWeb.user(5, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        assertEquals(DETAIL_JSP, web.forwardedTo);
        assertEquals(errors, web.requestAttrs.get("errors"));
        verify(web.resp, never()).sendRedirect(Mockito.anyString());
    }

    @Test
    @DisplayName("TC-PDET-11: Lỗi chung (chưa mua hàng) thì flash lỗi và redirect về #reviews")
    void generalErrorIsFlashed() throws Exception {
        when(reviewService.save(any())).thenThrow(new ValidationException(null, "Bạn chỉ đánh giá được sau khi đơn hoàn thành."));
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "review").param("productId", "11")
                .param("rating", "5").param("comment", "Vải mát, mặc rất thoải mái")
                .loginAs(FakeWeb.user(5, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        assertEquals("Bạn chỉ đánh giá được sau khi đơn hoàn thành.", web.sessionAttrs.get("flashMessage"));
        assertEquals("error", web.sessionAttrs.get("flashType"));
        verify(web.resp).sendRedirect("/saltair/product?slug=ao-polo#reviews");
    }

    @Test
    @DisplayName("TC-PDET-12: Lỗi hệ thống khi lưu đánh giá không bị nuốt")
    void dataAccessExceptionPropagates() {
        when(reviewService.save(any())).thenThrow(new DataAccessException("DB down", null));
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "review").param("productId", "11")
                .param("rating", "5").param("comment", "Vải mát, mặc rất thoải mái")
                .loginAs(FakeWeb.user(5, Role.CUSTOMER));

        assertThrows(DataAccessException.class, () -> servlet.doPost(web.req, web.resp));
    }

    @Test
    @DisplayName("TC-PDET-13: Xóa đánh giá của chính mình")
    void deleteReview() throws Exception {
        FakeWeb web = new FakeWeb().param("slug", "ao-polo").param("action", "deleteReview").param("productId", "11")
                .loginAs(FakeWeb.user(5, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        verify(reviewService).delete(5, 11);
        assertEquals("Đã xóa đánh giá của bạn.", web.sessionAttrs.get("flashMessage"));
        verify(web.resp).sendRedirect("/saltair/product?slug=ao-polo#reviews");
    }

    @Test
    @DisplayName("TC-PDET-14: Slug có ký tự đặc biệt được mã hóa trong URL quay lại")
    void slugIsUrlEncoded() throws Exception {
        FakeWeb web = new FakeWeb().param("slug", "áo thun&x=1").param("action", "deleteReview")
                .param("productId", "11").loginAs(FakeWeb.user(5, Role.CUSTOMER));

        servlet.doPost(web.req, web.resp);

        verify(web.resp).sendRedirect("/saltair/product?slug=%C3%A1o+thun%26x%3D1#reviews");
        verify(reviewService).delete(Mockito.eq(5), anyInt());
    }
}
