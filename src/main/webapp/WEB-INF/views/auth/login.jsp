<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  ĐĂNG NHẬP  (GET /login -> LoginServlet)
  Request attributes:
    authImage : String               - đường dẫn ảnh nửa trái (rỗng/null = khung xám)
    errors    : Map<String,String>   - (tùy chọn) lỗi theo ô: email, password
    message / messageType            - (tùy chọn) thông báo chung, xem flash.jspf (vd. sai email hoặc mật khẩu)
  Tham số URL: redirect (trang quay lại sau khi đăng nhập), ?as=customer|admin|guest (DevPreviewFilter, chỉ ở demoMode)
  Form: POST /login  action=login  fields=email,password,redirect
--%>
<t:layout title="Đăng nhập" description="Đăng nhập tài khoản Saltair để mua sắm, theo dõi đơn hàng và lưu sản phẩm yêu thích.">
  <jsp:attribute name="head"><link rel="stylesheet" href="${ctx}/assets/css/pages/account.css"></jsp:attribute>
  <jsp:attribute name="scripts"><script src="${ctx}/assets/js/pages/account.js" defer></script></jsp:attribute>
  <jsp:body>
    <div class="auth">
      <div class="auth__media${empty authImage ? '' : ' auth__media--photo'}">
        <t:img src="${authImage}" alt="Saltair - đồ basic cho những ngày nắng" eager="true"/>
        <div class="auth__caption">
          <span class="t-label">Saltair</span>
          <p class="h-1">Đồ basic cho những ngày nắng.</p>
        </div>
      </div>

      <div class="auth__form">
        <div class="auth__box">
          <div class="auth__intro acc-enter">
            <span class="t-label t-muted">Chào mừng trở lại</span>
            <h1 class="h-1">Đăng nhập</h1>
            <p class="t-muted">Đăng nhập để xem đơn hàng, địa chỉ giao hàng và danh sách yêu thích.</p>
          </div>

          <div class="acc-enter" style="--d:1">
            <%@ include file="/WEB-INF/views/common/flash.jspf" %>
            <form class="auth-form" method="post" action="${ctx}/login">
              <input type="hidden" name="action" value="login">
              <input type="hidden" name="redirect" value="${fn:escapeXml(param.redirect)}">

              <div class="field">
                <label class="field__label" for="login-email">Email</label>
                <input class="input${not empty errors.email ? ' is-invalid' : ''}" id="login-email" type="email" name="email"
                       value="${fn:escapeXml(param.email)}" placeholder="ban@email.com" autocomplete="email" maxlength="150" required>
                <c:if test="${not empty errors.email}"><span class="field__error"><c:out value="${errors.email}"/></span></c:if>
              </div>

              <div class="field">
                <div class="row row--between">
                  <label class="field__label" for="login-password">Mật khẩu</label>
                  <a class="t-small link-underline" href="${ctx}/forgot-password">Quên mật khẩu?</a>
                </div>
                <div class="input-group">
                  <input class="input${not empty errors.password ? ' is-invalid' : ''}" id="login-password" type="password" name="password"
                         placeholder="Nhập mật khẩu" autocomplete="current-password" required>
                  <button type="button" class="icon-btn input-group__btn pw-toggle" data-toggle-password="login-password" aria-pressed="false" aria-label="Hiện hoặc ẩn mật khẩu">
                    <t:icon name="eye" cls="pw-on"/><t:icon name="eye-off" cls="pw-off"/>
                  </button>
                </div>
                <c:if test="${not empty errors.password}"><span class="field__error"><c:out value="${errors.password}"/></span></c:if>
              </div>

              <button class="btn btn--primary btn--block" type="submit">Đăng nhập</button>
            </form>
          </div>

          <p class="auth__alt acc-enter" style="--d:2">Chưa có tài khoản? <a class="link-underline" href="${ctx}/register">Tạo tài khoản</a></p>

          <c:if test="${initParam.demoMode == true}">
            <div class="dev-box acc-enter" style="--d:3">
              <div class="dev-box__head">
                <span class="t-label">Xem nhanh giao diện</span>
                <span class="badge badge--muted">Chỉ dành cho dev</span>
              </div>
              <p class="t-small t-muted">Giả lập đăng nhập để xem các trang theo vai trò. Khối này tự ẩn khi demoMode tắt.</p>
              <div class="dev-box__links">
                <a class="chip" href="${ctx}/profile?as=customer">Khách hàng</a>
                <a class="chip" href="${ctx}/admin?as=admin">Quản trị</a>
                <a class="chip" href="${ctx}/home?as=guest">Khách (đăng xuất)</a>
              </div>
            </div>
          </c:if>
        </div>
      </div>
    </div>
  </jsp:body>
</t:layout>
