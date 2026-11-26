<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel">
    <h1>Hóa đơn <c:out value="${bill.code}"/></h1>
    <p>Nhân viên tạo: <strong><c:out value="${bill.staffName}"/></strong></p>
    <p>Ngày tạo: <fmt:formatDate value="${bill.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
    <p>Thẻ bàn: <c:out value="${empty bill.cardCode ? 'Mang đi' : bill.cardCode}"/></p>
    <p>Trạng thái: <span class="status ${bill.status}">${bill.status}</span></p>
    <div class="table-wrap"><table>
        <thead><tr><th>Đồ uống</th><th class="num">Số lượng</th><th class="num">Đơn giá</th><th class="num">Thành tiền</th></tr></thead>
        <tbody>
        <c:forEach var="l" items="${details}">
            <tr>
                <td><c:out value="${l.drinkName}"/></td>
                <td class="num">${l.quantity}</td>
                <td class="num"><fmt:formatNumber value="${l.price}" pattern="#,###"/> đ</td>
                <td class="num"><fmt:formatNumber value="${l.quantity * l.price}" pattern="#,###"/> đ</td>
            </tr>
        </c:forEach>
        </tbody>
    </table></div>
    <p class="price">Tổng tiền: <fmt:formatNumber value="${bill.total}" pattern="#,###"/> đ</p>
    <c:if test="${basePath == '/manager/bills' and bill.status != 'cancel'}">
        <form method="post" action="${ctx}/manager/bills/cancel" onsubmit="return confirm('Hủy hóa đơn này?')">
            <input type="hidden" name="id" value="${bill.id}"><button class="danger">Hủy hóa đơn</button>
        </form>
    </c:if>
    <p><a href="${ctx}${basePath}">Quay lại danh sách</a></p>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
