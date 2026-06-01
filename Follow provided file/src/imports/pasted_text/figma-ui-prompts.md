# FIGMA UI PROMPTS - HỆ THỐNG ĐẶT HÀNG NHẬP KHẨU

## 📋 TỔNG QUAN HỆ THỐNG

**Tên hệ thống:** Import Assignment System (Hệ thống đặt hàng nhập khẩu)
**Platform:** Desktop Application
**Mục đích:** Quản lý quy trình đặt hàng nhập khẩu từ nhiều Site quốc tế

**Các vai trò người dùng:**
1. Bộ phận bán hàng (Sales Department)
2. Bộ phận đặt hàng quốc tế (Overseas Order Placement)
3. Site hợp tác (Import Sites)
4. Bộ phận quản lý kho (Warehouse Management)
5. Quản trị viên hệ thống (System Administrator)

---

## 🎨 DESIGN SYSTEM & STYLE GUIDE

### Brand Colors
```
Primary: #2563EB (Blue - representing trust and international trade)
Secondary: #10B981 (Green - for success states and confirmations)
Accent: #F59E0B (Amber - for warnings and pending actions)
Danger: #EF4444 (Red - for errors and critical alerts)
Neutral Gray: #6B7280
Background: #F9FAFB
Surface: #FFFFFF
Text Primary: #111827
Text Secondary: #6B7280
```

### Typography
```
Font Family: Inter or SF Pro Display
Heading 1: 32px, Bold
Heading 2: 24px, Semibold
Heading 3: 20px, Semibold
Body Large: 16px, Regular
Body: 14px, Regular
Caption: 12px, Regular
```

### Spacing System
```
4px, 8px, 12px, 16px, 24px, 32px, 48px, 64px
```

---

## 🖥️ 1. DASHBOARD CHUNG (ALL ROLES)

### Prompt 1.1: Main Dashboard Layout
```
Design a modern desktop application dashboard for an import ordering system. 
Create a clean, professional interface with:

LAYOUT:
- Left sidebar (280px width) with navigation menu, dark blue (#1E3A8A) background
- Top header bar (72px height) with company logo, user profile, notifications, and global search
- Main content area with white background and subtle shadow cards
- Responsive grid system with 12 columns

SIDEBAR COMPONENTS:
- Company logo at top (48px height)
- Navigation menu items with icons:
  * Dashboard (home icon)
  * Orders Management (shopping cart icon)
  * Site Management (globe icon)
  * Warehouse (warehouse icon)
  * Reports & Analytics (chart icon)
  * Settings (gear icon)
- User role badge at bottom showing current role
- Collapse/expand button

HEADER:
- Search bar (400px width) with autocomplete dropdown
- Notification bell with badge counter
- Language switcher (EN/VI)
- User avatar with dropdown menu (profile, settings, logout)

DASHBOARD CARDS:
- 4 metric cards in a row showing:
  * Total Pending Orders (with trend indicator)
  * Active Sites (with status dots)
  * In-Stock Items (with percentage)
  * Deliveries This Month (with comparison to last month)
- Recent Orders table with 5 columns: Order ID, Date, Status, Site, Actions
- Quick Actions section with prominent buttons:
  * Create New Order Request (primary blue button)
  * Check Site Inventory (secondary button)
  * View Reports (outline button)

DESIGN STYLE:
- Modern, clean, minimalist
- Subtle shadows and rounded corners (8px radius)
- Professional color scheme with blue primary colors
- Clear visual hierarchy
- Accessible design with good contrast ratios
- Use of icons from Lucide or Heroicons
```

---

## 👔 2. BỘ PHẬN BÁN HÀNG (SALES DEPARTMENT)

### Prompt 2.1: Create Order Request Form
```
Design a comprehensive order request form for the Sales Department to submit merchandise orders.

LAYOUT:
- Modal/full-screen form with clear progress indicator (3 steps: Select Items → Review → Submit)
- Form width: 900px, centered
- White background with subtle border

STEP 1 - SELECT ITEMS:
- Header: "Create New Order Request" with date picker for submission
- Search bar to find merchandise by code or name
- Merchandise selection table with columns:
  * Checkbox for selection
  * Merchandise Code (sortable)
  * Merchandise Name
  * Current Stock Level (with status indicator)
  * Category
  * Add button
- Selected items panel on the right showing:
  * Merchandise thumbnail/icon
  * Code and name
  * Quantity input (with +/- buttons)
  * Unit dropdown (pcs, kg, boxes, etc.)
  * Desired delivery date picker (calendar)
  * Remove button
- Running total of selected items at bottom

STEP 2 - REVIEW:
- Summary card showing:
  * Total items: X
  * Earliest delivery date needed
  * Priority level selector (High/Medium/Low)
- Detailed table of all selected items with all information
- Edit and Delete options for each row
- Add notes/comments textarea (optional)

STEP 3 - SUBMIT:
- Confirmation screen with order summary
- Routing options: Send to Overseas Order Placement
- Submit button (prominent, blue) and Cancel button (ghost)
- Success animation after submission

VALIDATION:
- Required field indicators (red asterisk)
- Inline error messages
- Disabled submit until all required fields complete

DESIGN FEATURES:
- Drag-and-drop support for merchandise items
- Auto-save draft functionality indicator
- Tooltips for complex fields
- Empty state illustrations when no items selected
```

### Prompt 2.2: Sales Order History & Tracking
```
Create a comprehensive order tracking dashboard for Sales Department to monitor their submitted orders.

LAYOUT:
- Full-width table view with filters and search
- Header with "My Orders" title and "New Order Request" button

FILTERS SECTION:
- Date range picker (Last 7 days, Last 30 days, Custom range)
- Status filter chips:
  * All Orders
  * Pending Review (yellow badge)
  * Processing (blue badge)
  * Partially Fulfilled (orange badge)
  * Completed (green badge)
  * Issues (red badge)
- Search by order ID or merchandise code
- Export to CSV/Excel button

ORDERS TABLE:
Columns:
1. Order ID (clickable, opens detail modal)
2. Submission Date (with time)
3. Total Items (number badge)
4. Status (colored badge with icon)
5. Priority (High/Medium/Low chip)
6. Desired Delivery Range
7. Assigned Sites (avatar stack or count)
8. Completion % (progress bar)
9. Actions (View Details, Cancel, Duplicate icons)

Row Design:
- Alternating row colors for readability
- Hover state with subtle highlight
- Expand arrow to show item breakdown inline

DETAIL MODAL:
- Order information header
- Timeline showing order progress:
  * Order Submitted
  * Under Review
  * Sites Selected
  * Orders Placed
  * In Transit
  * Delivered
- Merchandise items breakdown table
- Site assignments with delivery method badges
- Expected vs Actual delivery dates comparison
- Communication thread with Order Placement team
- Download order confirmation PDF button

FEATURES:
- Bulk actions: Select multiple orders to export or compare
- Real-time status updates with notification badges
- Sorting by any column
- Pagination with page size selector (10, 25, 50, 100)
```

