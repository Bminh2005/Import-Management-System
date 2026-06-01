import { Link } from 'react-router';
import { Search, Eye, Package, Clock, CheckCircle2, XCircle } from 'lucide-react';
import { useState } from 'react';

interface ImportOrder {
  id: string;
  code: string;
  orderer: string;
  itemCount: number;
  expectedDate: string;
  status: 'all' | 'pending' | 'processed' | 'cancelled';
}

const mockOrders: ImportOrder[] = [
  {
    id: '1',
    code: 'DH001',
    orderer: 'Nguyễn Văn A',
    itemCount: 3,
    expectedDate: '15/03/2025',
    status: 'cancelled',
  },
  {
    id: '2',
    code: 'DH002',
    orderer: 'Trần Thị B',
    itemCount: 1,
    expectedDate: '18/03/2025',
    status: 'cancelled',
  },
  {
    id: '3',
    code: 'DH003',
    orderer: 'Lê Văn C',
    itemCount: 5,
    expectedDate: '20/03/2025',
    status: 'pending',
  },
  {
    id: '4',
    code: 'DH004',
    orderer: 'Phạm Thị D',
    itemCount: 2,
    expectedDate: '22/03/2025',
    status: 'processed',
  },
  {
    id: '5',
    code: 'DH005',
    orderer: 'Hoàng Văn E',
    itemCount: 4,
    expectedDate: '25/03/2025',
    status: 'cancelled',
  },
];

function getStatusColor(status: ImportOrder['status']) {
  const colors = {
    all: 'bg-gray-100 text-gray-700',
    pending: 'bg-yellow-50 text-yellow-700 border border-yellow-200',
    processed: 'bg-green-50 text-green-700 border border-green-200',
    cancelled: 'bg-red-50 text-red-700 border border-red-200',
  };
  return colors[status];
}

function getStatusLabel(status: ImportOrder['status']) {
  const labels = {
    all: 'Tất cả',
    pending: 'Chưa xử lý',
    processed: 'Đã xử lý',
    cancelled: 'Bị hủy',
  };
  return labels[status];
}

