# Bai tap 7: Lap trinh va kiem thu

## 1. Use case phu trach

- Ten use case: Xu ly don nhap kho / Xac nhan don hang nhap kho
- Tac nhan chinh: Bo phan Quan ly Kho
- Module duoc chon de kiem thu don vi: `InboundOrderRules`
- Full name class: `com.app.modules.warehouse.inbound.service.InboundOrderRules`

Module nay duoc tach rieng tu nghiep vu xac nhan nhap kho de co the kiem thu tu dong ma khong phu thuoc JavaFX UI, database hay API ngoai.

## 2. Mo ta module kiem thu

Class `InboundOrderRules` chiu trach nhiem xu ly cac quy tac nghiep vu cot loi truoc khi xac nhan nhap kho:

- Kiem tra danh sach mat hang khong duoc rong.
- Kiem tra so luong thuc nhan khong duoc am.
- Neu xac nhan nhap kho va co sai lech thi bat buoc phai co ly do sai lech.
- Xac dinh trang thai don sau khi doi chieu:
  - `IMPORTED` neu tat ca so luong thuc nhan khop so luong dat.
  - `MISMATCH` neu co it nhat mot mat hang sai lech.
- Tong hop ly do sai lech de luu vao don nhap kho.

### Cac phuong thuc duoc kiem thu

| Phuong thuc | Y nghia |
| --- | --- |
| `validateItems(List<InboundOrderItemResponse> items, boolean requireMismatchReason)` | Kiem tra tinh hop le cua danh sach mat hang |
| `resolveStatus(List<InboundOrderItemResponse> items)` | Xac dinh trang thai don nhap kho sau doi chieu |
| `resolveMismatchReason(List<InboundOrderItemResponse> items, String mismatchReason)` | Xac dinh noi dung ly do sai lech can luu |

## 3. Thiet ke test case bang ky thuat hop den

Ky thuat hop den duoc ap dung dua tren dac ta dau vao/dau ra cua use case, khong xet cau truc code ben trong.

### Bang test case hop den

| Ma TC | Muc tieu | Du lieu vao | Ket qua mong doi |
| --- | --- | --- | --- |
| BB-01 | Tat ca so luong khop | SP001: ordered=100, actual=100; SP002: ordered=50, actual=50 | Hop le, status=`IMPORTED`, mismatch reason rong |
| BB-02 | Co sai lech va co ly do | SP001: ordered=100, actual=95, reason=`Thieu 5 san pham` | Hop le, status=`MISMATCH`, ly do=`SP001: Thieu 5 san pham` |
| BB-03 | Ly do chung uu tien hon ly do tung item | Co item sai lech, mismatchReason=`Hang bi rach thung` | Ly do luu la `Hang bi rach thung` |
| BB-04 | Sai lech nhung khong nhap ly do | SP001: ordered=100, actual=95, reason rong | Nem `IllegalArgumentException` |
| BB-05 | So luong thuc nhan am | SP001: ordered=100, actual=-1 | Nem `IllegalArgumentException` |
| BB-06 | Danh sach mat hang rong | `items=[]` | Nem `IllegalArgumentException` |

### JUnit test class hop den

Full name:

```text
com.app.modules.warehouse.inbound.service.InboundOrderRulesBlackBoxTest
```

Cac test method:

- `matchingQuantitiesAreImported`
- `mismatchWithItemReasonIsAcceptedAndMarkedMismatch`
- `globalMismatchReasonOverridesItemReasons`
- `mismatchWithoutReasonIsRejectedWhenConfirming`
- `negativeActualQuantityIsRejected`
- `emptyItemListIsRejected`

## 4. Thiet ke test case bang ky thuat hop trang C1

Ky thuat hop trang C1 duoc ap dung de dam bao phu cac nhanh dieu kien trong module `InboundOrderRules`.

### Phan tich nhanh dieu kien

Trong `validateItems` co cac nhanh chinh:

1. `items == null || items.isEmpty()`
   - Nhanh loi khi danh sach null/rong.
   - Nhanh hop le khi danh sach co item.
2. `item.getActualQuantity() < 0`
   - Nhanh loi khi co so luong am.
   - Nhanh hop le khi tat ca so luong khong am.
3. `requireMismatchReason && item.hasMismatch() && reason blank`
   - Nhanh loi khi xac nhan don co sai lech nhung thieu ly do.
   - Nhanh hop le khi luu tam hoac da co ly do.

Trong `resolveStatus` co cac nhanh:

1. Co sai lech -> `MISMATCH`.
2. Khong sai lech -> `IMPORTED`.

