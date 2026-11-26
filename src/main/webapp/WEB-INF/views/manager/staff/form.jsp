<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow">
    <h1>${form.id > 0 ? 'Cập nhật nhân viên' : 'Thêm nhân viên'}</h1>
    <form method="post" action="${ctx}/manager/staff/${form.id > 0 ? 'update' : 'create'}">
        <input type="hidden" name="id" value="${form.id}">
        <label for="fullName">Họ tên</label>
        <input id="fullName" name="fullName" minlength="2" maxlength="100" required value="<c:out value='${form.fullName}'/>">
        <label for="email">Email</label>
        <input id="email" name="email" type="email" maxlength="150" required value="<c:out value='${form.email}'/>">
        <label for="phone">Số điện thoại</label>
        <input id="phone" name="phone" pattern="0[0-9]{9}" title="10 chữ số, bắt đầu bằng 0" required value="<c:out value='${form.phone}'/>">
        <c:if test="${form.id == 0}">
            <label for="password">Mật khẩu (6-50 ký tự)</label>
            <input id="password" name="password" type="password" minlength="6" maxlength="50" required>
        </c:if>
        <label class="check"><input type="checkbox" name="active" ${form.active ? 'checked' : ''}> Hoạt động</label>
        <button type="submit">Lưu</button>
        <a href="${ctx}/manager/staff">Quay lại</a>
    </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
