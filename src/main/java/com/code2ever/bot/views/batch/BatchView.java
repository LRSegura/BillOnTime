package com.code2ever.bot.views.batch;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.PendingPayment;
import com.code2ever.bot.data.service.batch.BatchRepository;
import com.code2ever.bot.data.service.pendingpayment.PendingPaymentRepository;
import com.code2ever.bot.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@PageTitle("Batch")
@Route(value = "bot/batch", layout = MainLayout.class)
@Uses(Icon.class)
public class BatchView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final Grid<BatchPayment> batchPaymentGrid = new Grid<>(BatchPayment.class, false);
    private final Grid<PendingPayment> pendingPaymentGrid = new Grid<>(PendingPayment.class, false);
    private final BatchRepository batchRepository;
    private final PendingPaymentRepository pendingPaymentRepository;
    private final Comparator<PendingPayment> comparator = Comparator.comparing(PendingPayment::getInitialDate);
    private final Set<PendingPayment> pendingPaymentSet = new TreeSet<>(comparator);
    private final Set<BatchPayment> batchPaymentSet = new TreeSet<>(Comparator.comparing(BatchPayment::getId));
    private BatchPayment currentBatchPayment;

    public BatchView(BatchRepository batchRepository, BatchRepository batchRepository1, PendingPaymentRepository pendingPaymentRepository) {
        this.batchRepository = batchRepository1;
        this.pendingPaymentRepository = pendingPaymentRepository;
        createBatchPaymentGrid();
        createPendingPaymentGrid();
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setHeightFull();
        splitLayout.setWidthFull();
        setUpSplitLayout(splitLayout);
        getContent().add(splitLayout);
        getContent().setHeightFull();
        batchPaymentSet.addAll(batchRepository.findAll());
    }


    public void createPendingPaymentGrid() {
        pendingPaymentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getBill().getName()).setAutoWidth(true).setHeader("Name");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getBill().getTotal()).setAutoWidth(true).setHeader("Total");
        pendingPaymentGrid.addColumn(PendingPayment::getInitialDate).setAutoWidth(true).setHeader("Initial Date");
        pendingPaymentGrid.addColumn(PendingPayment::getLimitDate).setAutoWidth(true).setHeader("Limit Date");
        pendingPaymentGrid.addColumn(pendingPayment -> pendingPayment.getIsPaid() ? "Yes" : "No").setAutoWidth(true).setHeader("Paid");
        pendingPaymentGrid.addComponentColumn(this::setUpComponentColumnPendingPaymentGrid);
    }

    private HorizontalLayout setUpComponentColumnPendingPaymentGrid(PendingPayment pendingPayment) {
        String initialLabel = pendingPayment.getIsPaid() ? "Cancel" : "Pay";
        Button payButton = new Button(initialLabel);
        payButton.addClickListener(event -> {
            listenerPayButton(pendingPayment, payButton);
        });

        HorizontalLayout actions = new HorizontalLayout(payButton);
        actions.setPadding(false);
        return actions;
    }

    private void listenerPayButton(PendingPayment pendingPayment, Button payButton) {
        PendingPayment payment = pendingPaymentRepository.findById(pendingPayment.getId()).orElseThrow();
        payment.setIsPaid(!payment.getIsPaid());
        String newLabel = payment.getIsPaid() ? "Cancel" : "Pay";
        payButton.setText(newLabel);
        pendingPaymentRepository.save(payment);
        pendingPaymentSet.remove(pendingPayment);
        pendingPaymentSet.add(payment);
        pendingPaymentGrid.setItems(pendingPaymentSet);
        boolean isAllPaid =
                pendingPaymentSet.stream().filter(PendingPayment::getIsPaid).count() == pendingPaymentSet.size();
        BatchPayment batch = batchRepository.findById(currentBatchPayment.getId()).orElseThrow();
        batchPaymentSet.remove(currentBatchPayment);
        batchPaymentSet.add(batch);
        batch.setIsBatchPaid(isAllPaid);
        batchRepository.save(batch);
        batchPaymentGrid.setItems(batchPaymentSet);
    }

    private void createBatchPaymentGrid() {
        batchPaymentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        batchPaymentGrid.addColumn(BatchPayment::getBatchDate).setAutoWidth(true).setHeader("Batch Date");
        batchPaymentGrid.addColumn(batchPayment -> batchPayment.getIsBatchPaid() ? "Yes" : "No").setAutoWidth(true).setHeader("Paid");

        batchPaymentGrid.setItems(batchPaymentSet);
        batchPaymentGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                currentBatchPayment = event.getValue();
                pendingPaymentSet.clear();
                pendingPaymentSet.addAll(pendingPaymentRepository.findPendingPaymentByBatchPayments(currentBatchPayment));
                pendingPaymentGrid.setItems(pendingPaymentSet);
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
