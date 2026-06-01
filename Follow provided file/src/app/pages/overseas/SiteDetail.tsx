import { useParams, Link } from 'react-router';
import { ArrowLeft, MapPin, User, Phone, Mail, Truck, Package, DollarSign, Calendar } from 'lucide-react';

interface SiteItem {
  code: string;
  name: string;
  category: string;
  availableQuantity: number;
  unit: string;
  unitPrice: number;
  lastUpdated: string;
}

const mockSiteData = {
  siteId: 'SITE-001',
  siteName: 'Site A - Hà Nội',
  location: 'Số 10 Lê Duẩn, Hoàn Kiếm, Hà Nội',
  address: 'Tầng 5, Tòa nhà ABC, Số 10 Lê Duẩn',
  district: 'Hoàn Kiếm',
  city: 'Hà Nội',
  country: 'Việt Nam',
  contactPerson: 'Nguyễn Văn An',
  contactPhone: '024-3942-1234',
  contactEmail: 'an.nguyen@siteA.com',
  shippingCost: 500000,
  estimatedDelivery: '2-3 ngày',
  status: 'active' as const,
  rating: 4.5,
  totalOrders: 245,
  items: [
    {
      code: 'MH001',
      name: 'Laptop Dell XPS 13',
      category: 'Máy tính',
      availableQuantity: 10,
      unit: 'cái',
      unitPrice: 25000000,
      lastUpdated: '2024-05-10',
    },
    {
      code: 'MH002',
      name: 'Bàn phím cơ Keychron K2',
      category: 'Phụ kiện',
      availableQuantity: 25,
      unit: 'cái',
      unitPrice: 2700000,
      lastUpdated: '2024-05-10',
    },
    {
      code: 'MH003',
      name: 'Chuột Logitech MX Master 3',
      category: 'Phụ kiện',
      availableQuantity: 15,
      unit: 'cái',
      unitPrice: 2000000,
      lastUpdated: '2024-05-09',
    },
    {
      code: 'MH004',
      name: 'Màn hình LG UltraWide 34"',
      category: 'Màn hình',
      availableQuantity: 8,
      unit: 'cái',
      unitPrice: 12000000,
      lastUpdated: '2024-05-08',
    },
    {
      code: 'MH005',
      name: 'Webcam Logitech C920',
      category: 'Phụ kiện',
      availableQuantity: 20,
      unit: 'cái',
      unitPrice: 1800000,
      lastUpdated: '2024-05-10',
    },
    {
      code: 'MH006',
      name: 'Tai nghe Sony WH-1000XM4',
      category: 'Audio',
      availableQuantity: 12,
      unit: 'cái',
      unitPrice: 7500000,
      lastUpdated: '2024-05-09',
    },
  ] as SiteItem[],
};