---

## 🌍 3. BỘ PHẬN ĐẶT HÀNG QUỐC TẾ (OVERSEAS ORDER PLACEMENT)

### Prompt 3.1: Order Processing Workspace
```
Design a sophisticated order processing interface for the Overseas Order Placement team to manage incoming requests and coordinate with Sites.

LAYOUT:
- Split-screen layout (70-30 split)
- Left panel: Incoming orders queue
- Right panel: Order details and processing tools

LEFT PANEL - ORDERS QUEUE:
- Header with filters:
  * New/In Progress/Completed tabs
  * Priority sorting toggle
  * Search by order ID or sales department
- Card-based list of orders showing:
  * Order ID and submission date
  * Sales department name/contact
  * Number of items (with icon badges)
  * Priority flag (high = red flag icon)
  * Desired delivery date countdown (e.g., "15 days remaining")
  * Quick status badges
- Selected order highlights with blue left border

RIGHT PANEL - PROCESSING WORKSPACE:
Header Section:
- Order ID and basic info
- Action buttons:
  * Start Processing (primary)
  * Request Clarification (secondary)
  * Reject with Reason (danger outline)

Tab Navigation:
1. Items List Tab
2. Site Matching Tab
3. Inventory Check Tab
4. Order Allocation Tab
5. Final Review Tab

DESIGN:
- Clean, organized workflow
- Color-coded status indicators throughout
- Drag-and-drop interactions where applicable
- Progress percentage for each order
- Keyboard shortcuts displayed on hover
```

### Prompt 3.2: Site Matching & Selection Interface
```
Create an intelligent Site matching interface to find and select appropriate Import Sites for merchandise orders.

LAYOUT:
- Two-column layout with merchandise on left, matched sites on right
- Connection lines showing relationships

LEFT COLUMN - MERCHANDISE ITEMS:
- List of items from order with:
  * Merchandise code and name
  * Quantity needed
  * Unit
  * Desired delivery date (with calendar icon)
  * Status indicator (Unmatched/Partially Matched/Fully Matched)
- Expand each item to see potential sites

RIGHT COLUMN - SITE POOL:
- Site cards showing:
  * Site code and name
  * Country flag icon
  * Stock levels for relevant items (green/yellow/red indicator)
  * Delivery methods available:
    - Ship delivery: X days (boat icon)
    - Air delivery: Y days (plane icon)
  * Reliability score (5-star rating)
  * Current capacity status
- Filter sites by:
  * Region/Country
  * Delivery speed
  * Stock availability
  * Past performance

MATCHING ALGORITHM VISUALIZATION:
- Visual flowchart showing criteria:
  * Step 1: Filter by merchandise availability
  * Step 2: Check delivery date feasibility
  * Step 3: Rank by priority (Ship > Air, High Stock > Low Stock)
  * Step 4: Minimize number of sites
- Results shown as ranked list with scores

INTERACTION:
- Drag merchandise items to site cards to manually assign
- Auto-assign button that follows algorithm
- Conflict warnings when dates can't be met
- Alternative suggestions when primary sites unavailable

ACTION PANEL:
- "Send Inventory Query" button for selected sites
- Batch select multiple items for same site
- Save matching template for similar future orders
- Review & Confirm button
```

### Prompt 3.3: Inventory Request & Response Tracking
```
Design an inventory tracking interface to manage stock queries sent to Sites and receive responses.

LAYOUT:
- Dashboard-style with multiple sections

TOP SECTION - QUERY STATUS OVERVIEW:
- 4 metric cards:
  * Queries Sent (total count)
  * Responses Received (with percentage)
  * Pending Responses (with urgency indicator)
  * Average Response Time (in hours)

QUERIES TABLE:
Columns:
1. Query ID (auto-generated)
2. Sent Date/Time
3. Site Name (with country flag)
4. Items Queried (count with expand option)
5. Status:
   - Sent (blue clock icon)
   - Response Received (green check)
   - Partial Response (yellow warning)
   - Delayed (red alert, >24 hours)
6. Response Time
7. Actions (View Details, Resend Query, Mark Complete)

DETAIL VIEW MODAL:
When clicking a query, show:
- Query information header
- Items requested table:
  * Merchandise Code
  * Merchandise Name
  * Query Sent: [timestamp]
  * Response Received: [timestamp] or "Pending"
  * In-Stock Quantity (from Site response)
  * Unit
  * Status icon
- Communication log with Site
- Automated reminder settings
- Manual reminder button

RESPONSE PROCESSING:
- When response arrives, highlight new responses with animation
- Side panel to review and validate stock numbers
- Flag discrepancies or unusually low stock
- Confirm and save to Stock Information File button

FEATURES:
- Auto-refresh every 30 seconds for new responses
- Push notifications for urgent responses
- Bulk query sending for multiple sites
- Template saved queries for common merchandise sets
- Export response data to CSV
```

### Prompt 3.4: Order Allocation Decision Engine
```
Create a sophisticated allocation interface where Order Placement staff finalize merchandise distribution across Sites.

LAYOUT:
- Main workspace with allocation table
- Right sidebar with decision criteria and validation

HEADER:
- Order ID and merchandise summary
- Optimization mode selector:
  * Auto-Allocate (algorithm-based)
  * Manual Override
  * Hybrid (suggestions + manual)
- Constraint indicators (delivery dates, stock limits)

ALLOCATION TABLE:
Rows: Each merchandise item
Columns: 
1. Merchandise Info (code, name, total qty needed, unit)
2. Desired Delivery Date
3. Sites Selected (multi-select dropdown with stock info)
4. Allocated Quantities per Site (editable input fields)
   - Site A: [qty] | Delivery: Ship 15d / Air 5d
   - Site B: [qty] | Delivery: Ship 20d / Air 7d
5. Delivery Method Selection (Ship/Air radio buttons per site)
6. Expected Arrival Date (calculated, with warning if past desired date)
7. Status (Complete/Incomplete icon)

VISUAL INDICATORS:
- Color coding for allocation status:
  * Green: Fully allocated, meets deadline
  * Yellow: Fully allocated, tight deadline
  * Orange: Partially allocated
  * Red: Cannot meet requirements
- Progress bars showing allocation percentage per item
- Warning icons for conflicts

RIGHT SIDEBAR - DECISION SUPPORT:
Priority Criteria Reminder:
1. ✓ Prioritize Ship over Air (cost efficiency)
2. ✓ Prioritize high-stock sites (reliability)
3. ✓ Minimize number of sites (complexity reduction)

Validation Panel:
- Delivery date feasibility check (pass/fail for each item)
- Total cost estimate (ship vs air comparison)
- Site capacity check (not over-allocating)
- List of conflicts/issues to resolve

ACTIONS:
- "Run Auto-Allocation" button (follows priority rules)
- "Validate Allocation" button (checks all constraints)
- "Save as Draft" (can return later)
- "Finalize & Send Orders" (primary action)
- "Request Exception Approval" (for impossible deadlines)

FEATURES:
- Undo/Redo allocation changes
- Comparison view: show multiple allocation scenarios side-by-side
- Copy allocation from similar past order
- Export allocation summary for review
```

