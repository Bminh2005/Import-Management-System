import { Link, useNavigate } from 'react-router';
import { ArrowLeft, Save, Trash2, Plus, Search, ChevronDown, ChevronUp } from 'lucide-react';
import { useState, useMemo } from 'react';

interface RequestItem {
  id: string;
  code: string;
  name: string;
  quantity: number;
  unit: string;
  deliveryDate: string;
}

export function CreateImportRequest() {
  const navigate = useNavigate();
  const [items, setItems] = useState<RequestItem[]>([]);
  const [showAddItemModal, setShowAddItemModal] = useState(false);
  
  // Search and Filter State
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [expandedItemId, setExpandedItemId] = useState<string | null>(null);
  
  // Dummy items to select from with more details
  const availableMerchandise = [
    { code: 'MH001', name: 'Laptop Dell XPS 13', unit: 'cái', category: 'electronics', categoryName: 'Điện tử', price: '25,000,000 VND', supplier: 'Dell Vietnam', description: 'Laptop cao cấp cho doanh nghiệp, RAM 16GB, SSD 512GB' },
    { code: 'MH002', name: 'Bàn phím cơ Keychron K2', unit: 'cái', category: 'accessories', categoryName: 'Phụ kiện', price: '2,500,000 VND', supplier: 'Keychron', description: 'Bàn phím cơ không dây, Switch Brown' },
    { code: 'MH003', name: 'Chuột Logitech MX Master 3', unit: 'cái', category: 'accessories', categoryName: 'Phụ kiện', price: '2,200,000 VND', supplier: 'Logitech', description: 'Chuột không dây cao cấp chuyên dụng cho đồ họa' },
    { code: 'MH004', name: 'Màn hình LG UltraWide 34"', unit: 'cái', category: 'electronics', categoryName: 'Điện tử', price: '12,000,000 VND', supplier: 'LG Electronics', description: 'Màn hình siêu rộng cho đa nhiệm, tần số quét 144Hz' },
    { code: 'MH005', name: 'Bàn làm việc Ergonomic', unit: 'cái', category: 'furniture', categoryName: 'Nội thất', price: '5,500,000 VND', supplier: 'ErgoLife', description: 'Bàn nâng hạ độ cao thông minh' },
  ];

  const filteredMerchandise = useMemo(() => {
    return availableMerchandise.filter((m) => {
      const matchesSearch = m.name.toLowerCase().includes(searchQuery.toLowerCase()) || m.code.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesCategory = selectedCategory === 'all' || m.category === selectedCategory;
      return matchesSearch && matchesCategory;
    });
  }, [searchQuery, selectedCategory]);

  const categories = [
    { value: 'all', label: 'Tất cả' },
    { value: 'electronics', label: 'Điện tử' },
    { value: 'accessories', label: 'Phụ kiện' },
    { value: 'furniture', label: 'Nội thất' },
  ];

  const updateItem = (itemId: string, field: string, value: any) => {
    setItems((prev) =>
      prev.map((item) => (item.id === itemId ? { ...item, [field]: value } : item))
    );
  };

  const deleteItem = (itemId: string) => {
    if (confirm('Bạn có chắc chắn muốn xóa mặt hàng này?')) {
      setItems((prev) => prev.filter((item) => item.id !== itemId));
    }
  };

  const handleAddItem = (merchandise: any) => {
    const newItem: RequestItem = {
      id: Date.now().toString(),
      code: merchandise.code,
      name: merchandise.name,
      quantity: 1,
      unit: merchandise.unit,
      deliveryDate: '',
    };
    setItems([...items, newItem]);
    setShowAddItemModal(false);
  };

  const handleSave = () => {
    if (items.length === 0) {
      alert('Vui lòng thêm ít nhất một mặt hàng!');
      return;
    }
    // Simulate API call
    alert('Đã tạo yêu cầu nhập hàng mới thành công!');
    navigate('/sales/requests');
  };

  const handleCancel = () => {
    if (items.length > 0) {
      if (confirm('Bạn có những thông tin chưa lưu. Bạn có chắc muốn hủy?')) {
        navigate('/sales/requests');
      }
    } else {
      navigate('/sales/requests');
    }
  };

  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <button
          onClick={handleCancel}
          className="p-2 hover:bg-[var(--muted)] rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div className="flex-1">
          <h1 className="text-3xl font-semibold text-[var(--text-primary)]">
            Tạo Yêu cầu Nhập hàng
          </h1>
          <p className="text-[var(--text-secondary)]">
            Thêm các mặt hàng cần nhập vào hệ thống
          </p>
        </div>
      </div>

      {/* Info Notice */}
      <div className="bg-blue-50 border border-blue-200 text-blue-800 px-4 py-3 rounded-lg text-sm">
        Vui lòng chọn các mặt hàng cần nhập và chỉ định số lượng cùng ngày nhận mong muốn. Mã yêu cầu sẽ được tạo tự động sau khi lưu.
      </div>

      {/* Items List - Editable */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-[var(--text-primary)]">
            Danh sách Mặt hàng
          </h2>
          <button
            onClick={() => setShowAddItemModal(true)}
            className="flex items-center gap-2 px-4 py-2 bg-[var(--primary)] text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            <Plus className="w-4 h-4" />
            Thêm mặt hàng
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-[var(--muted)]">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Mã hàng</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Tên mặt hàng</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Số lượng</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Đơn vị</th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase">Ngày nhận (Mong muốn)</th>
                <th className="px-4 py-3 text-right text-xs font-semibold uppercase">Thao tác</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {items.map((item) => (
                <tr key={item.id} className="hover:bg-[var(--muted)] transition-colors">
                  <td className="px-4 py-3 text-sm font-medium">{item.code}</td>
                  <td className="px-4 py-3 text-sm">{item.name}</td>
                  <td className="px-4 py-3">
                    <input
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={(e) =>
                        updateItem(item.id, 'quantity', parseInt(e.target.value) || 1)
                      }
                      className="w-24 px-2 py-1 border border-[var(--border)] rounded focus:outline-none focus:ring-2 focus:ring-[var(--primary)]"
                    />
                  </td>
                  <td className="px-4 py-3 text-sm">{item.unit}</td>
                  <td className="px-4 py-3">
                    <input
                      type="date"
                      value={item.deliveryDate}
                      onChange={(e) => updateItem(item.id, 'deliveryDate', e.target.value)}
                      className="px-2 py-1 border border-[var(--border)] rounded focus:outline-none focus:ring-2 focus:ring-[var(--primary)]"
                    />
                  </td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => deleteItem(item.id)}
                      className="p-1.5 text-red-600 hover:bg-red-50 rounded transition-colors"
                      title="Xóa"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {items.length === 0 && (
            <div className="text-center py-12 text-[var(--text-secondary)] bg-gray-50 border border-dashed border-gray-300 rounded-lg mt-4">
              Chưa có mặt hàng nào. Click "Thêm mặt hàng" để bắt đầu.
            </div>
          )}
        </div>
      </div>

      {/* Save/Cancel at bottom */}
      <div className="flex justify-end gap-3">
        <button
          onClick={handleCancel}
          className="px-6 py-2.5 border border-[var(--border)] rounded-lg hover:bg-[var(--muted)] transition-colors font-medium"
        >
          Hủy bỏ
        </button>
        <button
          onClick={handleSave}
          className="flex items-center gap-2 px-6 py-2.5 bg-[var(--primary)] text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
        >
          <Save className="w-5 h-5" />
          Tạo Yêu cầu
        </button>
      </div>

      {/* Add Item Modal */}
      {showAddItemModal && (
        <div
          className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4"
          onClick={() => {
            setShowAddItemModal(false);
            setExpandedItemId(null);
          }}
        >
          <div
            className="bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] flex flex-col"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="p-5 border-b border-[var(--border)] flex items-center justify-between bg-gray-50 rounded-t-lg">
              <h3 className="text-lg font-semibold text-[var(--text-primary)]">
                Chọn Mặt hàng
              </h3>
              <button
                onClick={() => {
                  setShowAddItemModal(false);
                  setExpandedItemId(null);
                }}
                className="text-gray-400 hover:text-gray-600 transition-colors"
              >
                ✕
              </button>
            </div>
            
            {/* Modal Filters */}
            <div className="p-5 border-b border-[var(--border)] space-y-4">
              <div className="flex items-center gap-4">
                <div className="flex-1 relative">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--text-secondary)]" />
                  <input
                    type="text"
                    placeholder="Tìm kiếm theo mã hàng hoặc tên..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="w-full pl-9 pr-4 py-2 text-sm border border-[var(--border)] rounded-md focus:outline-none focus:ring-1 focus:ring-[var(--primary)]"
                  />
                </div>
              </div>
              <div className="flex items-center gap-2 flex-wrap">
                {categories.map((cat) => (
                  <button
                    key={cat.value}
                    onClick={() => setSelectedCategory(cat.value)}
                    className={`px-3 py-1.5 rounded-md text-xs font-medium transition-colors ${
                      selectedCategory === cat.value
                        ? 'bg-[var(--primary)] text-white shadow-sm'
                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    }`}
                  >
                    {cat.label}
                  </button>
                ))}
              </div>
            </div>

            <div className="flex-1 overflow-y-auto p-5 bg-gray-50">
              <div className="space-y-3">
                {filteredMerchandise.length === 0 ? (
                  <div className="text-center py-8 text-sm text-[var(--text-secondary)]">
                    Không tìm thấy mặt hàng nào phù hợp.
                  </div>
                ) : (
                  filteredMerchandise.map((m) => (
                    <div key={m.code} className="bg-white border border-[var(--border)] rounded-lg overflow-hidden shadow-sm hover:border-[var(--primary)] transition-colors">
                      <div className="p-4 flex items-center justify-between">
                        <div className="flex items-start gap-3">
                          <button 
                            onClick={() => setExpandedItemId(expandedItemId === m.code ? null : m.code)}
                            className="mt-0.5 p-1 hover:bg-gray-100 rounded text-gray-500"
                          >
                            {expandedItemId === m.code ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                          </button>
                          <div>
                            <div className="font-semibold text-gray-900">{m.name}</div>
                            <div className="text-xs text-[var(--text-secondary)] mt-1 flex items-center gap-2">
                              <span className="font-mono text-blue-600 bg-blue-50 px-1.5 py-0.5 rounded">{m.code}</span>
                              <span>•</span>
                              <span>ĐVT: {m.unit}</span>
                              <span>•</span>
                              <span className="bg-gray-100 px-1.5 py-0.5 rounded">{m.categoryName}</span>
                            </div>
                          </div>
                        </div>
                        <button
                          onClick={() => handleAddItem(m)}
                          className="px-4 py-2 bg-[var(--primary)] text-white rounded-md hover:bg-blue-700 text-sm font-medium shadow-sm transition-colors"
                        >
                          Chọn
                        </button>
                      </div>
                      
                      {/* Expanded Details */}
                      {expandedItemId === m.code && (
                        <div className="px-12 py-3 bg-blue-50/30 border-t border-gray-100 text-xs text-gray-600 grid grid-cols-2 gap-x-4 gap-y-2">
                          <div><span className="font-medium text-gray-700">Nhà cung cấp:</span> {m.supplier}</div>
                          <div><span className="font-medium text-gray-700">Giá dự kiến:</span> {m.price}</div>
                          <div className="col-span-2"><span className="font-medium text-gray-700">Mô tả:</span> {m.description}</div>
                        </div>
                      )}
                    </div>
                  ))
                )}
              </div>
            </div>
            
            <div className="p-5 border-t border-[var(--border)] flex justify-end bg-white rounded-b-lg">
              <button
                onClick={() => {
                  setShowAddItemModal(false);
                  setExpandedItemId(null);
                }}
                className="px-5 py-2 border border-[var(--border)] rounded-md hover:bg-[var(--muted)] text-sm font-medium transition-colors"
              >
                Đóng
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
