package com.poly.sof309.controllers.support;

import com.polycoffee.entity.User;
import com.polycoffee.util.AuthUtil;
import com.polycoffee.util.FlashUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/** Request, response, session giả: gọi controller mà không cần Tomcat. */
public class WebMock {
    public static final String CONTEXT = "/polycoffee";

    public final HttpServletRequest request = mock(HttpServletRequest.class);
    public final HttpServletResponse response = mock(HttpServletResponse.class);
    public final HttpSession session = mock(HttpSession.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, Object> attributes = new HashMap<>();
    private final Map<String, Object> sessionAttributes = new HashMap<>();
    private boolean hasSession;

    private WebMock(String method, String path) {
        when(request.getMethod()).thenReturn(method);
        when(request.getServletPath()).thenReturn(path);
        when(request.getRequestURI()).thenReturn(CONTEXT + path);
        when(request.getContextPath()).thenReturn(CONTEXT);
        when(request.getProtocol()).thenReturn("HTTP/1.1");
        when(request.getParameter(anyString())).thenAnswer(i -> parameters.get(i.<String>getArgument(0)));
        when(request.getAttribute(anyString())).thenAnswer(i -> attributes.get(i.<String>getArgument(0)));
        doAnswer(i -> attributes.put(i.getArgument(0), i.getArgument(1)))
                .when(request).setAttribute(anyString(), org.mockito.ArgumentMatchers.any());
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        when(request.getSession()).thenAnswer(i -> {
            hasSession = true;
            return session;
        });
        when(request.getSession(false)).thenAnswer(i -> hasSession ? session : null);
        when(session.getAttribute(anyString())).thenAnswer(i -> sessionAttributes.get(i.<String>getArgument(0)));
        doAnswer(i -> sessionAttributes.put(i.getArgument(0), i.getArgument(1)))
                .when(session).setAttribute(anyString(), org.mockito.ArgumentMatchers.any());
        doAnswer(i -> sessionAttributes.remove(i.<String>getArgument(0)))
                .when(session).removeAttribute(anyString());
        doAnswer(i -> {
            sessionAttributes.clear();
            hasSession = false;
            return null;
        }).when(session).invalidate();
    }

    public static WebMock get(String path) {
        return new WebMock("GET", path);
    }

    public static WebMock post(String path) {
        return new WebMock("POST", path);
    }

    public WebMock param(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public WebMock loginAs(User user) {
        hasSession = true;
        sessionAttributes.put(AuthUtil.SESSION_USER, user);
        return this;
    }

    /** Chạy controller qua đúng đường vào của servlet container. */
    public WebMock run(HttpServlet servlet) {
        try {
            servlet.service(request, response);
        } catch (Exception e) {
            throw new AssertionError("Controller ném lỗi ngoài dự kiến", e);
        }
        return this;
    }

    public Object attr(String name) {
        return attributes.get(name);
    }

    public String error() {
        return (String) attributes.get("error");
    }

    public Object sessionAttr(String name) {
        return sessionAttributes.get(name);
    }

    public String flashSuccess() {
        return (String) sessionAttributes.get(FlashUtil.SUCCESS);
    }

    public String flashError() {
        return (String) sessionAttributes.get(FlashUtil.ERROR);
    }

    /** Tên view được forward, ví dụ "auth/login"; null nếu không forward. */
    public String view() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(request, atLeast(0)).getRequestDispatcher(captor.capture());
        if (captor.getAllValues().isEmpty()) {
            return null;
        }
        return captor.getValue().replace("/WEB-INF/views/", "").replace(".jsp", "");
    }

    /** Đường dẫn redirect đã bỏ context path; null nếu không redirect. */
    public String redirect() {
        try {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(response, atLeast(0)).sendRedirect(captor.capture());
            return captor.getAllValues().isEmpty() ? null : captor.getValue().replaceFirst("^" + CONTEXT, "");
        } catch (java.io.IOException e) {
            throw new AssertionError(e);
        }
    }

    /** Mã lỗi đã sendError; 0 nếu không có. */
    public int errorStatus() {
        try {
            ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
            verify(response, atLeast(0)).sendError(captor.capture());
            return captor.getAllValues().isEmpty() ? 0 : captor.getValue();
        } catch (java.io.IOException e) {
            throw new AssertionError(e);
        }
    }
}
