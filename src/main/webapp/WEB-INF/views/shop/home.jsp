<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  TRANG CHỦ  (GET /home -> HomeServlet)
  Dữ liệu: banners, newProducts, bestSellers, categories, latestReviews, reviewAvg, reviewTotal, latestBlogs, splitImage1, splitImage2
--%>
<t:layout title="Trang chủ" nav="home" overlay="true" description="Saltair - đồ basic phong cách biển, nhẹ, thoáng và dễ phối.">

  <%-- 1. Hero --%>
  <section class="hero" data-hero-slider aria-label="Nổi bật">
    <div class="hero__slides">
      <c:forEach items="${banners}" var="b" varStatus="s">
        <div class="hero__slide${s.first ? ' is-active' : ''}">
          <div class="hero__media"><t:img src="${b.image}" alt="${b.title}" dark="true" eager="${s.first}"/></div>
          <div class="hero__content">
            <p class="hero__label entrance">Bộ sưu tập mới</p>
            <c:choose><c:when test="${s.first}"><h1 class="h-1 hero__title entrance"><c:out value="${b.title}"/></h1></c:when><c:otherwise><p class="h-1 hero__title entrance"><c:out value="${b.title}"/></p></c:otherwise></c:choose>
            <div class="hero__cta entrance"><a class="btn btn--white" href="${ctx}${b.linkUrl}">Mua ngay</a></div>
          </div>
        </div>
      </c:forEach>
    </div>
    <c:if test="${fn:length(banners) > 1}">
      <div class="hero__dots">
        <c:forEach items="${banners}" var="b" varStatus="s"><button type="button" class="hero__dot${s.first ? ' is-active' : ''}" aria-label="Banner ${s.count}"></button></c:forEach>
      </div>
    </c:if>
  </section>

  <%-- 2. Dải lợi ích --%>
  <section class="usp" aria-label="Lợi ích">
    <div class="usp__item reveal"><t:icon name="star"/><p class="usp__title"><fmt:formatNumber value="${reviewAvg}" minFractionDigits="1" maxFractionDigits="1"/>/5 từ khách hàng</p><p class="usp__text">Dựa trên ${reviewTotal} đánh giá của khách đã nhận hàng.</p></div>
    <div class="usp__item reveal"><t:icon name="truck"/><p class="usp__title">Phí ship 30.000đ</p><p class="usp__text">Giao tận nơi, thanh toán khi nhận hàng.</p></div>
    <div class="usp__item reveal"><t:icon name="percent"/><p class="usp__title">Miễn phí ship với mã giảm giá</p><p class="usp__text">Áp dụng mã hợp lệ ở bước thanh toán.</p></div>
    <div class="usp__item reveal"><t:icon name="tag"/><p class="usp__title">Ưu đãi đơn đầu tiên</p><p class="usp__text">Nhập WELCOME10 giảm 10%, tối đa 50.000đ.</p></div>
  </section>

  <%-- 3. Hàng mới về --%>
  <section class="section--lg" data-carousel>
    <div class="container section__head reveal">
      <h2 class="h-1">Hàng mới về</h2>
      <div class="row">
        <div class="carousel__nav hide-mobile">
          <button type="button" class="carousel__btn" data-carousel-prev aria-label="Trước"><t:icon name="chevron-left"/></button>
          <button type="button" class="carousel__btn" data-carousel-next aria-label="Sau"><t:icon name="chevron-right"/></button>
        </div>
        <a class="btn btn--outline" href="${ctx}/products?sort=newest">Xem tất cả</a>
      </div>
    </div>
    <div class="carousel__track">
      <c:forEach items="${newProducts}" var="p" varStatus="s"><div class="carousel__item"><t:product-card product="${p}"/></div></c:forEach>
    </div>
    <div class="carousel__progress"><div class="carousel__bar"></div></div>
  </section>

  <%-- 4. Khối chữ nền navy --%>
  <section class="dark-text reveal">
    <span class="dark-text__label">Basic cho ngày nắng</span>
    <h2 class="h-1 dark-text__title">Thứ đầu tiên bạn mặc vào mỗi sáng nên thật dễ chịu.</h2>
    <p class="dark-text__body">Saltair làm đồ basic từ cotton mềm, form dễ mặc và màu dễ phối. Ít chi tiết, nhiều thoải mái, để bạn mặc nhiều hơn là đắn đo.</p>
  </section>

  <%-- 5. Hai thẻ ảnh --%>
  <section class="split-cards" data-stagger>
    <a class="split-card reveal" href="${ctx}/products?category=ao-ni-hoodie">
      <div class="split-card__media"><t:img src="${splitImage1}" alt="Áo nỉ và hoodie" dark="true"/></div>
      <div class="split-card__body">
        <span class="t-label">Áo nỉ và Hoodie</span>
        <p class="h-1 mt-8">Mềm đến ngỡ ngàng</p>
        <p>Nỉ bông dày dặn, ấm vừa phải. Mặc một lần là muốn mặc cả tuần.</p>
        <span class="btn btn--white">Khám phá</span>
      </div>
    </a>
    <a class="split-card reveal" href="${ctx}/products?category=ao-khoac">
      <div class="split-card__media"><t:img src="${splitImage2}" alt="Áo khoác" dark="true"/></div>
      <div class="split-card__body">
        <span class="t-label">Áo khoác</span>
        <p class="h-1 mt-8">Nhẹ cho những ngày nhiều gió</p>
        <p>Áo khoác gió, bomber, linen: khoác ngoài là xong một set đồ.</p>
        <span class="btn btn--white">Xem áo khoác</span>
      </div>
    </a>
  </section>

  <%-- 6. Danh mục --%>
  <h2 class="h-1 section-title reveal">Danh mục</h2>
  <section class="tile-row" data-stagger>
    <c:forEach items="${categories}" var="c">
      <a class="tile reveal${empty c.image ? ' tile--empty' : ''}" href="${ctx}/products?category=${fn:escapeXml(c.slug)}">
        <div class="tile__media"><t:img src="${c.image}" alt="${c.name}"/></div>
        <span class="tile__title"><c:out value="${c.name}"/></span>
      </a>
    </c:forEach>
  </section>

  <%-- 7. Bán chạy --%>
  <section class="section--lg" data-carousel>
    <div class="container section__head reveal">
      <h2 class="h-1">Bán chạy</h2>
      <div class="row">
        <div class="carousel__nav hide-mobile">
          <button type="button" class="carousel__btn" data-carousel-prev aria-label="Trước"><t:icon name="chevron-left"/></button>
          <button type="button" class="carousel__btn" data-carousel-next aria-label="Sau"><t:icon name="chevron-right"/></button>
        </div>
        <a class="btn btn--outline" href="${ctx}/products?sort=bestseller">Xem tất cả</a>
      </div>
    </div>
    <div class="carousel__track">
      <c:forEach items="${bestSellers}" var="p"><div class="carousel__item"><t:product-card product="${p}"/></div></c:forEach>
    </div>
    <div class="carousel__progress"><div class="carousel__bar"></div></div>
  </section>

  <%-- 8. Đánh giá khách hàng --%>
  <section class="container section reveal">
    <div class="reviews-strip">
      <div class="reviews-summary">
        <span class="reviews-summary__score"><fmt:formatNumber value="${reviewAvg}" minFractionDigits="1" maxFractionDigits="1"/></span>
        <t:stars rating="${reviewAvg}"/>
        <span class="t-small t-muted">Từ ${reviewTotal} đánh giá</span>
      </div>
      <div class="review-grid">
        <c:forEach items="${latestReviews}" var="r">
          <article class="review-card">
            <div class="review-card__meta"><span class="review-card__author"><c:out value="${r.userName}"/></span><span class="review-card__date"><fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy"/></span></div>
            <t:stars rating="${r.rating}"/>
            <p><c:out value="${r.comment}"/></p>
            <a class="review-card__product link-underline" href="${ctx}/product?slug=${fn:escapeXml(r.productSlug)}"><c:out value="${r.productName}"/></a>
          </article>
        </c:forEach>
      </div>
    </div>
  </section>

  <%-- 9. Khám phá thêm (Blog) --%>
  <section class="section--lg">
    <div class="container section__head reveal">
      <div>
        <span class="t-label">Khám phá thêm</span>
        <h2 class="h-1 mt-8">Không chỉ là đồ basic</h2>
      </div>
      <a class="btn btn--outline" href="${ctx}/blog">Đọc blog</a>
    </div>
    <div class="tile-row tile-row--blog" data-stagger>
      <c:forEach items="${latestBlogs}" var="b">
        <a class="reveal" href="${ctx}/blog/detail?slug=${fn:escapeXml(b.slug)}">
          <div class="media media--4x5"><t:img src="${b.coverImage}" alt="${b.title}"/></div>
          <div class="stack--sm" style="padding:16px 4px 0;display:grid;gap:6px">
            <span class="t-small t-muted"><fmt:formatDate value="${b.createdAt}" pattern="dd/MM/yyyy"/></span>
            <span class="h-2"><c:out value="${b.title}"/></span>
            <span class="t-muted"><c:out value="${b.summary}"/></span>
          </div>
        </a>
      </c:forEach>
    </div>
  </section>

  <%-- 10. Ưu đãi --%>
  <section class="cta-band reveal">
    <div>
      <p class="cta-band__label">Tham gia cùng Saltair</p>
      <h2 class="h-display mt-16">Mã ưu đãi.<br>Đơn đầu tiên.</h2>
    </div>
    <div class="stack">
      <p>Đăng ký tài khoản và nhập mã <strong>WELCOME10</strong> ở bước thanh toán để giảm 10% (tối đa 50.000đ) cho đơn từ 200.000đ. Dùng mã còn được miễn phí ship.</p>
      <div class="cta-band__form">
        <a class="btn btn--primary" href="${ctx}/register">Đăng ký ngay</a>
        <button type="button" class="btn btn--outline" data-copy="WELCOME10"><t:icon name="copy"/> Sao chép mã</button>
      </div>
    </div>
  </section>
</t:layout>
