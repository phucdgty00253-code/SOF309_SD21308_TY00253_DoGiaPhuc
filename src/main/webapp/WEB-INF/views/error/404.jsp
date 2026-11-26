<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow"><h1>Lỗi 404</h1><p>Không tìm thấy trang hoặc dữ liệu.</p><p><a href="${ctx}/home">Về trang chủ</a></p></section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
