package com.app.Ioms.navigation;

import com.app.Ioms.domain.Order;
import com.app.Ioms.ui.orders.*;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public final class Router {

    private static StackPane outlet;

    private Router() {}

    public static void init(StackPane rootOutlet) {
        outlet = rootOutlet;
    }

    private static void setContent(Node node) {
        outlet.getChildren().setAll(node);
    }

    public static void goToOrdersList() {
        setContent(new OrdersListView());
    }

    public static void goToCanceledOrders() {
        setContent(new CanceledOrdersView());
    }

    public static void goToOrderDetail(Order order) {
        setContent(new OrderDetailView(order));
    }
package com.app.Ioms.navigation;

import com.app.Ioms.domain.Order;
import com.app.Ioms.ui.orders.*;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public final class Router {
    private static StackPane outlet;

    private Router() {}

    public static void init(StackPane root) {
        outlet = root;
    }

    private static void setContent(Node node) {
        outlet.getChildren().setAll(node);
    }

    public static void goToOrdersList() {
        setContent(new OrdersListView());
    }

    public static void goToCanceledOrders() {
        setContent(new CanceledOrdersView());
    }

    public static void goToOrderDetail(Order order) {
        setContent(new OrderDetailView(order));
    }

    public static void goToAllocation(Order order) {
        setContent(new AllocationView(order));
    }

    public static void goToAllocationStart() {
        setContent(new AllocationView(null));
    }

    public static void goToConfirm(AllocationResult result) {
        setContent(new ConfirmOrdersView(result));
    }

    public static void goToConfirmPending() {
        setContent(new ConfirmOrdersView(null));
    }
}
    public static void goToAllocation(Order order) {
        setContent(new AllocationView(order));
    }

    // Shortcut to start allocation flow when no order pre-selected
    public static void goToAllocationStart() {
        setContent(new AllocationView(null));
    }

    public static void goToConfirm(AllocationResult result) {
        setContent(new ConfirmOrdersView(result));
    }

    // Confirm screen for pending allocations created in this session if any
    public static void goToConfirmPending() {
        setContent(new ConfirmOrdersView(null));
    }
}
