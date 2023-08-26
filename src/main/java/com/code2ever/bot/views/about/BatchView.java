package com.code2ever.bot.views.about;

import com.code2ever.bot.data.entity.AbstractEntity;
import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.PendingPayment;
import com.code2ever.bot.data.service.batch.BatchService;
import com.code2ever.bot.data.service.pendingpayment.PendingPaymentService;
import com.code2ever.bot.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Batch")
@Route(value = "bot/batch", layout = MainLayout.class)
@Uses(Icon.class)
public class BatchView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final Grid<BatchPayment> batchPaymentGrid = new Grid<>(BatchPayment.class, false);
    private final Grid<PendingPayment> pendingPaymentGrid = new Grid<>(PendingPayment.class, false);
    private final BatchService batchService;
    private final PendingPaymentService pendingPaymentService;

    public BatchView(BatchService batchService, PendingPaymentService pendingPaymentService) {
        this.batchService = batchService;
        this.pendingPaymentService = pendingPaymentService;
        createBatchPaymentGrid();
        createPendingPaymentGrid();
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setHeightFull();
        splitLayout.setWidthFull();
        setUpSplitLayout(splitLayout);
        getContent().add(splitLayout);
        getContent().setHeightFull();
    }

    private void createPendingPaymentGrid() {
        pendingPaymentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getBill().getName()).setAutoWidth(true).setHeader(
                "Name");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getBill().getTotal()).setAutoWidth(true).setHeader(
                "Total");
        pendingPaymentGrid.addColumn(PendingPayment::getInitialDate).setAutoWidth(true).setHeader(
                "Initial Date");
        pendingPaymentGrid.addColumn(PendingPayment::getLimitDate).setAutoWidth(true).setHeader(
                "Limit Date");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getIsPaid() ? "Yes" : "No").setAutoWidth(true).setHeader(
                "Paid");
    }

    private void createBatchPaymentGrid() {
        batchPaymentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        batchPaymentGrid.addColumn(BatchPayment::getBatchDate).setAutoWidth(true).setHeader("Batch Date");
        batchPaymentGrid.addColumn(batchPayment -> batchPayment.getIsBatchPaid() ? "Yes" : "No").setAutoWidth(true).setHeader(
                "Paid");

        batchPaymentGrid.setItems(batchService.findAll());
        batchPaymentGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                BatchPayment batchPayment = event.getValue();
                List<PendingPayment> pendingPayments = pendingPaymentService.findAllByBatchPayment(batchPayment);
                pendingPaymentGrid.setItems(pendingPayments);
            } else {
                pendingPaymentGrid.getDataProvider().refreshAll();

            }
        });
    }

    private void setUpSplitLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setHeightFull();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(batchPaymentGrid);

        Div wrapper2 = new Div();
        wrapper2.setHeightFull();
        wrapper2.setClassName("grid-wrapper");
        splitLayout.addToSecondary(wrapper2);
        wrapper2.add(pendingPaymentGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

    }
}
