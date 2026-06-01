import { Link } from 'react-router';
import { FileText, TrendingUp, Clock, Globe } from 'lucide-react';

interface MetricCard {
  title: string;
  value: string;
  icon: typeof FileText;
  color: string;
  bgColor: string;
}

interface RecentOrder {
  id: string;
  code: string;
  date: string;
  site: string;
  status: 'pending' | 'allocated';
}

const metrics: MetricCard[] = [
  {
    title: 'Tổng Yêu cầu Nhận',
    value: '156',
    icon: FileText,
    color: 'text-indigo-600',
    bgColor: 'bg-indigo-50',
  },
  {
    title: 'Chờ Phân bổ',
    value: '24',
    icon: Clock,
    color: 'text-orange-600',
    bgColor: 'bg-orange-50',
  },
  {
    title: 'Đã Phân bổ Tháng này',
    value: '132',
    icon: TrendingUp,
    color: 'text-emerald-600',
    bgColor: 'bg-emerald-50',
  },
  {
    title: 'Sites Đang Hợp tác',
    value: '18',
    icon: Globe,
    color: 'text-blue-600',
    bgColor: 'bg-blue-50',
  },
];

const recentOrders: RecentOrder[] = [
  { id: '1', code: 'REQ-2024-004', date: '2024-05-10', site: 'Chưa phân bổ', status: 'pending' },
  { id: '2', code: 'REQ-2024-002', date: '2024-05-09', site: 'Amazon US', status: 'allocated' },
  { id: '3', code: 'REQ-2024-001', date: '2024-05-08', site: 'Alibaba CN', status: 'allocated' },
];

function getStatusColor(status: RecentOrder['status']) {
  const colors = {
    pending: 'bg-orange-100 text-orange-800',
    allocated: 'bg-emerald-100 text-emerald-800',
  };
  return colors[status];
}

function getStatusLabel(status: RecentOrder['status']) {
  const labels = {
    pending: 'Chờ phân bổ',
    allocated: 'Đã phân bổ',
  };
  return labels[status];
}

export function OverseasDashboard() {
  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-semibold text-[var(--text-primary)] mb-2">
          Trang Chủ - Bộ phận Đặt hàng Quốc tế
        </h1>
        <p className="text-[var(--text-secondary)]">
          Tổng quan về hoạt động xử lý yêu cầu và phân bổ đơn hàng
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
            to="/overseas/process"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-indigo-500 hover:bg-indigo-50 transition-all group"
          >
            <div className="p-2 bg-indigo-600 text-white rounded-lg group-hover:scale-110 transition-transform">
              <FileText className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Xử lý Yêu cầu Đang chờ</p>
              <p className="text-sm text-[var(--text-secondary)]">Phân bổ đơn hàng ngay</p>
            </div>
          </Link>

          <Link
            to="/overseas/process"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-indigo-500 hover:bg-indigo-50 transition-all group"
          >
            <div className="p-2 bg-emerald-600 text-white rounded-lg group-hover:scale-110 transition-transform">
              <Globe className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Xem Danh sách Site</p>
              <p className="text-sm text-[var(--text-secondary)]">Quản lý nhà cung cấp (Sắp ra mắt)</p>
            </div>
          </Link>
        </div>
      </div>

      {/* Recent Orders */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-[var(--text-primary)]">
            Yêu cầu Gần đây
          </h2>
          <Link
            to="/overseas/process"
            className="text-sm text-indigo-600 hover:underline font-medium"
          >
            Xem tất cả →
          </Link>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-[var(--muted)]">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Mã yêu cầu
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Ngày nhận
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Site phân bổ
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
              {recentOrders.map((order) => (
                <tr key={order.id} className="hover:bg-[var(--muted)] transition-colors">
                  <td className="px-4 py-3">
                    <Link
                      to={`/overseas/process/${order.id}`}
                      className="text-sm font-medium text-indigo-600 hover:underline"
                    >
                      {order.code}
                    </Link>
                  </td>
                  <td className="px-4 py-3 text-sm text-[var(--text-secondary)]">
                    {order.date}
                  </td>
                  <td className="px-4 py-3 text-sm text-[var(--text-primary)]">
                    {order.site}
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
                      to={`/overseas/process/${order.id}`}
                      className="text-sm font-medium text-indigo-600 hover:underline"
                    >
                      Xem chi tiết
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
