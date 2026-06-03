package com.app.modules.sales.dashboard.ui;

import com.app.common.ui.IScreen;
import com.app.common.ui.MainLayoutUI;
import javafx.scene.Parent;

public class SalesMainUI extends MainLayoutUI implements IScreen {
    public SalesMainUI() {
        super();
        this.setLeft(new SalesSidebar());
        this.setPage(new SalesDashboardUI());
    }

    @Override
    public Parent getRoot() {
        return this;
    }
}
