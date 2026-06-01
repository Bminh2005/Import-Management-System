import { Plus, Search, Edit, Trash2, ChevronDown, ChevronUp } from 'lucide-react';
import { useState, Fragment } from 'react';

interface Merchandise {
  id: string;
  code: string;
  name: string;
  unit: string;
  category: string;
  price: string;
  supplier: string;
  status: 'active' | 'inactive';
  description: string;
}

const mockMerchandise: Merchandise[] = [
  {
    id: '1',
    code: 'MH001',
    name: 'Laptop Dell XPS 13',
    unit: 'cái',
    category: 'Điện tử',
    price: '25,000,000 VND',
    supplier: 'Dell Vietnam',
    status: 'active',
    description: 'Laptop cao cấp cho doanh nghiệp',
  },
  {
    id: '2',
    code: 'MH002',
    name: 'Bàn phím cơ Keychron K2',
    unit: 'cái',
    category: 'Phụ kiện',
    price: '2,500,000 VND',
    supplier: 'Keychron',
    status: 'active',
    description: 'Bàn phím cơ không dây',
  },
  {
    id: '3',
    code: 'MH003',
    name: 'Chuột Logitech MX Master 3',
    unit: 'cái',
    category: 'Phụ kiện',
    price: '2,200,000 VND',
    supplier: 'Logitech',
    status: 'active',
    description: 'Chuột không dây cao cấp',
  },
  {
    id: '4',
    code: 'MH004',
    name: 'Màn hình LG UltraWide 34"',
    unit: 'cái',
    category: 'Điện tử',
    price: '12,000,000 VND',
    supplier: 'LG Electronics',
    status: 'inactive',
    description: 'Màn hình siêu rộng cho đa nhiệm',
  },
];