export function ImportOrdersList() {
  const [selectedTab, setSelectedTab] = useState<ImportOrder['status']>('cancelled');
  const [searchQuery, setSearchQuery] = useState('');

  const stats = [
    {
      label: 'Tổng đơn hàng',
      value: mockOrders.length,
      icon: Package,
      gradient: 'from-blue-500 to-blue-600',
      bgColor: 'bg-blue-50',
      textColor: 'text-blue-700',
    },
    {
      label: 'Chưa xử lý',
      value: mockOrders.filter((o) => o.status === 'pending').length,
      icon: Clock,
      gradient: 'from-yellow-500 to-yellow-600',
      bgColor: 'bg-yellow-50',
      textColor: 'text-yellow-700',
    },
    {
      label: 'Đã xử lý',
      value: mockOrders.filter((o) => o.status === 'processed').length,
      icon: CheckCircle2,
      gradient: 'from-green-500 to-green-600',
      bgColor: 'bg-green-50',
      textColor: 'text-green-700',
    },
    {
      label: 'Bị hủy',
      value: mockOrders.filter((o) => o.status === 'cancelled').length,
      icon: XCircle,
      gradient: 'from-red-500 to-red-600',
      bgColor: 'bg-red-50',
      textColor: 'text-red-700',
    },
  ];

  const tabs: { value: ImportOrder['status']; label: string; count: number }[] = [
    { value: 'all', label: 'Tất cả', count: mockOrders.length },
    {
      value: 'pending',
      label: 'Chưa xử lý',
      count: mockOrders.filter((o) => o.status === 'pending').length,
    },
    {
      value: 'processed',
      label: 'Đã xử lý',
      count: mockOrders.filter((o) => o.status === 'processed').length,
    },
    {
      value: 'cancelled',
      label: 'Bị hủy',
      count: mockOrders.filter((o) => o.status === 'cancelled').length,
    },
  ];

  const filteredOrders =
    selectedTab === 'all'
      ? mockOrders
      : mockOrders.filter((order) => order.status === selectedTab);

  return (
    <div className="p-4 space-y-3 bg-[var(--background)]">
      {/* Header */}
      <div>
        <h1 className="text-lg font-semibold text-[var(--text-primary)] mb-0.5">
          Danh sách đơn hàng nhập khẩu
        </h1>
        <p className="text-xs text-[var(--text-secondary)]">Quản lý đơn hàng nhập khẩu và xử lý đơn bị hủy</p>
      </div>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3">
        {stats.map((stat) => (
          <div
            key={stat.label}
            className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-3 hover:shadow-md transition-all"
          >
            <div className="flex items-center justify-between">
              <div className="flex-1">
                <p className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide mb-1">
                  {stat.label}
                </p>
                <p className="text-xl font-bold text-[var(--text-primary)]">{stat.value}</p>
              </div>
              <div className={`w-10 h-10 rounded-lg bg-gradient-to-br ${stat.gradient} flex items-center justify-center`}>
                <stat.icon className="w-5 h-5 text-white" />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Search & Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-3 space-y-2.5">
        {/* Search */}
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--text-secondary)]" />
          <input
            type="text"
            placeholder="Tìm kiếm theo mã đơn, người đặt..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[var(--border)] rounded-md text-[var(--text-primary)] placeholder:text-[var(--text-secondary)] focus:outline-none focus:ring-1 focus:ring-[var(--primary)] focus:border-transparent"
          />
        </div>

        {/* Filter Tabs */}
        <div className="flex gap-2">
          {tabs.map((tab) => (
            <button
              key={tab.value}
              onClick={() => setSelectedTab(tab.value)}
              className={`px-4 py-1.5 rounded-md text-xs font-medium transition-all ${
                selectedTab === tab.value
                  ? 'bg-[var(--primary)] text-white shadow-sm'
                  : 'bg-[var(--muted)] text-[var(--text-secondary)] hover:bg-gray-200'
              }`}
            >
              {tab.label} ({tab.count})
            </button>
          ))}
        </div>
      </div>

      {/* Orders Table */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-[var(--border)]">
              <tr>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Mã đơn
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Người đặt
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Số lượng mặt hàng
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Ngày nhận hàng mong muốn
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Trạng thái
                  </span>
                </th>
                <th className="px-3 py-2 text-right">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Hành động
                  </span>
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filteredOrders.map((order) => (
                <tr key={order.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-3 py-2">
                    <span className="text-xs font-semibold text-[var(--text-primary)]">{order.code}</span>
                  </td>
                  <td className="px-3 py-2">
                    <span className="text-xs text-[var(--text-primary)]">{order.orderer}</span>
                  </td>
                  <td className="px-3 py-2">
                    <span className="text-xs text-[var(--text-primary)]">{order.itemCount} mặt hàng</span>
                  </td>
                  <td className="px-3 py-2">
                    <span className="text-xs text-[var(--text-primary)]">{order.expectedDate}</span>
                  </td>
                  <td className="px-3 py-2">
                    <span
                      className={`inline-flex items-center px-2 py-0.5 rounded-md text-[10px] font-medium ${getStatusColor(
                        order.status
                      )}`}
                    >
                      {getStatusLabel(order.status)}
                    </span>
                  </td>
                  <td className="px-3 py-2">
                    <div className="flex items-center justify-end gap-1.5">
                      <Link
                        to={`/overseas/import-orders/${order.id}`}
                        className="p-1.5 text-[var(--text-secondary)] hover:text-[var(--primary)] hover:bg-blue-50 rounded-md transition-colors"
                        title="Xem chi tiết"
                      >
                        <Eye className="w-4 h-4" />
                      </Link>
                      {order.status === 'cancelled' && (
                        <Link
                          to={`/overseas/import-orders/${order.id}`}
                          className="px-3 py-1.5 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-md text-xs font-medium hover:from-blue-700 hover:to-blue-800 transition-all shadow-sm hover:shadow"
                        >
                          Xử lý
                        </Link>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {filteredOrders.length === 0 && (
            <div className="text-center py-8 text-xs text-[var(--text-secondary)]">
              Không có đơn hàng nào trong danh mục này
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
