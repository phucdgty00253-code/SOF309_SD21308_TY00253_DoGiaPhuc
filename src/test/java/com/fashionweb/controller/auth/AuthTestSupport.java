package com.fashionweb.controller.auth;

import com.fashionweb.service.cart.CartService;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

/**
 * LoginSupport giữ một CartService tĩnh (tạo lúc nạp lớp). Nạp lớp đó một lần dưới mockConstruction để
 * mọi test đăng nhập/đăng ký dùng CartService giả, không chạm database.
 */
final class AuthTestSupport {

    private static boolean ready;

    private AuthTestSupport() {
    }

    static synchronized void prepareLoginSupport() {
        if (ready) {
            return;
        }
        try (MockedConstruction<CartService> ignored = Mockito.mockConstruction(CartService.class)) {
            LoginSupport.safeRedirect(null); // ép nạp lớp LoginSupport
        }
        ready = true;
    }
}
