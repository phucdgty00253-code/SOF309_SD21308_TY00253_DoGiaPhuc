<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  NHẬP MÃ 6 SỐ - bước 2 (#74). Giao diện 6 ô số làm ở #75; đây là bản đơn giản một ô.
    GET /login/code        -> LoginCodeServlet   (mode = "customer"), POST /login/code, gửi lại: POST /login/code/resend
    GET /admin/login/code  -> AdminLoginServlet  (mode = "admin"),    POST /admin/login/code, gửi lại: action=resend
  Request attributes:
    authImage  : String
    mode       : "customer" | "admin"
    email      : String              - email đang chờ (lấy từ session, không có trên URL)
    devCode    : String              - (chỉ khi auth.devCode=true) mã vừa tạo
    resendWait : int                 - số giây còn phải chờ trước khi gửi lại được
    errors     : Map<String,String>  - (tùy chọn) lỗi theo ô: code, admin
--%>
<c:set var="isAdmin" value="${mode eq 'admin'}"/>
<c:set var="codeAction" value="${isAdmin ? '/admin/login/code' : '/login/code'}"/>
<c:set var="resendAction" value="${isAdmin ? '/admin/login/code' : '/login/code/resend'}"/>
<t:layout title="Nhập mã" description="Nhập mã 6 số Saltair vừa gửi tới email của bạn.">
  <jsp:attribute name="head"><link rel="stylesheet" href="${ctx}/assets/css/pages/account.css"></jsp:attribute>
  <jsp:attribute name="scripts">
    <script>
      (() => {
        const btn = document.querySelector('[data-resend-wait]');
        if (!btn) return;
        let left = parseInt(btn.dataset.resendWait, 10) || 0;
        const label = btn.textContent;
        const tick = () => {
          if (left <= 0) { btn.disabled = false; btn.textContent = label; return; }
          btn.disabled = true; btn.textContent = label + ' (' + left + 's)'; left--; setTimeout(tick, 1000);
        };
        tick();
      })();
    </script>
  </jsp:attribute>
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
            <span class="t-label t-muted">${isAdmin ? 'Trang quản trị' : 'Đăng nhập'}</span>
            <h1 class="h-1">Nhập mã</h1>
            <p class="t-muted">Đã gửi tới <strong><c:out value="${email}"/></strong> ·
              <a class="link-underline" href="${ctx}${isAdmin ? '/admin/login' : '/login'}">Đổi email</a></p>
          </div>

          <div class="acc-enter" style="--d:1">
            <%@ include file="/WEB-INF/views/common/flash.jspf" %>
            <c:if test="${not empty devCode}">
              <p class="auth__devcode" data-dev-code="${devCode}">Chế độ thử: mã là <strong>${devCode}</strong></p>
            </c:if>
            <form class="auth-form" method="post" action="${ctx}${codeAction}">
              <div class="field">
                <label class="field__label" for="login-code">Mã 6 số</label>
                <input class="input code-input${not empty errors.code or not empty errors.admin ? ' is-invalid' : ''}" id="login-code" name="code"
                       inputmode="numeric" autocomplete="one-time-code" pattern="[0-9]{6}" maxlength="6" placeholder="••••••" required autofocus>
                <c:if test="${not empty errors.code}"><span class="field__error"><c:out value="${errors.code}"/></span></c:if>
                <c:if test="${not empty errors.admin}">
                  <span class="field__error"><c:out value="${errors.admin}"/> <a class="link-underline" href="${ctx}/admin/login">Tới trang đăng nhập quản trị</a></span>
                </c:if>
              </div>
              <button class="btn btn--primary btn--block" type="submit">Đăng nhập</button>
            </form>

            <form class="auth__resend" method="post" action="${ctx}${resendAction}">
              <c:if test="${isAdmin}"><input type="hidden" name="action" value="resend"></c:if>
              <span>Chưa nhận được mã?</span>
              <button type="submit" data-resend-wait="${resendWait}">Gửi lại</button>
            </form>
          </div>

          <p class="auth__alt t-small acc-enter" style="--d:2">Khi tiếp tục, bạn đồng ý với Điều khoản sử dụng của Saltair.</p>
        </div>
      </div>
    </div>
  </jsp:body>
</t:layout>
