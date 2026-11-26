<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="panel narrow">
    <h1>${form.id > 0 ? 'Cập nhật đồ uống' : 'Thêm đồ uống'}</h1>
    <form method="post" enctype="multipart/form-data" action="${ctx}/manager/drinks/${form.id > 0 ? 'update' : 'create'}">
        <input type="hidden" name="id" value="${form.id}">
        <label for="name">Tên đồ uống</label>
        <input id="name" name="name" minlength="2" maxlength="100" required value="<c:out value='${form.name}'/>">
        <label for="categoryId">Loại</label>
        <select id="categoryId" name="categoryId" required>
            <option value="">-- Chọn loại --</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat.id}" ${cat.id == form.categoryId ? 'selected' : ''}><c:out value="${cat.name}"/></option>
            </c:forEach>
        </select>
        <label for="price">Giá (1.000 - 1.000.000 đ)</label>
        <input id="price" name="price" type="number" min="1000" max="1000000" step="1" required value="${form.price > 0 ? form.price : ''}">
        <label for="description">Mô tả</label>
        <textarea id="description" name="description" maxlength="500" rows="3"><c:out value="${form.description}"/></textarea>
        <label for="imageFile">Ảnh (JPG, PNG, WEBP, tối đa 2 MB)</label>
        <input id="imageFile" name="imageFile" type="file" accept=".jpg,.jpeg,.png,.webp">
        <label class="check"><input type="checkbox" name="active" ${form.active ? 'checked' : ''}> Đang bán</label>
        <button type="submit">Lưu</button>
        <a href="${ctx}/manager/drinks">Quay lại</a>
    </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
