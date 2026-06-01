import { useState } from 'react';
import { Link } from 'react-router';
import { Search, Filter, ArrowUpDown } from 'lucide-react';
import { Input } from '../../components/ui/input';
import { Button } from '../../components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../../components/ui/select';

interface InboundOrder {
  id: string;
  orderCode: string;
  requestCode: string;
  receivedDate: string;
  expectedDate: string;
  supplier: string;
  itemCount: number;
  totalQuantity: number;
  status: 'pending' | 'processing' | 'completed' | 'discrepancy';
}

const mockOrders: InboundOrder[] = [
  {
    id: '1',
    orderCode: 'ORD-2024-015',
    requestCode: 'REQ-2024-004',
    receivedDate: '2024-05-10',
    expectedDate: '2024-05-12',
    supplier: 'Amazon US',
    itemCount: 5,
    totalQuantity: 250,
    status: 'pending',
  },
  {
    id: '2',
    orderCode: 'ORD-2024-014',
    requestCode: 'REQ-2024-003',
    receivedDate: '2024-05-10',
    expectedDate: '2024-05-11',
    supplier: 'Alibaba CN',
    itemCount: 3,
    totalQuantity: 180,
    status: 'processing',
  },
  {
    id: '3',
    orderCode: 'ORD-2024-013',
    requestCode: 'REQ-2024-002',
    receivedDate: '2024-05-09',
    expectedDate: '2024-05-10',
    supplier: 'eBay UK',
    itemCount: 7,
    totalQuantity: 420,
    status: 'completed',
  },
  {
    id: '4',
    orderCode: 'ORD-2024-012',
    requestCode: 'REQ-2024-001',
    receivedDate: '2024-05-09',
    expectedDate: '2024-05-09',
    supplier: 'Amazon US',
    itemCount: 4,
    totalQuantity: 200,
    status: 'discrepancy',
  },
  {
    id: '5',
    orderCode: 'ORD-2024-011',
    requestCode: 'REQ-2024-005',
    receivedDate: '2024-05-08',
    expectedDate: '2024-05-10',
    supplier: 'Alibaba CN',
    itemCount: 6,
    totalQuantity: 300,
    status: 'completed',
  },
];

function getStatusColor(status: InboundOrder['status']) {
  const colors = {
    pending: 'bg-orange-100 text-orange-800',
    processing: 'bg-blue-100 text-blue-800',
    completed: 'bg-emerald-100 text-emerald-800',
    discrepancy: 'bg-red-100 text-red-800',
  };
  return colors[status];
}

function getStatusLabel(status: InboundOrder['status']) {
  const labels = {
    pending: 'Chờ xử lý',
    processing: 'Đang xử lý',
    completed: 'Đã nhập kho',
    discrepancy: 'Có sai lệch',
  };
  return labels[status];
}

export function InboundOrdersList() {
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');

  const filteredOrders = mockOrders.filter((order) => {
    const matchesSearch =
      order.orderCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.requestCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.supplier.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesStatus = statusFilter === 'all' || order.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  return (
    <div className="p-4 space-y-3">
      {/* Header */}
      <div>
        <h1 className="text-lg font-semibold text-[var(--text-primary)] mb-0.5">
          Danh sách Đơn Nhập Kho
        </h1>
        <p className="text-xs text-[var(--text-secondary)]">
          Quản lý và xử lý các đơn hàng nhập kho từ nhà cung cấp
        </p>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-3">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-2.5">
          {/* Search */}
          <div className="md:col-span-2 relative">
            <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-[var(--text-secondary)]" />
            <Input
              type="text"
              placeholder="Tìm theo mã đơn hàng, mã yêu cầu, hoặc nhà cung cấp..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-8 h-8 text-xs"
            />
          </div>

          {/* Status Filter */}
          <Select value={statusFilter} onValueChange={setStatusFilter}>
            <SelectTrigger className="h-8 text-xs">
              <div className="flex items-center gap-1.5">
                <Filter className="w-3.5 h-3.5" />
                <SelectValue placeholder="Lọc theo trạng thái" />
              </div>
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Tất cả trạng thái</SelectItem>
              <SelectItem value="pending">Chờ xử lý</SelectItem>
              <SelectItem value="processing">Đang xử lý</SelectItem>
              <SelectItem value="completed">Đã nhập kho</SelectItem>
              <SelectItem value="discrepancy">Có sai lệch</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      {/* Results Summary */}
      <div className="flex items-center justify-between">
        <p className="text-xs text-[var(--text-secondary)]">
          Hiển thị <span className="font-medium text-[var(--text-primary)]">{filteredOrders.length}</span> đơn hàng
        </p>
        <Button variant="outline" size="sm" className="gap-1.5 h-7 text-xs">
          <ArrowUpDown className="w-3 h-3" />
          Sắp xếp
        </Button>
      </div>

      {/* Orders Table */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-[var(--border)]">
              <tr>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Mã đơn hàng
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Mã yêu cầu
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Nhà cung cấp
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Ngày nhận
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Ngày dự kiến
                </th>
                <th className="px-3 py-2 text-center text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Số mặt hàng
                </th>
                <th className="px-3 py-2 text-center text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Tổng SL
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Trạng thái
                </th>
                <th className="px-3 py-2 text-right text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Thao tác
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filteredOrders.length === 0 ? (
                <tr>
                  <td colSpan={9} className="px-3 py-6 text-center text-xs text-[var(--text-secondary)]">
                    Không tìm thấy đơn hàng phù hợp
                  </td>
                </tr>
              ) : (
                filteredOrders.map((order) => (
                  <tr key={order.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-3 py-2">
                      <Link
                        to={`/warehouse/inbound/${order.id}`}
                        className="text-xs font-medium text-emerald-600 hover:underline"
                      >
                        {order.orderCode}
                      </Link>
                    </td>
                    <td className="px-3 py-2 text-xs text-[var(--text-secondary)]">
                      {order.requestCode}
                    </td>
                    <td className="px-3 py-2 text-xs text-[var(--text-primary)]">
                      {order.supplier}
                    </td>
                    <td className="px-3 py-2 text-xs text-[var(--text-secondary)]">
                      {order.receivedDate}
                    </td>
                    <td className="px-3 py-2 text-xs text-[var(--text-secondary)]">
                      {order.expectedDate}
                    </td>
                    <td className="px-3 py-2 text-center text-xs text-[var(--text-primary)]">
                      {order.itemCount}
                    </td>
                    <td className="px-3 py-2 text-center text-xs text-[var(--text-primary)]">
                      {order.totalQuantity}
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
                    <td className="px-3 py-2 text-right">
                      <Link
                        to={`/warehouse/inbound/${order.id}`}
                        className="text-xs font-medium text-emerald-600 hover:underline"
                      >
                        Xử lý →
                      </Link>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
