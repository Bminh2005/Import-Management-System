import { useEffect } from 'react';
import { useNavigate } from 'react-router';

export function useKeyboardShortcuts() {
  const navigate = useNavigate();

  useEffect(() => {
    const handleKeyPress = (e: KeyboardEvent) => {
      // Navigation shortcuts
      if (e.ctrlKey && !e.shiftKey && !e.altKey) {
        switch (e.key) {
          case '1':
            e.preventDefault();
            navigate('/');
            break;
          case '2':
            e.preventDefault();
            navigate('/orders');
            break;
          case '3':
            e.preventDefault();
            navigate('/admin/sites');
            break;
          case '4':
            e.preventDefault();
            navigate('/warehouse/inventory');
            break;
          case '5':
            e.preventDefault();
            navigate('/reports');
            break;
          case ',':
            e.preventDefault();
            navigate('/admin/settings');
            break;
          case 'k':
            e.preventDefault();
            // Focus search bar
            const searchInput = document.querySelector('input[type="text"]') as HTMLInputElement;
            if (searchInput) searchInput.focus();
            break;
        }
      }

      // Quick actions
      if (e.ctrlKey && e.shiftKey && !e.altKey) {
        switch (e.key) {
          case 'N':
            e.preventDefault();
            navigate('/orders/new');
            break;
          case 'O':
            e.preventDefault();
            navigate('/overseas/orders');
            break;
        }
      }

      // Global shortcuts (no modifier)
      if (!e.ctrlKey && !e.shiftKey && !e.altKey && !e.metaKey) {
        // Escape to close modals/dialogs
        if (e.key === 'Escape') {
          // Will be handled by individual components
        }

        // F1 for help (prevent default browser help)
        if (e.key === 'F1') {
          e.preventDefault();
          // Open help modal
        }
      }
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [navigate]);
}
