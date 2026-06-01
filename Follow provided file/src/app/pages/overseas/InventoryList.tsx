import { Search, Package, ChevronDown, ChevronUp } from 'lucide-react';
import React, { useState } from 'react';

interface InventoryItem {
  id: string;
  code: string;
  name: string;
  category: string;
  totalStock: number;
  unit: string;
  sites: {
    siteCode: string;
    siteName: string;
    stock: number;
  }[];
}

const mockInventory: InventoryItem[] = [
  {
    id: '1',
    code: 'MH001',
    name: 'Vải lụa cao cấp',
    category: 'Vải',
    totalStock: 2500,
    unit: 'mét',
    sites: [
      { siteCode: 'SITE01', siteName: 'Kho Hong Kong', stock: 2000 },
      { siteCode: 'SITE03', siteName: 'Kho Bangkok', stock: 500 },
    ],
  },
  {
    id: '2',
    code: 'MH002',
    name: 'Vải cotton organic',
    category: 'Vải',
    totalStock: 3200,
    unit: 'mét',
    sites: [
      { siteCode: 'SITE01', siteName: 'Kho Hong Kong', stock: 1850 },
      { siteCode: 'SITE02', siteName: 'Kho Singapore', stock: 1350 },
    ],
  },
  {
    id: '3',
    code: 'MH003',
    name: 'Đá granite tự nhiên',
    category: 'Đá',
    totalStock: 1500,
    unit: 'viên',
    sites: [
      { siteCode: 'SITE01', siteName: 'Kho Hong Kong', stock: 450 },
      { siteCode: 'SITE04', siteName: 'Kho Shanghai', stock: 1050 },
    ],
  },
  {
    id: '4',
    code: 'MH004',
    name: 'Màn hình LG UltraWide 34"',
    category: 'Điện tử',
    totalStock: 150,
    unit: 'cái',
    sites: [
      { siteCode: 'SITE02', siteName: 'Kho Singapore', stock: 80 },
      { siteCode: 'SITE03', siteName: 'Kho Bangkok', stock: 70 },
    ],
  },
  {
    id: '5',
    code: 'MH005',
    name: 'Webcam Logitech C920',
    category: 'Điện tử',
    totalStock: 300,
    unit: 'cái',
    sites: [
      { siteCode: 'SITE01', siteName: 'Kho Hong Kong', stock: 150 },
      { siteCode: 'SITE02', siteName: 'Kho Singapore', stock: 100 },
      { siteCode: 'SITE04', siteName: 'Kho Shanghai', stock: 50 },
    ],
  },
];

const categories = ['Tất cả', 'Vải', 'Đá', 'Điện tử'];