export function SiteDetail() {
  const { id } = useParams();
  const site = mockSiteData;

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(value);
  };

  const getStatusColor = (status: string) => {
    const colors = {
      active: 'bg-green-100 text-green-800',
      inactive: 'bg-gray-100 text-gray-800',
      maintenance: 'bg-yellow-100 text-yellow-800',
    };
    return colors[status as keyof typeof colors] || colors.active;
  };

  const getStatusLabel = (status: string) => {
    const labels = {
      active: 'Đang hoạt động',
      inactive: 'Ngưng hoạt động',
      maintenance: 'Bảo trì',
    };
    return labels[status as keyof typeof labels] || status;
  };

  // Group items by category
  const itemsByCategory = site.items.reduce((acc, item) => {
    if (!acc[item.category]) {
      acc[item.category] = [];
    }
    acc[item.category].push(item);
    return acc;
  }, {} as Record<string, SiteItem[]>);

  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Link
          to="/overseas/process"
          className="p-2 hover:bg-[var(--muted)] rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
        </Link>
        <div className="flex-1">
          <h1 className="text-3xl font-semibold text-[var(--text-primary)]">
            Chi tiết Site: {site.siteName}
          </h1>
          <p className="text-[var(--text-secondary)]">
            Thông tin chi tiết và danh sách mặt hàng có sẵn
          </p>
        </div>
        <span
          className={`px-4 py-2 rounded-full text-sm font-medium ${getStatusColor(
            site.status
          )}`}
        >
          {getStatusLabel(site.status)}
        </span>
      </div>

      <div className="grid grid-cols-3 gap-6">
        {/* Site Information */}
        <div className="col-span-2 space-y-6">
          {/* Basic Info Card */}
          <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
            <h2 className="text-xl font-semibold text-[var(--text-primary)] mb-4">
              Thông tin Site
            </h2>

            <div className="space-y-4">
              {/* Location */}
              <div className="flex items-start gap-3">
                <MapPin className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
                <div className="flex-1">
                  <p className="text-sm font-semibold text-gray-500 uppercase mb-1">
                    Địa chỉ
                  </p>
                  <p className="text-base text-gray-900">{site.address}</p>
                  <p className="text-sm text-gray-600">
                    {site.district}, {site.city}, {site.country}
                  </p>
                </div>
              </div>

              <div className="h-px bg-gray-200" />

              {/* Contact Person */}
              <div className="flex items-start gap-3">
                <User className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
                <div className="flex-1">
                  <p className="text-sm font-semibold text-gray-500 uppercase mb-1">
                    Người liên hệ
                  </p>
                  <p className="text-base font-medium text-gray-900">{site.contactPerson}</p>
                </div>
              </div>

              {/* Contact Phone */}
              <div className="flex items-start gap-3">
                <Phone className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
                <div className="flex-1">
                  <p className="text-sm font-semibold text-gray-500 uppercase mb-1">
                    Số điện thoại
                  </p>
                  <a
                    href={`tel:${site.contactPhone}`}
                    className="text-base text-blue-600 hover:underline"
                  >
                    {site.contactPhone}
                  </a>
                </div>
              </div>

              {/* Contact Email */}
              <div className="flex items-start gap-3">
                <Mail className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
                <div className="flex-1">
                  <p className="text-sm font-semibold text-gray-500 uppercase mb-1">Email</p>
                  <a
                    href={`mailto:${site.contactEmail}`}
                    className="text-base text-blue-600 hover:underline"
                  >
                    {site.contactEmail}
                  </a>
                </div>
              </div>
            </div>
          </div>

          {/* Items by Category */}
          <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-semibold text-[var(--text-primary)]">
                Danh sách Mặt hàng
              </h2>
              <span className="text-sm text-gray-600">
                Tổng: {site.items.length} mặt hàng
              </span>
            </div>

            <div className="space-y-6">
              {Object.entries(itemsByCategory).map(([category, items]) => (
                <div key={category}>
                  <div className="flex items-center gap-2 mb-3">
                    <Package className="w-4 h-4 text-gray-500" />
                    <h3 className="font-semibold text-gray-900">{category}</h3>
                    <span className="text-xs px-2 py-0.5 bg-gray-100 text-gray-600 rounded-full">
                      {items.length} mặt hàng
                    </span>
                  </div>

                  <div className="overflow-x-auto">
                    <table className="w-full">
                      <thead className="bg-gray-50 border-y border-gray-200">
                        <tr>
                          <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">
                            Mã hàng
                          </th>
                          <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">
                            Tên mặt hàng
                          </th>
                          <th className="px-4 py-3 text-right text-xs font-semibold text-gray-600 uppercase">
                            Tồn kho
                          </th>
                          <th className="px-4 py-3 text-right text-xs font-semibold text-gray-600 uppercase">
                            Đơn giá
                          </th>
                          <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">
                            Cập nhật
                          </th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-gray-100">
                        {items.map((item) => (
                          <tr key={item.code} className="hover:bg-gray-50 transition-colors">
                            <td className="px-4 py-3 text-sm font-medium text-gray-900">
                              {item.code}
                            </td>
                            <td className="px-4 py-3 text-sm text-gray-900">{item.name}</td>
                            <td className="px-4 py-3 text-sm text-right">
                              <span className="font-semibold text-gray-900">
                                {item.availableQuantity}
                              </span>{' '}
                              <span className="text-gray-500">{item.unit}</span>
                            </td>
                            <td className="px-4 py-3 text-sm text-right font-medium text-gray-900">
                              {formatCurrency(item.unitPrice)}
                            </td>
                            <td className="px-4 py-3 text-sm text-gray-600">
                              {item.lastUpdated}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Sidebar - Statistics & Shipping Info */}
        <div className="space-y-6">
          {/* Shipping Info */}
          <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
            <h3 className="text-lg font-semibold text-[var(--text-primary)] mb-4">
              Thông tin Vận chuyển
            </h3>

            <div className="space-y-4">
              <div className="flex items-start gap-3">
                <Truck className="w-5 h-5 text-blue-600 mt-0.5 flex-shrink-0" />
                <div>
                  <p className="text-sm text-gray-500 mb-1">Phí vận chuyển</p>
                  <p className="text-lg font-bold text-gray-900">
                    {formatCurrency(site.shippingCost)}
                  </p>
                </div>
              </div>

              <div className="flex items-start gap-3">
                <Calendar className="w-5 h-5 text-blue-600 mt-0.5 flex-shrink-0" />
                <div>
                  <p className="text-sm text-gray-500 mb-1">Thời gian giao hàng</p>
                  <p className="text-base font-semibold text-gray-900">
                    {site.estimatedDelivery}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Statistics */}
          <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
            <h3 className="text-lg font-semibold text-[var(--text-primary)] mb-4">
              Thống kê
            </h3>

            <div className="space-y-4">
              <div>
                <p className="text-sm text-gray-500 mb-1">Đánh giá</p>
                <div className="flex items-center gap-2">
                  <span className="text-2xl font-bold text-yellow-600">{site.rating}</span>
                  <span className="text-yellow-500">★★★★★</span>
                </div>
              </div>

              <div className="h-px bg-gray-200" />

              <div>
                <p className="text-sm text-gray-500 mb-1">Tổng đơn hàng đã xử lý</p>
                <p className="text-2xl font-bold text-gray-900">{site.totalOrders}</p>
              </div>

              <div className="h-px bg-gray-200" />

              <div>
                <p className="text-sm text-gray-500 mb-1">Tổng mặt hàng có sẵn</p>
                <p className="text-2xl font-bold text-blue-600">{site.items.length}</p>
              </div>

              <div className="h-px bg-gray-200" />

              <div>
                <p className="text-sm text-gray-500 mb-1">Tổng giá trị hàng tồn kho</p>
                <p className="text-xl font-bold text-green-600">
                  {formatCurrency(
                    site.items.reduce(
                      (sum, item) => sum + item.availableQuantity * item.unitPrice,
                      0
                    )
                  )}
                </p>
              </div>
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-blue-50 rounded-lg border border-blue-200 p-6">
            <h3 className="text-sm font-semibold text-blue-900 uppercase mb-3">
              Thao tác nhanh
            </h3>
            <div className="space-y-2">
              <button className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium">
                Tạo đơn hàng mới
              </button>
              <button className="w-full px-4 py-2 bg-white border border-blue-300 text-blue-700 rounded-lg hover:bg-blue-50 transition-colors text-sm font-medium">
                Xem lịch sử đơn hàng
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
