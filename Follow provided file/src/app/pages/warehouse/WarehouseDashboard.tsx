import { Link } from 'react-router';
import { PackageCheck, Clock, TrendingUp, AlertTriangle } from 'lucide-react';

interface MetricCard {
  title: string;
  value: string;
  icon: typeof PackageCheck;
  color: string;
  bgColor: string;
}

interface RecentInbound {
  id: string;
  orderCode: string;
  receivedDate: string;
  supplier: string;
  status: 'pending' | 'processing' | 'completed' | 'discrepancy';
}

const metrics: MetricCard[] = [
  {
    title: 'Đơn Chờ Nhập Kho',
    value: '12',
    icon: Clock,
    color: 'text-orange-600',
    bgColor: 'bg-orange-50',
  },
  {
    title: 'Đang Xử Lý',
    value: '5',
    icon: PackageCheck,
    color: 'text-blue-600',
    bgColor: 'bg-blue-50',
  },
  {
    title: 'Đã Nhập Tháng này',
    value: '87',
    icon: TrendingUp,
    color: 'text-emerald-600',
    bgColor: 'bg-emerald-50',
  },
  {
    title: 'Có Sai Lệch',
    value: '3',
    icon: AlertTriangle,
    color: 'text-red-600',
    bgColor: 'bg-red-50',
  },
];

const recentInbounds: RecentInbound[] = [
  { id: '1', orderCode: 'ORD-2024-015', receivedDate: '2024-05-10', supplier: 'Amazon US', status: 'pending' },
  { id: '2', orderCode: 'ORD-2024-014', receivedDate: '2024-05-10', supplier: 'Alibaba CN', status: 'processing' },
  { id: '3', orderCode: 'ORD-2024-013', receivedDate: '2024-05-09', supplier: 'eBay UK', status: 'completed' },
  { id: '4', orderCode: 'ORD-2024-012', receivedDate: '2024-05-09', supplier: 'Amazon US', status: 'discrepancy' },
];

function getStatusColor(status: RecentInbound['status']) {
  const colors = {
    pending: 'bg-orange-100 text-orange-800',
    processing: 'bg-blue-100 text-blue-800',
    completed: 'bg-emerald-100 text-emerald-800',
    discrepancy: 'bg-red-100 text-red-800',
  };
  return colors[status];
}

function getStatusLabel(status: RecentInbound['status']) {
  const labels = {
    pending: 'Chờ xử lý',
    processing: 'Đang xử lý',
    completed: 'Đã nhập kho',
    discrepancy: 'Có sai lệch',
  };
  return labels[status];
}

export function WarehouseDashboard() {
  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-semibold text-[var(--text-primary)] mb-2">
          Trang Chủ - Bộ phận Quản lý Kho
        </h1>
        <p className="text-[var(--text-secondary)]">
          Tổng quan về hoạt động nhập kho và xử lý hàng hóa
        </p>
      </div>

      {/* Metrics Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {metrics.map((metric) => {
          const Icon = metric.icon;
          return (
            <div
              key={metric.title}
              className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6"
            >
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-sm text-[var(--text-secondary)] mb-1">{metric.title}</p>
                  <p className="text-3xl font-semibold text-[var(--text-primary)]">
                    {metric.value}
                  </p>
                </div>
                <div className={`p-3 rounded-lg ${metric.bgColor}`}>
                  <Icon className={`w-6 h-6 ${metric.color}`} />
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Quick Actions */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <h2 className="text-xl font-semibold text-[var(--text-primary)] mb-4">
          Thao tác Nhanh
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Link
            to="/warehouse/inbound"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-emerald-500 hover:bg-emerald-50 transition-all group"
          >
            <div className="p-2 bg-emerald-600 text-white rounded-lg group-hover:scale-110 transition-transform">
              <PackageCheck className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Xử lý Đơn Nhập Kho</p>
              <p className="text-sm text-[var(--text-secondary)]">Kiểm tra và nhập hàng vào kho</p>
            </div>
          </Link>

          <Link
            to="/warehouse/inbound"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-red-500 hover:bg-red-50 transition-all group"
          >
            <div className="p-2 bg-red-600 text-white rounded-lg group-hover:scale-110 transition-transform">
              <AlertTriangle className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Xem Đơn Có Sai Lệch</p>
              <p className="text-sm text-[var(--text-secondary)]">Xử lý các trường hợp thiếu/sai hàng</p>
            </div>
          </Link>
        </div>
      </div>

      {/* Recent Inbound Orders */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-[var(--text-primary)]">
            Đơn Nhập Kho Gần đây
          </h2>
          <Link
            to="/warehouse/inbound"
            className="text-sm text-emerald-600 hover:underline font-medium"
          >
            Xem tất cả →
          </Link>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-[var(--muted)]">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Mã đơn hàng
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Ngày nhận
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Nhà cung cấp
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Trạng thái
                </th>
                <th className="px-4 py-3 text-right text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Thao tác
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {recentInbounds.map((order) => (
                <tr key={order.id} className="hover:bg-[var(--muted)] transition-colors">
                  <td className="px-4 py-3">
                    <Link
                      to={`/warehouse/inbound/${order.id}`}
                      className="text-sm font-medium text-emerald-600 hover:underline"
                    >
                      {order.orderCode}
                    </Link>
                  </td>
                  <td className="px-4 py-3 text-sm text-[var(--text-secondary)]">
                    {order.receivedDate}
                  </td>
                  <td className="px-4 py-3 text-sm text-[var(--text-primary)]">
                    {order.supplier}
                  </td>
                  <td className="px-4 py-3">
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                        order.status
                      )}`}
                    >
                      {getStatusLabel(order.status)}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-right">
                    <Link
                      to={`/warehouse/inbound/${order.id}`}
                      className="text-sm font-medium text-emerald-600 hover:underline"
                    >
                      Xử lý
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
