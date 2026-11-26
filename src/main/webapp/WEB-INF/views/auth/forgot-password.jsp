<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow">
    <h1>Quên mật khẩu</h1>
    <form method="post" action="${ctx}/auth/forgot-password">
        <label for="email">Email đăng nhập</label>
        <input id="email" name="email" type="email" maxlength="150" required>
        <button type="submit">Gửi mật khẩu mới</button>
    </form>
    <p><a href="${ctx}/auth/login">Quay lại đăng nhập</a></p>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
