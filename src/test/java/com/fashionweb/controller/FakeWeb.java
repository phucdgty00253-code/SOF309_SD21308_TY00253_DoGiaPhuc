package com.fashionweb.controller;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.Role;
import com.fashionweb.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Request / response / session giả dùng chung cho test Controller (Servlet), không cần Tomcat và không cần database.
 * Tham số, header, attribute của request và của session đều lưu trong Map để test đọc lại được.
 */
public final class FakeWeb {

    public static final String CONTEXT = "/saltair";

    public final HttpServletRequest req = mock(HttpServletRequest.class);
    public final HttpServletResponse resp = mock(HttpServletResponse.class);
    public final HttpSession session = mock(HttpSession.class);
    public final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    public final Map<String, String> params = new HashMap<>();
    public final Map<String, String> headers = new HashMap<>();
    public final Map<String, Object> requestAttrs = new HashMap<>();
    public final Map<String, Object> sessionAttrs = new HashMap<>();

    /** Đường dẫn JSP gần nhất được forward tới. */
    public String forwardedTo;

    /** false: request chưa có session (getSession(false) trả null). */
    public boolean hasSession = true;

    public FakeWeb() {
        when(req.getContextPath()).thenReturn(CONTEXT);
        when(req.getServerName()).thenReturn("localhost");
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
        when(req.getParameter(anyString())).thenAnswer(i -> params.get(i.<String>getArgument(0)));
        when(req.getHeader(anyString())).thenAnswer(i -> headers.get(i.<String>getArgument(0)));
        when(req.getAttribute(anyString())).thenAnswer(i -> requestAttrs.get(i.<String>getArgument(0)));
        org.mockito.Mockito.doAnswer(i -> requestAttrs.put(i.getArgument(0), i.getArgument(1)))
                .when(req).setAttribute(anyString(), org.mockito.ArgumentMatchers.any());
        when(req.getSession()).thenReturn(session);
        when(req.getSession(anyBoolean())).thenAnswer(i -> i.<Boolean>getArgument(0) || hasSession ? session : null);
        when(req.getRequestDispatcher(anyString())).thenAnswer(i -> {
            forwardedTo = i.getArgument(0);
            return dispatcher;
        });

        when(session.getAttribute(anyString())).thenAnswer(i -> sessionAttrs.get(i.<String>getArgument(0)));
        org.mockito.Mockito.doAnswer(i -> sessionAttrs.put(i.getArgument(0), i.getArgument(1)))
                .when(session).setAttribute(anyString(), org.mockito.ArgumentMatchers.any());
        org.mockito.Mockito.doAnswer(i -> sessionAttrs.remove(i.<String>getArgument(0)))
                .when(session).removeAttribute(anyString());
    }

    public FakeWeb param(String name, String value) {
        params.put(name, value);
        return this;
    }

    public FakeWeb header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public FakeWeb loginAs(User user) {
        sessionAttrs.put(AppConstant.SESSION_USER, user);
        return this;
    }

    public static User user(int id, Role role) {
        User u = new User();
        u.setId(id);
        u.setEmail("user" + id + "@saltair.vn");
        u.setFullName("Người dùng " + id);
        u.setRole(role);
        u.setActive(true);
        return u;
    }
}
