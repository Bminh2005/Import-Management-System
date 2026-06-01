import { useParams, useNavigate, Link } from 'react-router';
import { ArrowLeft, AlertCircle, Package, CheckCircle2, Clock, MapPin, Truck, Calendar, Phone, Mail } from 'lucide-react';
import { useState } from 'react';

interface OrderItem {
  id: string;
  code: string;
  name: string;
  quantityOrdered: number;
  unit: string;
  shortage: number;
  unitPrice: number;
}

interface Site {
  siteCode: string;
  siteName: string;
  warehouse: string;
  availableStock: number;
  deliveryMethod: string;
  estimatedDate: string;
  unitPrice: number;
}

interface AllocatedSite {
  siteCode: string;
  siteName: string;
  quantity: number;
  deliveryMethod: string;
  estimatedDate: string;
  unitPrice: number;
}

const mockOrderData = {
  code: 'DH001',
  orderer: 'Nguyễn Văn A',
  expectedDate: '15/03/2025',
  processingDate: '06/05/2026',
  status: 'Bị hủy' as const,
  items: [
    {
      id: '1',
      code: 'MH001',
      name: 'Vải lụa cao cấp',
      quantityOrdered: 100,
      unit: 'mét',
      shortage: 50,
      unitPrice: 450000,
    },
    {
      id: '2',
      code: 'MH002',
      name: 'Vải cotton organic',
      quantityOrdered: 200,
      unit: 'mét',
      shortage: 185,
      unitPrice: 320000,
    },
    {
      id: '3',
      code: 'MH003',
      name: 'Đá granite tự nhiên',
      quantityOrdered: 60,
      unit: 'viên',
      shortage: 45,
      unitPrice: 850000,
    },
  ] as OrderItem[],
};

const mockAvailableSites: { [itemId: string]: Site[] } = {
  '1': [
    {
      siteCode: 'SITE01',
      siteName: 'Kho Hong Kong',
      warehouse: 'Hong Kong Central Warehouse',
      availableStock: 2000,
      deliveryMethod: 'Tàu',
      estimatedDate: '14/03/2025',
      unitPrice: 450000,
    },
    {
      siteCode: 'SITE03',
      siteName: 'Kho Bangkok',
      warehouse: 'Bangkok Logistics Hub',
      availableStock: 1500,
      deliveryMethod: 'Tàu',
      estimatedDate: '16/03/2025',
      unitPrice: 445000,
    },
    {
      siteCode: 'SITE02',
      siteName: 'Kho Singapore',
      warehouse: 'Singapore Distribution Center',
      availableStock: 800,
      deliveryMethod: 'Hàng không',
      estimatedDate: '13/03/2025',
      unitPrice: 460000,
    },
  ],
  '2': [
    {
      siteCode: 'SITE01',
      siteName: 'Kho Hong Kong',
      warehouse: 'Hong Kong Central Warehouse',
      availableStock: 1850,
      deliveryMethod: 'Tàu',
      estimatedDate: '14/03/2025',
      unitPrice: 320000,
    },
  ],
  '3': [
    {
      siteCode: 'SITE01',
      siteName: 'Kho Hong Kong',
      warehouse: 'Hong Kong Central Warehouse',
      availableStock: 450,
      deliveryMethod: 'Tàu',
      estimatedDate: '14/03/2025',
      unitPrice: 850000,
    },
    {
      siteCode: 'SITE04',
      siteName: 'Kho Shanghai',
      warehouse: 'Shanghai Regional Warehouse',
      availableStock: 1050,
      deliveryMethod: 'Tàu',
      estimatedDate: '16/03/2025',
      unitPrice: 840000,
    },
  ],
};

