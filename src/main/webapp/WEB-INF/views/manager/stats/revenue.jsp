<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Doanh thu theo ngày</h1>
<%@ include file="/WEB-INF/views/manager/stats/_range.jspf" %>
<c:choose>
<c:when test="${empty points}"><p>Không có dữ liệu trong khoảng thời gian này.</p></c:when>
<c:otherwise>
    <p class="price">Tổng doanh thu: <fmt:formatNumber value="${totalRevenue}" pattern="#,###"/> đ &middot; ${totalBills} hóa đơn</p>
    <section class="panel chart" aria-label="Biểu đồ doanh thu theo ngày">
        <c:forEach var="p" items="${points}">
            <div class="bar-row">
                <span><fmt:formatDate value="${p.date}" pattern="dd/MM"/></span>
                <span class="track"><span class="bar" style="width:${maxRevenue > 0 ? p.revenue * 100 / maxRevenue : 0}%"></span></span>
                <span class="num"><fmt:formatNumber value="${p.revenue}" pattern="#,###"/> đ</span>
            </div>
        </c:forEach>
    </section>
    <div class="table-wrap"><table>
        <thead><tr><th>Ngày</th><th class="num">Số hóa đơn</th><th class="num">Doanh thu</th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${points}">
            <tr>
                <td><fmt:formatDate value="${p.date}" pattern="dd/MM/yyyy"/></td>
                <td class="num">${p.billCount}</td>
                <td class="num"><fmt:formatNumber value="${p.revenue}" pattern="#,###"/> đ</td>
            </tr>
        </c:forEach>
        </tbody>
    </table></div>
</c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
