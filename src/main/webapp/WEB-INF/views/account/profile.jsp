<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow">
    <h1>Thông tin cá nhân</h1>
    <form method="post" action="${ctx}/account/profile">
        <label>Email</label>
        <input value="<c:out value='${sessionScope.user.email}'/>" disabled>
        <label for="fullName">Họ tên</label>
        <input id="fullName" name="fullName" minlength="2" maxlength="100" required value="<c:out value='${sessionScope.user.fullName}'/>">
        <label for="phone">Số điện thoại</label>
        <input id="phone" name="phone" pattern="0[0-9]{9}" title="10 chữ số, bắt đầu bằng 0" required value="<c:out value='${sessionScope.user.phone}'/>">
        <button type="submit">Lưu</button>
    </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
