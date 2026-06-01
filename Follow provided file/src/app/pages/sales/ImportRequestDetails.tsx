import { useParams, Link } from 'react-router';
import { ArrowLeft, Calendar, Package, XCircle, CreditCard, X, Eye } from 'lucide-react';
import { useState } from 'react';

interface RequestItem {
  code: string;
  name: string;
  quantity: number;
  unit: string;
  deliveryDate: string;
}

interface RejectedItem {
  code: string;
  name: string;
  quantity: number;
  unit: string;
  rejectedBy: 'overseas' | 'user';
  reason: string;
  rejectedDate: string;
}

interface OrderItem {
  code: string;
  name: string;
  quantity: number;
  unit: string;
  unitPrice: number;
  totalPrice: number;
}

interface Order {
  code: string;
  orderDate: string;
  site: string;
  itemCount: number;
  status: 'pending' | 'processing' | 'completed';
  totalValue: number;
  items: OrderItem[];
}

const mockRequestData = {
  code: 'REQ-2024-001',
  createdDate: '2024-05-08',
  status: 'processing' as const,
  itemCount: 3,
  items: [
    { code: 'MH001', name: 'Laptop Dell XPS 13', quantity: 2, unit: 'cái', deliveryDate: '2024-05-15' },
    { code: 'MH002', name: 'Bàn phím cơ Keychron K2', quantity: 3, unit: 'cái', deliveryDate: '2024-05-16' },
    { code: 'MH003', name: 'Chuột Logitech MX Master 3', quantity: 1, unit: 'cái', deliveryDate: '2024-05-15' },
  ] as RequestItem[],
  orders: [
    {
      code: 'ORD-2024-001',
      orderDate: '2024-05-08',
      site: 'Site A - Hanoi',
      itemCount: 2,
      status: 'processing' as const,
      totalValue: 52700000,
      items: [
        {
          code: 'MH001',
          name: 'Laptop Dell XPS 13',
          quantity: 2,
          unit: 'cái',
          unitPrice: 25000000,
          totalPrice: 50000000,
        },
        {
          code: 'MH002',
          name: 'Bàn phím cơ Keychron K2',
          quantity: 1,
          unit: 'cái',
          unitPrice: 2700000,
          totalPrice: 2700000,
        },
      ],
    },
    {
      code: 'ORD-2024-002',
      orderDate: '2024-05-08',
      site: 'Site B - HCMC',
      itemCount: 2,
      status: 'pending' as const,
      totalValue: 9400000,
      items: [
        {
          code: 'MH002',
          name: 'Bàn phím cơ Keychron K2',
          quantity: 2,
          unit: 'cái',
          unitPrice: 2700000,
          totalPrice: 5400000,
        },
        {
          code: 'MH003',
          name: 'Chuột Logitech MX Master 3',
          quantity: 2,
          unit: 'cái',
          unitPrice: 2000000,
          totalPrice: 4000000,
        },
      ],
    },
  ] as Order[],
  rejectedItems: [
    {
      code: 'MH004',
      name: 'Màn hình LG UltraWide 34"',
      quantity: 2,
      unit: 'cái',
      rejectedBy: 'overseas' as const,
      reason: 'Không tìm thấy Site có đủ tồn kho',
      rejectedDate: '2024-05-09',
    },
    {
      code: 'MH005',
      name: 'Webcam Logitech C920',
      quantity: 5,
      unit: 'cái',
      rejectedBy: 'user' as const,
      reason: 'Đã đặt hàng từ nguồn khác',
      rejectedDate: '2024-05-08',
    },
  ] as RejectedItem[],
};

function getStatusColor(status: string) {
  const colors = {
    draft: 'bg-gray-100 text-gray-800',
    pending: 'bg-yellow-100 text-yellow-800',
    processing: 'bg-blue-100 text-blue-800',
    completed: 'bg-green-100 text-green-800',
    cancelled: 'bg-red-100 text-red-800',
  };
  return colors[status as keyof typeof colors] || colors.draft;
}

function getStatusLabel(status: string) {
  const labels = {
    draft: 'Bản nháp',
    pending: 'Chờ xử lý',
    processing: 'Đang xử lý',
    completed: 'Hoàn thành',
    cancelled: 'Đã hủy',
  };
  return labels[status as keyof typeof labels] || status;
}

