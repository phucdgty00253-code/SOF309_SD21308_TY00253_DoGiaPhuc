<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Top 5 đồ uống bán chạy</h1>
<%@ include file="/WEB-INF/views/manager/stats/_range.jspf" %>
<div class="table-wrap"><table>
    <thead><tr><th>Hạng</th><th>Đồ uống</th><th class="num">Đơn giá hiện tại</th><th class="num">Số lượng bán</th><th class="num">Doanh thu</th></tr></thead>
    <tbody>
    <c:forEach var="d" items="${drinks}" varStatus="s">
        <tr>
            <td>${s.count}</td>
            <td><c:out value="${d.name}"/></td>
            <td class="num"><fmt:formatNumber value="${d.price}" pattern="#,###"/> đ</td>
            <td class="num">${d.totalQuantity}</td>
            <td class="num"><fmt:formatNumber value="${d.totalRevenue}" pattern="#,###"/> đ</td>
        </tr>
    </c:forEach>
    </tbody>
</table></div>
<c:if test="${empty drinks}"><p>Không có dữ liệu trong khoảng thời gian này.</p></c:if>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