### Prompt 3.5: Order Dispatch & Confirmation
```
Design the final step interface for sending confirmed orders to selected Sites.

LAYOUT:
- Confirmation review screen with clear sections

HEADER:
- "Finalize Order Dispatch" title
- Order ID and reference number
- Total sites involved (with avatar stack)

REVIEW SECTION 1 - ORDER SUMMARY:
Card showing:
- Original request from Sales Department
- Total items: X
- Total quantity across all items
- Desired delivery window
- Priority level

REVIEW SECTION 2 - SITE ALLOCATION BREAKDOWN:
For each Site, a card containing:
- Site Header:
  * Site name and code
  * Country flag
  * Contact information
  * Reliability rating
- Items table for this site:
  * Merchandise Code
  * Merchandise Name
  * Quantity Ordered
  * Unit
  * Delivery Method (Ship/Air badge)
  * Expected Delivery Date
- Site-specific notes/instructions textarea
- Estimated total cost for this site (if available)

COMMUNICATION SECTION:
- Order message template editor
- Standard order confirmation format pre-filled:
  * Order Date
  * Order ID
  * Items table
  * Delivery requirements
  * Contact information
- Attachment upload (supporting documents)
- Delivery method confirmation request

ACTION PANEL:
- "Send Orders to All Sites" primary button
- "Send to Selected Sites" secondary button
- "Save and Send Later" outline button
- "Cancel Entire Order" danger button

CONFIRMATION MODAL:
After clicking Send:
- Success animation
- List of sites with "Sent" checkmarks
- Email/System notification confirmation
- Order tracking number generated
- "View Tracking" and "Return to Dashboard" buttons

POST-DISPATCH:
- Order status automatically updated to "Orders Dispatched"
- Notification sent to Sales Department
- Email confirmations sent to all Sites
- Entry created in Order Tracking system
```

---

## 🏪 4. SITE HỢP TÁC (IMPORT SITES INTERFACE)

### Prompt 4.1: Site Portal - Inventory Query Response
```
Design a simple, efficient portal for Import Sites to respond to inventory queries from the Order Placement team.

LAYOUT:
- Clean, focused interface
- Mobile-responsive design (since Sites may use tablets/mobile)

HEADER:
- Site logo placeholder
- Site name and code
- Language selector
- Logout button

DASHBOARD:
- "Pending Queries" prominent section with count badge
- "Recent Orders" section
- "Inventory Management" quick link
- "Communication Center" link

QUERIES INBOX:
- List of pending queries with:
  * Query ID
  * Received Date/Time
  * Urgency indicator (standard/urgent red flag)
  * Number of items queried
  * Respond button (primary action)

QUERY RESPONSE FORM:
- Query details header:
  * From: Overseas Order Placement Department
  * Received: [timestamp]
  * Query ID: [number]

Items Table (editable):
Columns:
1. Merchandise Code (read-only)
2. Merchandise Name (read-only)
3. Current Stock Quantity (editable input, number)
   - Placeholder: "Enter quantity"
   - Validation: must be ≥ 0
4. Unit (dropdown: pcs, kg, boxes, cartons, pallets)
5. Additional Notes (optional text input)
   - e.g., "Restocking in 3 days", "Limited availability"

FEATURES:
- Quick actions:
  * "Mark All as 0" button for out-of-stock scenarios
  * "Import from Inventory System" button
  * Bulk edit option
- Auto-save draft (every 30 seconds)
- "Submit Response" button (validates all required fields)
- "Save Draft" button
- "Need More Information" button (sends message back)

CONFIRMATION:
- Success message: "Response submitted successfully"
- Timestamp of submission
- Receipt confirmation number
- "View Submitted Response" link

DESIGN:
- Large, touch-friendly input fields
- Clear labels and instructions
- Minimal distractions
- Focus on data entry efficiency
- Validation messages in red below fields
- Green checkmarks for completed fields
```

### Prompt 4.2: Site Order Fulfillment Dashboard
```
Create an order management dashboard for Sites to track and manage orders from the company.

LAYOUT:
- Full-width dashboard with multiple sections

TOP METRICS:
- 4 KPI cards:
  * Active Orders (current count)
  * This Month's Shipments (with trend)
  * Pending Confirmations (action required)
  * Delivery Performance (on-time %)

ORDERS TABLE:
Columns:
1. Order ID (clickable for details)
2. Received Date
3. Items Count (badge)
4. Total Quantity (sum across items)
5. Delivery Method Requested (Ship/Air icon badge)
6. Expected Ship Date (calculated based on processing time)
7. Status:
   - New (blue)
   - Confirmed (green)
   - Preparing (yellow)
   - Shipped (purple)
   - Delivered (green check)
8. Actions (Confirm Order, View Details, Mark Shipped)

FILTERS:
- Status filter chips
- Date range
- Delivery method
- Search by order ID or merchandise code

ORDER DETAIL MODAL:
- Order information header with ID and dates
- Items table:
  * Merchandise Code
  * Quantity Ordered
  * Unit
  * Delivery Method
  * Current packing status
- Shipping information section:
  * Carrier selection dropdown
  * Tracking number input
  * Actual ship date picker
  * Estimated delivery date (auto-calculated)
  * Upload shipping documents button
- Notes/communications with Order Placement team
- "Confirm Order Receipt" button
- "Update Status" button
- "Mark as Shipped" button (requires tracking info)

SHIPMENT TRACKING:
- Visual timeline for each order:
  * Order Received
  * Order Confirmed
  * Items Prepared
  * Shipped
  * In Transit
  * Delivered
- Photo upload capability for packaging/shipping proof
- Delivery confirmation request

DESIGN:
- Clear status indicators with icons
- Easy bulk actions for multiple orders
- Quick confirm buttons prominently placed
- Notification badges for actions needed
```

