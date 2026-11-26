<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Bán hàng</h1>
<div class="pos">
    <section>
        <h2>Đồ uống</h2>
        <c:if test="${empty bill}">
            <p class="muted">Bấm "Thêm" để mở hóa đơn mới.</p>
        </c:if>
        <div class="grid">
            <c:forEach var="d" items="${drinks}">
                <article class="card">
                    <h3><c:out value="${d.name}"/></h3>
                    <p class="price"><fmt:formatNumber value="${d.price}" pattern="#,###"/> đ</p>
                    <form method="post" action="${ctx}/employee/pos/init">
                        <input type="hidden" name="billId" value="${bill.status == 'waiting' ? bill.id : 0}">
                        <input type="hidden" name="drinkId" value="${d.id}">
                        <c:if test="${empty bill or bill.status != 'waiting'}">
                            <select name="cardId" aria-label="Thẻ bàn">
                                <option value="0">Mang đi</option>
                                <c:forEach var="k" items="${cards}"><c:if test="${k.status}"><option value="${k.id}"><c:out value="${k.code}"/></option></c:if></c:forEach>
                            </select>
                        </c:if>
                        <button type="submit">Thêm</button>
                    </form>
                </article>
            </c:forEach>
        </div>
    </section>
    <aside class="panel">
        <h2>Hóa đơn</h2>
        <c:choose>
            <c:when test="${empty bill}"><p>Chưa có hóa đơn đang mở.</p></c:when>
            <c:otherwise>
                <p><strong><c:out value="${bill.code}"/></strong> &middot; ${bill.status}</p>
                <ul class="lines">
                    <c:forEach var="l" items="${details}">
                        <li>
                            <span><c:out value="${l.drinkName}"/></span>
                            <span>${l.quantity} x <fmt:formatNumber value="${l.price}" pattern="#,###"/></span>
                            <c:if test="${bill.status == 'waiting'}">
                                <form method="post" action="${ctx}/employee/pos/update-quantity" class="actions">
                                    <input type="hidden" name="billId" value="${bill.id}">
                                    <input type="hidden" name="drinkId" value="${l.drinkId}">
                                    <button name="action" value="decrease" aria-label="Giảm">-</button>
                                    <button name="action" value="increase" aria-label="Tăng">+</button>
                                    <button name="action" value="remove" class="danger" aria-label="Xóa">x</button>
                                </form>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
                <p class="price">Tổng: <fmt:formatNumber value="${bill.total}" pattern="#,###"/> đ</p>
                <c:if test="${bill.status == 'waiting'}">
                    <div class="actions">
                        <form method="post" action="${ctx}/employee/pos/checkout">
                            <input type="hidden" name="billId" value="${bill.id}"><button>Thanh toán</button>
                        </form>
                        <form method="post" action="${ctx}/employee/pos/cancel" onsubmit="return confirm('Hủy hóa đơn này?')">
                            <input type="hidden" name="billId" value="${bill.id}"><button class="danger">Hủy đơn</button>
                        </form>
                    </div>
                </c:if>
                <p><a href="${ctx}/employee/pos">Mở hóa đơn khác</a></p>
            </c:otherwise>
        </c:choose>
    </aside>
</div>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
