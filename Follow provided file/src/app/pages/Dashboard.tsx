import { Link } from 'react-router';
import { Package, FileText, TrendingUp, Clock } from 'lucide-react';

interface MetricCard {
  title: string;
  value: string;
  icon: typeof Package;
  color: string;
  bgColor: string;
}

interface RecentRequest {
  id: string;
  code: string;
  date: string;
  itemCount: number;
  status: 'pending' | 'processing' | 'completed';
}

const metrics: MetricCard[] = [
  {
    title: 'Tổng số Mặt hàng',
    value: '124',
    icon: Package,
    color: 'text-blue-600',
    bgColor: 'bg-blue-50',
  },
  {
    title: 'Yêu cầu Chờ xử lý',
    value: '8',
    icon: Clock,
    color: 'text-yellow-600',
    bgColor: 'bg-yellow-50',
  },
  {
    title: 'Yêu cầu Tháng này',
    value: '32',
    icon: TrendingUp,
    color: 'text-green-600',
    bgColor: 'bg-green-50',
  },
  {
    title: 'Đã Phê duyệt',
    value: '24',
    icon: FileText,
    color: 'text-purple-600',
    bgColor: 'bg-purple-50',
  },
];

const recentRequests: RecentRequest[] = [
  { id: '1', code: 'REQ-2024-001', date: '2024-05-08', itemCount: 3, status: 'pending' },
  { id: '2', code: 'REQ-2024-002', date: '2024-05-07', itemCount: 5, status: 'processing' },
  { id: '3', code: 'REQ-2024-003', date: '2024-05-06', itemCount: 2, status: 'completed' },
];

function getStatusColor(status: RecentRequest['status']) {
  const colors = {
    pending: 'bg-yellow-100 text-yellow-800',
    processing: 'bg-blue-100 text-blue-800',
    completed: 'bg-green-100 text-green-800',
  };
  return colors[status];
}

function getStatusLabel(status: RecentRequest['status']) {
  const labels = {
    pending: 'Chờ xử lý',
    processing: 'Đang xử lý',
    completed: 'Hoàn thành',
  };
  return labels[status];
}

export function Dashboard() {
  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-semibold text-[var(--text-primary)] mb-2">
          Trang Chủ - Bộ phận Bán hàng
        </h1>
        <p className="text-[var(--text-secondary)]">
          Tổng quan về hoạt động quản lý mặt hàng và yêu cầu nhập khẩu
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
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link
            to="/sales/requests"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-[var(--primary)] hover:bg-blue-50 transition-all group"
          >
            <div className="p-2 bg-[var(--primary)] text-white rounded-lg group-hover:scale-110 transition-transform">
              <FileText className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Tạo Yêu cầu Nhập hàng</p>
              <p className="text-sm text-[var(--text-secondary)]">Tạo yêu cầu mới</p>
            </div>
          </Link>

          <Link
            to="/sales/merchandise"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-[var(--primary)] hover:bg-blue-50 transition-all group"
          >
            <div className="p-2 bg-[var(--secondary)] text-white rounded-lg group-hover:scale-110 transition-transform">
              <Package className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Thêm Mặt hàng Mới</p>
              <p className="text-sm text-[var(--text-secondary)]">Quản lý mặt hàng</p>
            </div>
          </Link>

          <Link
            to="/sales/requests"
            className="flex items-center gap-3 p-4 border-2 border-[var(--border)] rounded-lg hover:border-[var(--primary)] hover:bg-blue-50 transition-all group"
          >
            <div className="p-2 bg-purple-600 text-white rounded-lg group-hover:scale-110 transition-transform">
              <FileText className="w-5 h-5" />
            </div>
            <div>
              <p className="font-medium text-[var(--text-primary)]">Xem Tất cả Yêu cầu</p>
              <p className="text-sm text-[var(--text-secondary)]">Danh sách đầy đủ</p>
            </div>
          </Link>
        </div>
      </div>

      {/* Recent Requests */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-[var(--text-primary)]">
            Yêu cầu Nhập hàng Gần đây
          </h2>
          <Link
            to="/sales/requests"
            className="text-sm text-[var(--primary)] hover:underline font-medium"
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
                  Ngày tạo
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase">
                  Số mặt hàng
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
              {recentRequests.map((request) => (
                <tr key={request.id} className="hover:bg-[var(--muted)] transition-colors">
                  <td className="px-4 py-3">
                    <Link
                      to={`/sales/requests/${request.id}`}
                      className="text-sm font-medium text-[var(--primary)] hover:underline"
                    >
                      {request.code}
                    </Link>
                  </td>
                  <td className="px-4 py-3 text-sm text-[var(--text-secondary)]">
                    {request.date}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    <span className="inline-flex items-center justify-center w-8 h-8 bg-gray-100 text-[var(--text-primary)] text-sm font-medium rounded-full">
                      {request.itemCount}
                    </span>
                  </td>
                  <td className="px-4 py-3">
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                        request.status
                      )}`}
                    >
                      {getStatusLabel(request.status)}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-right">
                    <Link
                      to={`/sales/requests/${request.id}`}
                      className="text-sm font-medium text-[var(--primary)] hover:underline"
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
