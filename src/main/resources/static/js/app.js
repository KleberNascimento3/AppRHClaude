/* ===== AppRH — app.js ===== */

const AppRH = (() => {

  /* ---- Toast ---- */
  const toast = {
    container: null,

    _ensureContainer() {
      if (!this.container) {
        this.container = document.getElementById('appToastContainer');
      }
      return this.container;
    },

    show(message, type = 'success', duration = 4000) {
      const container = this._ensureContainer();
      if (!container || !message) return;

      const icons = {
        success: 'bi-check-circle-fill',
        error:   'bi-x-circle-fill',
        warning: 'bi-exclamation-triangle-fill',
        info:    'bi-info-circle-fill',
      };

      const colors = {
        success: '#22c55e',
        error:   '#ef4444',
        warning: '#f59e0b',
        info:    '#06b6d4',
      };

      const id = 'toast-' + Date.now();
      const el = document.createElement('div');
      el.id = id;
      el.className = 'toast align-items-center border-0 mb-2 show';
      el.setAttribute('role', 'alert');
      el.innerHTML = `
        <div class="d-flex align-items-center p-3 gap-2">
          <i class="bi ${icons[type] || icons.info}" style="color:${colors[type] || colors.info};font-size:1.1rem;flex-shrink:0"></i>
          <div class="flex-grow-1" style="font-size:.875rem;font-weight:500">${message}</div>
          <button type="button" class="btn-close btn-sm ms-2" onclick="this.closest('.toast').remove()" aria-label="Fechar"></button>
        </div>`;

      container.appendChild(el);

      if (duration > 0) {
        setTimeout(() => {
          el.classList.remove('show');
          setTimeout(() => el.remove(), 300);
        }, duration);
      }
    },
  };

  /* ---- Flash messages from server ---- */
  function initFlashMessages() {
    const msgEl = document.getElementById('serverMensagem');
    const errEl = document.getElementById('serverErro');
    if (msgEl && msgEl.dataset.value) toast.show(msgEl.dataset.value, 'success');
    if (errEl && errEl.dataset.value) toast.show(errEl.dataset.value, 'error');
  }

  /* ---- Confirm modal ---- */
  const confirm = {
    _resolve: null,

    show(title, body) {
      return new Promise(resolve => {
        this._resolve = resolve;
        const modal = document.getElementById('appConfirmModal');
        if (!modal) { resolve(window.confirm(body)); return; }
        modal.querySelector('#confirmTitle').textContent = title || 'Confirmar';
        modal.querySelector('#confirmBody').textContent  = body  || 'Tem certeza?';
        const btn = modal.querySelector('#confirmOkBtn');
        btn.onclick = () => { resolve(true);  bootstrap.Modal.getInstance(modal)?.hide(); };
        modal.querySelector('#confirmCancelBtn').onclick = () => { resolve(false); bootstrap.Modal.getInstance(modal)?.hide(); };
        new bootstrap.Modal(modal).show();
      });
    },
  };

  /* ---- Delete links with confirmation ---- */
  function initDeleteConfirm() {
    document.querySelectorAll('[data-confirm-delete]').forEach(el => {
      el.addEventListener('click', async function(e) {
        e.preventDefault();
        const name = this.dataset.confirmDelete || 'este registro';
        const ok = await confirm.show('Excluir registro', `Deseja excluir "${name}"? Esta ação não pode ser desfeita.`);
        if (ok) window.location.href = this.href;
      });
    });
  }

  /* ---- LGPD checkboxes ---- */
  function initLgpdCheckboxes() {
    document.querySelectorAll('input[type="checkbox"].lgpd').forEach(el => {
      el.addEventListener('change', function() {
        const cooperadoId = this.closest('tr')?.dataset?.cooperadoId;
        const type  = this.dataset.lgpd;
        const value = this.checked;
        if (!cooperadoId) return;

        fetch(`/ControlaLGPD/${cooperadoId}?type=${type}&value=${value}`, { method: 'POST' })
          .then(r => { if (r.ok) toast.show('Preferência salva.', 'success', 2000); else toast.show('Erro ao salvar preferência.', 'error'); })
          .catch(() => toast.show('Erro de conexão.', 'error'));
      });
    });
  }

  /* ---- Form submit spinner ---- */
  function initFormSpinners() {
    document.querySelectorAll('form[data-loading]').forEach(form => {
      form.addEventListener('submit', function() {
        const btn = this.querySelector('[type="submit"]');
        if (btn) btn.classList.add('btn-loading');
      });
    });
  }

  /* ---- Sidebar mobile toggle ---- */
  function initSidebar() {
    const toggle  = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('appSidebar');
    const overlay = document.getElementById('sidebarOverlay');
    if (!toggle || !sidebar) return;

    toggle.addEventListener('click', () => {
      sidebar.classList.toggle('sidebar-open');
      overlay?.classList.toggle('show');
    });

    overlay?.addEventListener('click', () => {
      sidebar.classList.remove('sidebar-open');
      overlay.classList.remove('show');
    });
  }

  /* ---- Mark active sidebar link ---- */
  function initActiveSidebarLink() {
    const path = window.location.pathname;
    document.querySelectorAll('.sidebar-link[href]').forEach(link => {
      const href = link.getAttribute('href');
      if (href && href !== '/' && path.startsWith(href)) {
        link.classList.add('active');
        const parent = link.closest('.collapse');
        if (parent) {
          parent.classList.add('show');
          const trigger = document.querySelector(`[data-bs-target="#${parent.id}"]`);
          if (trigger) trigger.setAttribute('aria-expanded', 'true');
        }
      } else if (href === '/' && path === '/') {
        link.classList.add('active');
      }
    });
  }

  /* ---- Init ---- */
  function init() {
    initSidebar();
    initActiveSidebarLink();
    initFlashMessages();
    initDeleteConfirm();
    initLgpdCheckboxes();
    initFormSpinners();
  }

  document.addEventListener('DOMContentLoaded', init);

  return { toast, confirm };
})();
