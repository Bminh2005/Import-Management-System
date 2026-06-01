import { Home, Package, Globe, ChevronLeft, ChevronRight, FileText, MapPin, PackageCheck, Warehouse } from 'lucide-react';
import { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router';

interface SidebarProps {
  currentRole?: string;
}

const salesNavItems = [
  { icon: Home, label: 'Trang Chủ Bán Hàng', href: '/sales', shortcut: 'Ctrl+1' },
  { icon: Package, label: 'Quản lý Mặt hàng', href: '/sales/merchandise', shortcut: 'Ctrl+2' },
  { icon: FileText, label: 'Yêu cầu Nhập hàng', href: '/sales/requests', shortcut: 'Ctrl+3' },
];

const overseasNavItems = [
  { icon: Home, label: 'Trang Chủ Quốc Tế', href: '/overseas', shortcut: 'Ctrl+1' },
  { icon: FileText, label: 'Đơn hàng Nhập khẩu', href: '/overseas/import-orders', shortcut: 'Ctrl+2' },
  { icon: MapPin, label: 'Danh sách Site', href: '/overseas/sites', shortcut: 'Ctrl+3' },
  { icon: Warehouse, label: 'Mặt hàng Tồn kho', href: '/overseas/inventory', shortcut: 'Ctrl+4' },
];

const warehouseNavItems = [
  { icon: Home, label: 'Trang Chủ Kho', href: '/warehouse', shortcut: 'Ctrl+1' },
  { icon: PackageCheck, label: 'Đơn Chờ Nhập Kho', href: '/warehouse/inbound', shortcut: 'Ctrl+2' },
];

export function Sidebar({ currentRole = 'Hệ thống Nhập hàng' }: SidebarProps) {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const location = useLocation();

  // Determine which navigation items to show based on current route
  const isOverseasRoute = location.pathname.startsWith('/overseas');
  const isSalesRoute = location.pathname.startsWith('/sales');
  const isWarehouseRoute = location.pathname.startsWith('/warehouse');

  let navigationItems: typeof salesNavItems = [];
  let role = 'Chọn Phân Hệ';

  if (isOverseasRoute) {
    navigationItems = overseasNavItems;
    role = 'Bộ phận Đặt hàng Quốc tế';
  } else if (isSalesRoute) {
    navigationItems = salesNavItems;
    role = 'Bộ phận Bán hàng';
  } else if (isWarehouseRoute) {
    navigationItems = warehouseNavItems;
    role = 'Bộ phận Quản lý Kho';
  } else {
    // Portal route
    navigationItems = [];
  }

  // Keyboard shortcut to toggle sidebar
  useEffect(() => {
    const handleKeyboard = (e: KeyboardEvent) => {
      if (e.ctrlKey && e.key === 'b') {
        e.preventDefault();
        setIsCollapsed(prev => !prev);
      }
    };
    window.addEventListener('keydown', handleKeyboard);
    return () => window.removeEventListener('keydown', handleKeyboard);
  }, []);

  // Don't show sidebar on portal (login) page
  if (location.pathname === '/') {
    return null;
  }

  return (
    <aside
      className={`h-screen bg-[var(--sidebar)] text-[var(--sidebar-foreground)] flex flex-col transition-all duration-200 ${
        isCollapsed ? 'w-16' : 'w-[240px]'
      }`}
    >
      {/* Logo */}
      <div className="h-[52px] flex items-center justify-center border-b border-[var(--sidebar-border)] px-3">
        {!isCollapsed ? (
          <Link to="/" className="flex items-center gap-2">
            <div className="w-8 h-8 bg-white rounded-md flex items-center justify-center">
              <Globe className="w-5 h-5 text-[var(--sidebar)]" />
            </div>
            <div>
              <h1 className="font-semibold text-sm leading-tight">Hệ thống Nhập hàng</h1>
              <p className="text-[10px] opacity-70 leading-tight">{role}</p>
            </div>
          </Link>
        ) : (
          <Link to="/">
            <Globe className="w-6 h-6" />
          </Link>
        )}
      </div>

      {/* Navigation Menu */}
      <nav className="flex-1 py-3 overflow-y-auto">
        <ul className="space-y-0.5 px-2">
          {navigationItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname === item.href ||
                           (item.href !== '/' &&
                            item.href !== '/sales' &&
                            item.href !== '/overseas' &&
                            item.href !== '/warehouse' &&
                            location.pathname.startsWith(`${item.href}/`));

            return (
              <li key={item.label}>
                <Link
                  to={item.href}
                  className={`group flex items-center justify-between px-3 py-2 rounded-md transition-all text-[13px] ${
                    isActive
                      ? 'bg-[var(--sidebar-primary)] text-[var(--sidebar-primary-foreground)] shadow-sm'
                      : 'hover:bg-[var(--sidebar-accent)] text-[var(--sidebar-foreground)]'
                  }`}
                  title={isCollapsed ? `${item.label} (${item.shortcut})` : undefined}
                >
                  <div className="flex items-center gap-2.5">
                    <Icon className="w-4 h-4 flex-shrink-0" />
                    {!isCollapsed && <span className="font-medium">{item.label}</span>}
                  </div>
                  {!isCollapsed && (
                    <span className="text-[10px] opacity-40 font-mono">{item.shortcut}</span>
                  )}
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>

      {/* User Role Badge */}
      {!isCollapsed && (
        <div className="p-3 border-t border-[var(--sidebar-border)] space-y-1.5">
          <div className="bg-[var(--sidebar-accent)] rounded-md p-2.5">
            <p className="text-[10px] opacity-70 mb-0.5">Vai trò hiện tại</p>
            <p className="text-xs font-semibold">{role}</p>
          </div>
          {location.pathname !== '/' && (
            <Link
              to="/"
              className="flex items-center justify-center w-full p-1.5 text-xs text-[var(--text-secondary)] hover:bg-[var(--sidebar-accent)] rounded-md transition-colors border border-[var(--sidebar-border)]"
            >
              Đổi bộ phận
            </Link>
          )}
        </div>
      )}

      {/* Collapse Button */}
      <button
        onClick={() => setIsCollapsed(!isCollapsed)}
        className="h-10 flex items-center justify-center border-t border-[var(--sidebar-border)] hover:bg-[var(--sidebar-accent)] transition-colors group"
        title="Thu gọn/Mở rộng (Ctrl+B)"
      >
        <div className="flex items-center gap-1.5">
          {isCollapsed ? (
            <ChevronRight className="w-4 h-4" />
          ) : (
            <>
              <ChevronLeft className="w-4 h-4" />
              <span className="text-[10px] opacity-40 font-mono">Thu Gọn</span>
            </>
          )}
        </div>
      </button>
    </aside>
  );
}
