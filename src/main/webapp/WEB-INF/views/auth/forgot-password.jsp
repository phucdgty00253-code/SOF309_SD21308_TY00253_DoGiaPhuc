<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  QUÊN MẬT KHẨU  (GET /forgot-password -> ForgotPasswordServlet)
  Request attributes:
    sent   : Boolean               - true khi param sent=1: hiện trạng thái "đã gửi liên kết"
    errors : Map<String,String>    - (tùy chọn) lỗi theo ô: email
    message / messageType          - (tùy chọn) thông báo chung, xem flash.jspf
  Form: POST /forgot-password  action=sendReset  fields=email
--%>
<t:layout title="Quên mật khẩu" description="Nhập email để nhận liên kết đặt lại mật khẩu Saltair.">
  <jsp:attribute name="head"><link rel="stylesheet" href="${ctx}/assets/css/pages/account.css"></jsp:attribute>
  <jsp:attribute name="scripts"><script src="${ctx}/assets/js/pages/account.js" defer></script></jsp:attribute>
  <jsp:body>
    <div class="auth auth--single">
      <div class="auth__form">
        <div class="auth__box auth-card acc-enter">
          <c:choose>
            <c:when test="${sent}">
              <span class="state-icon state-icon--ok"><t:icon name="check-circle"/></span>
              <div class="auth__intro">
                <h1 class="h-1">Kiểm tra email của bạn</h1>
                <p class="t-muted">Nếu email này đã đăng ký tại Saltair, chúng tôi vừa gửi liên kết đặt lại mật khẩu. Liên kết có hiệu lực trong thời gian ngắn, hãy kiểm tra cả hộp thư rác.</p>
              </div>
              <div class="btn-row">
                <a class="btn btn--primary btn--block" href="${ctx}/login">Quay lại đăng nhập</a>
                <a class="btn btn--ghost btn--block" href="${ctx}/forgot-password">Gửi lại liên kết</a>
              </div>
              <c:if test="${initParam.demoMode == true}">
                <div class="dev-box">
                  <div class="dev-box__head">
                    <span class="t-label">Xem nhanh giao diện</span>
                    <span class="badge badge--muted">Chỉ dành cho dev</span>
                  </div>
                  <div class="dev-box__links">
                    <a class="chip" href="${ctx}/reset-password?token=demo-token">Mở liên kết đặt lại</a>
                    <a class="chip" href="${ctx}/reset-password?invalid=1">Liên kết hết hạn</a>
                  </div>
                </div>
              </c:if>
            </c:when>
            <c:otherwise>
              <span class="state-icon"><t:icon name="mail"/></span>
              <div class="auth__intro">
                <h1 class="h-1">Quên mật khẩu?</h1>
                <p class="t-muted">Nhập email đã đăng ký, chúng tôi sẽ gửi liên kết để bạn đặt mật khẩu mới.</p>
              </div>
              <%@ include file="/WEB-INF/views/common/flash.jspf" %>
              <form class="auth-form" method="post" action="${ctx}/forgot-password">
                <input type="hidden" name="action" value="sendReset">
                <div class="field">
                  <label class="field__label" for="forgot-email">Email</label>
                  <input class="input${not empty errors.email ? ' is-invalid' : ''}" id="forgot-email" type="email" name="email"
                         value="${fn:escapeXml(param.email)}" placeholder="ban@email.com" autocomplete="email" maxlength="150" required>
                  <c:if test="${not empty errors.email}"><span class="field__error"><c:out value="${errors.email}"/></span></c:if>
                </div>
                <button class="btn btn--primary btn--block" type="submit">Gửi liên kết đặt lại</button>
              </form>
              <a class="link-underline t-small" href="${ctx}/login">Quay lại đăng nhập</a>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
  </jsp:body>
</t:layout>
