package com.fashionweb.controller.shop;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.controller.FakeWeb;
import com.fashionweb.model.Category;
import com.fashionweb.model.Color;
import com.fashionweb.model.Product;
import com.fashionweb.model.Size;
import com.fashionweb.service.shop.AttributeService;
import com.fashionweb.service.shop.CategoryService;
import com.fashionweb.service.shop.ProductService;
import com.fashionweb.util.Page;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test ProductListServlet: tham số sai được bỏ qua (không báo lỗi), kiểm tra tham số truyền xuống ProductService.search.
 */
@Tag("unit")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class ProductListServletTest {

    private MockedConstruction<ProductService> productConstruction;
    private MockedConstruction<CategoryService> categoryConstruction;
    private MockedConstruction<AttributeService> attributeConstruction;
    private ProductService productService;
    private CategoryService categoryService;
    private ProductListServlet servlet;

    @BeforeEach
    void setUp() {
        productConstruction = Mockito.mockConstruction(ProductService.class);
        categoryConstruction = Mockito.mockConstruction(CategoryService.class);
        attributeConstruction = Mockito.mockConstruction(AttributeService.class, (mock, ctx) -> {
            when(mock.findColors()).thenReturn(List.of(color("den", "Đen"), color("trang", "Trắng")));
            when(mock.findSizes()).thenReturn(List.of(size("S"), size("M"), size("L"), size("XL")));
        });
        servlet = new ProductListServlet();
        productService = productConstruction.constructed().get(0);
        categoryService = categoryConstruction.constructed().get(0);
        when(productService.search(any(), any(), any(), any(), any(), any(), anyString(), anyInt(), anyInt()))
                .thenReturn(new Page<Product>(List.of(), 1, AppConstant.PAGE_SIZE, 0));
    }

    @AfterEach
    void tearDown() {
        productConstruction.close();
        categoryConstruction.close();
        attributeConstruction.close();
    }

    private static Color color(String code, String name) {
        Color c = new Color();
        c.setCode(code);
        c.setName(name);
        return c;
    }

    private static Size size(String name) {
        Size s = new Size();
        s.setName(name);
        return s;
    }

    /** Các tham số servlet đã truyền xuống ProductService.search ở lần gọi cuối. */
    private record SearchArgs(String q, String category, Long min, Long max, String color, String size, String sort, int page) {
    }

    private SearchArgs lastSearch() {
        ArgumentCaptor<String> q = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> cat = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> min = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> max = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> color = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> size = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sort = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> page = ArgumentCaptor.forClass(Integer.class);
        verify(productService, Mockito.atLeastOnce()).search(q.capture(), cat.capture(), min.capture(), max.capture(),
                color.capture(), size.capture(), sort.capture(), page.capture(), anyInt());
        int last = q.getAllValues().size() - 1;
        return new SearchArgs(q.getAllValues().get(last), cat.getAllValues().get(last), min.getAllValues().get(last),
                max.getAllValues().get(last), color.getAllValues().get(last), size.getAllValues().get(last),
                sort.getAllValues().get(last), page.getAllValues().get(last));
    }

    private FakeWeb get(String... nameValue) throws Exception {
        FakeWeb web = new FakeWeb();
        for (int i = 0; i < nameValue.length; i += 2) {
            web.param(nameValue[i], nameValue[i + 1]);
        }
        servlet.doGet(web.req, web.resp);
        return web;
    }

    @Test
    @DisplayName("TC-PROD-01: Không có tham số: tìm mặc định, sắp xếp newest, trang 1, forward product-list.jsp")
    void defaults() throws Exception {
        FakeWeb web = get();

        assertEquals("/WEB-INF/views/shop/product-list.jsp", web.forwardedTo);
        assertEquals(new SearchArgs(null, null, null, null, null, null, "newest", 1), lastSearch());
        assertEquals("/products", web.requestAttrs.get("baseUrl"));
    }

    @ParameterizedTest(name = "[{index}] page=\"{0}\" -> tìm trang {1}")
    @DisplayName("TC-PROD-02: Số trang sai hoặc nhỏ hơn 1 thì dùng trang 1")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "NULL | 1", "'' | 1", "abc | 1", "0 | 1", "-5 | 1", "1 | 1", "2 | 1", "1.5 | 1"
    })
    void pageParsing(String page, int expected) throws Exception {
        if (page == null) {
            get();
        } else {
            get("page", page);
        }
        // Giả lập đúng 1 trang kết quả nên trang >1 sẽ bị đưa về trang cuối (1) ở lần tìm thứ hai
        assertEquals(expected, lastSearch().page());
    }

    @Test
    @DisplayName("TC-PROD-03: Trang vượt quá tổng số trang thì tìm lại ở trang cuối")
    void pageBeyondLastGoesToLastPage() throws Exception {
        when(productService.search(any(), any(), any(), any(), any(), any(), anyString(), anyInt(), anyInt()))
                .thenReturn(new Page<Product>(List.of(), 99, 12, 25)); // 25 sản phẩm / 12 = 3 trang

        get("page", "99");

        ArgumentCaptor<Integer> pages = ArgumentCaptor.forClass(Integer.class);
        verify(productService, times(2)).search(any(), any(), any(), any(), any(), any(), anyString(), pages.capture(), anyInt());
        assertEquals(List.of(99, 3), pages.getAllValues());
    }

    @ParameterizedTest(name = "[{index}] minPrice=\"{0}\", maxPrice=\"{1}\" -> {2}..{3}")
    @DisplayName("TC-PROD-04: Biên khoảng giá: sai/âm bị bỏ, min > max thì đổi chỗ")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "NULL     | NULL     | NULL   | NULL",
            "100000   | 500000   | 100000 | 500000",
            "500000   | 100000   | 100000 | 500000",
            "0        | 0        | 0      | 0",
            "-1       | 300000   | NULL   | 300000",
            "abc      | 300000   | NULL   | 300000",
            "'  '     | ''       | NULL   | NULL",
            "200000   | NULL     | 200000 | NULL",
            "NULL     | 200000   | NULL   | 200000",
            "99999999999999999999 | 100 | NULL | 100"
    })
    void priceRange(String min, String max, Long expectedMin, Long expectedMax) throws Exception {
        FakeWeb web = new FakeWeb();
        if (min != null) {
            web.param("minPrice", min);
        }
        if (max != null) {
            web.param("maxPrice", max);
        }
        servlet.doGet(web.req, web.resp);

        SearchArgs args = lastSearch();
        assertEquals(expectedMin, args.min());
        assertEquals(expectedMax, args.max());
    }

    @ParameterizedTest(name = "[{index}] sort=\"{0}\" -> {1}")
    @DisplayName("TC-PROD-05: Chỉ nhận 5 kiểu sắp xếp hợp lệ, còn lại dùng newest")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "newest | newest", "price_asc | price_asc", "price_desc | price_desc", "bestseller | bestseller",
            "rating | rating", "PRICE_ASC | newest", "random | newest", "'' | newest", "NULL | newest",
            "price_asc; DROP TABLE product | newest"
    })
    void sortWhitelist(String sort, String expected) throws Exception {
        FakeWeb web = new FakeWeb();
        if (sort != null) {
            web.param("sort", sort);
        }
        servlet.doGet(web.req, web.resp);

        assertEquals(expected, lastSearch().sort());
    }

    @ParameterizedTest(name = "[{index}] size=\"{0}\" -> {1}")
    @DisplayName("TC-PROD-06: Size không phân biệt hoa thường, size lạ bị bỏ")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "S | S", "m | M", " xl  | XL", "XXL | NULL", "'' | NULL", "NULL | NULL", "M' OR '1'='1 | NULL"
    })
    void sizeFilter(String size, String expected) throws Exception {
        FakeWeb web = new FakeWeb();
        if (size != null) {
            web.param("size", size);
        }
        servlet.doGet(web.req, web.resp);

        assertEquals(expected, lastSearch().size());
    }

    @ParameterizedTest(name = "[{index}] color=\"{0}\" -> {1}")
    @DisplayName("TC-PROD-07: Mã màu phải có trong danh sách màu, còn lại bị bỏ")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "den | den", "trang | trang", "DEN | NULL", "do | NULL", "'' | NULL"
    })
    void colorFilter(String color, String expected) throws Exception {
        FakeWeb web = new FakeWeb();
        web.param("color", color);
        servlet.doGet(web.req, web.resp);

        assertEquals(expected, lastSearch().color());
    }

    @Test
    @DisplayName("TC-PROD-08: Từ khóa dài hơn 100 ký tự bị cắt còn 100; cắt khoảng trắng hai đầu")
    void keywordBoundary() throws Exception {
        get("q", "a".repeat(101));
        assertEquals("a".repeat(100), lastSearch().q());

        get("q", "  áo thun  ");
        assertEquals("áo thun", lastSearch().q());

        get("q", "   ");
        assertNull(lastSearch().q());
    }

    @Test
    @DisplayName("TC-PROD-09: Danh mục có thật thì lọc, slug lạ thì bỏ; baseUrl và chip lọc được tạo đúng")
    void categoryAndBaseUrl() throws Exception {
        Category cat = new Category();
        cat.setName("Áo thun và Polo");
        cat.setSlug("ao-thun-polo");
        when(categoryService.findBySlug("ao-thun-polo")).thenReturn(cat);

        FakeWeb known = get("category", "ao-thun-polo", "q", "áo", "sort", "price_asc");
        assertEquals("ao-thun-polo", lastSearch().category());
        assertEquals("/products?q=%C3%A1o&category=ao-thun-polo&sort=price_asc", known.requestAttrs.get("baseUrl"));
        assertEquals(cat, known.requestAttrs.get("currentCategory"));
        assertEquals(3, ((List<?>) known.requestAttrs.get("activeFilters")).size());

        FakeWeb unknown = get("category", "khong-ton-tai");
        assertNull(lastSearch().category());
        assertNull(unknown.requestAttrs.get("currentCategory"));
        assertEquals("/products", unknown.requestAttrs.get("baseUrl"));
    }
}