Trong `resolveMismatchReason` co cac nhanh:

1. Khong co sai lech -> tra ve chuoi rong.
2. Co sai lech va co ly do chung -> tra ve ly do chung.
3. Co sai lech va khong co ly do chung -> tong hop ly do tung item.

### Bang test case hop trang C1

| Ma TC | Nhanh duoc phu | Du lieu vao | Ket qua mong doi |
| --- | --- | --- | --- |
| WB-01 | `items == null` | `items=null` | Nem `IllegalArgumentException` |
| WB-02 | `actualQuantity < 0` | SP001: ordered=10, actual=-1 | Nem `IllegalArgumentException` |
| WB-03 | `requireMismatchReason=false` | SP001: ordered=10, actual=8, reason rong | Khong nem loi khi luu tam |
| WB-04 | `requireMismatchReason=true` va reason blank | SP001: ordered=10, actual=8, reason=`   ` | Nem `IllegalArgumentException` |
| WB-05 | `hasMismatch=false` va `hasMismatch=true` | Mot item khop, mot item lech | Tra ve lan luot `IMPORTED` va `MISMATCH` |
| WB-06 | Khong co mismatch khi resolve reason | SP001: ordered=10, actual=10 | Tra ve chuoi rong |
| WB-07 | Tong hop ly do tung item | SP001 thieu 2, SP002 thua 1 | Tra ve `SP001: Thieu 2; SP002: Thua 1` |

### JUnit test class hop trang C1

Full name:

```text
com.app.modules.warehouse.inbound.service.InboundOrderRulesWhiteBoxC1Test
```

Cac test method:

- `validateItemsCoversNullInputBranch`
- `validateItemsCoversNegativeQuantityBranch`
- `validateItemsAllowsMissingReasonWhenSavingDraft`
- `validateItemsRejectsMissingReasonWhenConfirming`
- `resolveStatusCoversImportedAndMismatchBranches`
- `resolveMismatchReasonCoversNoMismatchBranch`
- `resolveMismatchReasonCoversAggregateItemReasonsBranch`

## 5. Kiem thu use case

Ben canh kiem thu don vi, use case `Xu ly don nhap kho` can duoc kiem thu theo cac scenario nguoi dung sau.

| Ma UC-TC | Scenario | Cac buoc test | Ket qua mong doi |
| --- | --- | --- | --- |
| UC-01 | Nhap kho thanh cong, khong sai lech | Mo man hinh xu ly don; nhap actual qty bang ordered qty; bam Xac nhan Nhap kho | Don cap nhat `IMPORTED`, ton kho tang theo so thuc nhan, hien popup thanh cong |
| UC-02 | Nhap kho co sai lech | Nhap actual qty khac ordered qty; nhap ly do sai lech; bam Xac nhan Nhap kho | Don cap nhat `MISMATCH`, ton kho tang theo actual qty, luu ly do sai lech |
| UC-03 | Sai lech nhung thieu ly do | Nhap actual qty khac ordered qty; de trong ly do; bam Xac nhan | He thong chan va yeu cau nhap ly do |
| UC-04 | Luu tam ket qua kiem dem | Nhap actual qty; bam Luu tam | Don cap nhat `PROCESSING`, chua cong ton kho |
| UC-05 | Khong cho xac nhan lai don da xac nhan | Mo don da `IMPORTED` hoac `MISMATCH`; bam Xac nhan | He thong chan de tranh cong kho hai lan |
| UC-06 | Dong bo API ngoai thanh cong | Xac nhan don hop le khi API `/api/orders` hoat dong | He thong hien xac nhan thanh cong va dong bo thanh cong |
| UC-07 | Dong bo API ngoai that bai | Xac nhan don hop le khi API ngoai loi/timeout | Nghiep vu nhap kho van thanh cong, he thong canh bao dong bo that bai |

## 6. Cach chay kiem thu tu dong

Can dung JDK 17. Lenh chay:

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd test
```

Ket qua mong doi:

```text
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 7. Ket luan

Module `InboundOrderRules` da duoc kiem thu bang ca hai ky thuat hop den va hop trang C1. Cac test case bao phu cac luong nghiep vu quan trong cua use case xu ly don nhap kho: nhap dung so luong, sai lech co ly do, sai lech thieu ly do, so luong am, danh sach rong, xac dinh trang thai va tong hop ly do sai lech.

Viec tach module nghiep vu thuan giup test tu dong bang JUnit de hon, dong thoi lam cho thiet ke phu hop hon voi cac nguyen ly da ap dung trong Bai tap 6.
