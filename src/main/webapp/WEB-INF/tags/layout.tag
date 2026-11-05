<%@ tag description="Khung trang cho Guest và Customer: thanh thông báo, menu, nội dung, footer" pageEncoding="UTF-8" body-content="scriptless" %>
<%@ attribute name="title" required="true" description="Tiêu đề trang (tự thêm ' | Saltair')" %>
<%@ attribute name="description" required="false" %>
<%@ attribute name="nav" required="false" description="Mục menu đang chọn: shop | new | best | blog | about" %>
<%@ attribute name="bodyClass" required="false" %>
<%@ attribute name="overlay" required="false" type="java.lang.Boolean" description="true = menu trong suốt đè lên hero (trang chủ)" %>
<%@ attribute name="head" fragment="true" required="false" description="Thêm vào <head>: CSS riêng của trang" %>
<%@ attribute name="scripts" fragment="true" required="false" description="Thêm cuối <body>: JS riêng của trang" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<fmt:setLocale value="vi_VN" scope="request"/>
<c:set var="cartCount" value="${empty sessionScope.cartCount ? 0 : sessionScope.cartCount}"/>
<!DOCTYPE html>
<html lang="vi" class="no-js">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="demo-mode" content="${initParam.demoMode}">
  <title><c:out value="${title}"/> | Saltair</title>
  <meta name="description" content="${fn:escapeXml(empty description ? 'Saltair - đồ basic cho những ngày nắng.' : description)}">
  <link rel="icon" type="image/png" href="${ctx}/assets/brand/saltair-logo.png">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;700;800&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${ctx}/assets/css/style.css">
  <jsp:invoke fragment="head"/>
</head>
<body class="${bodyClass}${overlay ? ' has-overlay-header' : ''}">
<%@ include file="/WEB-INF/views/common/icons.jspf" %>
<a class="skip-link" href="#main">Bỏ qua, đến nội dung chính</a>

<%-- Thanh thông báo --%>
<c:if test="${not empty applicationScope.announcements}">
  <div class="announcement-bar" data-announcements>
    <div class="announcement-bar__inner">
      <button type="button" class="announcement-bar__arrow" data-ann-prev aria-label="Thông báo trước"><t:icon name="chevron-left" size="sm"/></button>
      <div class="announcement-bar__viewport">
        <c:forEach items="${applicationScope.announcements}" var="a" varStatus="s">
          <p class="announcement-bar__msg${s.first ? ' is-active' : ''}"><c:out value="${a}"/></p>
        </c:forEach>
      </div>
      <button type="button" class="announcement-bar__arrow" data-ann-next aria-label="Thông báo sau"><t:icon name="chevron-right" size="sm"/></button>
    </div>
  </div>
</c:if>

<%-- Menu --%>
<header class="header${overlay ? ' header--overlay' : ''}">
  <div class="header__inner">
    <nav class="header__nav" aria-label="Menu chính">
      <button type="button" class="icon-btn header__burger" data-drawer-open="mobile-nav" aria-label="Mở menu"><t:icon name="menu" size="md"/></button>
      <div class="has-dropdown">
        <a class="nav-link${nav == 'shop' ? ' is-active' : ''}" href="${ctx}/products" data-dropdown-trigger>Cửa hàng <t:icon name="chevron-down"/></a>
        <div class="dropdown">
          <div class="dropdown__panel">
            <span class="dropdown__title">Danh mục</span>
            <c:forEach items="${applicationScope.navCategories}" var="c">
              <a class="link-underline" href="${ctx}/products?category=${fn:escapeXml(c.slug)}"><c:out value="${c.name}"/></a>
            </c:forEach>
            <a class="link-underline bold" href="${ctx}/products">Tất cả sản phẩm</a>
          </div>
        </div>
      </div>
      <a class="nav-link${nav == 'new' ? ' is-active' : ''}" href="${ctx}/products?sort=newest">Mới</a>
      <a class="nav-link${nav == 'best' ? ' is-active' : ''}" href="${ctx}/products?sort=bestseller">Bán chạy</a>
      <a class="nav-link${nav == 'blog' ? ' is-active' : ''}" href="${ctx}/blog">Blog</a>
      <a class="nav-link${nav == 'about' ? ' is-active' : ''}" href="${ctx}/about">Giới thiệu</a>
    </nav>

    <a class="header__logo" href="${ctx}/home" aria-label="Saltair - Trang chủ">
      <img src="${ctx}/assets/brand/saltair-logo.png" alt="Saltair">
    </a>

    <div class="header__actions">
      <button type="button" class="icon-btn" data-search-open aria-label="Tìm kiếm"><t:icon name="search" size="md"/></button>
      <c:choose>
        <c:when test="${empty sessionScope.currentUser}">
          <a class="icon-btn" href="${ctx}/login" aria-label="Đăng nhập"><t:icon name="user" size="md"/></a>
        </c:when>
        <c:otherwise>
          <div class="has-dropdown">
            <a class="icon-btn" href="${ctx}/profile" aria-label="Tài khoản" data-dropdown-trigger><t:icon name="user" size="md"/></a>
            <div class="dropdown dropdown--right">
              <div class="dropdown__panel">
                <span class="dropdown__title"><c:out value="${sessionScope.currentUser.fullName}"/></span>
                <c:if test="${sessionScope.currentUser.role == 'ADMIN'}"><a class="link-underline bold" href="${ctx}/admin">Trang quản trị</a></c:if>
                <a class="link-underline" href="${ctx}/profile">Tài khoản của tôi</a>
                <a class="link-underline" href="${ctx}/orders">Đơn hàng</a>
                <a class="link-underline" href="${ctx}/wishlist">Yêu thích</a>
                <a class="link-underline" href="${ctx}/addresses">Sổ địa chỉ</a>
                <a class="link-underline" href="${ctx}/logout">Đăng xuất</a>
              </div>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
      <a class="icon-btn" href="${ctx}/cart" aria-label="Giỏ hàng"><t:icon name="bag" size="md"/><span class="cart-badge"${cartCount > 0 ? '' : ' hidden'}>${cartCount}</span></a>
    </div>
  </div>