---

## 📦 5. BỘ PHẬN QUẢN LÝ KHO (WAREHOUSE MANAGEMENT)

### Prompt 5.1: Incoming Shipments Dashboard
```
Design a shipment receiving interface for Warehouse team to track incoming deliveries and verify against orders.

LAYOUT:
- Full-screen dashboard with left panel and main area

LEFT PANEL - EXPECTED SHIPMENTS:
- "Expected Arrivals" section header
- Filters:
  * Today/This Week/This Month
  * By Site
  * By Delivery Method (Ship/Air)
- List of expected shipments:
  * Shipment ID
  * Expected Arrival Date (with countdown)
  * Site Name (with flag)
  * Items Count
  * Delivery Method badge
  * Status (On Time/Delayed indicator)

MAIN AREA - RECEIVING WORKSPACE:
Header:
- "Receive Shipment" title
- Shipment ID input/scanner
- Search by Order ID or Site

RECEIVING FORM:
1. Shipment Information Section:
   - Shipment ID (auto-filled or manual entry)
   - Actual Arrival Date/Time picker
   - Site Name (auto-populated)
   - Carrier Information
   - Tracking Number
   - Upload delivery documents (POD, packing list)

2. Items Verification Table:
   Columns:
   - Merchandise Code
   - Merchandise Name
   - Quantity Ordered (from system, read-only)
   - Unit
   - Quantity Received (editable input)
   - Variance (auto-calculated: Received - Ordered)
     * Green if match
     * Yellow if over/under ±5%
     * Red if over/under >5%
   - Condition (dropdown: Good/Damaged/Missing)
   - Notes (text input for discrepancies)
   - Photos (upload button for damaged items)

3. Quality Check Section:
   - Overall condition rating (1-5 stars)
   - Packaging quality
   - Temperature check (if applicable)
   - Inspection notes textarea

ACTIONS:
- "Confirm All Items Received" button (green, if no variances)
- "Report Discrepancies" button (orange, if variances found)
- "Accept with Exceptions" button (yellow)
- "Reject Shipment" button (red, rare)

VARIANCE HANDLING:
When discrepancies detected:
- Modal popup with summary of all variances
- Require reason selection for each variance:
  * Overage: "Bonus from Site", "Counting Error", "Other"
  * Shortage: "Site Short-shipped", "Damaged in Transit", "Other"
- Automatic notification to Order Placement team
- Photo evidence upload strongly encouraged
- Require supervisor approval for large variances

CONFIRMATION:
- Success screen showing:
  * Items successfully received and added to inventory
  * Variances logged (if any)
  * Notifications sent (to Sales, Order Placement)
  * Print receiving report option
  * "Complete and Next Shipment" button

FEATURES:
- Barcode/QR code scanner integration
- Real-time inventory update
- Automatic discrepancy alerts
- Photo documentation for all shipments
- Digital signature for receiver
```

### Prompt 5.2: Warehouse Inventory Overview
```
Create a comprehensive inventory management interface for the Warehouse team.

LAYOUT:
- Dashboard with multiple widgets and a main inventory table

TOP SECTION - INVENTORY METRICS:
- 5 KPI cards in a row:
  * Total Items in Stock (with trend arrow)
  * Low Stock Alerts (count with red badge if >0)
  * Items Received This Week (with comparison)
  * Items Dispatched This Week
  * Warehouse Utilization (percentage with visual gauge)

INVENTORY TABLE:
Columns:
1. Merchandise Code (sortable, searchable)
2. Merchandise Name
3. Category (color-coded chip)
4. Current Stock Quantity
5. Unit
6. Minimum Stock Level (alert threshold)
7. Stock Status:
   - In Stock (green, >minimum)
   - Low Stock (yellow, ≤minimum but >0)
   - Out of Stock (red, 0)
8. Last Received Date
9. Last Dispatched Date
10. Location in Warehouse (e.g., "A-12-03")
11. Actions (View History, Adjust, Move)

FILTERS & SEARCH:
- Search by code, name, category
- Filter by:
  * Stock status
  * Category
  * Site of origin
  * Date range
- Sort by any column
- Export to Excel button

STOCK MOVEMENT HISTORY:
When clicking "View History" on an item:
- Modal showing timeline of all transactions:
  * Date/Time
  * Type (Received/Dispatched/Adjusted/Transferred)
  * Quantity Change (+/-)
  * Balance After
  * Related Order ID
  * User who performed action
  * Notes
- Graph showing stock level over time

STOCK ADJUSTMENT:
- "Adjust Stock" button opens modal:
  * Current quantity (read-only)
  * New quantity (input)
  * Reason dropdown:
    - Damaged goods removed
    - Found discrepancy
    - Returned to Site
    - Transferred to other location
    - Other (requires explanation)
  * Notes (required for adjustments)
  * Approval requirement for large adjustments
  * Photo upload for evidence

LOW STOCK ALERTS:
- Dedicated widget showing items below minimum:
  * Merchandise Code and Name
  * Current Stock vs Minimum
  * Suggested Reorder Quantity
  * "Notify Sales Department" button
  * Quick reorder action

DESIGN:
- Color-coded stock status throughout
- Visual indicators for urgent actions
- Quick filters prominently displayed
- Bulk actions support (adjust multiple items)
- Real-time updates when shipments received
```

---

## ⚙️ 6. QUẢN TRỊ VIÊN HỆ THỐNG (SYSTEM ADMINISTRATOR)

