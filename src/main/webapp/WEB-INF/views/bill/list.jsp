<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>${basePath == '/manager/bills' ? 'Quản lý hóa đơn' : 'Hóa đơn của tôi'}</h1>
<form class="panel inline" method="get" action="${ctx}${basePath}">
    <label for="from">Từ ngày</label><input id="from" type="date" name="from" value="<c:out value='${from}'/>">
    <label for="to">Đến ngày</label><input id="to" type="date" name="to" value="<c:out value='${to}'/>">
    <button type="submit">Lọc</button>
</form>
<c:if test="${not empty result}">
<div class="table-wrap"><table>
    <thead><tr><th>Mã hóa đơn</th><th>Ngày tạo</th><th>Nhân viên</th><th class="num">Tổng tiền</th><th>Trạng thái</th><th></th></tr></thead>
    <tbody>
    <c:forEach var="b" items="${result.items}">
        <tr>
            <td><c:out value="${b.code}"/></td>
            <td><fmt:formatDate value="${b.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
            <td><c:out value="${b.staffName}"/></td>
            <td class="num"><fmt:formatNumber value="${b.total}" pattern="#,###"/> đ</td>
            <td><span class="status ${b.status}">${b.status}</span></td>
            <td class="actions">
                <a href="${ctx}${basePath}/detail?id=${b.id}">Chi tiết</a>
                <c:if test="${b.status == 'waiting'}"><a href="${ctx}/employee/pos?billId=${b.id}">Sửa</a></c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table></div>
<c:url var="base" value="${basePath}">
    <c:param name="from" value="${from}"/>
    <c:param name="to" value="${to}"/>
</c:url>
<c:set var="pagerUrl" value="${fn:escapeXml(base)}&amp;page="/>
<%@ include file="/WEB-INF/views/_pager.jspf" %>
</c:if>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
