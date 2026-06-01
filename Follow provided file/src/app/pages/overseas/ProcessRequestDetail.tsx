import { useParams, Link, useNavigate } from 'react-router';
import { ArrowLeft, AlertCircle, CheckCircle, Truck, Package, MapPin, User, Phone, Clock, FileText, ChevronRight } from 'lucide-react';
import { useState } from 'react';

interface RequestItem {
  code: string;
  name: string;
  quantity: number;
  unit: string;
  deliveryDate: string;
  allocated?: number;
  allocations?: ItemAllocation[];
}

interface ItemAllocation {
  siteId: string;
  siteName: string;
  quantity: number;
}

interface SiteItem {
  code: string;
  name: string;
  availableQuantity: number;
  unit: string;
  unitPrice: number;
}

interface SiteMatch {
  siteId: string;
  siteName: string;
  location: string;
  contactPerson: string;
  contactPhone: string;
  shippingCost: number;
  estimatedDelivery: string;
  score: number;
  items: SiteItem[];
}

const mockRequestData = {
  code: 'REQ-2024-001',
  createdDate: '2024-05-08',
  status: 'pending' as const,
  priority: 'high' as const,
  itemCount: 3,
  items: [
    { code: 'MH001', name: 'Laptop Dell XPS 13', quantity: 5, unit: 'cái', deliveryDate: '2024-05-15' },
    { code: 'MH002', name: 'Bàn phím cơ Keychron K2', quantity: 10, unit: 'cái', deliveryDate: '2024-05-16' },
    { code: 'MH003', name: 'Chuột Logitech MX Master 3', quantity: 8, unit: 'cái', deliveryDate: '2024-05-15' },
  ] as RequestItem[],
};