export function ImportOrderDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [order] = useState(mockOrderData);
  const [selectedItem, setSelectedItem] = useState<OrderItem | null>(null);
  const [allocationMode, setAllocationMode] = useState<'auto' | 'manual'>('auto');
  const [selectedSites, setSelectedSites] = useState<{ [siteCode: string]: number }>({});
  const [allocatedItems, setAllocatedItems] = useState<{ [itemId: string]: AllocatedSite[] }>({});
  const [showErrorPopup, setShowErrorPopup] = useState(false);
  const [showAllocationErrorPopup, setShowAllocationErrorPopup] = useState(false);
  const [showNoSiteSelectedPopup, setShowNoSiteSelectedPopup] = useState(false);
  const [allocationError, setAllocationError] = useState({ allocated: 0, needed: 0, shortage: 0, unit: '' });

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(value);
  };

  const handleOpenAllocation = (item: OrderItem) => {
    setSelectedItem(item);
    setAllocationMode('auto');
    setSelectedSites({});

    // Auto-select first site with enough stock
    const sites = mockAvailableSites[item.id] || [];
    const siteWithStock = sites.find((site) => site.availableStock >= item.shortage);
    if (siteWithStock) {
      setSelectedSites({ [siteWithStock.siteCode]: item.shortage });
    }
  };

  const handleSwitchToAuto = () => {
    setAllocationMode('auto');
    if (!selectedItem) return;

    const sites = mockAvailableSites[selectedItem.id] || [];
    const siteWithStock = sites.find((site) => site.availableStock >= selectedItem.shortage);
    if (siteWithStock) {
      setSelectedSites({ [siteWithStock.siteCode]: selectedItem.shortage });
    } else {
      setSelectedSites({});
    }
  };

  const handleSwitchToManual = () => {
    setAllocationMode('manual');
    setSelectedSites({});
  };

  const handleManualSiteToggle = (siteCode: string) => {
    setSelectedSites((prev) => {
      const newSites = { ...prev };
      if (newSites[siteCode] !== undefined) {
        delete newSites[siteCode];
      } else {
        newSites[siteCode] = 0;
      }
      return newSites;
    });
  };

  const handleQuantityChange = (siteCode: string, quantity: number) => {
    setSelectedSites((prev) => ({
      ...prev,
      [siteCode]: quantity,
    }));
  };

  const getTotalAllocated = () => {
    return Object.values(selectedSites).reduce((sum, qty) => sum + qty, 0);
  };

  const handleConfirmAllocation = () => {
    if (!selectedItem) return;

    // Check if at least one site is selected
    if (Object.keys(selectedSites).length === 0) {
      setShowNoSiteSelectedPopup(true);
      return;
    }

    const totalAllocated = getTotalAllocated();

    if (totalAllocated !== selectedItem.shortage) {
      setAllocationError({
        allocated: totalAllocated,
        needed: selectedItem.shortage,
        shortage: selectedItem.shortage - totalAllocated,
        unit: selectedItem.unit,
      });
      setShowAllocationErrorPopup(true);
      return;
    }

    const sites = mockAvailableSites[selectedItem.id] || [];
    const allocatedSites: AllocatedSite[] = Object.entries(selectedSites)
      .filter(([_, qty]) => qty > 0)
      .map(([siteCode, quantity]) => {
        const site = sites.find((s) => s.siteCode === siteCode);
        return {
          siteCode,
          siteName: site?.siteName || '',
          quantity,
          deliveryMethod: site?.deliveryMethod || '',
          estimatedDate: site?.estimatedDate || '',
          unitPrice: site?.unitPrice || 0,
        };
      });

    setAllocatedItems((prev) => ({
      ...prev,
      [selectedItem.id]: allocatedSites,
    }));

    setSelectedItem(null);
  };

  const allItemsAllocated = order.items.every((item) => allocatedItems[item.id]);

  const handleProcessOrder = () => {
    if (!allItemsAllocated) {
      setShowErrorPopup(true);
      return;
    }

    navigate(`/overseas/import-orders/${id}/confirm`, {
      state: { order, allocatedItems },
    });
  };

  const totalItems = order.items.length;
  const allocatedItemsCount = Object.keys(allocatedItems).length;
  const allocationProgress = totalItems > 0 ? (allocatedItemsCount / totalItems) * 100 : 0;

  return (
    <div className="p-8 space-y-6 bg-[var(--background)]">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Link
          to="/overseas/import-orders"
          className="p-2 hover:bg-white rounded-lg transition-colors border border-[#E5E7EB]"
        >
          <ArrowLeft className="w-5 h-5 text-[#6B7280]" />
        </Link>
        <div className="flex-1">
          <h1 className="text-3xl font-semibold text-[#111827] mb-1">
            Chi tiết đơn hàng #{order.code}
          </h1>
          <p className="text-[#6B7280]">Xử lý đơn hàng bị hủy và phân bổ hàng thay thế</p>
        </div>
        <span className="inline-flex items-center px-4 py-2 rounded-full text-sm font-medium bg-red-50 text-red-700 border border-red-200">
          {order.status}
        </span>
      </div>

      {/* Progress Bar */}
      <div className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] p-6">
        <div className="flex items-center justify-between mb-3">
          <span className="text-sm font-semibold text-[#111827]">Tiến độ phân bổ</span>
          <span className="text-sm font-bold text-[#2563EB]">
            {allocatedItemsCount}/{totalItems} mặt hàng
          </span>
        </div>
        <div className="w-full h-3 bg-[#E5E7EB] rounded-full overflow-hidden">
          <div
            className="h-full bg-gradient-to-r from-blue-500 to-blue-600 transition-all duration-300 rounded-full"
            style={{ width: `${allocationProgress}%` }}
          />
        </div>
      </div>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-2">
                Tổng mặt hàng
              </p>
              <p className="text-3xl font-bold text-[#111827]">{totalItems}</p>
            </div>
            <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-blue-500 to-blue-600 flex items-center justify-center">
              <Package className="w-7 h-7 text-white" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-2">
                Đã phân bổ
              </p>
              <p className="text-3xl font-bold text-green-600">{allocatedItemsCount}</p>
            </div>
            <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-green-500 to-green-600 flex items-center justify-center">
              <CheckCircle2 className="w-7 h-7 text-white" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-2">
                Chưa phân bổ
              </p>
              <p className="text-3xl font-bold text-yellow-600">{totalItems - allocatedItemsCount}</p>
            </div>
            <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-yellow-500 to-yellow-600 flex items-center justify-center">
              <Clock className="w-7 h-7 text-white" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-2">
                Người đặt
              </p>
              <p className="text-lg font-bold text-[#111827]">{order.orderer}</p>
            </div>
          </div>
          <div className="mt-3 pt-3 border-t border-[#E5E7EB]">
            <p className="text-xs text-[#6B7280] mb-1">Ngày mong muốn</p>
            <p className="text-sm font-semibold text-[#111827]">{order.expectedDate}</p>
          </div>
        </div>
      </div>

      {/* Items List with Cards */}
      <div className="space-y-4">
        <h2 className="text-xl font-semibold text-[#111827]">Danh sách mặt hàng</h2>

        {order.items.map((item) => {
          const isAllocated = allocatedItems[item.id];
          const allocatedSites = allocatedItems[item.id] || [];

          return (
            <div
              key={item.id}
              className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] p-6 hover:shadow-md transition-all"
            >
              <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="text-lg font-semibold text-[#111827]">{item.code}</h3>
                    <span
                      className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${
                        isAllocated
                          ? 'bg-green-50 text-green-700 border border-green-200'
                          : 'bg-yellow-50 text-yellow-700 border border-yellow-200'
                      }`}
                    >
                      {isAllocated ? '✓ Đã phân bổ' : 'Chưa phân bổ'}
                    </span>
                  </div>
                  <p className="text-sm text-[#6B7280] mb-3">{item.name}</p>

                  <div className="grid grid-cols-4 gap-4">
                    <div>
                      <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                        Số lượng đặt
                      </p>
                      <p className="text-sm font-semibold text-[#111827]">
                        {item.quantityOrdered} {item.unit}
                      </p>
                    </div>
                    <div>
                      <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                        Thiếu
                      </p>
                      <p className="text-sm font-bold text-red-600">
                        {item.shortage} {item.unit}
                      </p>
                    </div>
                    <div>
                      <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                        Sites đã chọn
                      </p>
                      <p className="text-sm font-semibold text-[#2563EB]">
                        {allocatedSites.length} site
                      </p>
                    </div>
                    <div>
                      <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                        Đơn giá
                      </p>
                      <p className="text-sm font-bold text-green-600">
                        {formatCurrency(item.unitPrice)}
                      </p>
                    </div>
                  </div>
                </div>

                <button
                  onClick={() => handleOpenAllocation(item)}
                  className={`ml-6 px-6 py-3 rounded-lg font-medium transition-all shadow-md hover:shadow-lg ${
                    isAllocated
                      ? 'bg-gradient-to-r from-green-600 to-green-700 text-white hover:from-green-700 hover:to-green-800'
                      : 'bg-gradient-to-r from-blue-600 to-blue-700 text-white hover:from-blue-700 hover:to-blue-800'
                  }`}
                >
                  {isAllocated ? 'Chỉnh sửa' : 'Phân bổ site'}
                </button>
              </div>

              {isAllocated && allocatedSites.length > 0 && (
                <div className="mt-4 pt-4 border-t border-[#E5E7EB]">
                  <div className="flex items-center justify-between mb-3">
                    <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider">
                      Sites đã phân bổ
                    </p>
                    <p className="text-sm font-bold text-green-600">
                      Tổng: {formatCurrency(allocatedSites.reduce((sum, site) => sum + site.quantity * site.unitPrice, 0))}
                    </p>
                  </div>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    {allocatedSites.map((site, index) => (
                      <div
                        key={index}
                        className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg p-3 border border-blue-200"
                      >
                        <div className="flex items-start justify-between mb-2">
                          <div className="flex-1">
                            <p className="text-sm font-semibold text-[#111827] mb-1">
                              {site.siteCode} - {site.siteName}
                            </p>
                            <div className="flex items-center gap-3 text-xs text-[#6B7280]">
                              <span>
                                <span className="font-semibold text-[#2563EB]">{site.quantity}</span> {item.unit}
                              </span>
                              <span>• {site.deliveryMethod}</span>
                              <span>• {site.estimatedDate}</span>
                            </div>
                          </div>
                        </div>
                        <div className="bg-white rounded-lg px-2 py-1 border border-green-200">
                          <div className="flex items-center justify-between">
                            <span className="text-xs font-semibold text-green-700">Giá trị:</span>
                            <span className="text-sm font-bold text-green-900">
                              {formatCurrency(site.quantity * site.unitPrice)}
                            </span>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>

      {/* Process Button */}
      <div className="flex justify-end gap-3">
        <Link
          to="/overseas/import-orders"
          className="px-6 py-3 border border-[#E5E7EB] rounded-lg text-[#111827] font-medium hover:bg-[#F3F4F6] transition-colors"
        >
          Quay lại
        </Link>
        <button
          onClick={handleProcessOrder}
          className={`px-8 py-3 rounded-lg font-medium transition-all shadow-md ${
            allItemsAllocated
              ? 'bg-gradient-to-r from-blue-600 to-blue-700 text-white hover:from-blue-700 hover:to-blue-800 hover:shadow-lg'
              : 'bg-[#E5E7EB] text-[#9CA3AF] cursor-not-allowed'
          }`}
          disabled={!allItemsAllocated}
        >
          Xử lý toàn bộ đơn hàng
        </button>
      </div>

      {/* No Site Selected Popup */}
      {showNoSiteSelectedPopup && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60] p-4 animate-fadeIn"
          onClick={() => setShowNoSiteSelectedPopup(false)}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden animate-scaleIn"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Gradient Header */}
            <div className="bg-gradient-to-r from-orange-500 to-red-500 px-8 py-6 text-white relative overflow-hidden">
              <div className="absolute top-0 right-0 w-32 h-32 bg-white opacity-10 rounded-full -mr-16 -mt-16"></div>
              <div className="absolute bottom-0 left-0 w-24 h-24 bg-white opacity-10 rounded-full -ml-12 -mb-12"></div>
              <div className="relative flex items-center gap-4">
                <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
                  <AlertCircle className="w-8 h-8 text-orange-600" strokeWidth={2.5} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold text-white drop-shadow-md">Chưa chọn site</h3>
                  <p className="text-white text-sm font-medium">Vui lòng kiểm tra lại</p>
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="p-8">
              <div className="bg-orange-50 border-l-4 border-orange-500 rounded-lg p-4 mb-6">
                <p className="text-[#111827] font-medium">
                  Vui lòng chọn ít nhất một site và phân bổ số lượng trước khi xác nhận!
                </p>
              </div>

              <div className="flex justify-end gap-3">
                <button
                  onClick={() => setShowNoSiteSelectedPopup(false)}
                  className="px-8 py-3 bg-gradient-to-r from-orange-500 to-red-500 text-white rounded-xl font-semibold hover:from-orange-600 hover:to-red-600 transition-all shadow-lg hover:shadow-xl"
                >
                  Đã hiểu
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Allocation Error Popup */}
      {showAllocationErrorPopup && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60] p-4 animate-fadeIn"
          onClick={() => setShowAllocationErrorPopup(false)}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl max-w-lg w-full mx-4 overflow-hidden animate-scaleIn"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Gradient Header */}
            <div className="bg-gradient-to-r from-orange-500 to-red-500 px-8 py-6 text-white relative overflow-hidden">
              <div className="absolute top-0 right-0 w-32 h-32 bg-white opacity-10 rounded-full -mr-16 -mt-16"></div>
              <div className="absolute bottom-0 left-0 w-24 h-24 bg-white opacity-10 rounded-full -ml-12 -mb-12"></div>
              <div className="relative flex items-center gap-4">
                <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
                  <AlertCircle className="w-8 h-8 text-orange-600" strokeWidth={2.5} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold text-white drop-shadow-md">Số lượng chưa đủ</h3>
                  <p className="text-white text-sm font-medium">Vui lòng kiểm tra lại phân bổ</p>
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="p-8">
              <div className="bg-orange-50 border-l-4 border-orange-500 rounded-lg p-4 mb-6">
                <p className="text-[#111827] font-medium mb-1">
                  Số lượng phân bổ chưa đủ yêu cầu!
                </p>
                <p className="text-sm text-[#6B7280]">
                  Vui lòng điều chỉnh số lượng hoặc chọn thêm site khác.
                </p>
              </div>

              {/* Summary Cards */}
              <div className="grid grid-cols-3 gap-4 mb-6">
                <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-4 border border-blue-200">
                  <p className="text-xs font-semibold text-blue-700 uppercase tracking-wider mb-1">
                    Đã phân bổ
                  </p>
                  <p className="text-2xl font-bold text-blue-900">
                    {allocationError.allocated}
                  </p>
                  <p className="text-xs text-blue-600 mt-1">{allocationError.unit}</p>
                </div>

                <div className="bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-4 border border-gray-200">
                  <p className="text-xs font-semibold text-gray-700 uppercase tracking-wider mb-1">
                    Yêu cầu
                  </p>
                  <p className="text-2xl font-bold text-gray-900">
                    {allocationError.needed}
                  </p>
                  <p className="text-xs text-gray-600 mt-1">{allocationError.unit}</p>
                </div>

                <div className="bg-gradient-to-br from-red-50 to-red-100 rounded-xl p-4 border-2 border-red-300">
                  <p className="text-xs font-semibold text-red-700 uppercase tracking-wider mb-1">
                    Còn thiếu
                  </p>
                  <p className="text-2xl font-bold text-red-900">
                    {allocationError.shortage}
                  </p>
                  <p className="text-xs text-red-600 mt-1">{allocationError.unit}</p>
                </div>
              </div>

              {/* Progress Bar */}
              <div className="mb-6">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-semibold text-[#111827]">Tiến độ phân bổ</span>
                  <span className="text-sm font-bold text-orange-600">
                    {Math.round((allocationError.allocated / allocationError.needed) * 100)}%
                  </span>
                </div>
                <div className="w-full h-3 bg-gray-200 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-orange-500 to-red-500 rounded-full transition-all duration-300"
                    style={{
                      width: `${(allocationError.allocated / allocationError.needed) * 100}%`,
                    }}
                  />
                </div>
              </div>

              <div className="flex justify-end gap-3">
                <button
                  onClick={() => setShowAllocationErrorPopup(false)}
                  className="px-8 py-3 bg-gradient-to-r from-orange-500 to-red-500 text-white rounded-xl font-semibold hover:from-orange-600 hover:to-red-600 transition-all shadow-lg hover:shadow-xl"
                >
                  Điều chỉnh lại
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Error Popup */}
      {showErrorPopup && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60] p-4 animate-fadeIn"
          onClick={() => setShowErrorPopup(false)}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl max-w-lg w-full mx-4 overflow-hidden animate-scaleIn"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Gradient Header */}
            <div className="bg-gradient-to-r from-yellow-500 to-orange-500 px-8 py-6 text-white relative overflow-hidden">
              <div className="absolute top-0 right-0 w-32 h-32 bg-white opacity-10 rounded-full -mr-16 -mt-16"></div>
              <div className="absolute bottom-0 left-0 w-24 h-24 bg-white opacity-10 rounded-full -ml-12 -mb-12"></div>
              <div className="relative flex items-center gap-4">
                <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
                  <AlertCircle className="w-8 h-8 text-yellow-600" strokeWidth={2.5} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold text-white drop-shadow-md">Chưa hoàn tất phân bổ</h3>
                  <p className="text-white text-sm font-medium">Còn mặt hàng chưa được phân bổ</p>
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="p-8">
              <div className="bg-yellow-50 border-l-4 border-yellow-500 rounded-lg p-4 mb-6">
                <p className="text-[#111827] font-medium mb-1">
                  Vui lòng phân bổ site cho tất cả mặt hàng!
                </p>
                <p className="text-sm text-[#6B7280]">
                  Bạn cần hoàn tất phân bổ cho tất cả mặt hàng trước khi xử lý đơn hàng.
                </p>
              </div>

              {/* Unallocated Items List */}
              <div className="bg-gradient-to-br from-orange-50 to-yellow-50 border-2 border-orange-200 rounded-xl p-5 mb-6">
                <div className="flex items-center gap-2 mb-4">
                  <Package className="w-5 h-5 text-orange-600" />
                  <p className="font-semibold text-orange-900">
                    Mặt hàng chưa được phân bổ ({order.items.filter((item) => !allocatedItems[item.id]).length})
                  </p>
                </div>
                <ul className="space-y-3">
                  {order.items
                    .filter((item) => !allocatedItems[item.id])
                    .map((item) => (
                      <li
                        key={item.id}
                        className="flex items-center gap-3 bg-white rounded-lg p-3 border border-orange-200"
                      >
                        <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-red-500 rounded-lg flex items-center justify-center flex-shrink-0">
                          <span className="text-white text-xs font-bold">!</span>
                        </div>
                        <div className="flex-1">
                          <p className="font-semibold text-[#111827]">{item.code}</p>
                          <p className="text-sm text-[#6B7280]">{item.name}</p>
                        </div>
                        <div className="text-right">
                          <p className="text-xs text-[#6B7280]">Thiếu</p>
                          <p className="font-bold text-red-600">{item.shortage} {item.unit}</p>
                        </div>
                      </li>
                    ))}
                </ul>
              </div>

              <div className="flex justify-end gap-3">
                <button
                  onClick={() => setShowErrorPopup(false)}
                  className="px-8 py-3 bg-gradient-to-r from-yellow-500 to-orange-500 text-white rounded-xl font-semibold hover:from-yellow-600 hover:to-orange-600 transition-all shadow-lg hover:shadow-xl"
                >
                  Tiếp tục phân bổ
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      <style>{`
        @keyframes fadeIn {
          from {
            opacity: 0;
          }
          to {
            opacity: 1;
          }
        }

        @keyframes scaleIn {
          from {
            opacity: 0;
            transform: scale(0.9);
          }
          to {
            opacity: 1;
            transform: scale(1);
          }
        }

        .animate-fadeIn {
          animation: fadeIn 0.2s ease-out;
        }

        .animate-scaleIn {
          animation: scaleIn 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
        }
      `}</style>

      {/* Allocation Modal */}
      {selectedItem && (
        <div
          className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 p-4"
          onClick={() => setSelectedItem(null)}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl max-w-6xl w-full max-h-[90vh] overflow-hidden flex flex-col"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Gradient Header - Sticky */}
            <div className="bg-gradient-to-r from-blue-600 to-blue-700 px-8 py-6 text-white relative overflow-hidden flex-shrink-0">
              <div className="absolute top-0 right-0 w-32 h-32 bg-white opacity-10 rounded-full -mr-16 -mt-16"></div>
              <div className="flex items-start justify-between mb-4 relative">
                <div className="flex-1">
                  <h3 className="text-2xl font-bold text-white drop-shadow-md mb-2">
                    Phân bổ site cho {selectedItem.code}
                  </h3>
                  <p className="text-white font-medium">{selectedItem.name}</p>
                </div>
                <button
                  onClick={() => setSelectedItem(null)}
                  className="text-white hover:bg-white hover:bg-opacity-20 rounded-lg p-2 transition-colors"
                >
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth={2.5}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>

              {/* Header Stats */}
              <div className="grid grid-cols-3 gap-4">
                <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
                  <p className="text-blue-700 text-xs font-bold uppercase tracking-wider mb-1">Cần phân bổ</p>
                  <p className="text-2xl font-bold text-blue-900">
                    {selectedItem.shortage} {selectedItem.unit}
                  </p>
                </div>
                <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
                  <p className="text-blue-700 text-xs font-bold uppercase tracking-wider mb-1">Đã phân bổ</p>
                  <p className="text-2xl font-bold text-blue-900">
                    {getTotalAllocated()} {selectedItem.unit}
                  </p>
                </div>
                <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
                  <p className="text-blue-700 text-xs font-bold uppercase tracking-wider mb-1">Còn lại</p>
                  <p className="text-2xl font-bold text-blue-900">
                    {selectedItem.shortage - getTotalAllocated()} {selectedItem.unit}
                  </p>
                </div>
              </div>
            </div>

            {/* Scrollable Content */}
            <div className="flex-1 overflow-y-auto p-8">
              {/* Tabs */}
              <div className="flex gap-3 mb-6">
                <button
                  onClick={handleSwitchToAuto}
                  className={`px-6 py-3 rounded-lg font-medium transition-all ${
                    allocationMode === 'auto'
                      ? 'bg-[#2563EB] text-white shadow-md'
                      : 'bg-[#F3F4F6] text-[#6B7280] hover:bg-[#E5E7EB]'
                  }`}
                >
                  Tự động phân bổ
                </button>
                <button
                  onClick={handleSwitchToManual}
                  className={`px-6 py-3 rounded-lg font-medium transition-all ${
                    allocationMode === 'manual'
                      ? 'bg-[#2563EB] text-white shadow-md'
                      : 'bg-[#F3F4F6] text-[#6B7280] hover:bg-[#E5E7EB]'
                  }`}
                >
                  Thủ công
                </button>
              </div>

              {/* Sites Cards */}
              <div className="space-y-4">
                {(mockAvailableSites[selectedItem.id] || []).map((site) => {
                  const isSelected = selectedSites[site.siteCode] !== undefined;
                  const quantity = selectedSites[site.siteCode] || 0;

                  return (
                    <div
                      key={site.siteCode}
                      className={`border-2 rounded-xl p-6 transition-all ${
                        isSelected
                          ? 'border-blue-500 bg-blue-50'
                          : 'border-[#E5E7EB] bg-white hover:border-blue-200'
                      }`}
                    >
                      <div className="flex items-start gap-4">
                        {/* Checkbox */}
                        <div className="pt-1">
                          <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => {
                              if (allocationMode === 'manual') {
                                handleManualSiteToggle(site.siteCode);
                              }
                            }}
                            disabled={allocationMode === 'auto'}
                            className="w-5 h-5 border-2 border-[#9CA3AF] rounded bg-white checked:bg-white checked:border-[#2563EB] focus:ring-2 focus:ring-[#2563EB] disabled:opacity-50 appearance-none checked:bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEzLjMzMzMgNC42NjY2N0w2IDEyTDIuNjY2NjcgOC42NjY2NyIgc3Ryb2tlPSIjMjU2M0VCIiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgo8L3N2Zz4K')] bg-center bg-no-repeat"
                          />
                        </div>

                        {/* Site Information */}
                        <div className="flex-1">
                          <div className="flex items-start justify-between mb-4">
                            <div>
                              <h4 className="text-lg font-bold text-[#111827] mb-1">
                                {site.siteCode} - {site.siteName}
                              </h4>
                              <p className="text-sm text-[#6B7280]">{site.warehouse}</p>
                            </div>
                            <div className="text-right">
                              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                                Tồn kho
                              </p>
                              <p className="text-lg font-bold text-[#2563EB]">
                                {site.availableStock.toLocaleString()} {selectedItem.unit}
                              </p>
                            </div>
                          </div>

                          <div className="grid grid-cols-3 gap-4 mb-4">
                            <div className="flex items-center gap-2 text-sm text-[#6B7280]">
                              <Truck className="w-4 h-4" />
                              <span>{site.deliveryMethod}</span>
                            </div>
                            <div className="flex items-center gap-2 text-sm text-[#6B7280]">
                              <Calendar className="w-4 h-4" />
                              <span>Giao: {site.estimatedDate}</span>
                            </div>
                            <div className="text-right">
                              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                                Đơn giá
                              </p>
                              <p className="text-sm font-bold text-green-600">
                                {formatCurrency(site.unitPrice)}
                              </p>
                            </div>
                          </div>

                          {/* Quantity Input */}
                          {allocationMode === 'manual' && isSelected && (
                            <div className="mt-4 pt-4 border-t border-[#E5E7EB]">
                              <label className="block text-sm font-semibold text-[#111827] mb-2">
                                Số lượng phân bổ
                              </label>
                              <input
                                type="number"
                                min="0"
                                max={site.availableStock}
                                value={quantity}
                                onChange={(e) =>
                                  handleQuantityChange(
                                    site.siteCode,
                                    parseInt(e.target.value) || 0
                                  )
                                }
                                className="w-full px-4 py-3 border-2 border-[#E5E7EB] rounded-lg text-[#111827] text-lg font-semibold focus:outline-none focus:ring-2 focus:ring-[#2563EB] focus:border-transparent"
                                placeholder="Nhập số lượng"
                              />
                              {quantity > 0 && (
                                <div className="mt-3 p-3 bg-green-50 border border-green-200 rounded-lg">
                                  <div className="flex items-center justify-between">
                                    <span className="text-sm font-semibold text-green-800">Tổng giá trị:</span>
                                    <span className="text-lg font-bold text-green-900">
                                      {formatCurrency(quantity * site.unitPrice)}
                                    </span>
                                  </div>
                                </div>
                              )}
                            </div>
                          )}

                          {allocationMode === 'auto' && isSelected && (
                            <div className="mt-4 pt-4 border-t border-[#E5E7EB] space-y-3">
                              <div className="flex items-center justify-between">
                                <span className="text-sm text-[#6B7280]">Số lượng tự động:</span>
                                <span className="font-bold text-[#2563EB]">
                                  {quantity} {selectedItem.unit}
                                </span>
                              </div>
                              <div className="p-3 bg-green-50 border border-green-200 rounded-lg">
                                <div className="flex items-center justify-between">
                                  <span className="text-sm font-semibold text-green-800">Tổng giá trị:</span>
                                  <span className="text-lg font-bold text-green-900">
                                    {formatCurrency(quantity * site.unitPrice)}
                                  </span>
                                </div>
                              </div>
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* Summary */}
              <div className="mt-6 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-6 border-2 border-blue-200">
                <div className="space-y-4">
                  <div className="flex items-center justify-between">
                    <span className="text-lg font-semibold text-[#111827]">Tổng phân bổ:</span>
                    <div className="text-right">
                      <span className="text-2xl font-bold text-[#2563EB]">
                        {getTotalAllocated()}
                      </span>
                      <span className="text-lg text-[#6B7280]">
                        {' '}/ {selectedItem.shortage} {selectedItem.unit}
                      </span>
                    </div>
                  </div>
                  {getTotalAllocated() > 0 && (
                    <div className="pt-4 border-t border-blue-200">
                      <div className="flex items-center justify-between">
                        <span className="text-lg font-semibold text-green-800">Tổng giá trị đơn hàng:</span>
                        <span className="text-2xl font-bold text-green-900">
                          {formatCurrency(
                            (mockAvailableSites[selectedItem.id] || []).reduce((total, site) => {
                              const quantity = selectedSites[site.siteCode] || 0;
                              return total + quantity * site.unitPrice;
                            }, 0)
                          )}
                        </span>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Footer - Fixed */}
            <div className="border-t border-[#E5E7EB] px-8 py-6 bg-[#F8F9FA] flex-shrink-0">
              <div className="flex justify-end gap-3">
                <button
                  onClick={() => setSelectedItem(null)}
                  className="px-6 py-3 border border-[#E5E7EB] rounded-lg text-[#111827] font-medium hover:bg-white transition-colors"
                >
                  Hủy
                </button>
                <button
                  onClick={handleConfirmAllocation}
                  className="px-8 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-medium hover:from-blue-700 hover:to-blue-800 transition-all shadow-md hover:shadow-lg"
                >
                  Xác nhận phân bổ
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