export function InventoryList() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('Tất cả');
  const [expandedItems, setExpandedItems] = useState<Set<string>>(new Set());

  const filteredInventory = mockInventory.filter((item) => {
    const matchesSearch =
      item.code.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.name.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory =
      selectedCategory === 'Tất cả' || item.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  const toggleExpand = (itemId: string) => {
    setExpandedItems((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(itemId)) {
        newSet.delete(itemId);
      } else {
        newSet.add(itemId);
      }
      return newSet;
    });
  };

  return (
    <div className="p-4 space-y-3 bg-[var(--background)]">
      {/* Header */}
      <div>
        <h1 className="text-lg font-semibold text-[var(--text-primary)] mb-0.5">Mặt hàng Tồn kho</h1>
        <p className="text-xs text-[var(--text-secondary)]">Quản lý tồn kho mặt hàng tại các site</p>
      </div>

      {/* Search & Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-3 space-y-2.5">
        {/* Search */}
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--text-secondary)]" />
          <input
            type="text"
            placeholder="Tìm kiếm theo mã mặt hàng, tên..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[var(--border)] rounded-md text-[var(--text-primary)] placeholder:text-[var(--text-secondary)] focus:outline-none focus:ring-1 focus:ring-[var(--primary)] focus:border-transparent"
          />
        </div>

        {/* Category Filter */}
        <div className="flex gap-2">
          {categories.map((category) => (
            <button
              key={category}
              onClick={() => setSelectedCategory(category)}
              className={`px-4 py-1.5 rounded-md text-xs font-medium transition-all ${
                selectedCategory === category
                  ? 'bg-[var(--primary)] text-white shadow-sm'
                  : 'bg-[var(--muted)] text-[var(--text-secondary)] hover:bg-gray-200'
              }`}
            >
              {category}
            </button>
          ))}
        </div>
      </div>

      {/* Inventory Table */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-[var(--border)]">
              <tr>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Mã mặt hàng
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Tên mặt hàng
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Danh mục
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Tổng tồn kho
                  </span>
                </th>
                <th className="px-3 py-2 text-left">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Số site
                  </span>
                </th>
                <th className="px-3 py-2 text-right">
                  <span className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide">
                    Thao tác
                  </span>
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filteredInventory.map((item) => {
                const isExpanded = expandedItems.has(item.id);
                return (
                  <React.Fragment key={item.id}>
                    <tr className="hover:bg-gray-50 transition-colors">
                      <td className="px-3 py-2">
                        <span className="text-xs font-semibold text-[var(--text-primary)]">{item.code}</span>
                      </td>
                      <td className="px-3 py-2">
                        <div className="flex items-center gap-1.5">
                          <Package className="w-3.5 h-3.5 text-[var(--text-secondary)]" />
                          <span className="text-xs text-[var(--text-primary)]">{item.name}</span>
                        </div>
                      </td>
                      <td className="px-3 py-2">
                        <span className="inline-flex items-center px-2 py-0.5 rounded-md text-[10px] font-medium bg-purple-50 text-purple-700">
                          {item.category}
                        </span>
                      </td>
                      <td className="px-3 py-2">
                        <span className="text-xs font-semibold text-[var(--primary)]">
                          {item.totalStock.toLocaleString()} {item.unit}
                        </span>
                      </td>
                      <td className="px-3 py-2">
                        <span className="inline-flex items-center justify-center w-6 h-6 bg-gray-100 text-[var(--text-primary)] text-xs font-medium rounded-full">
                          {item.sites.length}
                        </span>
                      </td>
                      <td className="px-3 py-2 text-right">
                        <button
                          onClick={() => toggleExpand(item.id)}
                          className="inline-flex items-center gap-0.5 text-xs font-medium text-[var(--primary)] hover:underline"
                        >
                          {isExpanded ? 'Thu gọn' : 'Xem chi tiết'}
                          {isExpanded ? (
                            <ChevronUp className="w-3 h-3" />
                          ) : (
                            <ChevronDown className="w-3 h-3" />
                          )}
                        </button>
                      </td>
                    </tr>
                    {isExpanded && (
                      <tr>
                        <td colSpan={6} className="px-3 py-3 bg-gray-50">
                          <div className="space-y-2">
                            <h4 className="text-xs font-semibold text-[var(--text-primary)]">
                              Tồn kho tại các site:
                            </h4>
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
                              {item.sites.map((site, idx) => (
                                <div
                                  key={idx}
                                  className="bg-white border border-[var(--border)] rounded-md p-2.5"
                                >
                                  <div className="flex items-center justify-between">
                                    <div>
                                      <p className="text-[10px] text-[var(--text-secondary)] font-semibold uppercase tracking-wide">
                                        {site.siteCode}
                                      </p>
                                      <p className="text-xs font-semibold text-[var(--text-primary)] mt-0.5">
                                        {site.siteName}
                                      </p>
                                    </div>
                                    <div className="text-right">
                                      <p className="text-sm font-bold text-[var(--primary)]">
                                        {site.stock.toLocaleString()}
                                      </p>
                                      <p className="text-[10px] text-[var(--text-secondary)]">{item.unit}</p>
                                    </div>
                                  </div>
                                </div>
                              ))}
                            </div>
                          </div>
                        </td>
                      </tr>
                    )}
                  </React.Fragment>
                );
              })}
            </tbody>
          </table>
        </div>

        {filteredInventory.length === 0 && (
          <div className="text-center py-8 text-xs text-[var(--text-secondary)]">
            Không tìm thấy mặt hàng nào phù hợp với tiêu chí tìm kiếm
          </div>
        )}
      </div>
    </div>
  );
}
