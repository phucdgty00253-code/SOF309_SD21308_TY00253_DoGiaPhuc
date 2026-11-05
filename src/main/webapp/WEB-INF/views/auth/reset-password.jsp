<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  ĐẶT LẠI MẬT KHẨU  (GET /reset-password?token=... -> ResetPasswordServlet)
  Request attributes:
    token      : String               - token lấy từ URL (đã trim, có thể rỗng)
    tokenValid : Boolean              - false khi thiếu token, token sai/hết hạn hoặc param invalid=1: hiện trạng thái "liên kết hết hạn"
    errors     : Map<String,String>   - (tùy chọn) lỗi theo ô: password, confirmPassword
    message / messageType             - (tùy chọn) thông báo chung, xem flash.jspf
  Form: POST /reset-password  action=resetPassword  fields=token,password,confirmPassword
--%>
<t:layout title="Đặt lại mật khẩu" description="Tạo mật khẩu mới cho tài khoản Saltair của bạn.">
  <jsp:attribute name="head"><link rel="stylesheet" href="${ctx}/assets/css/pages/account.css"></jsp:attribute>
  <jsp:attribute name="scripts"><script src="${ctx}/assets/js/pages/account.js" defer></script></jsp:attribute>
  <jsp:body>
    <div class="auth auth--single">
      <div class="auth__form">
        <div class="auth__box auth-card acc-enter">
          <c:choose>
            <c:when test="${tokenValid}">
              <span class="state-icon"><t:icon name="lock"/></span>
              <div class="auth__intro">
                <h1 class="h-1">Đặt mật khẩu mới</h1>
                <p class="t-muted">Mật khẩu mới tối thiểu 6 ký tự. Sau khi đổi, bạn đăng nhập lại bằng mật khẩu này.</p>
              </div>
              <%@ include file="/WEB-INF/views/common/flash.jspf" %>
              <form class="auth-form" method="post" action="${ctx}/reset-password" data-demo>
                <input type="hidden" name="action" value="resetPassword">
                <input type="hidden" name="token" value="${fn:escapeXml(token)}">

                <div class="field">
                  <label class="field__label" for="reset-password">Mật khẩu mới</label>
                  <div class="input-group">
                    <input class="input${not empty errors.password ? ' is-invalid' : ''}" id="reset-password" type="password" name="password"
                           placeholder="Tối thiểu 6 ký tự" autocomplete="new-password" minlength="6" maxlength="72" required>
                    <button type="button" class="icon-btn input-group__btn pw-toggle" data-toggle-password="reset-password" aria-pressed="false" aria-label="Hiện hoặc ẩn mật khẩu">
                      <t:icon name="eye" cls="pw-on"/><t:icon name="eye-off" cls="pw-off"/>
                    </button>
                  </div>
                  <div class="pw-meter" data-password-meter="reset-password" data-level="0" aria-hidden="true">
                    <i></i><i></i><i></i><span class="pw-meter__label" data-meter-label></span>
                  </div>
                  <c:if test="${not empty errors.password}"><span class="field__error"><c:out value="${errors.password}"/></span></c:if>
                </div>

                <div class="field">
                  <label class="field__label" for="reset-confirm">Nhập lại mật khẩu mới</label>
                  <div class="input-group">
                    <input class="input${not empty errors.confirmPassword ? ' is-invalid' : ''}" id="reset-confirm" type="password" name="confirmPassword"
                           placeholder="Nhập lại mật khẩu" autocomplete="new-password" minlength="6" maxlength="72" data-match="reset-password" required>
                    <button type="button" class="icon-btn input-group__btn pw-toggle" data-toggle-password="reset-confirm" aria-pressed="false" aria-label="Hiện hoặc ẩn mật khẩu nhập lại">
                      <t:icon name="eye" cls="pw-on"/><t:icon name="eye-off" cls="pw-off"/>
                    </button>
                  </div>
                  <c:if test="${not empty errors.confirmPassword}"><span class="field__error"><c:out value="${errors.confirmPassword}"/></span></c:if>
                </div>

                <button class="btn btn--primary btn--block" type="submit">Lưu mật khẩu mới</button>
              </form>
              <a class="link-underline t-small" href="${ctx}/login">Quay lại đăng nhập</a>
            </c:when>
            <c:otherwise>
              <span class="state-icon state-icon--warn"><t:icon name="alert"/></span>
              <div class="auth__intro">
                <h1 class="h-1">Liên kết đã hết hạn</h1>
                <p class="t-muted">Liên kết đặt lại mật khẩu này không còn hiệu lực hoặc đã được sử dụng. Hãy yêu cầu một liên kết mới.</p>
              </div>
              <div class="btn-row">
                <a class="btn btn--primary btn--block" href="${ctx}/forgot-password">Gửi lại liên kết</a>
                <a class="btn btn--ghost btn--block" href="${ctx}/login">Quay lại đăng nhập</a>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
  </jsp:body>
</t:layout>
