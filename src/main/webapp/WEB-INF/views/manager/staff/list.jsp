<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Quản lý nhân viên</h1>
<form class="panel inline" method="get" action="${ctx}/manager/staff">
    <input name="name" placeholder="Tên nhân viên" value="<c:out value='${name}'/>">
    <input name="email" placeholder="Email" value="<c:out value='${email}'/>">
    <select name="status" aria-label="Trạng thái">
        <option value="">Mọi trạng thái</option>
        <option value="1" ${status == '1' ? 'selected' : ''}>Hoạt động</option>
        <option value="0" ${status == '0' ? 'selected' : ''}>Đã khóa</option>
    </select>
    <button type="submit">Tìm</button>
    <a class="button" href="${ctx}/manager/staff/form">Thêm nhân viên</a>
</form>
<div class="table-wrap"><table>
    <thead><tr><th>Họ tên</th><th>Email</th><th>Điện thoại</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
    <tbody>
    <c:forEach var="s" items="${result.items}">
        <tr>
            <td><c:out value="${s.fullName}"/></td>
            <td><c:out value="${s.email}"/></td>
            <td><c:out value="${s.phone}"/></td>
            <td>${s.active ? 'Hoạt động' : 'Đã khóa'}</td>
            <td class="actions">
                <a href="${ctx}/manager/staff/form?id=${s.id}">Sửa</a>
                <form method="post" action="${ctx}/manager/staff/status">
                    <input type="hidden" name="id" value="${s.id}">
                    <input type="hidden" name="active" value="${!s.active}">
                    <button>${s.active ? 'Khóa' : 'Mở khóa'}</button>
                </form>
                <form method="post" action="${ctx}/manager/staff/reset-password" onsubmit="return confirm('Cấp lại mật khẩu và gửi email?')">
                    <input type="hidden" name="id" value="${s.id}"><button>Cấp lại mật khẩu</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table></div>
<c:url var="base" value="/manager/staff">
    <c:param name="name" value="${name}"/>
    <c:param name="email" value="${email}"/>
    <c:param name="status" value="${status}"/>
</c:url>
<c:set var="pagerUrl" value="${fn:escapeXml(base)}&amp;page="/>
<%@ include file="/WEB-INF/views/_pager.jspf" %>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
