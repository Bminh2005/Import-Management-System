import { Search, Bell, Globe2, ChevronDown, User } from 'lucide-react';
import { useState } from 'react';
import { useLocation } from 'react-router';

export function Header() {
  const [notificationCount] = useState(5);
  const [currentLang, setCurrentLang] = useState('VI');
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [showLangMenu, setShowLangMenu] = useState(false);
  
  const location = useLocation();

  // Determine which navigation items to show based on current route
  const isOverseasRoute = location.pathname.startsWith('/overseas');
  const isSalesRoute = location.pathname.startsWith('/sales');
  const isWarehouseRoute = location.pathname.startsWith('/warehouse');

  // Don't show header on portal (login) page
  if (location.pathname === '/') {
    return null;
  }

  let role = 'Người dùng';
  if (isOverseasRoute) {
    role = 'Bộ phận Đặt hàng Quốc tế';
  } else if (isSalesRoute) {
    role = 'Bộ phận Bán hàng';
  } else if (isWarehouseRoute) {
    role = 'Bộ phận Quản lý Kho';
  }

  return (
    <header className="h-[52px] bg-white border-b border-[var(--border)] flex items-center justify-between px-4 shadow-sm">
      {/* Search Bar */}
      <div className="flex-1 max-w-[320px]">
        <div className="relative">
          <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--text-secondary)]" />
          <input
            type="text"
            placeholder="Tìm kiếm yêu cầu, mặt hàng..."
            className="w-full pl-8 pr-3 py-1.5 text-sm bg-[var(--muted)] border border-transparent rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)] focus:bg-white transition-all"
          />
        </div>
      </div>

      {/* Right Section */}
      <div className="flex items-center gap-2">
        {/* Language Switcher */}
        <div className="relative">
          <button
            onClick={() => setShowLangMenu(!showLangMenu)}
            className="flex items-center gap-1.5 px-2.5 py-1.5 rounded-md hover:bg-[var(--muted)] transition-colors"
          >
            <Globe2 className="w-4 h-4 text-[var(--text-secondary)]" />
            <span className="text-xs font-medium">{currentLang}</span>
            <ChevronDown className="w-3 h-3 text-[var(--text-secondary)]" />
          </button>

          {showLangMenu && (
            <div className="absolute right-0 mt-1 w-28 bg-white rounded-md shadow-lg border border-[var(--border)] py-0.5 z-50">
              {['EN', 'VI'].map((lang) => (
                <button
                  key={lang}
                  onClick={() => {
                    setCurrentLang(lang);
                    setShowLangMenu(false);
                  }}
                  className={`w-full px-3 py-1.5 text-left text-xs hover:bg-[var(--muted)] transition-colors ${
                    currentLang === lang ? 'bg-[var(--muted)] font-medium' : ''
                  }`}
                >
                  {lang === 'EN' ? 'English' : 'Tiếng Việt'}
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Notifications */}
        <button className="relative p-1.5 rounded-md hover:bg-[var(--muted)] transition-colors">
          <Bell className="w-4 h-4 text-[var(--text-secondary)]" />
          {notificationCount > 0 && (
            <span className="absolute -top-0.5 -right-0.5 w-4 h-4 bg-[var(--destructive)] text-white text-[10px] font-semibold rounded-full flex items-center justify-center">
              {notificationCount}
            </span>
          )}
        </button>

        {/* User Profile */}
        <div className="relative">
          <button
            onClick={() => setShowUserMenu(!showUserMenu)}
            className="flex items-center gap-2 pl-2.5 pr-1.5 py-1.5 rounded-md hover:bg-[var(--muted)] transition-colors"
          >
            <div className="w-7 h-7 bg-gradient-to-br from-[var(--primary)] to-[var(--secondary)] rounded-full flex items-center justify-center">
              <User className="w-4 h-4 text-white" />
            </div>
            <div className="text-left hidden md:block">
              <p className="text-xs font-medium leading-tight">Nguyễn Văn A</p>
              <p className="text-[10px] text-[var(--text-secondary)] leading-tight">{role}</p>
            </div>
            <ChevronDown className="w-3 h-3 text-[var(--text-secondary)]" />
          </button>

          {showUserMenu && (
            <div className="absolute right-0 mt-1 w-44 bg-white rounded-md shadow-lg border border-[var(--border)] py-0.5 z-50">
              <a
                href="#profile"
                className="block px-3 py-1.5 text-xs hover:bg-[var(--muted)] transition-colors"
              >
                Hồ sơ cá nhân
              </a>
              <a
                href="#settings"
                className="block px-3 py-1.5 text-xs hover:bg-[var(--muted)] transition-colors"
              >
                Cài đặt
              </a>
              <hr className="my-0.5 border-[var(--border)]" />
              <button className="w-full text-left px-3 py-1.5 text-xs text-[var(--destructive)] hover:bg-[var(--muted)] transition-colors">
                Đăng xuất
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}