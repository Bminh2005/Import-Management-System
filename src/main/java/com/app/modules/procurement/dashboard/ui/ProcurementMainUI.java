package com.app.modules.procurement.dashboard.ui;

import com.app.common.ui.IScreen;
import com.app.common.ui.MainLayoutUI;
import com.app.modules.procurement.importorder.ui.ProcurementDashboardUI;
import javafx.scene.Parent;

public class ProcurementMainUI extends MainLayoutUI implements IScreen {
    public ProcurementMainUI(){
        super();
        this.setLeft(new ProcurementSidebar());
        this.setPage(new ProcurementDashboardUI());
    }
    @Override
    public Parent getRoot() {
        return this;
    }
}
