import { useNavigate, useLocation, Link } from 'react-router';
import { CheckCircle, Package, MapPin, Calendar, ArrowRight } from 'lucide-react';
import { useEffect, useState } from 'react';

export function ReplacementOrderSuccess() {
  const navigate = useNavigate();
  const location = useLocation();
  const [successData, setSuccessData] = useState<any>(null);

  useEffect(() => {
    const data = location.state;
    if (!data) {
      navigate('/overseas/import-orders');
      return;
    }
    setSuccessData(data);
  }, [location.state, navigate]);

  if (!successData) {
    return null;
  }

  const siteLabels = successData.sites.map((site: string, index: number) => {
    if (index === successData.sites.length - 1) {
      return site;
    }
    return `${site}, `;
  });

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 via-blue-50 to-indigo-50 flex items-center justify-center p-8">
      <div className="max-w-4xl w-full">
        {/* Success Icon with Animation */}
        <div className="flex justify-center mb-8">
          <div className="relative">
            <div className="absolute inset-0 bg-gradient-to-br from-green-400 to-green-600 rounded-full blur-2xl opacity-20 animate-pulse"></div>
            <div className="relative w-32 h-32 bg-white rounded-full flex items-center justify-center shadow-2xl border-4 border-green-500">
              <CheckCircle className="w-20 h-20 text-green-600" strokeWidth={2.5} />
            </div>
          </div>
        </div>

        {/* Success Message */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-green-900 mb-3">
            Đã gửi đơn hàng thay thế thành công!
          </h1>
          <p className="text-lg font-medium text-green-700">
            Thông tin chi tiết đã được gửi qua email đến các site liên quan.
          </p>
        </div>

        {/* Success Details Card */}
        <div className="bg-white rounded-2xl shadow-xl border border-[#E5E7EB] overflow-hidden mb-8">
          {/* Gradient Header */}
          <div className="bg-gradient-to-r from-green-600 to-green-700 px-8 py-6 text-white relative overflow-hidden">
            <div className="absolute top-0 right-0 w-32 h-32 bg-white opacity-10 rounded-full -mr-16 -mt-16"></div>
            <div className="flex items-center justify-between relative">
              <div>
                <p className="text-white text-sm font-semibold mb-1">Mã đơn hàng mới</p>
                <p className="text-4xl font-bold text-white drop-shadow-md">{successData.newOrderCode}</p>
              </div>
              <div className="w-16 h-16 bg-white rounded-xl flex items-center justify-center shadow-lg">
                <Package className="w-9 h-9 text-green-600" strokeWidth={2.5} />
              </div>
            </div>
          </div>

          {/* Details Grid */}
          <div className="p-8">
            <div className="grid grid-cols-3 gap-6 mb-6">
              <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-6 border-2 border-blue-300">
                <div className="flex items-center gap-3 mb-2">
                  <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-lg flex items-center justify-center shadow-lg">
                    <MapPin className="w-5 h-5 text-white" strokeWidth={2.5} />
                  </div>
                  <p className="text-xs font-bold text-blue-800 uppercase tracking-wider">
                    Tổng sites
                  </p>
                </div>
                <p className="text-3xl font-bold text-blue-900">{successData.sites.length}</p>
                <p className="text-xs font-semibold text-blue-700 mt-1">{siteLabels}</p>
              </div>

              <div className="bg-gradient-to-br from-purple-50 to-pink-50 rounded-xl p-6 border-2 border-purple-300">
                <div className="flex items-center gap-3 mb-2">
                  <div className="w-10 h-10 bg-gradient-to-br from-purple-500 to-purple-600 rounded-lg flex items-center justify-center shadow-lg">
                    <Calendar className="w-5 h-5 text-white" strokeWidth={2.5} />
                  </div>
                  <p className="text-xs font-bold text-purple-800 uppercase tracking-wider">
                    Ngày xử lý
                  </p>
                </div>
                <p className="text-2xl font-bold text-purple-900">{successData.processingDate}</p>
              </div>

              <div className="bg-gradient-to-br from-green-50 to-emerald-50 rounded-xl p-6 border-2 border-green-300">
                <div className="flex items-center gap-3 mb-2">
                  <div className="w-10 h-10 bg-gradient-to-br from-green-500 to-green-600 rounded-lg flex items-center justify-center shadow-lg">
                    <CheckCircle className="w-5 h-5 text-white" strokeWidth={2.5} />
                  </div>
                  <p className="text-xs font-bold text-green-800 uppercase tracking-wider">
                    Trạng thái
                  </p>
                </div>
                <p className="text-sm font-bold text-green-900">Đã xử lý đơn #{successData.originalOrderCode}</p>
              </div>
            </div>

            {/* Info Banner */}
            <div className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-6 border-2 border-blue-200">
              <h3 className="text-sm font-semibold text-[#111827] mb-3">Bước tiếp theo</h3>
              <ul className="space-y-2 text-sm text-[#6B7280]">
                <li className="flex items-start gap-2">
                  <span className="text-blue-600 mt-0.5">✓</span>
                  <span>Đơn hàng đã được gửi đến {successData.sites.length} site</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="text-blue-600 mt-0.5">✓</span>
                  <span>Email xác nhận đã được gửi đến các bên liên quan</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="text-blue-600 mt-0.5">✓</span>
                  <span>Bạn có thể theo dõi tiến độ tại trang đơn hàng nhập khẩu</span>
                </li>
              </ul>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="flex justify-center gap-4">
          <Link
            to="/overseas"
            className="px-8 py-4 bg-white border-2 border-[#E5E7EB] rounded-xl text-[#111827] font-semibold hover:bg-[#F3F4F6] hover:border-[#D1D5DB] transition-all shadow-sm hover:shadow-md"
          >
            Quay về trang chủ
          </Link>
          <Link
            to="/overseas/import-orders"
            className="px-8 py-4 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-xl font-semibold hover:from-blue-700 hover:to-blue-800 transition-all shadow-lg hover:shadow-xl flex items-center gap-2"
          >
            Xem danh sách đơn hàng
            <ArrowRight className="w-5 h-5" />
          </Link>
        </div>
      </div>
    </div>
  );
}
