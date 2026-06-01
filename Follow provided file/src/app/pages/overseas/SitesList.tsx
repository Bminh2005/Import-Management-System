import { Link } from 'react-router';
import { Search, MapPin, Eye } from 'lucide-react';
import { useState } from 'react';

interface Site {
  id: string;
  code: string;
  name: string;
  warehouse: string;
  location: string;
  status: 'active' | 'inactive';
  totalInventory: number;
}

const mockSites: Site[] = [
  {
    id: '1',
    code: 'SITE01',
    name: 'Kho Hong Kong',
    warehouse: 'Hong Kong Central Warehouse',
    location: 'Hong Kong',
    status: 'active',
    totalInventory: 15000,
  },
  {
    id: '2',
    code: 'SITE02',
    name: 'Kho Singapore',
    warehouse: 'Singapore Distribution Center',
    location: 'Singapore',
    status: 'active',
    totalInventory: 12000,
  },
  {
    id: '3',
    code: 'SITE03',
    name: 'Kho Bangkok',
    warehouse: 'Bangkok Logistics Hub',
    location: 'Bangkok, Thailand',
    status: 'active',
    totalInventory: 8500,
  },
  {
    id: '4',
    code: 'SITE04',
    name: 'Kho Shanghai',
    warehouse: 'Shanghai Regional Warehouse',
    location: 'Shanghai, China',
    status: 'active',
    totalInventory: 20000,
  },
  {
    id: '5',
    code: 'SITE05',
    name: 'Kho Kuala Lumpur',
    warehouse: 'KL Storage Facility',
    location: 'Kuala Lumpur, Malaysia',
    status: 'inactive',
    totalInventory: 3000,
  },
];

function getStatusColor(status: Site['status']) {
  return status === 'active'
    ? 'bg-green-50 text-green-700 border border-green-200'
    : 'bg-gray-100 text-gray-600';
}

function getStatusLabel(status: Site['status']) {
  return status === 'active' ? 'Hoạt động' : 'Không hoạt động';
}

export function SitesList() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStatus, setSelectedStatus] = useState<string>('all');

  const statusFilters = [
    { value: 'all', label: 'Tất cả', count: 5 },
    { value: 'active', label: 'Hoạt động', count: 4 },
    { value: 'inactive', label: 'Không hoạt động', count: 1 },
  ];

  const filteredSites = mockSites.filter((site) => {
    const matchesSearch =
      site.code.toLowerCase().includes(searchQuery.toLowerCase()) ||
      site.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      site.location.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = selectedStatus === 'all' || site.status === selectedStatus;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="p-4 space-y-3 bg-[var(--background)]">
      {/* Header */}
      <div>
        <h1 className="text-lg font-semibold text-[var(--text-primary)] mb-0.5">Danh sách Site</h1>
        <p className="text-xs text-[var(--text-secondary)]">Quản lý danh sách các site cung cấp hàng hóa</p>
      </div>

      {/* Search & Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-3 space-y-2.5">
        {/* Search */}
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--text-secondary)]" />
          <input
            type="text"
            placeholder="Tìm kiếm theo mã site, tên, địa điểm..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[var(--border)] rounded-md text-[var(--text-primary)] placeholder:text-[var(--text-secondary)] focus:outline-none focus:ring-1 focus:ring-[var(--primary)] focus:border-transparent"
          />
        </div>

        {/* Filter Tabs */}
        <div className="flex gap-2">
          {statusFilters.map((filter) => (
            <button
              key={filter.value}
              onClick={() => setSelectedStatus(filter.value)}
              className={`px-4 py-1.5 rounded-md text-xs font-medium transition-all ${
                selectedStatus === filter.value
                  ? 'bg-[var(--primary)] text-white shadow-sm'
                  : 'bg-[var(--muted)] text-[var(--text-secondary)] hover:bg-gray-200'
              }`}
            >
              {filter.label} ({filter.count})
            </button>
          ))}
        </div>
      </div>

      {/* Sites Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
        {filteredSites.map((site) => (
          <div
            key={site.id}
            className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-4 hover:shadow-md transition-all"
          >
            <div className="flex items-start justify-between mb-3">
              <div className="flex items-center gap-2">
                <div className="w-9 h-9 bg-blue-50 rounded-md flex items-center justify-center">
                  <MapPin className="w-5 h-5 text-[var(--primary)]" />
                </div>
                <div>
                  <h3 className="font-semibold text-sm text-[var(--text-primary)]">{site.code}</h3>
                  <p className="text-xs text-[var(--text-secondary)]">{site.name}</p>
                </div>
              </div>
              <span
                className={`inline-flex items-center px-2 py-0.5 rounded-md text-[10px] font-medium ${getStatusColor(
                  site.status
                )}`}
              >
                {getStatusLabel(site.status)}
              </span>
            </div>

            <div className="space-y-2">
              <div>
                <p className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide mb-0.5">
                  KHO
                </p>
                <p className="text-xs text-[var(--text-primary)]">{site.warehouse}</p>
              </div>

              <div>
                <p className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide mb-0.5">
                  ĐỊA ĐIỂM
                </p>
                <p className="text-xs text-[var(--text-primary)]">{site.location}</p>
              </div>

              <div>
                <p className="text-[10px] font-semibold text-[var(--text-secondary)] uppercase tracking-wide mb-0.5">
                  TỔNG TỒN KHO
                </p>
                <p className="text-xs font-semibold text-[var(--primary)]">
                  {site.totalInventory.toLocaleString()} mặt hàng
                </p>
              </div>
            </div>

            <div className="mt-3 pt-3 border-t border-[var(--border)]">
              <Link
                to={`/overseas/sites/${site.id}`}
                className="w-full px-3 py-1.5 text-xs font-medium text-[var(--primary)] hover:bg-blue-50 rounded-md transition-colors flex items-center justify-center gap-1.5"
              >
                <Eye className="w-3.5 h-3.5" />
                Xem chi tiết
              </Link>
            </div>
          </div>
        ))}
      </div>

      {filteredSites.length === 0 && (
        <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-8 text-center">
          <p className="text-xs text-[var(--text-secondary)]">
            Không tìm thấy site nào phù hợp với tiêu chí tìm kiếm
          </p>
        </div>
      )}
    </div>
  );
}
