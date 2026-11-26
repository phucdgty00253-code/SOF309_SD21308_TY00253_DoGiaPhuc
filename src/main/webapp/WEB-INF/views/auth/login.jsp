<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow">
    <h1>Đăng nhập</h1>
    <form method="post" action="${ctx}/auth/login">
        <label for="email">Email</label>
        <input id="email" name="email" type="email" maxlength="150" required value="<c:out value='${email}'/>">
        <label for="password">Mật khẩu</label>
        <input id="password" name="password" type="password" maxlength="50" required>
        <button type="submit" id="login">Đăng nhập</button>
    </form>
    <p><a href="${ctx}/auth/forgot-password">Quên mật khẩu?</a></p>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
