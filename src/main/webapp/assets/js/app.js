/* ==========================================================================
   Saltair - JavaScript dùng chung (thuần, không thư viện)
   Mỗi khối là một tính năng độc lập, kích hoạt bằng thuộc tính data-* trong JSP.
   Phần đánh dấu [EXTRA] là hiệu ứng thêm ngoài trang gốc.
   ========================================================================== */
(() => {
  'use strict';

  const $ = (sel, root = document) => root.querySelector(sel);
  const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  const canHover = window.matchMedia('(hover: hover)').matches;

  document.documentElement.classList.remove('no-js');

  /** Chế độ giao diện: form chưa có logic chỉ hiện thông báo. Bật/tắt ở context-param demoMode trong web.xml. */
  const DEMO = ($('meta[name="demo-mode"]') || {}).content === 'true';
  const DEMO_MESSAGE = 'Đang ở chế độ giao diện: chức năng này sẽ được gắn logic sau.';

  /* ---------- Toast ---------- */
  function toast(message, ms = 2800) {
    let box = $('.toasts');
    if (!box) {
      box = document.createElement('div');
      box.className = 'toasts';
      box.setAttribute('role', 'status');
      box.setAttribute('aria-live', 'polite');
      document.body.appendChild(box);
    }
    const el = document.createElement('div');
    el.className = 'toast';
    el.textContent = message;
    box.appendChild(el);
    setTimeout(() => {
      el.classList.add('is-leaving');
      setTimeout(() => el.remove(), 260);
    }, ms);
  }

  function formatVnd(n) {
    return new Intl.NumberFormat('vi-VN').format(Math.round(n)) + 'đ';
  }

  window.Saltair = { toast, formatVnd, demo: DEMO };

  /* ---------- Thanh thông báo ---------- */
  $$('[data-announcements]').forEach((root) => {
    const msgs = $$('.announcement-bar__msg', root);
    if (!msgs.length) return;
    let i = 0;
    let timer;
    const show = (n) => {
      i = (n + msgs.length) % msgs.length;
      msgs.forEach((m, idx) => m.classList.toggle('is-active', idx === i));
    };
    const start = () => {
      if (msgs.length > 1 && !reduceMotion) timer = setInterval(() => show(i + 1), 5000);
    };
    const stop = () => clearInterval(timer);
    $('[data-ann-prev]', root)?.addEventListener('click', () => { stop(); show(i - 1); start(); });
    $('[data-ann-next]', root)?.addEventListener('click', () => { stop(); show(i + 1); start(); });
    root.addEventListener('mouseenter', stop);
    root.addEventListener('mouseleave', start);
    show(0);
    start();
  });

  /* ---------- Header: sticky, thu nhỏ khi cuộn ---------- */
  const header = $('.header');
  if (header) {
    const onScroll = () => header.classList.toggle('is-scrolled', window.scrollY > 8);
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });
  }

  /* ---------- Menu thả xuống (cảm ứng: bấm để mở) ---------- */
  $$('.has-dropdown').forEach((item) => {
    const trigger = $('[data-dropdown-trigger]', item);
    if (!trigger) return;
    trigger.addEventListener('click', (e) => {
      if (canHover) return; // máy tính: mở bằng hover
      e.preventDefault();
      $$('.has-dropdown.is-open').forEach((o) => o !== item && o.classList.remove('is-open'));
      item.classList.toggle('is-open');
    });
  });
  document.addEventListener('click', (e) => {
    if (!e.target.closest('.has-dropdown')) $$('.has-dropdown.is-open').forEach((o) => o.classList.remove('is-open'));
  });

  /* ---------- Lớp phủ, tìm kiếm, ngăn kéo ---------- */
  const overlay = $('#overlay');
  const lockScroll = (lock) => { document.body.style.overflow = lock ? 'hidden' : ''; };

  function closeAllPanels() {
    $$('.drawer.is-open, .search-panel.is-open').forEach((p) => p.classList.remove('is-open'));
    overlay?.classList.remove('is-open');
    lockScroll(false);
  }
  function openPanel(panel, focusSelector) {
    if (!panel) return;
    closeAllPanels();
    panel.classList.add('is-open');
    overlay?.classList.add('is-open');
    lockScroll(true);
    if (focusSelector) setTimeout(() => $(focusSelector, panel)?.focus(), 150);
  }

  $$('[data-search-open]').forEach((b) => b.addEventListener('click', () => openPanel($('#search-panel'), 'input')));
  $$('[data-drawer-open]').forEach((b) => b.addEventListener('click', () => openPanel(document.getElementById(b.dataset.drawerOpen))));
  $$('[data-panel-close]').forEach((b) => b.addEventListener('click', closeAllPanels));
  overlay?.addEventListener('click', closeAllPanels);
  document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeAllPanels(); });

  /* ---------- Băng chuyền ---------- */
  $$('[data-carousel]').forEach((root) => {
    const track = $('.carousel__track', root);
    if (!track) return;
    const prev = $('[data-carousel-prev]', root);
    const next = $('[data-carousel-next]', root);
    const bar = $('.carousel__bar', root);
    const update = () => {
      const max = track.scrollWidth - track.clientWidth;
      const ratio = track.scrollWidth ? track.clientWidth / track.scrollWidth : 1;
      if (bar) {
        bar.style.width = Math.min(100, ratio * 100) + '%';
        bar.style.left = (max > 0 ? (track.scrollLeft / max) * (100 - ratio * 100) : 0) + '%';
      }
      if (prev) prev.disabled = track.scrollLeft <= 2;
      if (next) next.disabled = track.scrollLeft >= max - 2;
    };
    const step = () => Math.max(track.clientWidth * 0.8, 240);
    prev?.addEventListener('click', () => track.scrollBy({ left: -step(), behavior: 'smooth' }));
    next?.addEventListener('click', () => track.scrollBy({ left: step(), behavior: 'smooth' }));
    track.addEventListener('scroll', update, { passive: true });
    window.addEventListener('resize', update);
    update();

    // Kéo bằng chuột trên máy tính [EXTRA]
    let down = false, startX = 0, startLeft = 0, moved = false;
    track.addEventListener('pointerdown', (e) => {
      if (e.pointerType !== 'mouse') return;
      down = true; moved = false; startX = e.clientX; startLeft = track.scrollLeft;
    });
    window.addEventListener('pointermove', (e) => {
      if (!down) return;
      const dx = e.clientX - startX;
      if (Math.abs(dx) > 4) { moved = true; track.style.scrollSnapType = 'none'; track.style.scrollBehavior = 'auto'; }
      track.scrollLeft = startLeft - dx;
    });
    window.addEventListener('pointerup', () => {
      if (!down) return;
      down = false;
      track.style.scrollSnapType = ''; track.style.scrollBehavior = '';
    });
    track.addEventListener('click', (e) => { if (moved) { e.preventDefault(); e.stopPropagation(); moved = false; } }, true);
  });

  /* ---------- Hero nhiều ảnh ---------- */
  $$('[data-hero-slider]').forEach((root) => {
    const slides = $$('.hero__slide', root);
    const dots = $$('.hero__dot', root);
    if (slides.length < 2) return;
    let i = 0, timer;
    const show = (n) => {
      i = (n + slides.length) % slides.length;
      slides.forEach((s, k) => s.classList.toggle('is-active', k === i));
      dots.forEach((d, k) => d.classList.toggle('is-active', k === i));
    };
    const start = () => { if (!reduceMotion) timer = setInterval(() => show(i + 1), 6500); };
    const stop = () => clearInterval(timer);
    dots.forEach((d, k) => d.addEventListener('click', () => { stop(); show(k); start(); }));
    root.addEventListener('mouseenter', stop);
    root.addEventListener('mouseleave', start);
    show(0);
    start();
  });

  /* ---------- Hiện dần khi cuộn tới [EXTRA] ---------- */
  $$('[data-stagger]').forEach((group) => {
    $$(':scope > .reveal, :scope .reveal', group).forEach((el, idx) => el.style.setProperty('--i', Math.min(idx, 8)));
  });
  const revealEls = $$('.reveal');
  if ('IntersectionObserver' in window && !reduceMotion) {
    const io = new IntersectionObserver((entries) => {
      entries.forEach((en) => {
        if (en.isIntersecting) { en.target.classList.add('is-visible'); io.unobserve(en.target); }
      });
    }, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });
    revealEls.forEach((el) => io.observe(el));
  } else {
    revealEls.forEach((el) => el.classList.add('is-visible'));
  }

  /* ---------- Đếm số tăng dần cho Dashboard [EXTRA] ---------- */
  $$('[data-countup]').forEach((el) => {
    const target = Number(el.dataset.countup);
    const suffix = el.dataset.suffix || '';
    if (!Number.isFinite(target) || reduceMotion) return;
    const run = () => {
      const t0 = performance.now(), dur = 900;
      const tick = (t) => {
        const p = Math.min(1, (t - t0) / dur);
        const eased = 1 - Math.pow(1 - p, 3);
        el.textContent = new Intl.NumberFormat('vi-VN').format(Math.round(target * eased)) + suffix;
        if (p < 1) requestAnimationFrame(tick);
      };
      requestAnimationFrame(tick);
    };
    if ('IntersectionObserver' in window) {
      const io = new IntersectionObserver((en) => { if (en[0].isIntersecting) { run(); io.disconnect(); } });
      io.observe(el);
    } else run();
  });

  /* ---------- Số lượng ---------- */
  $$('.qty').forEach((q) => {
    const input = $('input', q);
    if (!input) return;
    const clamp = (v) => {
      const min = Number(input.min || 1), max = Number(input.max || 99);
      return Math.min(max, Math.max(min, v));
    };
    const set = (v) => { input.value = clamp(v); input.dispatchEvent(new Event('change', { bubbles: true })); };
    $('[data-qty-minus]', q)?.addEventListener('click', () => set(Number(input.value) - 1));
    $('[data-qty-plus]', q)?.addEventListener('click', () => set(Number(input.value) + 1));
    input.addEventListener('blur', () => { input.value = clamp(Number(input.value) || 1); });
  });

  /* ---------- Hộp thoại ---------- */
  $$('[data-dialog-open]').forEach((b) => b.addEventListener('click', () => {
    const d = document.getElementById(b.dataset.dialogOpen);
    if (d && typeof d.showModal === 'function') {
      // Cho phép truyền dữ liệu vào form trong dialog: data-fill='{"name":"x"}'
      if (b.dataset.fill) {
        try {
          const data = JSON.parse(b.dataset.fill);
          Object.entries(data).forEach(([k, v]) => {
            const f = d.querySelector('[name="' + k + '"]');
            if (!f) return;
            if (f.type === 'checkbox') f.checked = !!v; else f.value = v;
          });
        } catch (err) { /* bỏ qua */ }
      }
      const title = b.dataset.dialogTitle;
      if (title) { const t = $('[data-dialog-title]', d); if (t) t.textContent = title; }
      d.showModal();
    }
  }));
  $$('dialog').forEach((d) => {
    d.addEventListener('click', (e) => { if (e.target === d) d.close(); });
    $$('[data-dialog-close]', d).forEach((b) => b.addEventListener('click', () => d.close()));
  });

  /* ---------- Yêu thích ---------- */
  $$('[data-wish]').forEach((b) => b.addEventListener('click', (e) => {
    e.preventDefault();
    e.stopPropagation();
    const paint = (on) => {
      b.classList.toggle('is-on', on);
      b.classList.remove('is-pop'); void b.offsetWidth; b.classList.add('is-pop');
      b.setAttribute('aria-pressed', String(on));
    };
    const url = b.dataset.wishUrl;
    if (!url || !b.dataset.productId) { // nút chưa nối server: chỉ đổi giao diện
      const on = !b.classList.contains('is-on');
      paint(on);
      if (DEMO) toast(on ? 'Đã thêm vào yêu thích (chế độ giao diện)' : 'Đã bỏ khỏi yêu thích (chế độ giao diện)');
      return;
    }
    // Gửi lên WishlistServlet (action=toggle). Chưa đăng nhập thì AuthFilter chuyển sang trang đăng nhập.
    fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'X-Requested-With': 'XMLHttpRequest' },
      body: 'action=toggle&productId=' + encodeURIComponent(b.dataset.productId)
    }).then((res) => {
      if (res.redirected || !(res.headers.get('Content-Type') || '').includes('json')) {
        window.location.href = res.redirected ? res.url : url;
        return null;
      }
      return res.json();
    }).then((data) => {
      if (!data) return;
      paint(data.inWishlist);
      toast(data.inWishlist ? 'Đã thêm vào yêu thích' : 'Đã bỏ khỏi yêu thích');
    }).catch(() => toast('Không cập nhật được yêu thích, vui lòng thử lại.'));
  }));

  /* ---------- Chế độ giao diện: chặn form/nút chưa có logic ---------- */
  function bumpCart(n) {
    const badge = $('.cart-badge');
    if (!badge) return;
    const cur = Number(badge.textContent) || 0;
    badge.textContent = String(cur + n);
    badge.hidden = false;
    badge.classList.remove('is-bump'); void badge.offsetWidth; badge.classList.add('is-bump');
  }
  $$('form[data-demo]').forEach((form) => form.addEventListener('submit', (e) => {
    if (!DEMO) return;
    if (!form.checkValidity()) return; // để trình duyệt tự báo lỗi
    e.preventDefault();
    if (form.hasAttribute('data-demo-cart')) bumpCart(1);
    toast(form.dataset.demoMessage || DEMO_MESSAGE);
  }));
  $$('[data-demo]:not(form)').forEach((el) => el.addEventListener('click', (e) => {
    if (!DEMO) return;
    e.preventDefault();
    toast(el.dataset.demoMessage || DEMO_MESSAGE);
  }));

  /* ---------- Xác nhận trước khi thực hiện ---------- */
  $$('[data-confirm]').forEach((el) => {
    const evt = el.tagName === 'FORM' ? 'submit' : 'click';
    el.addEventListener(evt, (e) => { if (!window.confirm(el.dataset.confirm)) { e.preventDefault(); e.stopImmediatePropagation(); } }, true);
  });

  /* ---------- Hiện/ẩn mật khẩu ---------- */
  $$('[data-toggle-password]').forEach((b) => b.addEventListener('click', () => {
    const input = document.getElementById(b.dataset.togglePassword);
    if (!input) return;
    input.type = input.type === 'password' ? 'text' : 'password';
    b.setAttribute('aria-pressed', String(input.type === 'text'));
  }));

  /* ---------- Tab ---------- */
  $$('[data-tabs]').forEach((root) => {
    const tabs = $$('[role="tab"]', root);
    const panels = $$('[role="tabpanel"]', root);
    const select = (idx) => {
      tabs.forEach((t, i) => { t.classList.toggle('is-active', i === idx); t.setAttribute('aria-selected', String(i === idx)); });
      panels.forEach((p, i) => { p.hidden = i !== idx; });
    };
    tabs.forEach((t, i) => t.addEventListener('click', () => select(i)));
    select(Math.max(0, tabs.findIndex((t) => t.classList.contains('is-active'))));
  });

  /* ---------- Tự gửi form khi đổi ô chọn ---------- */
  $$('select[data-autosubmit], input[data-autosubmit]').forEach((el) => el.addEventListener('change', () => el.form && el.form.submit()));

  /* ---------- Sao chép mã ---------- */
  $$('[data-copy]').forEach((b) => b.addEventListener('click', async () => {
    try { await navigator.clipboard.writeText(b.dataset.copy); toast('Đã sao chép: ' + b.dataset.copy); }
    catch (err) { toast(b.dataset.copy); }
  }));

  /* ---------- Chọn size/màu: cập nhật nhãn hiển thị ---------- */
  $$('[data-variant-form]').forEach((form) => {
    const colorLabel = $('[data-selected-color]', form);
    $$('input[name="colorId"]', form).forEach((r) => r.addEventListener('change', () => {
      if (colorLabel) colorLabel.textContent = r.dataset.name || '';
    }));
    const checked = $('input[name="colorId"]:checked', form);
    if (checked && colorLabel) colorLabel.textContent = checked.dataset.name || '';
  });

  /* ---------- Ảnh lỗi/trống: thay bằng khung xám ---------- */
  document.addEventListener('error', (e) => {
    const img = e.target;
    if (!(img instanceof HTMLImageElement) || img.dataset.failed) return;
    img.dataset.failed = '1';
    const ph = document.createElement('div');
    ph.className = 'ph';
    ph.setAttribute('role', 'img');
    ph.setAttribute('aria-label', img.alt || 'Ảnh chưa có');
    ph.innerHTML = '<svg class="icon" aria-hidden="true"><use href="#i-image"></use></svg>';
    img.replaceWith(ph);
  }, true);
})();