</header>

<%-- Lớp phủ, tìm kiếm, menu điện thoại --%>
<div class="overlay" id="overlay"></div>
<div class="search-panel" id="search-panel" role="dialog" aria-label="Tìm kiếm">
  <form class="search-panel__row" action="${ctx}/products" method="get">
    <input class="input" type="search" name="q" placeholder="Tìm sản phẩm..." aria-label="Từ khóa" autocomplete="off">
    <button class="btn btn--primary" type="submit">Tìm</button>
    <button class="icon-btn" type="button" data-panel-close aria-label="Đóng"><t:icon name="x" size="md"/></button>
  </form>
  <div class="search-panel__hints">
    <span class="t-small t-muted">Gợi ý:</span>
    <c:forEach items="${applicationScope.navCategories}" var="c">
      <a class="chip" href="${ctx}/products?category=${fn:escapeXml(c.slug)}"><c:out value="${c.name}"/></a>
    </c:forEach>
  </div>
</div>
<aside class="drawer drawer--left" id="mobile-nav" aria-label="Menu">
  <div class="drawer__head">
    <img src="${ctx}/assets/brand/saltair-logo.png" alt="Saltair" height="36" style="height:36px;width:auto">
    <button type="button" class="icon-btn" data-panel-close aria-label="Đóng menu"><t:icon name="x" size="md"/></button>
  </div>
  <div class="drawer__body mobile-nav">
    <a href="${ctx}/products">Cửa hàng</a>
    <div class="sub">
      <c:forEach items="${applicationScope.navCategories}" var="c"><a href="${ctx}/products?category=${fn:escapeXml(c.slug)}"><c:out value="${c.name}"/></a></c:forEach>
    </div>
    <a href="${ctx}/products?sort=newest">Mới</a>
    <a href="${ctx}/products?sort=bestseller">Bán chạy</a>
    <a href="${ctx}/blog">Blog</a>
    <a href="${ctx}/about">Giới thiệu</a>
    <c:choose>
      <c:when test="${empty sessionScope.currentUser}"><a href="${ctx}/login">Đăng nhập</a></c:when>
      <c:otherwise><a href="${ctx}/profile">Tài khoản</a><a href="${ctx}/orders">Đơn hàng</a><a href="${ctx}/logout">Đăng xuất</a></c:otherwise>
    </c:choose>
  </div>
</aside>

<main id="main">
  <jsp:doBody/>
</main>

<%-- Footer --%>
<footer class="footer">
  <div class="footer__grid">
    <div class="footer__brand">
      <img src="${ctx}/assets/brand/saltair-logo.png" alt="Saltair">
      <p style="max-width:384px">Saltair làm những món đồ basic nhẹ, thoáng và dễ phối, cho những ngày nắng và cả những ngày không. Chọn ít, mặc nhiều.</p>
    </div>
    <div>
      <p class="footer__title">Cửa hàng</p>
      <ul class="footer__list">
        <c:forEach items="${applicationScope.navCategories}" var="c"><li><a class="link-underline" href="${ctx}/products?category=${fn:escapeXml(c.slug)}"><c:out value="${c.name}"/></a></li></c:forEach>
        <li><a class="link-underline" href="${ctx}/products?sort=newest">Hàng mới về</a></li>
      </ul>
    </div>
    <div>
      <p class="footer__title">Liên hệ</p>
      <ul class="footer__list">
        <li>support@saltair.vn</li>
        <li>0900 000 000</li>
        <li>123 Đường Biển Xanh, Đà Nẵng</li>
        <li class="t-muted">Thứ 2 - Thứ 7, 9:00 - 18:00</li>
      </ul>
    </div>
    <div>
      <p class="footer__title">Về Saltair</p>
      <ul class="footer__list">
        <li><a class="link-underline" href="${ctx}/about">Giới thiệu</a></li>
        <li><a class="link-underline" href="${ctx}/blog">Blog</a></li>
        <li><a class="link-underline" href="${ctx}/orders">Đơn hàng của tôi</a></li>
        <li><a class="link-underline" href="${ctx}/wishlist">Yêu thích</a></li>
      </ul>
    </div>
  </div>
  <div class="footer__bottom">
    <span>© 2026 Saltair. Đồ án học tập, không bán hàng thật.</span>
    <span>Phí ship cố định 30.000đ · Miễn phí khi dùng mã giảm giá</span>
  </div>
</footer>

<script src="${ctx}/assets/js/app.js" defer></script>
<jsp:invoke fragment="scripts"/>
</body>
</html>