### Prompt 6.1: User Management Interface
```
Design a comprehensive user and role management interface for System Administrators.

LAYOUT:
- Split view: Users list on left, details/editor on right

LEFT PANEL - USERS LIST:
- Header with "User Management" title
- "Add New User" button (primary, top-right)
- Search bar (by name, email, department)
- Filters:
  * All Users
  * By Role (Sales/Order Placement/Site/Warehouse/Admin)
  * Active/Inactive status
  * By Department

Users Table:
Columns:
1. Avatar (circular, with initials if no photo)
2. Full Name
3. Email
4. Role (colored badge)
5. Department
6. Status (Active/Inactive toggle)
7. Last Login (timestamp)
8. Actions (Edit, Deactivate, Delete icons)

RIGHT PANEL - USER DETAILS/EDITOR:
When selecting a user or adding new:

BASIC INFORMATION:
- Profile photo upload (circular, with drag-drop or browse)
- Full Name (text input, required)
- Email (text input, required, validation)
- Phone Number (text input with country code dropdown)
- Employee ID (auto-generated or manual)
- Department (dropdown)

ROLE & PERMISSIONS:
- Role Selection (radio buttons):
  * Sales Department Staff
  * Overseas Order Placement Staff
  * Import Site User
  * Warehouse Staff
  * System Administrator
- Each role shows description and permission summary
- Custom Permissions section (expandable):
  * Can View Orders (checkbox)
  * Can Create Orders (checkbox)
  * Can Modify Orders (checkbox)
  * Can Delete Orders (checkbox)
  * Can Manage Sites (checkbox)
  * Can Access Reports (checkbox)
  * Can Manage Users (checkbox, admin only)
  * Can Configure System (checkbox, admin only)

ACCOUNT SETTINGS:
- Username (unique, validation)
- Temporary Password (auto-generate button or manual input)
- Require Password Change on First Login (checkbox)
- Account Expiration Date (date picker, optional)
- Multi-Factor Authentication Required (toggle)

ACTIONS:
- "Save User" button (primary)
- "Save and Add Another" button
- "Cancel" button
- For existing users: "Deactivate Account" and "Reset Password" buttons

USER ACTIVITY LOG:
- Tab showing user's recent activities:
  * Login/Logout times
  * Actions performed (order created, shipment received, etc.)
  * IP addresses
  * Device information
- Export activity log button

DESIGN:
- Clear separation between active and inactive users
- Visual role badges with distinct colors
- Required fields marked clearly
- Inline validation for email and username
- Confirmation dialogs for destructive actions
```

### Prompt 6.2: Site Configuration Management
```
Create an interface for administrators to manage Import Site information and configuration.

LAYOUT:
- Grid/card view of all Sites with detail panel

SITES OVERVIEW:
- Header with "Import Sites Management" (50 sites currently)
- "Add New Site" button
- View toggle: Grid View / List View
- Search and filters:
  * By Region/Country
  * By Active/Inactive status
  * By Performance Rating
  * By Merchandise Categories

GRID VIEW - SITE CARDS:
Each card shows:
- Site name and code (header)
- Country flag icon and location
- Active/Inactive status badge
- Performance rating (5 stars)
- Number of merchandise items handled
- Last order date
- Quick actions: Edit, View Details, Deactivate

LIST VIEW - SITE TABLE:
Columns:
1. Site Code
2. Site Name
3. Country (flag + name)
4. Active Status (toggle switch)
5. Merchandise Categories Count
6. Ship Delivery Days
7. Air Delivery Days
8. Performance Rating
9. Last Updated
10. Actions

SITE DETAIL/EDIT MODAL:
Tabs: Basic Info | Delivery Settings | Merchandise Catalog | Contact Info | Performance

TAB 1 - BASIC INFORMATION:
- Site Code (unique, required)
- Import Site Name (required)
- Country (dropdown with flags)
- Full Address (textarea)
- Time Zone
- Currency
- Active Status (toggle with confirmation)
- Registration Date (read-only)

TAB 2 - DELIVERY SETTINGS:
- Ship Delivery Section:
  * Number of Days for Delivery by Ship (input)
  * Ship Delivery Available (toggle)
  * Ship Carriers Used (multi-select)
  * Special Shipping Notes (textarea)
- Air Delivery Section:
  * Number of Days for Delivery by Air (input)
  * Air Delivery Available (toggle)
  * Air Carriers Used (multi-select)
  * Air Freight Notes (textarea)
- Delivery Schedule (weekdays available)
- Minimum Order Quantity (input)
- Maximum Order Capacity per Month (input)

TAB 3 - MERCHANDISE CATALOG:
- "Merchandise Items This Site Handles" header
- Add Merchandise button
- Search/filter merchandise
- Table of merchandise with:
  * Merchandise Code
  * Merchandise Name
  * Category
  * Typical Stock Level
  * Last Updated Stock Info
  * Remove from Site button
- Bulk import from CSV option
- Export current catalog button

TAB 4 - CONTACT INFORMATION:
- Primary Contact Person:
  * Name
  * Title/Position
  * Email
  * Phone
- Secondary Contact Person (optional)
- Emergency Contact (after hours)
- Preferred Communication Method (dropdown)
- Language Preference
- Time Zone for Contact (auto from country)

TAB 5 - PERFORMANCE METRICS:
(Read-only analytics)
- Performance Rating visualization (star rating with breakdown)
- Total Orders Fulfilled (number)
- On-Time Delivery Rate (percentage with chart)
- Order Accuracy Rate (percentage)
- Average Response Time to Queries (hours)
- Recent Performance Trend (graph, last 6 months)
- Notes on Performance Issues (text log)

ACTIONS:
- "Save Site Information" button
- "Save and Close" button
- "Delete Site" button (with strong confirmation warning)
- "Deactivate Site" toggle (softer than delete)

BULK OPERATIONS:
- Select multiple sites for:
  * Bulk update delivery days
  * Bulk activate/deactivate
  * Export selected sites data
  * Send notification to multiple sites

DESIGN:
- Map integration showing site locations globally
- Color-coded performance indicators
- Clear visual separation between active and inactive
- Warning before deleting sites with active orders
```

