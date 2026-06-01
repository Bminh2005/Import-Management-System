import { Link } from 'react-router';
import { Plus, Search, Eye, Edit } from 'lucide-react';
import { useState } from 'react';

interface ImportRequest {
  id: string;
  code: string;
  createdDate: string;
  itemCount: number;
  status: 'draft' | 'pending' | 'processing' | 'completed' | 'cancelled';
}

const mockRequests: ImportRequest[] = [
  {
    id: '1',
    code: 'REQ-2024-001',
    createdDate: '2024-05-08',
    itemCount: 3,
    status: 'pending',
  },
  {
    id: '2',
    code: 'REQ-2024-002',
    createdDate: '2024-05-07',
    itemCount: 5,
    status: 'processing',
  },
  {
    id: '3',
    code: 'REQ-2024-003',
    createdDate: '2024-05-06',
    itemCount: 2,
    status: 'completed',
  },
  {
    id: '4',
    code: 'REQ-2024-004',
    createdDate: '2024-05-05',
    itemCount: 4,
    status: 'draft',
  },
];

function getStatusColor(status: ImportRequest['status']) {
  const colors = {
    draft: 'bg-gray-100 text-gray-800',
    pending: 'bg-yellow-100 text-yellow-800',
    processing: 'bg-blue-100 text-blue-800',
    completed: 'bg-green-100 text-green-800',
    cancelled: 'bg-red-100 text-red-800',
  };
  return colors[status];
}

function getStatusLabel(status: ImportRequest['status']) {
  const labels = {
    draft: 'Bản nháp',
    pending: 'Chờ xử lý',
    processing: 'Đang xử lý',
    completed: 'Hoàn thành',
    cancelled: 'Đã hủy',
  };
  return labels[status];
}

export function ImportRequestsList() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStatus, setSelectedStatus] = useState<string>('all');

  const statusFilters = [
    { value: 'all', label: 'Tất cả', count: 24 },
    { value: 'draft', label: 'Bản nháp', count: 5 },
    { value: 'pending', label: 'Chờ xử lý', count: 8 },
    { value: 'processing', label: 'Đang xử lý', count: 6 },
    { value: 'completed', label: 'Hoàn thành', count: 4 },
    { value: 'cancelled', label: 'Đã hủy', count: 1 },
  ];

  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-semibold text-[var(--text-primary)] mb-2">
            Yêu cầu Nhập hàng
          </h1>
          <p className="text-[var(--text-secondary)]">Quản lý yêu cầu nhập hàng của bạn</p>
        </div>
        <Link
          to="/sales/requests/new"
          className="flex items-center gap-2 px-6 py-3 bg-[var(--primary)] text-white rounded-lg hover:bg-blue-700 transition-colors shadow-sm"
        >
          <Plus className="w-5 h-5" />
          <span>Tạo Yêu cầu Mới</span>
        </Link>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6 space-y-4">
        {/* Search */}
        <div className="flex items-center gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-[var(--text-secondary)]" />
            <input
              type="text"
              placeholder="Tìm kiếm theo mã yêu cầu..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-[var(--border)] rounded-lg focus:outline-none focus:ring-2 focus:ring-[var(--primary)]"
            />
          </div>
        </div>

        {/* Status Filter Chips */}
        <div className="flex items-center gap-2 flex-wrap">
          {statusFilters.map((filter) => (
            <button
              key={filter.value}
              onClick={() => setSelectedStatus(filter.value)}
              className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                selectedStatus === filter.value
                  ? 'bg-[var(--primary)] text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {filter.label} ({filter.count})
            </button>
          ))}
        </div>
      </div>

      {/* Requests Table */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)]">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-[var(--muted)] border-b-2 border-[var(--border)]">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">
                  Mã yêu cầu
                </th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">
                  Ngày tạo
                </th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">
                  Số mặt hàng
                </th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">
                  Trạng thái
                </th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">
                  Thao tác
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {mockRequests.map((request) => (
                <tr
                  key={request.id}
                  className="hover:bg-blue-50 transition-colors cursor-pointer"
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Link
                      to={`/sales/requests/${request.id}`}
                      className="text-sm font-medium text-[var(--primary)] hover:underline"
                    >
                      {request.code}
                    </Link>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-[var(--text-secondary)]">
                    {request.createdDate}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="inline-flex items-center justify-center w-8 h-8 bg-gray-100 text-[var(--text-primary)] text-sm font-medium rounded-full">
                      {request.itemCount}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                        request.status
                      )}`}
                    >
                      {getStatusLabel(request.status)}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right">
                    <div className="flex items-center justify-end gap-2">
                      <Link
                        to={`/sales/requests/${request.id}`}
                        className="p-1.5 text-[var(--text-secondary)] hover:text-[var(--primary)] hover:bg-blue-50 rounded transition-colors"
                        title="Xem chi tiết"
                      >
                        <Eye className="w-4 h-4" />
                      </Link>
                      {(request.status === 'draft' ||
                        request.status === 'pending' ||
                        request.status === 'processing') && (
                        <Link
                          to={`/sales/requests/${request.id}/edit`}
                          className="p-1.5 text-[var(--text-secondary)] hover:text-[var(--primary)] hover:bg-blue-50 rounded transition-colors"
                          title="Chỉnh sửa"
                        >
                          <Edit className="w-4 h-4" />
                        </Link>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        <div className="px-6 py-4 border-t border-[var(--border)] flex items-center justify-between">
          <p className="text-sm text-[var(--text-secondary)]">Hiển thị 1-4 trong 24 yêu cầu</p>
          <div className="flex items-center gap-2">
            <button className="px-4 py-2 border border-[var(--border)] rounded-lg text-sm font-medium hover:bg-[var(--muted)] transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              Trước
            </button>
            <button className="px-4 py-2 bg-[var(--primary)] text-white rounded-lg text-sm font-medium">
              1
            </button>
            <button className="px-4 py-2 border border-[var(--border)] rounded-lg text-sm font-medium hover:bg-[var(--muted)] transition-colors">
              2
            </button>
            <button className="px-4 py-2 border border-[var(--border)] rounded-lg text-sm font-medium hover:bg-[var(--muted)] transition-colors">
              Sau
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
