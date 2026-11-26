<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Thực đơn PolyCoffee</h1>
<nav class="chips">
    <a class="${categoryId == 0 ? 'current' : ''}" href="${ctx}/home">Tất cả</a>
    <c:forEach var="cat" items="${categories}">
        <c:if test="${cat.active}">
            <a class="${categoryId == cat.id ? 'current' : ''}" href="${ctx}/home?categoryId=${cat.id}"><c:out value="${cat.name}"/></a>
        </c:if>
    </c:forEach>
</nav>
<div class="grid">
    <c:forEach var="d" items="${drinks}">
        <article class="card">
            <c:choose>
                <c:when test="${not empty d.image}"><img src="${ctx}/uploads/<c:out value='${d.image}'/>" alt="<c:out value='${d.name}'/>"></c:when>
                <c:otherwise><div class="noimage" aria-hidden="true">&#9749;</div></c:otherwise>
            </c:choose>
            <h3><c:out value="${d.name}"/></h3>
            <p class="muted"><c:out value="${d.categoryName}"/></p>
            <p class="price"><fmt:formatNumber value="${d.price}" pattern="#,###"/> đ</p>
        </article>
    </c:forEach>
</div>
<c:if test="${empty drinks}"><p>Chưa có đồ uống nào.</p></c:if>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