### Prompt 6.3: System Settings & Configuration
```
Design a comprehensive system settings interface for configuring application-wide parameters.

LAYOUT:
- Left sidebar with settings categories
- Main panel with settings content

SIDEBAR CATEGORIES:
1. General Settings
2. Order Management
3. Notifications
4. Email Templates
5. Data & Reports
6. Security
7. Integrations
8. Backup & Maintenance
9. Audit Logs

GENERAL SETTINGS:
- Application Name (text input)
- Company Logo Upload
- Default Language (dropdown)
- Default Time Zone
- Default Currency
- Date Format (MM/DD/YYYY or DD/MM/YYYY)
- Number Format (decimal separator, thousands separator)
- Business Hours (time range picker)
- Support Contact Information

ORDER MANAGEMENT SETTINGS:
- Auto-Assign Order IDs (toggle, format: ORD-YYYYMMDD-XXXX)
- Default Priority for New Orders (High/Medium/Low)
- Enable Order Draft Auto-Save (toggle, interval: X minutes)
- Order Approval Workflow (toggle, configure approvers)
- Delivery Date Buffer (days before marking as late)
- Automatic Site Selection (toggle algorithm on/off)
- Site Selection Criteria Weights:
  * Delivery Method Priority (Ship vs Air)
  * Stock Level Weight (slider 0-100%)
  * Site Count Minimization (slider 0-100%)
- Maximum Sites per Order (number input)

NOTIFICATION SETTINGS:
For each user role, configure:
- Email Notifications (toggle)
  * New Order Assigned
  * Inventory Query Received
  * Shipment Arrived
  * Low Stock Alert
  * Order Delayed Warning
- In-App Notifications (toggle, same categories)
- SMS Notifications (toggle, for urgent only)
- Notification Frequency (real-time/daily digest/weekly)
- Quiet Hours (no notifications during these times)

EMAIL TEMPLATE EDITOR:
- Template List:
  * Order Confirmation to Site
  * Inventory Query Request
  * Shipment Notification
  * Low Stock Alert
  * User Welcome Email
  * Password Reset
- Rich text editor with variables:
  * {{ORDER_ID}}
  * {{SITE_NAME}}
  * {{DELIVERY_DATE}}
  * {{USER_NAME}}
  * etc.
- Preview button
- Send Test Email button
- Reset to Default Template button

DATA & REPORTS:
- Default Report Date Range (dropdown)
- Auto-Generate Reports Schedule:
  * Daily Order Summary (time picker)
  * Weekly Performance Report (day + time)
  * Monthly Analytics (date + time)
- Report Recipients (multi-select users)
- Data Retention Policy:
  * Keep Orders for X years
  * Keep Audit Logs for Y years
  * Auto-Archive Settings
- Export Format Defaults (Excel/CSV/PDF)

SECURITY SETTINGS:
- Password Policy:
  * Minimum Length (8-20 characters)
  * Require Uppercase (toggle)
  * Require Numbers (toggle)
  * Require Special Characters (toggle)
  * Password Expiration (days, or never)
  * Password History (prevent reuse of last X passwords)
- Session Management:
  * Session Timeout (minutes of inactivity)
  * Maximum Concurrent Sessions per User
  * Force Logout on All Devices button
- Two-Factor Authentication:
  * Enforce 2FA for All Users (toggle)
  * Enforce 2FA for Admins Only (toggle)
  * Allowed 2FA Methods (SMS/Authenticator App/Email)
- IP Whitelisting:
  * Enable IP Restrictions (toggle)
  * Allowed IP Addresses/Ranges (list)
  * Add/Remove IP button

INTEGRATIONS:
- Available Integrations list:
  * ERP System (connect button, status indicator)
  * Accounting Software
  * Email Service (SMTP settings)
  * SMS Gateway
  * Cloud Storage (for documents)
  * External Warehouse System
- For each integration:
  * Configuration fields (API keys, endpoints, etc.)
  * Test Connection button
  * Enable/Disable toggle
  * Sync Frequency (for data integrations)
  * Last Sync Status and Time

BACKUP & MAINTENANCE:
- Automatic Backup Schedule:
  * Full Backup Frequency (daily/weekly)
  * Incremental Backup (hourly/daily)
  * Backup Time (time picker)
  * Backup Location (server path or cloud)
- Manual Backup Actions:
  * "Backup Now" button
  * "Restore from Backup" button (with date selector)
  * Download Backup File button
- Maintenance Mode:
  * Enable Maintenance Mode (toggle with warning)
  * Maintenance Message (text for users)
  * Scheduled Maintenance (date/time picker)
- System Health:
  * Database Size (GB)
  * Storage Space Used/Available
  * Last Successful Backup (timestamp)
  * System Uptime

AUDIT LOGS VIEWER:
- Filter logs by:
  * Date range
  * User
  * Action type (Login, Create, Update, Delete, etc.)
  * Module (Orders, Sites, Warehouse, Users)
- Logs table:
  * Timestamp
  * User
  * Action
  * Entity Affected (e.g., Order #12345)
  * IP Address
  * Details (expandable)
- Export Logs button
- Clear Old Logs button (with retention policy check)

DESIGN:
- Save indicator: "All changes saved" with timestamp
- Unsaved changes warning when navigating away
- Restore defaults button for each section
- Help tooltips for complex settings
- Validation for required fields
- Preview/test functionality where applicable
- Clear visual hierarchy for settings groups
```

---

## 📊 7. REPORTS & ANALYTICS (ALL ROLES)

### Prompt 7.1: Analytics Dashboard
```
Design a comprehensive analytics dashboard showing key metrics and insights for the import ordering system.

LAYOUT:
- Full-width dashboard with multiple sections
- Filter panel at top for date range and dimensions

TOP FILTERS:
- Date Range Selector:
  * Quick options: Today, Last 7 Days, Last 30 Days, Last Quarter, Last Year
  * Custom Range picker
- Compare to Previous Period (toggle)
- Department/Role Filter (if admin)
- Export Dashboard button (PDF/Excel)

SECTION 1 - KEY PERFORMANCE INDICATORS:
6 metric cards in 2 rows:
Row 1:
- Total Orders (with trend % vs previous period)
- Order Fulfillment Rate (percentage with gauge)
- Average Delivery Time (days, with trend)
Row 2:
- Total Sites Active (count with status breakdown)
- Inventory Turnover Rate
- System Utilization (orders vs capacity)

Each card has:
- Large number/percentage
- Trend indicator (↑↓ with color)
- Sparkline mini-chart showing trend
- Click to drill down

SECTION 2 - ORDERS OVER TIME:
- Line chart showing:
  * Orders Created (blue line)
  * Orders Fulfilled (green line)
  * X-axis: Time (daily/weekly/monthly based on range)
  * Y-axis: Number of orders
- Toggle between view modes: Line/Bar/Area chart
- Hover tooltips with exact values

SECTION 3 - SITE PERFORMANCE:
- Horizontal bar chart showing top 10 sites by:
  * Total Orders Fulfilled
  * On-Time Delivery Rate
  * Average Response Time
- Site name with flag on Y-axis
- Metric value on X-axis
- Color-coded bars (green for good, yellow for average, red for issues)
- "View All Sites" link

SECTION 4 - DELIVERY METHOD ANALYSIS:
- Donut chart showing split:
  * Ship Delivery (% and count)
  * Air Delivery (% and count)
- Center shows total shipments
- Legend with percentages
- Cost comparison sub-text

SECTION 5 - TOP MERCHANDISE ITEMS:
- Table of top 20 most-ordered items:
  * Rank
  * Merchandise Code
  * Merchandise Name
  * Total Quantity Ordered
  * Number of Orders
  * Trend (↑↓)
- Sortable columns
- Export to CSV button

SECTION 6 - ORDER STATUS BREAKDOWN:
- Stacked bar chart showing order counts by status:
  * Pending Review
  * Processing
  * Orders Sent
  * In Transit
  * Delivered
  * Issues/Delayed
- Each segment color-coded
- Hover for exact numbers
- Time dimension on X-axis

SECTION 7 - REGIONAL ANALYSIS:
- World map visualization:
  * Sites marked as pins/circles
  * Size of circle = volume of orders
  * Color = performance (green/yellow/red)
  * Click on site for quick stats popup
- Region breakdown table below map

SECTION 8 - VARIANCE & EXCEPTIONS:
- List of recent issues:
  * Delivery Delays (count, with link to details)
  * Stock Shortages (count)
  * Order Variances (quantity discrepancies)
  * Site Unresponsive (count)
- Each with action link to investigate

DESIGN:
- Clean, modern data visualization
- Consistent color scheme across all charts
- Interactive elements (hover, click, drill-down)
- Responsive layout adapting to screen size
- Loading skeletons while data fetches
- Empty states for no data scenarios
- Refresh button with auto-refresh toggle
```