export function MerchandiseList() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [expandedRow, setExpandedRow] = useState<string | null>(null);
  const [showAddModal, setShowAddModal] = useState(false);

  const categories = [
    { value: 'all', label: 'Tất cả', count: 24 },
    { value: 'electronics', label: 'Điện tử', count: 12 },
    { value: 'accessories', label: 'Phụ kiện', count: 8 },
    { value: 'furniture', label: 'Nội thất', count: 4 },
  ];

  return (
    <div className="p-4 space-y-3">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text-primary)] mb-0.5">
            Quản lý Mặt hàng
          </h1>
          <p className="text-xs text-[var(--text-secondary)]">Danh sách các mặt hàng trong hệ thống</p>
        </div>
        <button 
          onClick={() => setShowAddModal(true)}
          className="flex items-center gap-1.5 px-4 py-2 bg-[var(--primary)] text-white rounded-md hover:bg-blue-700 transition-colors shadow-sm text-xs"
        >
          <Plus className="w-4 h-4" />
          <span>Thêm Mặt hàng Mới</span>
        </button>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-3 space-y-2.5">
        {/* Search */}
        <div className="flex items-center gap-3">
          <div className="flex-1 relative">
            <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--text-secondary)]" />
            <input
              type="text"
              placeholder="Tìm kiếm theo mã hàng hoặc tên..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-8 pr-3 py-2 text-sm border border-[var(--border)] rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]"
            />
          </div>
        </div>

        {/* Category Filter Chips */}
        <div className="flex items-center gap-2 flex-wrap">
          {categories.map((category) => (
            <button
              key={category.value}
              onClick={() => setSelectedCategory(category.value)}
              className={`px-3 py-1.5 rounded-md text-xs font-medium transition-colors ${
                selectedCategory === category.value
                  ? 'bg-[var(--primary)] text-white shadow-sm'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {category.label} ({category.count})
            </button>
          ))}
        </div>
      </div>

      {/* Merchandise Table */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)]">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-[var(--border)]">
              <tr>
                <th className="px-2 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide w-8"></th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Mã Hàng
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Tên Hàng
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Đơn vị
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Danh mục
                </th>
                <th className="px-3 py-2 text-left text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Trạng thái
                </th>
                <th className="px-3 py-2 text-right text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                  Thao tác
                </th>
              </tr>
            </thead>
            <tbody>
              {mockMerchandise.map((item) => (
                <Fragment key={item.id}>
                  <tr className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="px-2 py-2">
                      <button
                        onClick={() => setExpandedRow(expandedRow === item.id ? null : item.id)}
                        className="p-0.5 hover:bg-gray-200 rounded"
                      >
                        {expandedRow === item.id ? (
                          <ChevronUp className="w-3.5 h-3.5" />
                        ) : (
                          <ChevronDown className="w-3.5 h-3.5" />
                        )}
                      </button>
                    </td>
                    <td className="px-3 py-2 font-mono text-xs text-blue-600">{item.code}</td>
                    <td className="px-3 py-2 text-xs text-gray-700 font-medium">{item.name}</td>
                    <td className="px-3 py-2 text-xs text-gray-700">{item.unit}</td>
                    <td className="px-3 py-2 text-xs text-gray-700">{item.category}</td>
                    <td className="px-3 py-2">
                      <span
                        className={`px-2 py-0.5 rounded-md text-[10px] ${
                          item.status === 'active'
                            ? 'bg-green-100 text-green-800'
                            : 'bg-gray-100 text-gray-800'
                        }`}
                      >
                        {item.status === 'active' ? 'Đang hoạt động' : 'Ngừng hoạt động'}
                      </span>
                    </td>
                    <td className="px-3 py-2">
                      <div className="flex gap-1 justify-end">
                        <button
                          className="p-1.5 hover:bg-blue-50 rounded text-blue-600"
                          title="Sửa"
                        >
                          <Edit className="w-3.5 h-3.5" />
                        </button>
                        <button
                          className="p-1.5 hover:bg-red-50 rounded text-red-600"
                          title="Xóa"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </td>
                  </tr>
                  {expandedRow === item.id && (
                    <tr className="bg-blue-50/50 border-b border-gray-100">
                      <td></td>
                      <td colSpan={6} className="px-3 py-3">
                        <div className="grid grid-cols-2 gap-3 text-xs">
                          <div>
                            <span className="font-semibold text-gray-700">Giá:</span>
                            <span className="ml-2 text-gray-600">{item.price}</span>
                          </div>
                          <div>
                            <span className="font-semibold text-gray-700">Nhà cung cấp:</span>
                            <span className="ml-2 text-gray-600">{item.supplier}</span>
                          </div>
                          <div className="col-span-2">
                            <span className="font-semibold text-gray-700">Mô tả:</span>
                            <p className="mt-0.5 text-gray-600">{item.description}</p>
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </Fragment>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        <div className="px-3 py-2.5 border-t border-[var(--border)] flex items-center justify-between">
          <p className="text-xs text-[var(--text-secondary)]">Hiển thị 1-4 trong 24 mặt hàng</p>
          <div className="flex items-center gap-1.5">
            <button className="px-3 py-1.5 border border-[var(--border)] rounded-md text-xs font-medium hover:bg-[var(--muted)] transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              Trước
            </button>
            <button className="px-3 py-1.5 bg-[var(--primary)] text-white rounded-md text-xs font-medium">
              1
            </button>
            <button className="px-3 py-1.5 border border-[var(--border)] rounded-md text-xs font-medium hover:bg-[var(--muted)] transition-colors">
              2
            </button>
            <button className="px-3 py-1.5 border border-[var(--border)] rounded-md text-xs font-medium hover:bg-[var(--muted)] transition-colors">
              Sau
            </button>
          </div>
        </div>
      </div>

      {/* Add Merchandise Modal */}
      {showAddModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4" onClick={() => setShowAddModal(false)}>
          <div className="bg-white rounded-lg shadow-xl w-full max-w-lg" onClick={e => e.stopPropagation()}>
            <div className="px-6 py-4 border-b border-[var(--border)] flex items-center justify-between">
              <h2 className="text-lg font-semibold text-[var(--text-primary)]">Thêm Mặt hàng Mới</h2>
              <button onClick={() => setShowAddModal(false)} className="text-gray-400 hover:text-gray-600">✕</button>
            </div>
            
            <div className="p-6 space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="col-span-2 sm:col-span-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Mã hàng <span className="text-red-500">*</span></label>
                  <input type="text" className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md bg-gray-100 text-gray-500 cursor-not-allowed" value="Tự động tạo (VD: MH005)" disabled />
                </div>
                <div className="col-span-2 sm:col-span-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Tên mặt hàng <span className="text-red-500">*</span></label>
                  <input type="text" className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]" placeholder="Nhập tên mặt hàng..." />
                </div>
                <div className="col-span-2 sm:col-span-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Đơn vị tính <span className="text-red-500">*</span></label>
                  <input type="text" className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]" placeholder="VD: cái, chiếc, hộp..." />
                </div>
                <div className="col-span-2 sm:col-span-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Danh mục <span className="text-red-500">*</span></label>
                  <select className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]">
                    <option value="">Chọn danh mục</option>
                    <option value="electronics">Điện tử</option>
                    <option value="accessories">Phụ kiện</option>
                    <option value="furniture">Nội thất</option>
                  </select>
                </div>
                <div className="col-span-2 sm:col-span-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Giá dự kiến</label>
                  <input type="text" className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]" placeholder="VD: 15,000,000" />
                </div>
                <div className="col-span-2 sm:col-span-1">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Nhà cung cấp</label>
                  <input type="text" className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]" placeholder="Tên nhà cung cấp..." />
                </div>
                <div className="col-span-2">
                  <label className="block text-xs font-medium text-gray-700 mb-1">Mô tả chi tiết</label>
                  <textarea rows={3} className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)] resize-none" placeholder="Nhập mô tả mặt hàng..."></textarea>
                </div>
              </div>
            </div>
            
            <div className="px-6 py-4 border-t border-[var(--border)] bg-gray-50 rounded-b-lg flex justify-end gap-2">
              <button 
                onClick={() => setShowAddModal(false)}
                className="px-4 py-2 border border-gray-300 text-gray-700 bg-white rounded-md hover:bg-gray-50 text-sm font-medium transition-colors"
              >
                Hủy
              </button>
              <button 
                onClick={() => {
                  alert('Đã thêm mặt hàng mới thành công!');
                  setShowAddModal(false);
                }}
                className="px-4 py-2 bg-[var(--primary)] text-white rounded-md hover:bg-blue-700 text-sm font-medium transition-colors"
              >
                Lưu Mặt hàng
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
