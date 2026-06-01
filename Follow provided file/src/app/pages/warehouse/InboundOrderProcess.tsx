import { useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router';
import { ArrowLeft, Package, AlertTriangle, CheckCircle, XCircle } from 'lucide-react';
import { Button } from '../../components/ui/button';
import { Input } from '../../components/ui/input';
import { Label } from '../../components/ui/label';
import { Textarea } from '../../components/ui/textarea';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '../../components/ui/alert-dialog';
import { toast } from 'sonner';

interface OrderItem {
  id: string;
  merchandiseCode: string;
  merchandiseName: string;
  orderedQuantity: number;
  actualQuantity: number | null;
  discrepancyReason: string;
}

interface OrderInfo {
  orderCode: string;
  requestCode: string;
  supplier: string;
  receivedDate: string;
  expectedDate: string;
  status: string;
}

export function InboundOrderProcess() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [showDiscrepancyAlert, setShowDiscrepancyAlert] = useState(false);

  const [orderInfo] = useState<OrderInfo>({
    orderCode: 'ORD-2024-015',
    requestCode: 'REQ-2024-004',
    supplier: 'Amazon US',
    receivedDate: '2024-05-10',
    expectedDate: '2024-05-12',
    status: 'pending',
  });

  const [items, setItems] = useState<OrderItem[]>([
    {
      id: '1',
      merchandiseCode: 'MER-001',
      merchandiseName: 'Laptop Dell XPS 15',
      orderedQuantity: 50,
      actualQuantity: null,
      discrepancyReason: '',
    },
    {
      id: '2',
      merchandiseCode: 'MER-002',
      merchandiseName: 'MacBook Pro 16"',
      orderedQuantity: 30,
      actualQuantity: null,
      discrepancyReason: '',
    },
    {
      id: '3',
      merchandiseCode: 'MER-003',
      merchandiseName: 'iPad Air 11"',
      orderedQuantity: 100,
      actualQuantity: null,
      discrepancyReason: '',
    },
    {
      id: '4',
      merchandiseCode: 'MER-004',
      merchandiseName: 'AirPods Pro 2',
      orderedQuantity: 40,
      actualQuantity: null,
      discrepancyReason: '',
    },
    {
      id: '5',
      merchandiseCode: 'MER-005',
      merchandiseName: 'Magic Mouse 2',
      orderedQuantity: 30,
      actualQuantity: null,
      discrepancyReason: '',
    },
  ]);

  const handleActualQuantityChange = (itemId: string, value: string) => {
    const numValue = value === '' ? null : parseInt(value, 10);

    if (numValue !== null && (isNaN(numValue) || numValue < 0)) {
      return;
    }

    setItems((prev) =>
      prev.map((item) =>
        item.id === itemId
          ? { ...item, actualQuantity: numValue, discrepancyReason: numValue === item.orderedQuantity ? '' : item.discrepancyReason }
          : item
      )
    );
  };

  const handleDiscrepancyReasonChange = (itemId: string, value: string) => {
    setItems((prev) =>
      prev.map((item) => (item.id === itemId ? { ...item, discrepancyReason: value } : item))
    );
  };

  const getDiscrepancy = (item: OrderItem) => {
    if (item.actualQuantity === null) return null;
    return item.actualQuantity - item.orderedQuantity;
  };

  const hasDiscrepancy = (item: OrderItem) => {
    const discrepancy = getDiscrepancy(item);
    return discrepancy !== null && discrepancy !== 0;
  };

  const isFormValid = () => {
    for (const item of items) {
      if (item.actualQuantity === null) return false;
      if (hasDiscrepancy(item) && !item.discrepancyReason.trim()) return false;
    }
    return true;
  };

  const handleConfirmInbound = () => {
    if (!isFormValid()) {
      toast.error('Vui lòng nhập đầy đủ thông tin', {
        description: 'Số lượng thực tế là bắt buộc. Lý do sai lệch bắt buộc nếu số lượng khác với đơn đặt.',
      });
      return;
    }

    const hasAnyDiscrepancy = items.some((item) => hasDiscrepancy(item));

    // Show alert dialog if there are discrepancies
    if (hasAnyDiscrepancy) {
      setShowDiscrepancyAlert(true);
      return;
    }

    // If no discrepancies, proceed directly
    processInbound();
  };

  const processInbound = () => {
    const hasAnyDiscrepancy = items.some((item) => hasDiscrepancy(item));

    toast.success(
      hasAnyDiscrepancy ? 'Đã ghi nhận sai lệch và nhập kho' : 'Nhập kho thành công',
      {
        description: hasAnyDiscrepancy
          ? 'Đơn hàng có sai lệch đã được ghi nhận và cập nhật vào kho.'
          : `Đơn hàng ${orderInfo.orderCode} đã được nhập kho đầy đủ.`,
      }
    );

    setTimeout(() => {
      navigate('/warehouse/inbound');
    }, 1500);
  };

  const handleReject = () => {
    toast.error('Đã từ chối đơn hàng', {
      description: 'Đơn hàng sẽ được đánh dấu để xử lý lại.',
    });
    setTimeout(() => {
      navigate('/warehouse/inbound');
    }, 1500);
  };

  return (
    <div className="p-8 space-y-6">
      {/* Header */}
      <div>
        <Link
          to="/warehouse/inbound"
          className="inline-flex items-center gap-2 text-sm text-emerald-600 hover:underline mb-4"
        >
          <ArrowLeft className="w-4 h-4" />
          Quay lại danh sách
        </Link>
        <h1 className="text-3xl font-semibold text-[var(--text-primary)] mb-2">
          Xử lý Đơn Nhập Kho
        </h1>
        <p className="text-[var(--text-secondary)]">
          Kiểm tra và nhập số lượng thực tế nhận được
        </p>
      </div>

      {/* Order Info */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-start gap-4 mb-4">
          <div className="p-3 bg-emerald-100 rounded-lg">
            <Package className="w-6 h-6 text-emerald-600" />
          </div>
          <div className="flex-1">
            <h2 className="text-xl font-semibold text-[var(--text-primary)] mb-1">
              {orderInfo.orderCode}
            </h2>
            <p className="text-sm text-[var(--text-secondary)]">
              Mã yêu cầu: {orderInfo.requestCode}
            </p>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-4 border-t border-[var(--border)]">
          <div>
            <p className="text-xs text-[var(--text-secondary)] mb-1">Nhà cung cấp</p>
            <p className="text-sm font-medium text-[var(--text-primary)]">{orderInfo.supplier}</p>
          </div>
          <div>
            <p className="text-xs text-[var(--text-secondary)] mb-1">Ngày nhận</p>
            <p className="text-sm font-medium text-[var(--text-primary)]">{orderInfo.receivedDate}</p>
          </div>
          <div>
            <p className="text-xs text-[var(--text-secondary)] mb-1">Ngày dự kiến</p>
            <p className="text-sm font-medium text-[var(--text-primary)]">{orderInfo.expectedDate}</p>
          </div>
        </div>
      </div>

      {/* Items Processing */}
      <div className="bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <h2 className="text-xl font-semibold text-[var(--text-primary)] mb-4">
          Danh sách Mặt hàng
        </h2>

        <div className="space-y-6">
          {items.map((item) => {
            const discrepancy = getDiscrepancy(item);
            const hasDisc = hasDiscrepancy(item);

            return (
              <div
                key={item.id}
                className={`p-4 border rounded-lg ${
                  hasDisc ? 'border-red-300 bg-red-50/50' : 'border-[var(--border)]'
                }`}
              >
                {/* Item Header */}
                <div className="flex items-start justify-between mb-4">
                  <div className="flex-1">
                    <h3 className="font-medium text-[var(--text-primary)] mb-1">
                      {item.merchandiseName}
                    </h3>
                    <p className="text-sm text-[var(--text-secondary)]">
                      Mã: {item.merchandiseCode}
                    </p>
                  </div>
                  {hasDisc && (
                    <div className="flex items-center gap-2 px-3 py-1 bg-red-100 text-red-800 rounded-full text-xs font-medium">
                      <AlertTriangle className="w-3 h-3" />
                      Có sai lệch
                    </div>
                  )}
                </div>

                {/* Quantity Inputs */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                  <div>
                    <Label className="text-xs text-[var(--text-secondary)] mb-1.5">
                      Số lượng đặt
                    </Label>
                    <div className="px-3 py-2 bg-[var(--muted)] rounded-md text-sm font-medium text-[var(--text-primary)]">
                      {item.orderedQuantity}
                    </div>
                  </div>

                  <div>
                    <Label htmlFor={`actual-${item.id}`} className="text-xs text-[var(--text-secondary)] mb-1.5">
                      Số lượng thực tế <span className="text-red-500">*</span>
                    </Label>
                    <Input
                      id={`actual-${item.id}`}
                      type="number"
                      min="0"
                      placeholder="Nhập số lượng..."
                      value={item.actualQuantity ?? ''}
                      onChange={(e) => handleActualQuantityChange(item.id, e.target.value)}
                      className={hasDisc ? 'border-red-300' : ''}
                    />
                  </div>

                  <div>
                    <Label className="text-xs text-[var(--text-secondary)] mb-1.5">
                      Chênh lệch
                    </Label>
                    <div
                      className={`px-3 py-2 rounded-md text-sm font-medium ${
                        discrepancy === null
                          ? 'bg-[var(--muted)] text-[var(--text-secondary)]'
                          : discrepancy === 0
                          ? 'bg-emerald-100 text-emerald-800'
                          : discrepancy > 0
                          ? 'bg-blue-100 text-blue-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {discrepancy === null
                        ? '---'
                        : discrepancy === 0
                        ? 'Khớp'
                        : discrepancy > 0
                        ? `+${discrepancy}`
                        : discrepancy}
                    </div>
                  </div>
                </div>

                {/* Discrepancy Reason */}
                {hasDisc && (
                  <div>
                    <Label htmlFor={`reason-${item.id}`} className="text-xs text-[var(--text-secondary)] mb-1.5">
                      Lý do sai lệch <span className="text-red-500">*</span>
                    </Label>
                    <Textarea
                      id={`reason-${item.id}`}
                      placeholder="Nhập lý do sai lệch (ví dụ: Hàng bị móp vỡ thùng, thiếu hàng từ nhà cung cấp...)"
                      value={item.discrepancyReason}
                      onChange={(e) => handleDiscrepancyReasonChange(item.id, e.target.value)}
                      className="resize-none border-red-300"
                      rows={2}
                    />
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>

      {/* Actions */}
      <div className="flex items-center justify-between gap-4 bg-white rounded-lg shadow-sm border border-[var(--border)] p-6">
        <div className="flex items-start gap-3">
          <AlertTriangle className="w-5 h-5 text-orange-600 mt-0.5" />
          <div className="flex-1">
            <p className="text-sm font-medium text-[var(--text-primary)] mb-1">
              Lưu ý khi xử lý đơn hàng
            </p>
            <p className="text-xs text-[var(--text-secondary)]">
              Số lượng thực tế và lý do sai lệch (nếu có) là bắt buộc trước khi xác nhận nhập kho.
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <Button variant="outline" onClick={handleReject} className="gap-2">
            <XCircle className="w-4 h-4" />
            Từ chối
          </Button>
          <Button
            onClick={handleConfirmInbound}
            disabled={!isFormValid()}
            className="gap-2 bg-emerald-600 hover:bg-emerald-700"
          >
            <CheckCircle className="w-4 h-4" />
            Xác nhận Nhập kho
          </Button>
        </div>
      </div>

      {/* Discrepancy Alert Dialog */}
      <AlertDialog open={showDiscrepancyAlert} onOpenChange={setShowDiscrepancyAlert}>
        <AlertDialogContent className="max-w-2xl">
          <AlertDialogHeader>
            <div className="flex items-center gap-3 mb-2">
              <div className="p-3 bg-red-100 rounded-lg">
                <AlertTriangle className="w-6 h-6 text-red-600" />
              </div>
              <AlertDialogTitle className="text-xl">Phát hiện Sai lệch Số lượng</AlertDialogTitle>
            </div>
            <AlertDialogDescription className="text-base">
              Hệ thống phát hiện có sự chênh lệch giữa số lượng đặt và số lượng thực tế nhận được. Vui lòng kiểm tra lại thông tin trước khi xác nhận.
            </AlertDialogDescription>
          </AlertDialogHeader>

          {/* Discrepancy Summary */}
          <div className="my-4 space-y-3">
            <p className="text-sm font-semibold text-[var(--text-primary)]">
              Danh sách mặt hàng có sai lệch:
            </p>
            <div className="max-h-64 overflow-y-auto space-y-2">
              {items
                .filter((item) => hasDiscrepancy(item))
                .map((item) => {
                  const discrepancy = getDiscrepancy(item);
                  return (
                    <div
                      key={item.id}
                      className="p-3 border border-red-200 bg-red-50/50 rounded-lg"
                    >
                      <div className="flex items-start justify-between mb-2">
                        <div>
                          <p className="text-sm font-medium text-[var(--text-primary)]">
                            {item.merchandiseName}
                          </p>
                          <p className="text-xs text-[var(--text-secondary)]">
                            Mã: {item.merchandiseCode}
                          </p>
                        </div>
                        <div
                          className={`px-2 py-1 rounded text-xs font-medium ${
                            discrepancy && discrepancy > 0
                              ? 'bg-blue-100 text-blue-800'
                              : 'bg-red-100 text-red-800'
                          }`}
                        >
                          {discrepancy && discrepancy > 0
                            ? `Thừa ${discrepancy}`
                            : `Thiếu ${Math.abs(discrepancy ?? 0)}`}
                        </div>
                      </div>
                      <div className="grid grid-cols-2 gap-2 mb-2 text-xs">
                        <div>
                          <span className="text-[var(--text-secondary)]">Số lượng đặt:</span>{' '}
                          <span className="font-medium">{item.orderedQuantity}</span>
                        </div>
                        <div>
                          <span className="text-[var(--text-secondary)]">Số lượng thực tế:</span>{' '}
                          <span className="font-medium">{item.actualQuantity}</span>
                        </div>
                      </div>
                      <div className="text-xs">
                        <span className="text-[var(--text-secondary)]">Lý do:</span>{' '}
                        <span className="text-[var(--text-primary)]">{item.discrepancyReason}</span>
                      </div>
                    </div>
                  );
                })}
            </div>
          </div>

          <div className="p-3 bg-orange-50 border border-orange-200 rounded-lg">
            <p className="text-xs text-orange-800">
              <strong>Lưu ý:</strong> Sai lệch sẽ được ghi nhận vào hệ thống. Hãy đảm bảo lý do sai lệch chính xác trước khi tiếp tục.
            </p>
          </div>

          <AlertDialogFooter>
            <AlertDialogCancel>Quay lại Kiểm tra</AlertDialogCancel>
            <AlertDialogAction
              onClick={processInbound}
              className="bg-emerald-600 hover:bg-emerald-700"
            >
              Xác nhận và Nhập kho
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
