import { useNavigate, useLocation, Link } from 'react-router';
import { useState, useEffect } from 'react';
import { ArrowLeft, Package, MapPin, CheckCircle2, AlertCircle, CheckCircle } from 'lucide-react';

interface ReplacementItem {
  itemCode: string;
  itemName: string;
  siteCode: string;
  siteName: string;
  quantity: number;
  unit: string;
  deliveryMethod: string;
  estimatedDate: string;
  unitPrice: number;
}

export function ConfirmReplacementOrder() {
  const navigate = useNavigate();
  const location = useLocation();
  const [replacementItems, setReplacementItems] = useState<ReplacementItem[]>([]);
  const [orderData, setOrderData] = useState<any>(null);

  useEffect(() => {
    const { order, allocatedItems } = location.state || {};

    if (!order || !allocatedItems) {
      navigate('/overseas/import-orders');
      return;
    }

    setOrderData(order);

    const items: ReplacementItem[] = [];
    order.items.forEach((item: any) => {
      const allocations = allocatedItems[item.id] || [];
      allocations.forEach((allocation: any) => {
        items.push({
          itemCode: item.code,
          itemName: item.name,
          siteCode: allocation.siteCode,
          siteName: allocation.siteName,
          quantity: allocation.quantity,
          unit: item.unit,
          deliveryMethod: allocation.deliveryMethod,
          estimatedDate: allocation.estimatedDate,
          unitPrice: allocation.unitPrice || 0,
        });
      });
    });

    setReplacementItems(items);
  }, [location.state, navigate]);

  const handleConfirm = () => {
    const uniqueSites = [...new Set(replacementItems.map((item) => item.siteCode))];
    navigate('/overseas/import-orders/success', {
      state: {
        newOrderCode: 'DHM001',
        originalOrderCode: orderData?.code,
        sites: uniqueSites,
        processingDate: orderData?.processingDate || new Date().toLocaleDateString('vi-VN'),
      },
    });
  };

  if (!orderData) {
    return null;
  }

  const uniqueSites = [...new Set(replacementItems.map((item) => item.siteCode))];
  const totalItems = orderData?.items?.length || 0;
  const totalValue = replacementItems.reduce((sum, item) => sum + item.quantity * item.unitPrice, 0);

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(value);
  };

  // Group items by site
  const itemsBySite = replacementItems.reduce((acc, item) => {
    if (!acc[item.siteCode]) {
      acc[item.siteCode] = {
        siteName: item.siteName,
        items: [],
      };
    }
    acc[item.siteCode].items.push(item);
    return acc;
  }, {} as Record<string, { siteName: string; items: ReplacementItem[] }>);

  return (
    <div className="p-8 space-y-6 bg-[var(--background)]">
      {/* Header */}
      <div className="flex items-center gap-4">
        <button
          onClick={() => navigate(-1)}
          className="p-2 hover:bg-white rounded-lg transition-colors border border-[#E5E7EB]"
        >
          <ArrowLeft className="w-5 h-5 text-[#6B7280]" />
        </button>
        <div className="flex-1">
          <h1 className="text-3xl font-semibold text-[#111827] mb-1">
            Xác nhận đơn hàng thay thế
          </h1>
          <p className="text-[#6B7280]">Kiểm tra thông tin trước khi gửi đơn hàng thay thế</p>
        </div>
      </div>

      {/* Gradient Header Card */}
      <div className="bg-gradient-to-r from-green-600 to-green-700 rounded-2xl shadow-lg p-8 text-white">
        <div className="flex items-center gap-3 mb-6">
          <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
            <CheckCircle2 className="w-8 h-8 text-green-600" strokeWidth={2.5} />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-white drop-shadow-md">Sẵn sàng gửi đơn hàng thay thế</h2>
            <p className="text-white font-medium">Đơn hàng gốc: #{orderData.code}</p>
          </div>
        </div>

        <div className="grid grid-cols-4 gap-4">
          <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
            <p className="text-green-700 text-xs font-bold uppercase tracking-wider mb-1">Tổng mặt hàng</p>
            <p className="text-2xl font-bold text-green-900">{totalItems}</p>
          </div>
          <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
            <p className="text-green-700 text-xs font-bold uppercase tracking-wider mb-1">Tổng sites</p>
            <p className="text-2xl font-bold text-green-900">{uniqueSites.length}</p>
          </div>
          <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
            <p className="text-green-700 text-xs font-bold uppercase tracking-wider mb-1">Người đặt</p>
            <p className="text-xl font-bold text-green-900 truncate">{orderData.orderer}</p>
          </div>
          <div className="bg-white bg-opacity-90 rounded-lg p-4 backdrop-blur-sm border-2 border-white shadow-lg">
            <p className="text-green-700 text-xs font-bold uppercase tracking-wider mb-1">Ngày xử lý</p>
            <p className="text-xl font-bold text-green-900">{orderData.processingDate}</p>
          </div>
        </div>
      </div>

      {/* Replacement Items by Site */}
      <div className="space-y-6">
        {Object.entries(itemsBySite).map(([siteCode, siteData]) => {
          const siteTotal = siteData.items.reduce((sum, item) => sum + item.quantity * item.unitPrice, 0);

          return (
            <div key={siteCode} className="bg-white rounded-xl shadow-sm border border-[#E5E7EB] overflow-hidden">
              {/* Site Header */}
              <div className="bg-gradient-to-r from-blue-500 to-blue-600 px-6 py-4 text-white">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-12 h-12 bg-white rounded-xl flex items-center justify-center shadow-lg">
                      <MapPin className="w-6 h-6 text-blue-600" strokeWidth={2.5} />
                    </div>
                    <div>
                      <h3 className="text-xl font-bold drop-shadow-md">{siteCode}</h3>
                      <p className="text-white text-sm font-medium">{siteData.siteName}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-white text-xs font-semibold mb-1">Tổng giá trị đơn hàng</p>
                    <p className="text-2xl font-bold text-white drop-shadow-md">{formatCurrency(siteTotal)}</p>
                  </div>
                </div>
              </div>

              {/* Site Items */}
              <div className="p-6">
                <h4 className="text-sm font-semibold text-[#6B7280] uppercase tracking-wider mb-4">
                  Danh sách mặt hàng ({siteData.items.length})
                </h4>
                <div className="space-y-3">
                  {siteData.items.map((item, index) => (
                    <div
                      key={index}
                      className="border border-[#E5E7EB] rounded-lg p-4 hover:border-blue-300 hover:bg-blue-50 transition-all"
                    >
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <div className="flex items-center gap-3 mb-3">
                            <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-lg flex items-center justify-center">
                              <Package className="w-5 h-5 text-white" strokeWidth={2.5} />
                            </div>
                            <div className="flex-1">
                              <h5 className="font-semibold text-[#111827]">
                                {item.itemCode} - {item.itemName}
                              </h5>
                              <div className="flex items-center gap-4 mt-1">
                                <span className="text-sm text-[#6B7280]">
                                  Số lượng:{' '}
                                  <span className="font-bold text-[#2563EB]">
                                    {item.quantity} {item.unit}
                                  </span>
                                </span>
                                <span className="text-sm text-[#6B7280]">
                                  Đơn giá:{' '}
                                  <span className="font-bold text-green-600">
                                    {formatCurrency(item.unitPrice)}
                                  </span>
                                </span>
                              </div>
                            </div>
                          </div>

                          <div className="ml-13 grid grid-cols-3 gap-4">
                            <div>
                              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                                Phương tiện
                              </p>
                              <p className="text-sm font-semibold text-[#111827]">{item.deliveryMethod}</p>
                            </div>

                            <div>
                              <p className="text-xs font-semibold text-[#6B7280] uppercase tracking-wider mb-1">
                                Ngày giao dự kiến
                              </p>
                              <p className="text-sm font-semibold text-[#111827]">{item.estimatedDate}</p>
                            </div>

                            <div className="bg-green-50 border border-green-200 rounded-lg p-2">
                              <p className="text-xs font-semibold text-green-800 uppercase tracking-wider mb-1">
                                Thành tiền
                              </p>
                              <p className="text-sm font-bold text-green-900">
                                {formatCurrency(item.quantity * item.unitPrice)}
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Total Value Card */}
      <div className="bg-gradient-to-r from-green-600 to-green-700 rounded-xl p-6 shadow-lg border-2 border-green-500">
        <div className="flex items-center justify-between text-white">
          <div className="flex items-center gap-3">
            <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
              <CheckCircle className="w-8 h-8 text-green-600" strokeWidth={2.5} />
            </div>
            <div>
              <p className="text-white text-sm font-semibold mb-1">Tổng giá trị toàn bộ đơn hàng</p>
              <p className="text-xs text-white text-opacity-90">{uniqueSites.length} site • {replacementItems.length} mặt hàng</p>
            </div>
          </div>
          <div className="text-right">
            <p className="text-4xl font-bold text-white drop-shadow-md">{formatCurrency(totalValue)}</p>
          </div>
        </div>
      </div>

      {/* Summary Card */}
      <div className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-6 border-2 border-blue-200">
        <div className="flex items-center gap-3 mb-4">
          <AlertCircle className="w-6 h-6 text-blue-600" />
          <h3 className="text-lg font-semibold text-[#111827]">Lưu ý quan trọng</h3>
        </div>
        <ul className="space-y-2 text-sm text-[#6B7280]">
          <li>• Đơn hàng thay thế sẽ được gửi đến {uniqueSites.length} site</li>
          <li>• Thông tin chi tiết sẽ được gửi qua email đến các site liên quan</li>
          <li>• Bạn có thể theo dõi tiến độ xử lý sau khi xác nhận</li>
        </ul>
      </div>

      {/* Actions */}
      <div className="flex justify-end gap-3">
        <button
          onClick={() => navigate(-1)}
          className="px-6 py-3 border border-[#E5E7EB] rounded-lg text-[#111827] font-medium hover:bg-[#F3F4F6] transition-colors"
        >
          Quay lại
        </button>
        <button
          onClick={handleConfirm}
          className="px-8 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg font-medium hover:from-green-700 hover:to-green-800 transition-all shadow-md hover:shadow-lg"
        >
          Xác nhận và Gửi đơn hàng
        </button>
      </div>
    </div>
  );
}
