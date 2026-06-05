package com.app.modules.sales.dashboard.ui;

import com.app.common.ui.IScreen;
import com.app.common.ui.MainLayoutUI;
import javafx.scene.Parent;

/**
 * Màn Sales sau đăng nhập: layout chung + sidebar có điều hướng qua {@link SalesShell}.
 */
public class SalesMainUI extends MainLayoutUI implements IScreen {

    private final SalesShell shell;

    public SalesMainUI() {
        super();
        this.shell = new SalesShell(this);
        shell.showDashboard();
    }

    public SalesShell getShell() {
        return shell;
    }

    @Override
    public Parent getRoot() {
        return this;
    }
}
