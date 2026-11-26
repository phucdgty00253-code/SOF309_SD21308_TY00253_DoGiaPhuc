<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Quản lý đồ uống</h1>
<form class="panel inline" method="get" action="${ctx}/manager/drinks">
    <input name="keyword" placeholder="Tên đồ uống" maxlength="100" value="<c:out value='${keyword}'/>">
    <select name="categoryId" aria-label="Loại">
        <option value="0">Tất cả loại</option>
        <c:forEach var="cat" items="${categories}">
            <option value="${cat.id}" ${cat.id == categoryId ? 'selected' : ''}><c:out value="${cat.name}"/></option>
        </c:forEach>
    </select>
    <select name="status" aria-label="Trạng thái">
        <option value="">Mọi trạng thái</option>
        <option value="1" ${status == '1' ? 'selected' : ''}>Đang bán</option>
        <option value="0" ${status == '0' ? 'selected' : ''}>Ngừng bán</option>
    </select>
    <button type="submit">Tìm</button>
    <a class="button" href="${ctx}/manager/drinks/form">Thêm đồ uống</a>
</form>
<div class="table-wrap"><table>
    <thead><tr><th>Mã</th><th>Tên</th><th>Loại</th><th class="num">Giá</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
    <tbody>
    <c:forEach var="d" items="${result.items}">
        <tr>
            <td>${d.id}</td>
            <td><c:out value="${d.name}"/></td>
            <td><c:out value="${d.categoryName}"/></td>
            <td class="num"><fmt:formatNumber value="${d.price}" pattern="#,###"/> đ</td>
            <td>${d.active ? 'Đang bán' : 'Ngừng bán'}</td>
            <td class="actions">
                <a href="${ctx}/manager/drinks/form?id=${d.id}">Sửa</a>
                <form method="post" action="${ctx}/manager/drinks/delete" onsubmit="return confirm('Xóa đồ uống này?')">
                    <input type="hidden" name="id" value="${d.id}"><button class="danger">Xóa</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table></div>
<c:url var="base" value="/manager/drinks">
    <c:param name="keyword" value="${keyword}"/>
    <c:param name="categoryId" value="${categoryId}"/>
    <c:param name="status" value="${status}"/>
</c:url>
<c:set var="pagerUrl" value="${fn:escapeXml(base)}&amp;page="/>
<%@ include file="/WEB-INF/views/_pager.jspf" %>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
