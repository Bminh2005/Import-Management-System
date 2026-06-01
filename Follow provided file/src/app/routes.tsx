import { createBrowserRouter } from 'react-router';
import { RootLayout } from './layouts/RootLayout';
import { Dashboard } from './pages/Dashboard';
import { NotFound } from './pages/NotFound';

// Sales Department
import { MerchandiseList } from './pages/sales/MerchandiseList';
import { ImportRequestsList } from './pages/sales/ImportRequestsList';
import { CreateImportRequest } from './pages/sales/CreateImportRequest';
import { ImportRequestDetails } from './pages/sales/ImportRequestDetails';
import { EditImportRequest } from './pages/sales/EditImportRequest';
import { Portal } from './pages/Portal';

// Overseas Order Placement Department
import { OverseasDashboard } from './pages/overseas/OverseasDashboard';
import { ProcessImportRequests } from './pages/overseas/ProcessImportRequests';
import { ProcessRequestDetail } from './pages/overseas/ProcessRequestDetail';
import { SiteDetail } from './pages/overseas/SiteDetail';
import { ImportOrdersList } from './pages/overseas/ImportOrdersList';
import { ImportOrderDetail } from './pages/overseas/ImportOrderDetail';
import { ConfirmReplacementOrder } from './pages/overseas/ConfirmReplacementOrder';
import { ReplacementOrderSuccess } from './pages/overseas/ReplacementOrderSuccess';
import { SitesList } from './pages/overseas/SitesList';
import { InventoryList } from './pages/overseas/InventoryList';

// Warehouse Management Department
import { WarehouseDashboard } from './pages/warehouse/WarehouseDashboard';
import { InboundOrdersList } from './pages/warehouse/InboundOrdersList';
import { InboundOrderProcess } from './pages/warehouse/InboundOrderProcess';

export const router = createBrowserRouter([
  {
    path: '/',
    Component: RootLayout,
    children: [
      // Portal to select department
      { index: true, Component: Portal },

      // Sales Department Routes
      { path: 'sales', Component: Dashboard },
      { path: 'sales/merchandise', Component: MerchandiseList },
      { path: 'sales/requests', Component: ImportRequestsList },
      { path: 'sales/requests/new', Component: CreateImportRequest },
      { path: 'sales/requests/:id/edit', Component: EditImportRequest },
      { path: 'sales/requests/:id', Component: ImportRequestDetails },

      // Overseas Order Placement Routes
      { path: 'overseas', Component: OverseasDashboard },
      { path: 'overseas/process', Component: ProcessImportRequests },
      { path: 'overseas/process/:id', Component: ProcessRequestDetail },
      { path: 'overseas/sites/:id', Component: SiteDetail },
      { path: 'overseas/sites', Component: SitesList },
      { path: 'overseas/import-orders', Component: ImportOrdersList },
      { path: 'overseas/import-orders/success', Component: ReplacementOrderSuccess },
      { path: 'overseas/import-orders/:id/confirm', Component: ConfirmReplacementOrder },
      { path: 'overseas/import-orders/:id', Component: ImportOrderDetail },
      { path: 'overseas/inventory', Component: InventoryList },

      // Warehouse Management Routes
      { path: 'warehouse', Component: WarehouseDashboard },
      { path: 'warehouse/inbound', Component: InboundOrdersList },
      { path: 'warehouse/inbound/:id', Component: InboundOrderProcess },

      // 404
      { path: '*', Component: NotFound },
    ],
  },
]);
