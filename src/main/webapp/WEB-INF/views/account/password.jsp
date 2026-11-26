<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow">
    <h1>Đổi mật khẩu</h1>
    <form method="post" action="${ctx}/account/password">
        <label for="oldPassword">Mật khẩu hiện tại</label>
        <input id="oldPassword" name="oldPassword" type="password" required>
        <label for="newPassword">Mật khẩu mới (6-50 ký tự)</label>
        <input id="newPassword" name="newPassword" type="password" minlength="6" maxlength="50" required>
        <label for="confirmPassword">Nhập lại mật khẩu mới</label>
        <input id="confirmPassword" name="confirmPassword" type="password" minlength="6" maxlength="50" required>
        <button type="submit">Đổi mật khẩu</button>
    </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
