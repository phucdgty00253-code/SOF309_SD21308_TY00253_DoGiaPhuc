<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  ĐĂNG KÝ  (GET /register -> RegisterServlet)
  Request attributes:
    authImage : String               - đường dẫn ảnh nửa trái (rỗng/null = khung xám)
    errors    : Map<String,String>   - (tùy chọn) lỗi theo ô: fullName, email, phone, password, confirmPassword, agree
    message / messageType            - (tùy chọn) thông báo chung, xem flash.jspf
  Form: POST /register  action=register  fields=fullName,email,phone,password,confirmPassword,agree
--%>
<t:layout title="Tạo tài khoản" description="Tạo tài khoản Saltair để đặt hàng nhanh hơn và nhận ưu đãi cho đơn đầu tiên.">
  <jsp:attribute name="head"><link rel="stylesheet" href="${ctx}/assets/css/pages/account.css"></jsp:attribute>
  <jsp:attribute name="scripts"><script src="${ctx}/assets/js/pages/account.js" defer></script></jsp:attribute>
  <jsp:body>
    <div class="auth">
      <div class="auth__media${empty authImage ? '' : ' auth__media--photo'}">
        <t:img src="${authImage}" alt="Saltair - đồ basic cho những ngày nắng" eager="true"/>
        <div class="auth__caption">
          <span class="t-label">Saltair</span>
          <p class="h-1">Chọn ít, mặc nhiều.</p>
        </div>
      </div>

      <div class="auth__form">
        <div class="auth__box auth__box--wide">
          <div class="auth__intro acc-enter">
            <span class="t-label t-muted">Tham gia cùng Saltair</span>
            <h1 class="h-1">Tạo tài khoản</h1>
            <p class="t-muted">Chỉ mất một phút. Có tài khoản, bạn đặt hàng nhanh hơn và theo dõi được từng đơn.</p>
          </div>

          <div class="acc-enter" style="--d:1">
            <%@ include file="/WEB-INF/views/common/flash.jspf" %>
            <form class="auth-form" method="post" action="${ctx}/register" data-demo>
              <input type="hidden" name="action" value="register">

              <div class="field">
                <label class="field__label" for="reg-name">Họ và tên</label>
                <input class="input${not empty errors.fullName ? ' is-invalid' : ''}" id="reg-name" type="text" name="fullName"
                       value="${fn:escapeXml(param.fullName)}" placeholder="Nguyễn Văn A" autocomplete="name" minlength="2" maxlength="100" required>
                <c:if test="${not empty errors.fullName}"><span class="field__error"><c:out value="${errors.fullName}"/></span></c:if>
              </div>

                <div class="field">
                  <label class="field__label" for="reg-email">Email</label>
                  <input class="input${not empty errors.email ? ' is-invalid' : ''}" id="reg-email" type="email" name="email"
                         value="${fn:escapeXml(param.email)}" placeholder="ban@email.com" autocomplete="email" maxlength="150" required>
                  <c:if test="${not empty errors.email}"><span class="field__error"><c:out value="${errors.email}"/></span></c:if>
                </div>
                <div class="field">
                  <label class="field__label" for="reg-phone">Số điện thoại</label>
                  <input class="input${not empty errors.phone ? ' is-invalid' : ''}" id="reg-phone" type="tel" name="phone"
                         value="${fn:escapeXml(param.phone)}" placeholder="0900000000" autocomplete="tel" inputmode="numeric"
                         pattern="0[0-9]{9}" title="Số điện thoại gồm 10 chữ số, bắt đầu bằng 0" required>
                  <c:if test="${not empty errors.phone}"><span class="field__error"><c:out value="${errors.phone}"/></span></c:if>
                </div>

              <div class="field">
                <label class="field__label" for="reg-password">Mật khẩu</label>
                <div class="input-group">
                  <input class="input${not empty errors.password ? ' is-invalid' : ''}" id="reg-password" type="password" name="password"
                         placeholder="Tối thiểu 6 ký tự" autocomplete="new-password" minlength="6" maxlength="72" required>
                  <button type="button" class="icon-btn input-group__btn pw-toggle" data-toggle-password="reg-password" aria-pressed="false" aria-label="Hiện hoặc ẩn mật khẩu">
                    <t:icon name="eye" cls="pw-on"/><t:icon name="eye-off" cls="pw-off"/>
                  </button>
                </div>
                <div class="pw-meter" data-password-meter="reg-password" data-level="0" aria-hidden="true">
                  <i></i><i></i><i></i><span class="pw-meter__label" data-meter-label></span>
                </div>
                <span class="field__hint">Mật khẩu tối thiểu 6 ký tự. Dùng thêm chữ hoa, số hoặc ký tự đặc biệt để mạnh hơn.</span>
                <c:if test="${not empty errors.password}"><span class="field__error"><c:out value="${errors.password}"/></span></c:if>
              </div>

              <div class="field">
                <label class="field__label" for="reg-confirm">Nhập lại mật khẩu</label>
                <div class="input-group">
                  <input class="input${not empty errors.confirmPassword ? ' is-invalid' : ''}" id="reg-confirm" type="password" name="confirmPassword"
                         placeholder="Nhập lại mật khẩu" autocomplete="new-password" minlength="6" maxlength="72" data-match="reg-password" required>
                  <button type="button" class="icon-btn input-group__btn pw-toggle" data-toggle-password="reg-confirm" aria-pressed="false" aria-label="Hiện hoặc ẩn mật khẩu nhập lại">
                    <t:icon name="eye" cls="pw-on"/><t:icon name="eye-off" cls="pw-off"/>
                  </button>
                </div>
                <c:if test="${not empty errors.confirmPassword}"><span class="field__error"><c:out value="${errors.confirmPassword}"/></span></c:if>
              </div>

              <div class="field">
                <label class="checkbox">
                  <input type="checkbox" name="agree" value="true" required>
                  <span>Tôi đồng ý với điều khoản sử dụng và chính sách bảo mật của Saltair.</span>
                </label>
                <c:if test="${not empty errors.agree}"><span class="field__error"><c:out value="${errors.agree}"/></span></c:if>
              </div>

              <button class="btn btn--primary btn--block" type="submit">Tạo tài khoản</button>
            </form>
          </div>

          <p class="auth__alt acc-enter" style="--d:2">Đã có tài khoản? <a class="link-underline" href="${ctx}/login">Đăng nhập</a></p>
        </div>
      </div>
    </div>
  </jsp:body>
</t:layout>
