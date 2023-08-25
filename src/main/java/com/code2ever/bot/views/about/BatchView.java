package com.code2ever.bot.views.about;

import com.code2ever.bot.data.entity.AbstractEntity;
import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.PendingPayment;
import com.code2ever.bot.data.service.BatchService;
import com.code2ever.bot.data.service.PendingPaymentService;
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
@Route(value = "batch", layout = MainLayout.class)
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
    private void createPendingPaymentGrid(){
        pendingPaymentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        pendingPaymentGrid.addColumn(AbstractEntity::getId).setAutoWidth(true).setHeader(
                "Id");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getBill().getName()).setAutoWidth(true).setHeader(
                "Bill Name");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getBill().getTotal()).setAutoWidth(true).setHeader(
                "Bill Price");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getPaid() ? "Yes":"No").setAutoWidth(true).setHeader(
                "Bill Price");
//        pendingPaymentGrid.setItems(pendingPaymentService.findAll());
    }

    private void createBatchPaymentGrid(){
        batchPaymentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        batchPaymentGrid.addColumn(BatchPayment::getBatchDate).setAutoWidth(true).setHeader("Batch Date");
        batchPaymentGrid.addColumn(batchPayment -> batchPayment.getIsBatchPaid() ? "Yes":"No").setAutoWidth(true).setHeader(
                "Paid");
//        batchPaymentGrid.addComponentColumn()
//        batchPaymentGrid.addComponentColumn(batchPayment -> batchPayment.getIsBatchPaid())
//                .setTooltipGenerator(person -> person.getStatus())
//                .setHeader("Status");
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

    private void setUpSplitLayout(SplitLayout splitLayout){
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