export function ProcessRequestDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [requestItems, setRequestItems] = useState<RequestItem[]>(mockRequestData.items);
  const [showAllocationModal, setShowAllocationModal] = useState(false);
  const [selectedItem, setSelectedItem] = useState<RequestItem | null>(null);
  const [siteMatches, setSiteMatches] = useState<SiteMatch[]>([]);
  const [isLoadingSites, setIsLoadingSites] = useState(false);
  const [selectedSites, setSelectedSites] = useState<{ siteId: string; quantity: number }[]>([]);
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const request = mockRequestData;

  const handleOpenAllocation = (item: RequestItem) => {
    setSelectedItem(item);
    setIsLoadingSites(true);
    setShowAllocationModal(true);

    // Load existing allocations if any
    if (item.allocations && item.allocations.length > 0) {
      setSelectedSites(item.allocations.map(a => ({ siteId: a.siteId, quantity: a.quantity })));
    } else {
      setSelectedSites([]);
    }

    // Simulate API call to find sites
    setTimeout(() => {
      // Filter sites based on which item we're looking for
      const allSites: SiteMatch[] = [
        {
          siteId: 'SITE-001',
          siteName: 'Site A - Hà Nội',
          location: 'Số 10 Lê Duẩn, Hoàn Kiếm, Hà Nội',
          contactPerson: 'Nguyễn Văn An',
          contactPhone: '024-3942-1234',
          shippingCost: 500000,
          estimatedDelivery: '2024-05-12',
          score: 95,
          items: [
            { code: 'MH001', name: 'Laptop Dell XPS 13', availableQuantity: 10, unit: 'cái', unitPrice: 25000000 },
            { code: 'MH002', name: 'Bàn phím cơ Keychron K2', availableQuantity: 25, unit: 'cái', unitPrice: 2700000 },
            { code: 'MH003', name: 'Chuột Logitech MX Master 3', availableQuantity: 15, unit: 'cái', unitPrice: 2000000 },
          ],
        },
        {
          siteId: 'SITE-002',
          siteName: 'Site B - TP. Hồ Chí Minh',
          location: 'Số 268 Lý Thường Kiệt, Quận 10, TP.HCM',
          contactPerson: 'Trần Thị Bình',
          contactPhone: '028-3835-5678',
          shippingCost: 750000,
          estimatedDelivery: '2024-05-13',
          score: 88,
          items: [
            { code: 'MH001', name: 'Laptop Dell XPS 13', availableQuantity: 8, unit: 'cái', unitPrice: 24500000 },
            { code: 'MH002', name: 'Bàn phím cơ Keychron K2', availableQuantity: 30, unit: 'cái', unitPrice: 2650000 },
          ],
        },
        {
          siteId: 'SITE-003',
          siteName: 'Site C - Đà Nẵng',
          location: 'Số 45 Nguyễn Văn Linh, Hải Châu, Đà Nẵng',
          contactPerson: 'Lê Minh Châu',
          contactPhone: '0236-3812-9999',
          shippingCost: 600000,
          estimatedDelivery: '2024-05-14',
          score: 75,
          items: [
            { code: 'MH002', name: 'Bàn phím cơ Keychron K2', availableQuantity: 20, unit: 'cái', unitPrice: 2700000 },
            { code: 'MH003', name: 'Chuột Logitech MX Master 3', availableQuantity: 12, unit: 'cái', unitPrice: 1950000 },
          ],
        },
      ];

      // Filter sites that have the selected item
      const filteredSites = allSites.filter(site =>
        site.items.some(siteItem => siteItem.code === item.code)
      );

      setSiteMatches(filteredSites);
      setIsLoadingSites(false);
    }, 1000);
  };

  const handleSaveAllocation = () => {
    if (!selectedItem) return;

    const totalAllocated = selectedSites.reduce((sum, s) => sum + s.quantity, 0);

    if (totalAllocated === 0) {
      alert('Vui lòng phân bổ ít nhất 1 sản phẩm!');
      return;
    }

    // Update item with new allocations
    const updatedItems = requestItems.map((item) => {
      if (item.code === selectedItem.code) {
        const allocations = selectedSites
          .filter(s => s.quantity > 0)
          .map(s => {
            const site = siteMatches.find(sm => sm.siteId === s.siteId)!;
            return {
              siteId: s.siteId,
              siteName: site.siteName,
              quantity: s.quantity,
            };
          });
        return {
          ...item,
          allocated: totalAllocated,
          allocations,
        };
      }
      return item;
    });

    setRequestItems(updatedItems);
    setShowAllocationModal(false);
    setSelectedItem(null);
    setSelectedSites([]);
  };

  const handleUpdateQuantity = (siteId: string, quantity: number) => {
    const existing = selectedSites.find(s => s.siteId === siteId);
    if (existing) {
      setSelectedSites(selectedSites.map(s =>
        s.siteId === siteId ? { ...s, quantity } : s
      ));
    } else {
      setSelectedSites([...selectedSites, { siteId, quantity }]);
    }
  };

  const handleConfirmAll = () => {
    const allAllocated = requestItems.every(item =>
      (item.allocated || 0) >= item.quantity
    );

    if (!allAllocated) {
      if (!confirm('Một số mặt hàng chưa được phân bổ đủ. Bạn có chắc muốn tiếp tục?')) {
        return;
      }
    }

    setShowConfirmModal(true);
  };

  const handleSendOrders = () => {
    setShowConfirmModal(false);
    alert('Đã gửi đơn hàng thành công đến các Site!');
    navigate('/overseas/process');
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(value);
  };

  const totalItems = requestItems.length;
  const fullyAllocatedItems = requestItems.filter(
    (item) => (item.allocated || 0) >= item.quantity
  ).length;
  const partiallyAllocatedItems = requestItems.filter(
    (item) => (item.allocated || 0) > 0 && (item.allocated || 0) < item.quantity
  ).length;
  const unallocatedItems = requestItems.filter((item) => !item.allocated || item.allocated === 0)
    .length;
  const progressPercentage = Math.round((fullyAllocatedItems / totalItems) * 100);

  return (
    <div className="p-8 space-y-6 bg-gray-50 min-h-screen">
      {/* Header */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <div className="flex items-center gap-4 mb-4">
          <Link
            to="/overseas/process"
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <ArrowLeft className="w-5 h-5 text-gray-600" />
          </Link>
          <div className="flex-1">
            <div className="flex items-center gap-3 mb-2">
              <h1 className="text-2xl font-bold text-gray-900">
                Xử lý Yêu cầu: {request.code}
              </h1>
              <span className="px-3 py-1 bg-red-100 text-red-800 text-sm font-medium rounded-full">
                Độ ưu tiên: Cao
              </span>
            </div>
            <p className="text-gray-600">
              Phân bổ mặt hàng cho các Site phù hợp và tạo đơn hàng
            </p>
          </div>
          <div className="text-right">
            <p className="text-sm text-gray-500 mb-1">Tiến độ phân bổ</p>
            <div className="flex items-center gap-3">
              <div className="w-32 h-2 bg-gray-200 rounded-full overflow-hidden">
                <div
                  className="h-full bg-green-600 transition-all duration-300"
                  style={{ width: `${progressPercentage}%` }}
                />
              </div>
              <span className="text-lg font-bold text-gray-900">{progressPercentage}%</span>
            </div>
          </div>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-4 gap-4">
          <div className="bg-blue-50 rounded-lg p-3 border border-blue-200">
            <div className="flex items-center gap-2 mb-1">
              <FileText className="w-4 h-4 text-blue-600" />
              <p className="text-xs font-semibold text-blue-900 uppercase">Tổng mặt hàng</p>
            </div>
            <p className="text-2xl font-bold text-blue-600">{totalItems}</p>
          </div>

          <div className="bg-green-50 rounded-lg p-3 border border-green-200">
            <div className="flex items-center gap-2 mb-1">
              <CheckCircle className="w-4 h-4 text-green-600" />
              <p className="text-xs font-semibold text-green-900 uppercase">Đã phân bổ đủ</p>
            </div>
            <p className="text-2xl font-bold text-green-600">{fullyAllocatedItems}</p>
          </div>

          <div className="bg-yellow-50 rounded-lg p-3 border border-yellow-200">
            <div className="flex items-center gap-2 mb-1">
              <Clock className="w-4 h-4 text-yellow-600" />
              <p className="text-xs font-semibold text-yellow-900 uppercase">Phân bổ 1 phần</p>
            </div>
            <p className="text-2xl font-bold text-yellow-600">{partiallyAllocatedItems}</p>
          </div>

          <div className="bg-red-50 rounded-lg p-3 border border-red-200">
            <div className="flex items-center gap-2 mb-1">
              <AlertCircle className="w-4 h-4 text-red-600" />
              <p className="text-xs font-semibold text-red-900 uppercase">Chưa phân bổ</p>
            </div>
            <p className="text-2xl font-bold text-red-600">{unallocatedItems}</p>
          </div>
        </div>
      </div>


      <div className="grid grid-cols-3 gap-6">
        {/* Main Content - Items List */}
        <div className="col-span-2 space-y-4">

          {requestItems.map((item, index) => {
            const allocated = item.allocated || 0;
            const remaining = item.quantity - allocated;
            const hasAllocations = (item.allocations?.length || 0) > 0;
            const itemProgress = Math.round((allocated / item.quantity) * 100);
            const isComplete = allocated >= item.quantity;
            const isPartial = allocated > 0 && allocated < item.quantity;

            return (
              <div
                key={item.code}
                className="bg-white rounded-lg shadow-sm border-2 border-gray-200 hover:border-gray-300 transition-all overflow-hidden"
              >
                {/* Item Header */}
                <div className="bg-gray-50 px-5 py-4 border-b border-gray-200">
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <span className="text-sm font-bold text-gray-500">#{index + 1}</span>
                        <h3 className="text-lg font-bold text-gray-900">{item.name}</h3>
                        <span className="px-2.5 py-0.5 bg-gray-200 text-gray-700 text-xs font-medium rounded">
                          {item.code}
                        </span>
                      </div>
                      <div className="flex items-center gap-4 text-sm text-gray-600">
                        <span className="flex items-center gap-1.5">
                          <Package className="w-4 h-4" />
                          Yêu cầu: <strong className="text-gray-900">{item.quantity} {item.unit}</strong>
                        </span>
                        <span className="flex items-center gap-1.5">
                          <Clock className="w-4 h-4" />
                          Ngày nhận: <strong className="text-gray-900">{item.deliveryDate}</strong>
                        </span>
                      </div>
                    </div>

                    {/* Status Badge */}
                    <div>
                      {isComplete ? (
                        <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-green-100 text-green-800 text-sm font-semibold rounded-full">
                          <CheckCircle className="w-4 h-4" />
                          Hoàn thành
                        </span>
                      ) : isPartial ? (
                        <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-yellow-100 text-yellow-800 text-sm font-semibold rounded-full">
                          <Clock className="w-4 h-4" />
                          Đang xử lý
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-red-100 text-red-800 text-sm font-semibold rounded-full">
                          <AlertCircle className="w-4 h-4" />
                          Chưa phân bổ
                        </span>
                      )}
                    </div>
                  </div>

                  {/* Progress Bar */}
                  <div className="mt-3">
                    <div className="flex items-center justify-between mb-1.5">
                      <span className="text-xs font-semibold text-gray-600">
                        Đã phân bổ: {allocated}/{item.quantity} {item.unit}
                      </span>
                      <span className="text-xs font-bold text-gray-900">{itemProgress}%</span>
                    </div>
                    <div className="h-2.5 bg-gray-200 rounded-full overflow-hidden">
                      <div
                        className={`h-full transition-all duration-300 ${
                          isComplete
                            ? 'bg-green-600'
                            : isPartial
                            ? 'bg-yellow-500'
                            : 'bg-red-500'
                        }`}
                        style={{ width: `${itemProgress}%` }}
                      />
                    </div>
                  </div>
                </div>

                {/* Item Body */}
                <div className="p-5 space-y-3">
                  {/* Allocation Details */}
                  {hasAllocations ? (
                    <div className="space-y-2">
                      <div className="flex items-center justify-between mb-2">
                        <p className="text-sm font-semibold text-gray-700">
                          Đã phân bổ cho {item.allocations!.length} Site:
                        </p>
                        <button
                          onClick={() => handleOpenAllocation(item)}
                          className="text-sm font-medium text-blue-600 hover:text-blue-700 hover:underline flex items-center gap-1"
                        >
                          Chỉnh sửa
                          <ChevronRight className="w-4 h-4" />
                        </button>
                      </div>
                      <div className="space-y-2">
                        {item.allocations!.map((alloc, idx) => (
                          <div
                            key={idx}
                            className="flex items-center justify-between bg-blue-50 border border-blue-200 rounded-lg px-3 py-2"
                          >
                            <span className="text-sm font-medium text-blue-900">
                              {alloc.siteName}
                            </span>
                            <span className="text-sm font-bold text-blue-600">
                              {alloc.quantity} {item.unit}
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  ) : (
                    <div className="bg-gray-50 border border-gray-200 rounded-lg px-4 py-3 text-center">
                      <p className="text-sm text-gray-600">
                        Chưa có phân bổ nào cho mặt hàng này
                      </p>
                    </div>
                  )}

                  {/* Action Button */}
                  {remaining > 0 && (
                    <div className="pt-3 border-t border-gray-200">
                      <button
                        onClick={() => handleOpenAllocation(item)}
                        className="w-full px-4 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg hover:from-blue-700 hover:to-blue-800 transition-all font-semibold flex items-center justify-center gap-2 shadow-sm"
                      >
                        <Package className="w-5 h-5" />
                        {hasAllocations ? `Phân bổ thêm ${remaining} ${item.unit}` : 'Phân bổ Site'}
                      </button>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>

        {/* Sidebar - Summary */}
        <div className="space-y-6">
          {/* Summary Card */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 sticky top-6">
            <h3 className="text-lg font-bold text-gray-900 mb-4 flex items-center gap-2">
              <FileText className="w-5 h-5 text-blue-600" />
              Tổng quan phân bổ
            </h3>

            <div className="space-y-4">
              {/* Overall Progress */}
              <div className="pb-4 border-b border-gray-200">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-semibold text-gray-600">Tiến độ tổng thể</span>
                  <span className="text-2xl font-bold text-gray-900">{progressPercentage}%</span>
                </div>
                <div className="h-3 bg-gray-200 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-green-500 to-green-600 transition-all duration-300"
                    style={{ width: `${progressPercentage}%` }}
                  />
                </div>
              </div>

              {/* Stats */}
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Tổng mặt hàng</span>
                  <span className="text-sm font-bold text-gray-900">{totalItems}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600 flex items-center gap-1.5">
                    <CheckCircle className="w-4 h-4 text-green-600" />
                    Đã phân bổ đủ
                  </span>
                  <span className="text-sm font-bold text-green-600">{fullyAllocatedItems}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600 flex items-center gap-1.5">
                    <Clock className="w-4 h-4 text-yellow-600" />
                    Phân bổ 1 phần
                  </span>
                  <span className="text-sm font-bold text-yellow-600">
                    {partiallyAllocatedItems}
                  </span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600 flex items-center gap-1.5">
                    <AlertCircle className="w-4 h-4 text-red-600" />
                    Chưa phân bổ
                  </span>
                  <span className="text-sm font-bold text-red-600">{unallocatedItems}</span>
                </div>
              </div>

              <div className="h-px bg-gray-200" />

              {/* Total Sites */}
              <div className="bg-blue-50 rounded-lg p-3 border border-blue-200">
                <p className="text-xs font-semibold text-blue-900 uppercase mb-1">
                  Tổng số Site đã chọn
                </p>
                <p className="text-2xl font-bold text-blue-600">
                  {
                    new Set(
                      requestItems.flatMap((item) => item.allocations?.map((a) => a.siteId) || [])
                    ).size
                  }{' '}
                  Site
                </p>
              </div>

              {/* Action Buttons */}
              <div className="space-y-2 pt-2">
                <button
                  onClick={handleConfirmAll}
                  className="w-full px-4 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg hover:from-green-700 hover:to-green-800 transition-all font-bold shadow-md flex items-center justify-center gap-2"
                >
                  <CheckCircle className="w-5 h-5" />
                  Xác nhận & Gửi đơn hàng
                </button>
                <Link
                  to="/overseas/process"
                  className="w-full px-4 py-2.5 bg-white border-2 border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors font-semibold flex items-center justify-center gap-2"
                >
                  Quay lại danh sách
                </Link>
              </div>
            </div>
          </div>

          {/* Info Card */}
          <div className="bg-blue-50 rounded-lg border border-blue-200 p-5">
            <h4 className="text-sm font-bold text-blue-900 mb-2 flex items-center gap-1.5">
              <AlertCircle className="w-4 h-4" />
              Lưu ý quan trọng
            </h4>
            <ul className="text-xs text-blue-800 space-y-1.5">
              <li>• Đảm bảo phân bổ đủ số lượng cho tất cả mặt hàng</li>
              <li>• Kiểm tra kỹ thông tin Site trước khi xác nhận</li>
              <li>• Đơn hàng sẽ được gửi ngay sau khi xác nhận</li>
              <li>• Không thể hoàn tác sau khi gửi đơn hàng</li>
            </ul>
          </div>
        </div>
      </div>

      {/* Allocation Modal */}
      {showAllocationModal && selectedItem && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
          onClick={() => {
            setShowAllocationModal(false);
            setSelectedItem(null);
            setSelectedSites([]);
          }}
        >
          <div
            className="bg-white rounded-xl shadow-2xl w-full max-w-5xl max-h-[90vh] overflow-hidden flex flex-col"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Modal Header */}
            <div className="bg-gradient-to-r from-blue-600 to-blue-700 p-6 text-white sticky top-0 z-10">
              <div className="flex items-start justify-between mb-3">
                <div className="flex-1">
                  <h3 className="text-2xl font-bold mb-2">Phân bổ Site</h3>
                  <p className="text-blue-100 font-medium">{selectedItem.name}</p>
                </div>
                <button
                  onClick={() => {
                    setShowAllocationModal(false);
                    setSelectedItem(null);
                    setSelectedSites([]);
                  }}
                  className="p-2 hover:bg-white/20 rounded-lg transition-colors"
                >
                  <span className="text-2xl">&times;</span>
                </button>
              </div>

              <div className="grid grid-cols-3 gap-4 mt-4">
                <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                  <p className="text-blue-100 text-xs font-semibold uppercase mb-1">
                    Yêu cầu
                  </p>
                  <p className="text-2xl font-bold">
                    {selectedItem.quantity} {selectedItem.unit}
                  </p>
                </div>
                <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                  <p className="text-blue-100 text-xs font-semibold uppercase mb-1">
                    Đã phân bổ
                  </p>
                  <p className="text-2xl font-bold">
                    {selectedSites.reduce((sum, s) => sum + s.quantity, 0)} {selectedItem.unit}
                  </p>
                </div>
                <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                  <p className="text-blue-100 text-xs font-semibold uppercase mb-1">Còn lại</p>
                  <p className="text-2xl font-bold">
                    {selectedItem.quantity - selectedSites.reduce((sum, s) => sum + s.quantity, 0)}{' '}
                    {selectedItem.unit}
                  </p>
                </div>
              </div>
            </div>

            {/* Modal Body */}
            <div className="flex-1 overflow-y-auto p-6 bg-gray-50">
              {isLoadingSites ? (
                <div className="flex flex-col items-center justify-center py-16">
                  <div className="animate-spin rounded-full h-20 w-20 border-4 border-blue-200 border-t-blue-600 mb-4"></div>
                  <p className="text-lg font-semibold text-gray-900 mb-1">
                    Đang tìm kiếm Site phù hợp...
                  </p>
                  <p className="text-sm text-gray-500">
                    Hệ thống đang kiểm tra tồn kho tại các Site
                  </p>
                </div>
              ) : siteMatches.length === 0 ? (
                <div className="text-center py-16">
                  <div className="mb-4">
                    <div className="w-20 h-20 bg-gray-200 rounded-full flex items-center justify-center mx-auto mb-4">
                      <Package className="w-10 h-10 text-gray-400" />
                    </div>
                    <p className="text-xl font-semibold text-gray-900 mb-2">
                      Không tìm thấy Site phù hợp
                    </p>
                    <p className="text-sm text-gray-600">
                      Không có Site nào có sẵn {selectedItem.name}
                    </p>
                    <p className="text-sm text-gray-500 mt-1">
                      Vui lòng liên hệ nhà cung cấp khác hoặc cập nhật yêu cầu
                    </p>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="mb-4">
                    <h4 className="text-lg font-bold text-gray-900 mb-1">
                      Tìm thấy {siteMatches.length} Site có sẵn
                    </h4>
                    <p className="text-sm text-gray-600">
                      Chọn số lượng cần phân bổ cho mỗi Site
                    </p>
                  </div>

                  <div className="space-y-4">
                  {siteMatches.map((site) => {
                    const allocation = selectedSites.find(s => s.siteId === site.siteId);
                    const quantity = allocation?.quantity || 0;
                    const siteItem = site.items.find(item => item.code === selectedItem.code);
                    const maxQuantity = siteItem?.availableQuantity || 0;
                    const isSelected = quantity > 0;

                    return (
                      <div
                        key={site.siteId}
                        className={`border-2 rounded-xl overflow-hidden transition-all ${
                          isSelected
                            ? 'border-blue-500 bg-blue-50 shadow-md'
                            : 'border-gray-200 bg-white hover:border-gray-300 hover:shadow-sm'
                        }`}
                      >
                        {/* Site Header */}
                        <div className={`p-5 border-b ${isSelected ? 'bg-blue-100 border-blue-300' : 'bg-gray-50 border-gray-200'}`}>
                          <div className="flex items-center justify-between mb-3">
                            <div className="flex items-center gap-3">
                              <Link
                                to={`/overseas/sites/${site.siteId}`}
                                className="font-bold text-lg text-blue-600 hover:text-blue-700 hover:underline flex items-center gap-1.5"
                                target="_blank"
                              >
                                {site.siteName}
                                <ChevronRight className="w-4 h-4" />
                              </Link>
                              <span className="px-3 py-1 bg-green-100 text-green-800 text-xs font-bold rounded-full">
                                {site.score}/100
                              </span>
                            </div>
                            {isSelected && (
                              <span className="px-3 py-1 bg-blue-600 text-white text-xs font-bold rounded-full flex items-center gap-1">
                                <CheckCircle className="w-3.5 h-3.5" />
                                Đã chọn
                              </span>
                            )}
                          </div>

                          <div className="grid grid-cols-2 gap-4 text-sm">
                            <div className="flex items-start gap-2">
                              <MapPin className="w-4 h-4 text-gray-500 mt-0.5 flex-shrink-0" />
                              <div>
                                <p className="text-gray-500 mb-1">Địa chỉ</p>
                                <p className="font-medium text-gray-900">{site.location}</p>
                              </div>
                            </div>
                            <div className="flex items-start gap-2">
                              <User className="w-4 h-4 text-gray-500 mt-0.5 flex-shrink-0" />
                              <div>
                                <p className="text-gray-500 mb-1">Người liên hệ</p>
                                <p className="font-medium text-gray-900">{site.contactPerson}</p>
                                <p className="text-gray-600 text-xs flex items-center gap-1 mt-0.5">
                                  <Phone className="w-3 h-3" />
                                  {site.contactPhone}
                                </p>
                              </div>
                            </div>
                            <div className="flex items-start gap-2">
                              <Truck className="w-4 h-4 text-gray-500 mt-0.5 flex-shrink-0" />
                              <div>
                                <p className="text-gray-500 mb-1">Phí vận chuyển</p>
                                <p className="font-semibold text-gray-900">
                                  {formatCurrency(site.shippingCost)}
                                </p>
                              </div>
                            </div>
                            <div className="flex items-start gap-2">
                              <AlertCircle className="w-4 h-4 text-gray-500 mt-0.5 flex-shrink-0" />
                              <div>
                                <p className="text-gray-500 mb-1">Giao hàng dự kiến</p>
                                <p className="font-semibold text-gray-900">{site.estimatedDelivery}</p>
                              </div>
                            </div>
                          </div>
                        </div>

                        {/* Site Items */}
                        <div className="p-4">
                          <div className="mb-3">
                            <p className="text-xs font-semibold text-gray-500 uppercase mb-2">
                              Mặt hàng tại Site
                            </p>
                            <div className="overflow-x-auto">
                              <table className="w-full text-sm">
                                <thead className="bg-gray-100 border-b border-gray-200">
                                  <tr>
                                    <th className="px-3 py-2 text-left text-xs font-semibold text-gray-600">
                                      Mã hàng
                                    </th>
                                    <th className="px-3 py-2 text-left text-xs font-semibold text-gray-600">
                                      Tên mặt hàng
                                    </th>
                                    <th className="px-3 py-2 text-right text-xs font-semibold text-gray-600">
                                      Tồn kho
                                    </th>
                                    <th className="px-3 py-2 text-right text-xs font-semibold text-gray-600">
                                      Đơn giá
                                    </th>
                                  </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-100">
                                  {site.items.map((item) => {
                                    const isSelected = item.code === selectedItem.code;
                                    return (
                                      <tr
                                        key={item.code}
                                        className={
                                          isSelected ? 'bg-blue-50 font-medium' : 'hover:bg-gray-50'
                                        }
                                      >
                                        <td className="px-3 py-2 text-gray-700">{item.code}</td>
                                        <td className="px-3 py-2 text-gray-900">
                                          {item.name}
                                          {isSelected && (
                                            <span className="ml-2 px-2 py-0.5 bg-blue-100 text-blue-700 text-xs rounded">
                                              Đang chọn
                                            </span>
                                          )}
                                        </td>
                                        <td className="px-3 py-2 text-right">
                                          <span className="font-semibold text-gray-900">
                                            {item.availableQuantity}
                                          </span>{' '}
                                          <span className="text-gray-500">{item.unit}</span>
                                        </td>
                                        <td className="px-3 py-2 text-right text-gray-900">
                                          {formatCurrency(item.unitPrice)}
                                        </td>
                                      </tr>
                                    );
                                  })}
                                </tbody>
                              </table>
                            </div>
                          </div>

                          {/* Allocation Input */}
                          <div className={`pt-4 border-t ${isSelected ? 'border-blue-200' : 'border-gray-200'}`}>
                            <div className="space-y-3">
                              <div>
                                <label className="text-sm font-bold text-gray-900 mb-2 block">
                                  Số lượng phân bổ từ Site này:
                                </label>
                                <div className="flex items-center gap-3">
                                  <div className="relative flex-1">
                                    <input
                                      type="number"
                                      min="0"
                                      max={maxQuantity}
                                      value={quantity}
                                      onChange={(e) =>
                                        handleUpdateQuantity(site.siteId, parseInt(e.target.value) || 0)
                                      }
                                      className={`w-full px-4 py-3 border-2 rounded-lg focus:outline-none focus:ring-2 text-lg font-bold ${
                                        isSelected
                                          ? 'border-blue-500 bg-white focus:ring-blue-300'
                                          : 'border-gray-300 focus:ring-blue-500'
                                      }`}
                                      placeholder="0"
                                    />
                                    <span className="absolute right-4 top-1/2 -translate-y-1/2 text-sm font-medium text-gray-500">
                                      / {maxQuantity}
                                    </span>
                                  </div>
                                  <span className="text-sm font-semibold text-gray-700 whitespace-nowrap">
                                    {selectedItem.unit}
                                  </span>
                                </div>
                              </div>

                              {quantity > 0 && (
                                <div className="flex items-center justify-between bg-green-50 border border-green-200 rounded-lg px-4 py-3">
                                  <span className="text-sm font-semibold text-green-900">
                                    Tổng giá trị ước tính:
                                  </span>
                                  <span className="text-lg font-bold text-green-600">
                                    {formatCurrency(quantity * (siteItem?.unitPrice || 0))}
                                  </span>
                                </div>
                              )}
                            </div>
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
              )}
            </div>

            {/* Modal Footer */}
            <div className="bg-white border-t-2 border-gray-200 p-6 sticky bottom-0">
              <div className="flex items-center justify-between mb-4">
                <div className="grid grid-cols-2 gap-6 flex-1">
                  <div className="bg-gray-100 rounded-lg p-3">
                    <p className="text-xs font-semibold text-gray-600 uppercase mb-1">
                      Tổng phân bổ
                    </p>
                    <p className="text-xl font-bold text-gray-900">
                      {selectedSites.reduce((sum, s) => sum + s.quantity, 0)} / {selectedItem.quantity}{' '}
                      {selectedItem.unit}
                    </p>
                  </div>
                  <div className="bg-blue-100 rounded-lg p-3">
                    <p className="text-xs font-semibold text-blue-900 uppercase mb-1">
                      Site đã chọn
                    </p>
                    <p className="text-xl font-bold text-blue-600">
                      {selectedSites.filter(s => s.quantity > 0).length} Site
                    </p>
                  </div>
                </div>
              </div>

              <div className="flex gap-3">
                <button
                  onClick={() => {
                    setShowAllocationModal(false);
                    setSelectedItem(null);
                    setSelectedSites([]);
                  }}
                  className="flex-1 px-6 py-3 border-2 border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors font-semibold"
                >
                  Hủy
                </button>
                <button
                  onClick={handleSaveAllocation}
                  className="flex-1 px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg hover:from-blue-700 hover:to-blue-800 transition-all font-bold shadow-md"
                >
                  Lưu phân bổ
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Confirm Modal */}
      {showConfirmModal && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
          onClick={() => setShowConfirmModal(false)}
        >
          <div
            className="bg-white rounded-xl shadow-2xl max-w-3xl w-full overflow-hidden"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Modal Header */}
            <div className="bg-gradient-to-r from-green-600 to-green-700 p-6 text-white">
              <div className="flex items-center gap-3 mb-2">
                <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center">
                  <CheckCircle className="w-7 h-7" />
                </div>
                <div>
                  <h3 className="text-2xl font-bold">Xác nhận Gửi Đơn hàng</h3>
                  <p className="text-green-100 text-sm">
                    Kiểm tra lại thông tin trước khi gửi đến các Site
                  </p>
                </div>
              </div>
            </div>

            {/* Modal Body */}
            <div className="p-6">
              <div className="mb-6">
                <h4 className="text-lg font-bold text-gray-900 mb-3">
                  Tổng quan phân bổ:
                </h4>

                <div className="space-y-2 mb-6">
                  {requestItems.map((item) => {
                    const allocated = item.allocated || 0;
                    const remaining = item.quantity - allocated;
                    const progress = Math.round((allocated / item.quantity) * 100);

                    return (
                      <div
                        key={item.code}
                        className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors"
                      >
                        <div className="flex items-center justify-between mb-2">
                          <span className="text-sm font-semibold text-gray-900">{item.name}</span>
                          <div className="flex items-center gap-2">
                            <span className="text-sm font-bold text-gray-900">
                              {allocated}/{item.quantity} {item.unit}
                            </span>
                            {remaining > 0 ? (
                              <span className="text-xs px-2.5 py-1 bg-red-100 text-red-700 rounded-full font-semibold">
                                Thiếu {remaining}
                              </span>
                            ) : (
                              <span className="text-xs px-2.5 py-1 bg-green-100 text-green-700 rounded-full font-semibold flex items-center gap-1">
                                <CheckCircle className="w-3 h-3" />
                                Đủ
                              </span>
                            )}
                          </div>
                        </div>
                        <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
                          <div
                            className={`h-full transition-all ${
                              remaining === 0 ? 'bg-green-600' : 'bg-yellow-500'
                            }`}
                            style={{ width: `${progress}%` }}
                          />
                        </div>
                      </div>
                    );
                  })}
                </div>

                <div className="grid grid-cols-2 gap-4 mb-4">
                  <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                    <p className="text-xs font-semibold text-blue-900 uppercase mb-1">
                      Tổng số Site
                    </p>
                    <p className="text-2xl font-bold text-blue-600">
                      {
                        new Set(
                          requestItems.flatMap((item) => item.allocations?.map((a) => a.siteId) || [])
                        ).size
                      }{' '}
                      Site
                    </p>
                  </div>
                  <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                    <p className="text-xs font-semibold text-green-900 uppercase mb-1">
                      Tiến độ hoàn thành
                    </p>
                    <p className="text-2xl font-bold text-green-600">{progressPercentage}%</p>
                  </div>
                </div>

                {unallocatedItems > 0 && (
                  <div className="bg-yellow-50 border-2 border-yellow-300 rounded-lg p-4 flex items-start gap-3">
                    <AlertCircle className="w-5 h-5 text-yellow-600 mt-0.5 flex-shrink-0" />
                    <div>
                      <p className="text-sm font-bold text-yellow-900 mb-1">Cảnh báo</p>
                      <p className="text-sm text-yellow-800">
                        Một số mặt hàng chưa được phân bổ đủ. Đơn hàng vẫn sẽ được tạo nhưng có thể
                        không đáp ứng đầy đủ yêu cầu.
                      </p>
                    </div>
                  </div>
                )}
              </div>
            </div>

            {/* Modal Footer */}
            <div className="bg-gray-50 border-t border-gray-200 p-6 flex justify-end gap-3">
              <button
                onClick={() => setShowConfirmModal(false)}
                className="px-6 py-3 border-2 border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors font-semibold"
              >
                Quay lại kiểm tra
              </button>
              <button
                onClick={handleSendOrders}
                className="px-6 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg hover:from-green-700 hover:to-green-800 transition-all font-bold shadow-md flex items-center gap-2"
              >
                <CheckCircle className="w-5 h-5" />
                Xác nhận & Gửi đơn hàng
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