function getOrderStatusColor(status: 'pending' | 'processing' | 'completed' | 'cancelled') {
  const colors = {
    pending: 'bg-yellow-100 text-yellow-800',
    processing: 'bg-blue-100 text-blue-800',
    completed: 'bg-green-100 text-green-800',
    cancelled: 'bg-red-100 text-red-800',
  };
  return colors[status];
}

function getOrderStatusLabel(status: 'pending' | 'processing' | 'completed' | 'cancelled') {
  const labels = {
    pending: 'Chờ xử lý',
    processing: 'Đang xử lý',
    completed: 'Hoàn thành',
    cancelled: 'Đã hủy',
  };
  return labels[status];
}

export function ImportRequestDetails() {
  const { id } = useParams();
  const [request, setRequest] = useState(mockRequestData);
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [showCancelOrderModal, setShowCancelOrderModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [paymentStep, setPaymentStep] = useState<'method' | 'details' | 'confirm'>('method');
  const [paymentMethod, setPaymentMethod] = useState<'card' | 'transfer' | ''>('');
  const [cancelOrderReason, setCancelOrderReason] = useState('');

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(value);
  };

  const handleCancelOrder = () => {
    if (!cancelOrderReason.trim()) {
      alert('Vui lòng nhập lý do hủy đơn hàng!');
      return;
    }

    if (selectedOrder) {
      // Remove order from list
      setRequest((prev) => ({
        ...prev,
        orders: prev.orders.filter((order) => order.code !== selectedOrder.code),
      }));

      alert(`Đã hủy đơn hàng ${selectedOrder.code} thành công!`);
      setShowCancelOrderModal(false);
      setSelectedOrder(null);
      setCancelOrderReason('');
    }
  };

  const handlePayOrder = () => {
    alert(`Đã xác nhận thanh toán đơn hàng ${selectedOrder?.code} bằng ${paymentMethod === 'card' ? 'thẻ' : 'chuyển khoản'}!`);
    setShowPaymentModal(false);
    setSelectedOrder(null);
    setPaymentStep('method');
    setPaymentMethod('');
  };

  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Link
          to="/sales/requests"
          className="p-2 hover:bg-[var(--muted)] rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
        </Link>
        <div className="flex-1">
          <h1 className="text-3xl font-semibold text-[var(--text-primary)]">
            Chi tiết Yêu cầu Nhập hàng
          </h1>
          <p className="text-[var(--text-secondary)]">
            Thông tin chi tiết về yêu cầu và các đơn hàng liên quan
          </p>
        </div>
      </div>

      {/* Request Info Card */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <h2 className="text-xl font-semibold text-[var(--text-primary)] mb-4">
          Thông tin Yêu cầu
        </h2>

        <div className="grid grid-cols-4 gap-6">
          <div>
            <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
              Mã yêu cầu
            </p>
            <p className="text-base font-semibold text-[var(--text-primary)]">
              {request.code}
            </p>
          </div>

          <div>
            <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
              Ngày tạo
            </p>
            <p className="text-base font-semibold text-[var(--text-primary)]">
              {request.createdDate}
            </p>
          </div>

          <div>
            <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
              Trạng thái
            </p>
            <span
              className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
                request.status
              )}`}
            >
              {getStatusLabel(request.status)}
            </span>
          </div>

          <div>
            <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
              Số mặt hàng
            </p>
            <p className="text-base font-semibold text-[var(--text-primary)]">
              {request.itemCount}
            </p>
          </div>
        </div>
      </div>

      {/* Request Items Table */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <h2 className="text-xl font-semibold text-[var(--text-primary)] mb-4">
          Danh sách Mặt hàng
        </h2>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-[var(--muted)]">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Mã hàng</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">
                  Tên mặt hàng
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Số lượng</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Đơn vị</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">
                  Ngày nhận mong muốn
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {request.items.map((item) => (
                <tr key={item.code} className="hover:bg-[var(--muted)] transition-colors">
                  <td className="px-4 py-3 text-sm font-medium">{item.code}</td>
                  <td className="px-4 py-3 text-sm">{item.name}</td>
                  <td className="px-4 py-3 text-sm">{item.quantity}</td>
                  <td className="px-4 py-3 text-sm">{item.unit}</td>
                  <td className="px-4 py-3 text-sm">{item.deliveryDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Rejected/Cancelled Items */}
      {request.rejectedItems.length > 0 && (
        <div className="bg-white rounded-lg shadow-sm border border-red-200 p-6">
          <h2 className="text-xl font-semibold text-red-700 mb-4">
            Mặt hàng Bị từ chối / Hủy
          </h2>

          <div className="space-y-3">
            {request.rejectedItems.map((item, index) => (
              <div
                key={index}
                className="border border-red-200 rounded-lg p-4 bg-red-50"
              >
                <div className="flex items-start justify-between mb-2">
                  <div>
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-semibold text-gray-900">{item.name}</span>
                      <span className="px-2 py-0.5 bg-gray-200 text-gray-700 text-xs font-medium rounded">
                        {item.code}
                      </span>
                    </div>
                    <p className="text-sm text-gray-600">
                      Số lượng: {item.quantity} {item.unit}
                    </p>
                  </div>
                  <div className="text-right">
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        item.rejectedBy === 'overseas'
                          ? 'bg-orange-100 text-orange-800'
                          : 'bg-gray-100 text-gray-800'
                      }`}
                    >
                      {item.rejectedBy === 'overseas'
                        ? 'Từ chối bởi Đặt hàng Quốc tế'
                        : 'Hủy bởi người dùng'}
                    </span>
                    <p className="text-xs text-gray-500 mt-1">{item.rejectedDate}</p>
                  </div>
                </div>
                <div className="mt-3 pt-3 border-t border-red-200">
                  <p className="text-sm font-medium text-gray-700">Lý do:</p>
                  <p className="text-sm text-gray-600 mt-1">{item.reason}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Related Orders */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-center gap-3 mb-4">
          <h2 className="text-xl font-semibold text-[var(--text-primary)]">Đơn hàng liên quan</h2>
          <span className="text-sm text-[var(--text-secondary)]">
            ({request.orders.length} đơn)
          </span>
        </div>

        {request.orders.length === 0 ? (
          <div className="text-center py-12 text-[var(--text-secondary)] italic">
            Chưa có đơn hàng nào được tạo từ yêu cầu này
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-[var(--muted)]">
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase">
                    Mã đơn hàng
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Ngày tạo</th>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase">
                    Site được phân
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase">
                    Số mặt hàng
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase">
                    Trạng thái
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-semibold uppercase">Thao tác</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border)]">
                {request.orders.map((order) => (
                  <tr key={order.code} className="hover:bg-[var(--muted)] transition-colors">
                    <td className="px-4 py-3 text-sm font-medium">{order.code}</td>
                    <td className="px-4 py-3 text-sm">{order.orderDate}</td>
                    <td className="px-4 py-3 text-sm">{order.site}</td>
                    <td className="px-4 py-3 text-sm">{order.itemCount}</td>
                    <td className="px-4 py-3">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getOrderStatusColor(
                          order.status
                        )}`}
                      >
                        {getOrderStatusLabel(order.status)}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          onClick={() => setSelectedOrder(order)}
                          className="p-2 text-[#6B7280] hover:text-[#2563EB] hover:bg-blue-50 rounded-lg transition-colors"
                          title="Xem chi tiết"
                        >
                          <Eye className="w-5 h-5" />
                        </button>
                        <button
                          onClick={() => {
                            setSelectedOrder(order);
                            setShowCancelOrderModal(true);
                          }}
                          className="p-2 text-[#6B7280] hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                          title="Hủy đơn hàng"
                        >
                          <XCircle className="w-5 h-5" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Order Details Modal */}
      {selectedOrder && (
        <div
          className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50"
          onClick={() => setSelectedOrder(null)}
        >
          <div
            className="bg-white rounded-lg shadow-xl max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="p-6 space-y-6">
              {/* Modal Header */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-2xl font-semibold text-[var(--text-primary)]">
                    Chi tiết Đơn hàng
                  </h3>
                  <p className="text-[var(--text-secondary)]">
                    Thông tin chi tiết về đơn hàng và mặt hàng
                  </p>
                </div>
                <button
                  onClick={() => setSelectedOrder(null)}
                  className="text-[var(--text-secondary)] hover:text-[var(--text-primary)]"
                >
                  ✕
                </button>
              </div>

              <div className="h-px bg-[var(--border)]" />

              {/* Order Info */}
              <div className="space-y-4">
                <h4 className="font-semibold text-lg">Thông tin Đơn hàng</h4>

                <div className="grid grid-cols-3 gap-6">
                  <div>
                    <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
                      Mã đơn hàng
                    </p>
                    <p className="text-base font-semibold">{selectedOrder.code}</p>
                  </div>

                  <div>
                    <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
                      Ngày tạo
                    </p>
                    <p className="text-base font-semibold">{selectedOrder.orderDate}</p>
                  </div>

                  <div>
                    <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
                      Trạng thái
                    </p>
                    <span
                      className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getOrderStatusColor(
                        selectedOrder.status
                      )}`}
                    >
                      {getOrderStatusLabel(selectedOrder.status)}
                    </span>
                  </div>

                  <div>
                    <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
                      Mã yêu cầu gốc
                    </p>
                    <p className="text-base font-semibold">{request.code}</p>
                  </div>

                  <div>
                    <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
                      Site được phân
                    </p>
                    <p className="text-base font-semibold">{selectedOrder.site}</p>
                  </div>

                  <div>
                    <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase mb-1">
                      Tổng giá trị
                    </p>
                    <p className="text-base font-semibold text-[var(--primary)]">
                      {formatCurrency(selectedOrder.totalValue)}
                    </p>
                  </div>
                </div>
              </div>

              <div className="h-px bg-[var(--border)]" />

              {/* Order Items */}
              <div className="space-y-4">
                <h4 className="font-semibold text-lg">Danh sách Mặt hàng</h4>

                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-[var(--muted)]">
                      <tr>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                          Mã hàng
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                          Tên mặt hàng
                        </th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-[var(--text-secondary)] uppercase">
                          Số lượng
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                          Đơn vị
                        </th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-[var(--text-secondary)] uppercase">
                          Đơn giá
                        </th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-[var(--text-secondary)] uppercase">
                          Thành tiền
                        </th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-[var(--border)]">
                      {selectedOrder.items.map((item) => (
                        <tr key={item.code} className="hover:bg-[var(--muted)] transition-colors">
                          <td className="px-4 py-3 text-sm font-medium">{item.code}</td>
                          <td className="px-4 py-3 text-sm">{item.name}</td>
                          <td className="px-4 py-3 text-sm text-right font-medium">
                            {item.quantity}
                          </td>
                          <td className="px-4 py-3 text-sm">{item.unit}</td>
                          <td className="px-4 py-3 text-sm text-right">
                            {formatCurrency(item.unitPrice)}
                          </td>
                          <td className="px-4 py-3 text-sm text-right font-semibold text-[var(--primary)]">
                            {formatCurrency(item.totalPrice)}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                    <tfoot className="bg-gray-50 border-t-2 border-[var(--border)]">
                      <tr>
                        <td colSpan={5} className="px-4 py-3 text-right font-semibold">
                          Tổng cộng:
                        </td>
                        <td className="px-4 py-3 text-right font-bold text-lg text-[var(--primary)]">
                          {formatCurrency(selectedOrder.totalValue)}
                        </td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>

              {/* Footer */}
              <div className="flex justify-end gap-3 pt-4">
                <button
                  onClick={() => setSelectedOrder(null)}
                  className="px-6 py-2.5 border border-[var(--border)] rounded-lg hover:bg-[var(--muted)] transition-colors font-medium"
                >
                  Đóng
                </button>
                {(selectedOrder.status === 'pending' || selectedOrder.status === 'processing') && (
                  <>
                    {selectedOrder.status === 'pending' && (
                      <button
                        onClick={() => {
                          setSelectedOrder(null);
                          setShowCancelOrderModal(true);
                        }}
                        className="flex items-center gap-2 px-6 py-2.5 bg-red-600 text-white rounded-lg font-medium hover:bg-red-700 transition-colors"
                      >
                        <XCircle className="w-5 h-5" />
                        Hủy đơn hàng
                      </button>
                    )}
                    <button
                      onClick={() => {
                        setSelectedOrder(null);
                        setPaymentStep('method');
                        setPaymentMethod('');
                        setShowPaymentModal(true);
                      }}
                      className="flex items-center gap-2 px-6 py-2.5 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition-colors"
                    >
                      <CreditCard className="w-5 h-5" />
                      Thanh toán
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Cancel Order Modal */}
      {showCancelOrderModal && selectedOrder && (
        <div
          className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 p-4"
          onClick={() => {
            setShowCancelOrderModal(false);
            setSelectedOrder(null);
            setCancelOrderReason('');
          }}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl max-w-lg w-full mx-4 overflow-hidden"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Header */}
            <div className="bg-gradient-to-r from-red-600 to-red-700 px-8 py-6 text-white">
              <div className="flex items-center gap-3">
                <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
                  <XCircle className="w-8 h-8 text-red-600" strokeWidth={2.5} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold drop-shadow-md">Hủy đơn hàng</h3>
                  <p className="text-white text-sm font-medium">Xác nhận hủy đơn hàng {selectedOrder.code}</p>
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="p-8 space-y-4">
              <div className="bg-red-50 border-l-4 border-red-500 rounded-lg p-4">
                <p className="text-sm text-red-800">
                  <strong>Cảnh báo:</strong> Hành động này sẽ hủy đơn hàng{' '}
                  <strong>{selectedOrder.code}</strong>. Đơn hàng sẽ không thể khôi phục sau khi hủy.
                </p>
              </div>

              <div className="grid grid-cols-2 gap-4 p-4 bg-gray-50 rounded-lg">
                <div>
                  <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-1">
                    Site
                  </p>
                  <p className="text-sm font-semibold text-gray-900">{selectedOrder.site}</p>
                </div>
                <div>
                  <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-1">
                    Giá trị
                  </p>
                  <p className="text-sm font-semibold text-green-600">
                    {formatCurrency(selectedOrder.totalValue)}
                  </p>
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  Lý do hủy đơn hàng <span className="text-red-600">*</span>
                </label>
                <textarea
                  value={cancelOrderReason}
                  onChange={(e) => setCancelOrderReason(e.target.value)}
                  placeholder="Nhập lý do hủy đơn hàng..."
                  rows={4}
                  className="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent resize-none"
                />
              </div>
            </div>

            {/* Footer */}
            <div className="border-t border-gray-200 px-8 py-6 bg-gray-50 flex justify-end gap-3">
              <button
                onClick={() => {
                  setShowCancelOrderModal(false);
                  setSelectedOrder(null);
                  setCancelOrderReason('');
                }}
                className="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-white transition-colors"
              >
                Quay lại
              </button>
              <button
                onClick={handleCancelOrder}
                className="px-8 py-3 bg-gradient-to-r from-red-600 to-red-700 text-white rounded-lg font-semibold hover:from-red-700 hover:to-red-800 transition-all shadow-md hover:shadow-lg"
              >
                Xác nhận hủy
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Payment Modal - Multi-step */}
      {showPaymentModal && selectedOrder && (
        <div
          className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 p-4"
          onClick={() => {
            setShowPaymentModal(false);
            setSelectedOrder(null);
            setPaymentStep('method');
            setPaymentMethod('');
          }}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl max-w-2xl w-full mx-4 overflow-hidden"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Header */}
            <div className="bg-gradient-to-r from-green-600 to-green-700 px-8 py-6 text-white">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-lg">
                    <CreditCard className="w-8 h-8 text-green-600" strokeWidth={2.5} />
                  </div>
                  <div>
                    <h3 className="text-2xl font-bold drop-shadow-md">Thanh toán đơn hàng</h3>
                    <p className="text-white text-sm font-medium">{selectedOrder.code}</p>
                  </div>
                </div>
                <button
                  onClick={() => {
                    setShowPaymentModal(false);
                    setSelectedOrder(null);
                    setPaymentStep('method');
                    setPaymentMethod('');
                  }}
                  className="text-white hover:bg-white hover:bg-opacity-20 rounded-lg p-2 transition-colors"
                >
                  <X className="w-6 h-6" />
                </button>
              </div>

              {/* Progress Steps */}
              <div className="flex items-center gap-2 mt-6">
                <div className="flex items-center gap-2 flex-1">
                  <div
                    className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
                      paymentStep === 'method'
                        ? 'bg-white text-green-600'
                        : 'bg-white bg-opacity-30 text-white'
                    }`}
                  >
                    1
                  </div>
                  <span className="text-white text-sm font-medium">Chọn phương thức</span>
                </div>
                <div className="flex items-center gap-2 flex-1">
                  <div
                    className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
                      paymentStep === 'confirm'
                        ? 'bg-white text-green-600'
                        : 'bg-white bg-opacity-30 text-white'
                    }`}
                  >
                    2
                  </div>
                  <span className="text-white text-sm font-medium">Xác nhận</span>
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="p-8">
              {/* Step 1: Select Payment Method */}
              {paymentStep === 'method' && (
                <div className="space-y-6">
                  <div className="p-4 bg-gradient-to-r from-green-50 to-emerald-50 border-2 border-green-200 rounded-xl">
                    <p className="text-xs font-semibold text-green-700 uppercase tracking-wider mb-2">
                      Tổng tiền thanh toán
                    </p>
                    <p className="text-3xl font-bold text-green-900">
                      {formatCurrency(selectedOrder.totalValue)}
                    </p>
                  </div>

                  <div>
                    <h4 className="text-lg font-semibold text-gray-900 mb-4">
                      Chọn phương thức thanh toán
                    </h4>
                    <div className="space-y-3">
                      {/* Card Payment */}
                      <button
                        onClick={() => setPaymentMethod('card')}
                        className={`w-full p-5 rounded-xl border-2 transition-all text-left ${
                          paymentMethod === 'card'
                            ? 'border-green-500 bg-green-50'
                            : 'border-gray-200 hover:border-green-300 bg-white'
                        }`}
                      >
                        <div className="flex items-center gap-4">
                          <div
                            className={`w-12 h-12 rounded-lg flex items-center justify-center ${
                              paymentMethod === 'card'
                                ? 'bg-green-500'
                                : 'bg-gray-100'
                            }`}
                          >
                            <CreditCard
                              className={`w-6 h-6 ${
                                paymentMethod === 'card' ? 'text-white' : 'text-gray-600'
                              }`}
                              strokeWidth={2.5}
                            />
                          </div>
                          <div className="flex-1">
                            <p className="font-semibold text-gray-900">Thanh toán bằng thẻ</p>
                            <p className="text-sm text-gray-600">
                              Visa, Mastercard, JCB, American Express
                            </p>
                          </div>
                          <div
                            className={`w-5 h-5 rounded-full border-2 flex items-center justify-center ${
                              paymentMethod === 'card'
                                ? 'border-green-500 bg-green-500'
                                : 'border-gray-300'
                            }`}
                          >
                            {paymentMethod === 'card' && (
                              <div className="w-2 h-2 bg-white rounded-full" />
                            )}
                          </div>
                        </div>
                      </button>

                      {/* Bank Transfer */}
                      <button
                        onClick={() => setPaymentMethod('transfer')}
                        className={`w-full p-5 rounded-xl border-2 transition-all text-left ${
                          paymentMethod === 'transfer'
                            ? 'border-green-500 bg-green-50'
                            : 'border-gray-200 hover:border-green-300 bg-white'
                        }`}
                      >
                        <div className="flex items-center gap-4">
                          <div
                            className={`w-12 h-12 rounded-lg flex items-center justify-center ${
                              paymentMethod === 'transfer'
                                ? 'bg-green-500'
                                : 'bg-gray-100'
                            }`}
                          >
                            <svg
                              className={`w-6 h-6 ${
                                paymentMethod === 'transfer' ? 'text-white' : 'text-gray-600'
                              }`}
                              fill="none"
                              stroke="currentColor"
                              viewBox="0 0 24 24"
                              strokeWidth={2.5}
                            >
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"
                              />
                            </svg>
                          </div>
                          <div className="flex-1">
                            <p className="font-semibold text-gray-900">Chuyển khoản ngân hàng</p>
                            <p className="text-sm text-gray-600">
                              Chuyển khoản qua Internet Banking hoặc Mobile Banking
                            </p>
                          </div>
                          <div
                            className={`w-5 h-5 rounded-full border-2 flex items-center justify-center ${
                              paymentMethod === 'transfer'
                                ? 'border-green-500 bg-green-500'
                                : 'border-gray-300'
                            }`}
                          >
                            {paymentMethod === 'transfer' && (
                              <div className="w-2 h-2 bg-white rounded-full" />
                            )}
                          </div>
                        </div>
                      </button>
                    </div>
                  </div>
                </div>
              )}

              {/* Step 2: Confirm Payment */}
              {paymentStep === 'confirm' && (
                <div className="space-y-6">
                  <div className="bg-green-50 border-l-4 border-green-500 rounded-lg p-4">
                    <p className="text-sm text-green-800">
                      <strong>Xác nhận thanh toán</strong> - Vui lòng kiểm tra kỹ thông tin trước khi xác nhận
                    </p>
                  </div>

                  <div className="space-y-4">
                    <div className="p-4 bg-gray-50 rounded-lg">
                      <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
                        Phương thức thanh toán
                      </p>
                      <p className="text-base font-semibold text-gray-900">
                        {paymentMethod === 'card' ? 'Thanh toán bằng thẻ' : 'Chuyển khoản ngân hàng'}
                      </p>
                    </div>

                    <div className="p-4 bg-gray-50 rounded-lg">
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-1">
                            Mã đơn hàng
                          </p>
                          <p className="text-sm font-semibold text-gray-900">{selectedOrder.code}</p>
                        </div>
                        <div>
                          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-1">
                            Site
                          </p>
                          <p className="text-sm font-semibold text-gray-900">{selectedOrder.site}</p>
                        </div>
                      </div>
                    </div>

                    <div className="p-6 bg-gradient-to-r from-green-50 to-emerald-50 border-2 border-green-200 rounded-xl">
                      <p className="text-xs font-semibold text-green-700 uppercase tracking-wider mb-2">
                        Tổng tiền thanh toán
                      </p>
                      <p className="text-3xl font-bold text-green-900">
                        {formatCurrency(selectedOrder.totalValue)}
                      </p>
                    </div>

                    {paymentMethod === 'transfer' && (
                      <div className="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                        <p className="text-sm font-semibold text-blue-900 mb-3">
                          Thông tin chuyển khoản:
                        </p>
                        <div className="space-y-2 text-sm text-blue-800">
                          <p><strong>Ngân hàng:</strong> Vietcombank Chi nhánh Hà Nội</p>
                          <p><strong>Số tài khoản:</strong> 0123456789</p>
                          <p><strong>Chủ tài khoản:</strong> CÔNG TY ABC</p>
                          <p><strong>Nội dung:</strong> {selectedOrder.code}</p>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              )}
            </div>

            {/* Footer */}
            <div className="border-t border-gray-200 px-8 py-6 bg-gray-50 flex justify-between">
              {paymentStep === 'method' ? (
                <>
                  <button
                    onClick={() => {
                      setShowPaymentModal(false);
                      setSelectedOrder(null);
                      setPaymentStep('method');
                      setPaymentMethod('');
                    }}
                    className="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-white transition-colors"
                  >
                    Hủy
                  </button>
                  <button
                    onClick={() => {
                      if (!paymentMethod) {
                        alert('Vui lòng chọn phương thức thanh toán!');
                        return;
                      }
                      setPaymentStep('confirm');
                    }}
                    disabled={!paymentMethod}
                    className="px-8 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg font-semibold hover:from-green-700 hover:to-green-800 transition-all shadow-md hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    Tiếp tục
                  </button>
                </>
              ) : (
                <>
                  <button
                    onClick={() => setPaymentStep('method')}
                    className="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-white transition-colors"
                  >
                    Quay lại
                  </button>
                  <button
                    onClick={handlePayOrder}
                    className="px-8 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg font-semibold hover:from-green-700 hover:to-green-800 transition-all shadow-md hover:shadow-lg"
                  >
                    Xác nhận thanh toán
                  </button>
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
