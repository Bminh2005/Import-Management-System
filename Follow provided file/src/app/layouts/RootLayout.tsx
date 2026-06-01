import { Outlet } from 'react-router';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { Toaster } from '../components/ui/sonner';
import { useKeyboardShortcuts } from '../hooks/useKeyboardShortcuts';

export function RootLayout() {
  // Enable global keyboard shortcuts
  useKeyboardShortcuts();

  return (
    <div className="size-full flex bg-[var(--background)]">
      {/* Sidebar - Fixed width for desktop */}
      <Sidebar currentRole="Bộ phận Bán hàng" />

      {/* Main Content Area - Full remaining width */}
      <div className="flex-1 flex flex-col overflow-hidden min-w-0">
        {/* Header - Fixed height */}
        <Header />

        {/* Page Content - Scrollable */}
        <main className="flex-1 overflow-y-auto">
          <Outlet />
        </main>
      </div>

      {/* Toast Notifications */}
      <Toaster />
    </div>
  );
}