---

## 🔔 8. NOTIFICATIONS & COMMUNICATION

### Prompt 8.1: Notification Center
```
Design a centralized notification system accessible to all users.

LAYOUT:
- Bell icon in header with badge showing unread count
- Click opens dropdown panel (400px wide, max 600px tall)

NOTIFICATION PANEL:
Header:
- "Notifications" title
- "Mark All as Read" link
- Filter tabs: All / Unread / Mentions / Alerts
- Settings icon (opens notification preferences)

Notification List:
Each notification shows:
- Icon based on type (colored circle with symbol):
  * Order: shopping cart icon (blue)
  * Inventory: box icon (orange)
  * Shipment: truck icon (purple)
  * System: bell icon (gray)
  * Alert: warning triangle (red)
- Notification text (bold if unread)
- Timestamp (e.g., "2 hours ago")
- Related entity link (e.g., "Order #12345")
- Unread indicator (blue dot on left)
- Hover actions: Mark as Read, Delete

Notification Types Examples:
- "New order request #12345 assigned to you"
- "Site XYZ responded to inventory query"
- "Shipment #789 has arrived at warehouse"
- "Low stock alert: Item ABC-001 below minimum"
- "Order #456 delivery delayed - action required"
- "@John mentioned you in a comment on Order #789"

ACTIONS:
- Click notification → navigates to related page and marks as read
- Swipe left on mobile → Delete
- Long press → context menu (Mark Read, Delete, Mute Similar)

FOOTER:
- "View All Notifications" link (opens full-page view)
- Notification preferences link

FULL-PAGE VIEW:
- Same as panel but full width
- Infinite scroll / pagination
- Advanced filters:
  * Date range
  * Notification type
  * Related module
  * Priority (All/High/Normal)
- Bulk actions:
  * Select multiple
  * Mark as Read/Unread
  * Delete selected
  * Archive selected

PREFERENCES MODAL:
Configure per notification type:
- Enable/Disable (toggle)
- Delivery method:
  * In-App (default)
  * Email (checkbox)
  * SMS (checkbox, for high priority only)
- Frequency:
  * Real-time
  * Daily digest
  * Weekly summary
- Quiet hours (time range picker)

DESIGN:
- Badge updates in real-time
- Smooth animations for new notifications
- Sound notification (toggle in preferences)
- Desktop browser notifications (if permitted)
- Distinct styling for high-priority/urgent notifications
- Unread notifications stand out visually
- Skeleton loaders while fetching
```

---

## 🔍 9. SEARCH & FILTERING SYSTEM

### Prompt 9.1: Global Search Interface
```
Design a powerful global search feature accessible from any page.

TRIGGER:
- Search bar in top header (400px width)
- Keyboard shortcut: Cmd/Ctrl + K
- Placeholder: "Search orders, sites, merchandise..." with icon

SEARCH DROPDOWN:
Opens below search bar (600px wide, max 500px tall):

QUICK FILTERS (Chips/Pills):
- All Results
- Orders
- Merchandise
- Sites
- Shipments
- Users (admin only)

SEARCH RESULTS:
Grouped by entity type:

ORDERS:
- Order ID (clickable)
- Status badge
- Date
- Sales department
- Quick action: "View Details" button
- Shows top 3, "See all X orders →" link if more

MERCHANDISE:
- Code and Name
- Current stock level
- Category
- Quick action: "View Inventory" button

SITES:
- Site code and name
- Country flag
- Active/inactive badge
- Quick action: "View Site" button

SHIPMENTS:
- Shipment ID
- Expected arrival date
- Status
- Quick action: "Track Shipment" button

RECENT SEARCHES:
If search is empty, show:
- "Recent Searches" section
- List of last 5 searches with clock icon
- "Clear recent searches" link

NO RESULTS:
- Empty state illustration
- "No results found for '[query]'"
- Suggestions: "Try different keywords" or "Check spelling"
- Quick links to create new order/add site/etc.

ADVANCED SEARCH:
- "Advanced Search" link at bottom
- Opens modal with detailed filters:
  * Entity type selection
  * Date range
  * Status filters
  * Custom field filters
  * Sort options
- "Search" button to execute

DESIGN:
- Real-time search (debounced, 300ms)
- Highlight matching text in results
- Keyboard navigation (up/down arrows, enter to select)
- ESC to close
- Loading state with skeleton screens
- Max 15 results total in dropdown, then "View All Results" page
```

---

## 📱 10. RESPONSIVE & MOBILE CONSIDERATIONS

### Prompt 10.1: Mobile-Responsive Adaptations
```
Although this is a desktop app, design mobile/tablet responsive views for key interfaces that Sites might access on mobile devices.

RESPONSIVE BREAKPOINTS:
- Desktop: ≥1280px
- Tablet: 768px - 1279px
- Mobile: <768px

MOBILE ADAPTATIONS:

SITE PORTAL (Mobile-First):
- Bottom navigation bar with icons:
  * Home
  * Queries (with badge)
  * Orders
  * Profile
- Hamburger menu for secondary functions
- Full-width cards instead of tables
- Swipe gestures:
  * Swipe right on notification: Mark as read
  * Swipe left on order: Quick actions menu
  * Pull down to refresh

INVENTORY QUERY RESPONSE (Mobile):
- Vertical card layout
- Large, touch-friendly input fields (min 44px height)
- Number keypad for quantity inputs
- Floating "Submit Response" button at bottom
- Sticky header showing query ID
- Collapsible sections for long lists

ORDER DETAILS (Tablet):
- Side-by-side layout (50-50 split)
- Left: Order info
- Right: Items list
- Bottom action bar with primary buttons

DESIGN PRINCIPLES:
- Touch targets minimum 44x44px
- Large fonts (min 16px for body text)
- Clear visual hierarchy
- Minimize typing (use dropdowns, toggles, buttons)
- Progressive disclosure (show details on tap)
- Offline support for critical functions
- Loading indicators for network operations
```

