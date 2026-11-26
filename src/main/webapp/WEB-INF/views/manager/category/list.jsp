<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Quản lý loại đồ uống</h1>
<form class="panel inline" method="post" action="${ctx}/manager/categories/${form.id > 0 ? 'update' : 'create'}">
    <input type="hidden" name="id" value="${form.id}">
    <label for="name">Tên loại</label>
    <input id="name" name="name" minlength="2" maxlength="50" required value="<c:out value='${form.name}'/>">
    <label class="check"><input type="checkbox" name="active" ${form.active ? 'checked' : ''}> Hoạt động</label>
    <button type="submit">${form.id > 0 ? 'Cập nhật' : 'Thêm mới'}</button>
    <c:if test="${form.id > 0}"><a href="${ctx}/manager/categories">Hủy sửa</a></c:if>
</form>
<div class="table-wrap"><table>
    <thead><tr><th>Mã</th><th>Tên loại</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
    <tbody>
    <c:forEach var="cat" items="${categories}">
        <tr>
            <td>${cat.id}</td>
            <td><c:out value="${cat.name}"/></td>
            <td>${cat.active ? 'Hoạt động' : 'Ngừng'}</td>
            <td class="actions">
                <a href="${ctx}/manager/categories?editId=${cat.id}">Sửa</a>
                <form method="post" action="${ctx}/manager/categories/delete" onsubmit="return confirm('Xóa loại này?')">
                    <input type="hidden" name="id" value="${cat.id}"><button class="danger">Xóa</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table></div>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
