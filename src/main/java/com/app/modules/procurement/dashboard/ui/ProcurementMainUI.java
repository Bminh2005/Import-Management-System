package com.app.modules.procurement.dashboard.ui;

import com.app.common.ui.IScreen;
import com.app.common.ui.MainLayoutUI;
import com.app.modules.procurement.importorder.ui.ProcurementDashboardUI;
import com.app.modules.procurement.importorder.ui.ProcurementSidebar;
import javafx.scene.Parent;

public class ProcurementMainUI extends MainLayoutUI implements IScreen {
    public ProcurementMainUI(){
        super();
        this.setLeft(new ProcurementSidebar());
        this.setPage(new ProcurementDashboardUI());
        getStylesheets().addAll(
                getClass().getResource("/com/app/modules/procurement/order/ui/site-allocation-dialog.css").toExternalForm(),
                getClass().getResource("/com/app/modules/procurement/order/ui/site-order-confirm-dialog.css").toExternalForm(),
                getClass().getResource("/com/app/modules/procurement/order/ui/site-order-detail.css").toExternalForm(),
                getClass().getResource("/com/app/modules/procurement/order/ui/site-order-list.css").toExternalForm(),
                getClass().getResource("/com/app/modules/procurement/order/ui/site-order-reallocation.css").toExternalForm(),
                getClass().getResource("/com/app/modules/procurement/order/ui/site-order-shared.css").toExternalForm(),
                getClass().getResource("/com/app/modules/procurement/order/ui/site-order-success.css").toExternalForm()
        );
    }
    @Override
    public Parent getRoot() {
        return this;
    }
}
