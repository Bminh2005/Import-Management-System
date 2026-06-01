# Desktop App Features - Import Assignment System

## ⌨️ Keyboard Shortcuts

### Global Navigation
- `Ctrl + 1` - Dashboard
- `Ctrl + 2` - Orders Management
- `Ctrl + 3` - Site Management  
- `Ctrl + 4` - Warehouse
- `Ctrl + 5` - Reports & Analytics
- `Ctrl + ,` - Settings
- `Ctrl + B` - Toggle Sidebar
- `Ctrl + K` - Focus Search Bar

### Quick Actions
- `Ctrl + Shift + N` - Create New Order
- `Ctrl + Shift + O` - Open Overseas Orders
- `F5` - Refresh Current View
- `F1` - Help (Reserved)
- `Esc` - Close Modals/Dialogs

### Table Operations (Orders List)
- `Enter` - View Details of Selected Order
- `Ctrl + E` - Edit Selected Order
- `Ctrl + D` - Duplicate Order
- `Del` - Cancel/Delete Order
- Click row - Select/Deselect
- `Shift + Click` - Multi-select (planned)
- Right-click - Context Menu

## 🖱️ Desktop UI Patterns

### Context Menus
- Right-click on table rows for quick actions
- Context-aware menu items based on selection
- Keyboard shortcuts displayed in menu

### Table Enhancements
- **Row Selection**: Click anywhere on row to select
- **Multi-Select**: Checkbox column for batch operations
- **Sortable Columns**: Click column headers to sort
- **Fixed Width Columns**: Optimized for desktop screens
- **Hover States**: Visual feedback on row hover
- **Selection Highlighting**: Blue border for selected rows

### Sidebar
- **Collapsible**: Toggle with Ctrl+B or button
- **Active Indicator**: Current page highlighted
- **Keyboard Hints**: Shortcuts shown on menu items
- **Role Badge**: Current user role displayed
- **Smooth Animations**: Transitions on collapse/expand

### Layout
- **Fixed Desktop Width**: No mobile breakpoints
- **Max Width**: 1920px for ultra-wide screens
- **Optimized Grid**: 4-column layout for metrics
- **Scrollable Content**: Main area scrolls, header/sidebar fixed

## 🎨 Desktop-Specific Design

### Removed Mobile Features
- ❌ No responsive breakpoints (sm:, md:, lg:)
- ❌ No hamburger menus
- ❌ No mobile-first grid layouts
- ❌ No touch-specific interactions

### Desktop Optimizations
- ✅ Fixed table column widths
- ✅ Always-visible sidebar
- ✅ Keyboard-first interactions
- ✅ Context menus (right-click)
- ✅ Hover states and tooltips
- ✅ Keyboard shortcuts visible
- ✅ Multi-window support ready
- ✅ Dense information display

## 🔧 Technical Implementation

### Components
- `useKeyboardShortcuts` hook - Global keyboard handling
- `ContextMenu` component - Right-click menus
- `RootLayout` - Desktop-optimized layout
- Table components with selection state

### State Management
- Row selection with Set<string>
- Sort column and direction tracking
- Context menu positioning
- Keyboard shortcut registration

### Accessibility
- Keyboard navigation throughout
- Focus management
- ARIA labels for interactive elements
- Visual feedback for all actions

## 📋 Planned Desktop Features

### Coming Soon
- [ ] Drag and drop for reordering
- [ ] Split panes/resizable panels
- [ ] Multiple window support
- [ ] Excel-like table editing
- [ ] Batch operations on selected rows
- [ ] Advanced filtering UI
- [ ] Data export to Excel/CSV
- [ ] Print layouts
- [ ] Custom column visibility
- [ ] Saved view preferences

## 🚀 Usage Tips

1. **Use keyboard shortcuts** - Much faster than clicking
2. **Right-click for context** - Quick actions on any item
3. **Select multiple rows** - Checkbox column for batch ops
4. **Sort by clicking headers** - Quick data organization
5. **Press Ctrl+K** - Quick search anything
6. **Toggle sidebar** - More screen space when needed

## 🔗 Integration Notes for Java Backend

This frontend is designed to connect with a Java backend via REST APIs:

### Expected API Structure
```
GET    /api/orders              - List orders
POST   /api/orders              - Create order
GET    /api/orders/{id}         - Get order details
PUT    /api/orders/{id}         - Update order
DELETE /api/orders/{id}         - Delete order
GET    /api/sites               - List sites
GET    /api/warehouse/inventory - Inventory data
GET    /api/reports/analytics   - Analytics data
```

### Authentication
- JWT tokens recommended
- Axios interceptors for auth headers
- Automatic token refresh handling

### Desktop-Specific Considerations
- Larger payload sizes acceptable (desktop bandwidth)
- Real-time updates via WebSocket preferred
- Local caching for offline capability
- Background sync for large operations