---

## 🎯 ADDITIONAL FEATURES & EDGE CASES

### Prompt 11.1: Error States & Empty States
```
Design friendly error and empty state screens for various scenarios.

ERROR STATES:

1. Network Error:
- Icon: WiFi with X
- Title: "Connection Lost"
- Message: "Please check your internet connection and try again."
- Action: "Retry" button
- Secondary: "Go to Dashboard" link

2. Permission Denied:
- Icon: Lock or shield
- Title: "Access Denied"
- Message: "You don't have permission to view this page."
- Action: "Contact Administrator" button
- Secondary: "Go Back" link

3. 404 Not Found:
- Icon: Magnifying glass with question mark
- Title: "Page Not Found"
- Message: "The page you're looking for doesn't exist."
- Action: "Go to Dashboard" button
- Secondary: Search bar to find what you're looking for

4. Server Error (500):
- Icon: Server with warning
- Title: "Something Went Wrong"
- Message: "We're experiencing technical difficulties. Please try again later."
- Error ID: "Error ID: 12345 (for support reference)"
- Action: "Refresh Page" button
- Secondary: "Report Issue" link

EMPTY STATES:

1. No Orders Yet:
- Illustration: Empty list or clipboard
- Title: "No Orders Yet"
- Message: "Get started by creating your first order request."
- Action: "Create New Order" button (primary, large)

2. No Search Results:
- Illustration: Empty search result
- Title: "No Results Found"
- Message: "Try adjusting your filters or search terms."
- Action: "Clear Filters" button

3. No Notifications:
- Illustration: Bell with check mark
- Title: "You're All Caught Up!"
- Message: "No new notifications."
- Optional: Cute animation

4. No Sites Available:
- Illustration: Globe or empty pins
- Title: "No Sites Found"
- Message: "No import sites match your criteria."
- Action: "Add New Site" button (if admin)

DESIGN PRINCIPLES:
- Friendly, non-technical language
- Helpful illustrations (not intimidating)
- Clear calls-to-action
- Provide context and next steps
- Use brand colors consistently
- Maintain personality even in errors
```

---

## 🚀 ONBOARDING & HELP

### Prompt 12.1: First-Time User Onboarding
```
Design an onboarding experience to guide new users through the system.

WELCOME SCREEN:
- Full-screen overlay (skippable)
- Welcome message: "Welcome to [System Name]"
- Brief introduction: "Your complete solution for managing import orders"
- "Get Started" button
- "Skip Tour" link

GUIDED TOUR (Role-Based):
Interactive tooltips/popovers highlighting key features:

For Sales Department:
1. Dashboard Overview
   - Tooltip: "This is your dashboard where you can see all your orders at a glance"
2. Create Order Button
   - Tooltip: "Click here to create a new order request"
3. Order Status Filters
   - Tooltip: "Filter your orders by status to find what you need quickly"
4. Finish: "You're all set! Explore on your own or contact support."

For Order Placement:
1. Incoming Orders Queue
2. Site Matching Tool
3. Allocation Algorithm
4. Dispatch Workflow

INTERACTIVE DEMO MODE:
- "Try It Out" sandbox environment
- Sample data pre-loaded
- Guided tasks:
  * "Create a sample order"
  * "Respond to an inventory query"
  * "Receive a shipment"
- Progress tracker (e.g., "2 of 5 tasks completed")
- "Exit Demo" button

HELP CENTER ACCESS:
- "?" icon in header
- Click opens:
  * Quick Help (contextual help for current page)
  * Search Help Articles
  * Video Tutorials
  * Contact Support
  * Keyboard Shortcuts
  * What's New (release notes)

CONTEXTUAL HELP:
- "?" icons next to complex fields
- Hover to show tooltip with explanation
- "Learn more" link to detailed help article

TOOLTIPS DESIGN:
- Positioned to avoid obscuring content
- Arrow pointing to relevant element
- "Next" and "Previous" buttons
- "Skip Tour" option
- Progress dots (e.g., 3/7)
- Keyboard navigation (arrow keys, ESC to close)
```

---

## 📋 FINAL GUIDELINES

**Color Accessibility:**
- Ensure all text has at least 4.5:1 contrast ratio (WCAG AA)
- Don't rely solely on color to convey information (use icons + text)
- Provide colorblind-friendly color schemes

**Consistency:**
- Use the same components across all screens (buttons, inputs, cards)
- Maintain consistent spacing and layout grid
- Reuse design patterns (e.g., all tables look similar)

**Performance:**
- Show loading states for all async operations
- Use skeleton screens instead of generic spinners
- Optimize for fast initial load (<3 seconds)

**Internationalization:**
- Design for text expansion (some languages 30% longer than English)
- Use flags carefully (not all users associate with flags)
- RTL language support consideration
- Date/time/number format localization

**Data Density:**
- Balance information density with readability
- Provide comfortable (default), compact, and expanded view modes for tables
- Use progressive disclosure for complex data

**Icons:**
- Use consistent icon library throughout (Lucide, Heroicons, or Feather)
- Ensure icons have labels or tooltips for accessibility
- Size: 16px for inline, 20px for buttons, 24px for headers

**Feedback & Validation:**
- Real-time validation for form fields
- Success messages are non-intrusive (toast/snackbar)
- Error messages are helpful and specific
- Confirmation dialogs for destructive actions

**Keyboard Navigation:**
- All interactive elements accessible via keyboard
- Clear focus indicators
- Logical tab order
- Keyboard shortcuts for power users

---

## 📦 DELIVERABLES CHECKLIST

When using these prompts in Figma, ensure you create:

✅ Complete design system with components library
✅ All main user flows for each role
✅ Responsive layouts (desktop primary, mobile for Sites)
✅ Error and empty states
✅ Loading states and animations
✅ Interactive prototypes for key workflows
✅ Accessibility annotations
✅ Developer handoff documentation

---

## 🎯 HOW TO USE THESE PROMPTS

1. **Start with Design System**: Use the style guide prompt first to establish colors, typography, and spacing
2. **Build Component Library**: Create reusable components (buttons, inputs, cards, tables) based on the system
3. **Create Screens by Role**: Work through each role's interface sequentially
4. **Connect Flows**: Create interactive prototypes showing user journeys
5. **Add Details**: Include error states, empty states, loading states
6. **Review & Iterate**: Check for consistency, accessibility, and usability

Each prompt can be fed individually to Figma AI design tools or used as detailed specifications for manual design work.