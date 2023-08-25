package com.code2ever.bot.views.bill;

import com.code2ever.bot.data.entity.AbstractEntity;
import com.code2ever.bot.data.entity.Bill;
import com.code2ever.bot.data.service.BillService;
import com.code2ever.bot.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Bill")
@Route(value = "bill", layout = MainLayout.class)
@Uses(Icon.class)
public class BillView extends Composite<VerticalLayout> {

    private final HorizontalLayout layoutRowUp = new HorizontalLayout();
    private final HorizontalLayout layoutRowBottom = new HorizontalLayout();
    private final VerticalLayout leftColumn = new VerticalLayout();
    private final VerticalLayout centralColumn = new VerticalLayout();
    private final VerticalLayout rightColumn = new VerticalLayout();
    private final H3 h3 = new H3();
    private final HorizontalLayout row1InCentralColumn = new HorizontalLayout();
    private final VerticalLayout column1InRow1InCentralColumn = new VerticalLayout();
    private final TextField billNameField = new TextField();
    private final BigDecimalField billTotalField = new BigDecimalField();
    private final IntegerField billDeadLineField = new IntegerField();
    private final Select<Integer> billDay = new Select<>();
    private final Checkbox isCreditCard = new Checkbox();
    private final VerticalLayout column2InRow1InCentralColumn = new VerticalLayout();
    private final HorizontalLayout row2InCentralColumn = new HorizontalLayout();
    private final Button buttonSave = new Button();
    private final Button buttonCancel = new Button();

    private final Grid<Bill> billGrid = new Grid<>(Bill.class, false);

    private final BillService service;

    public BillView(BillService service) {
        this.service = service;
        initComponent();
    }

    private void initComponent() {
        getContent().setWidthFull();
        getContent().addClassName(Padding.LARGE);
        initPrincipalLayoutRow();
        initPrincipalColumns();
        initCentralColumn();
        initUiComponents();
    }

    private void createBillGrid(){
        billGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        billGrid.addColumn(Bill::getName).setAutoWidth(true).setHeader(
                "Name");
        billGrid.addColumn(Bill::getTotal).setAutoWidth(true).setHeader(
                "Total");
        billGrid.addColumn(Bill::getBillingDay).setAutoWidth(true).setHeader(
                "Billing Day");
        billGrid.addColumn(Bill::getDeadLine).setAutoWidth(true).setHeader(
                "DeadLine");
        billGrid.setItems(service.findAll());
    }

    private void initUiComponents() {
        billNameField.setLabel("Name");
        billNameField.setWidthFull();
        billNameField.setId("billNameField");
        billNameField.setRequiredIndicatorVisible(false);

        billTotalField.setLabel("Total");
        billTotalField.setWidthFull();
        billTotalField.setId("billPriceField");
        Div pricePrefix = new Div();
        pricePrefix.setText("$");
        billTotalField.setPrefixComponent(pricePrefix);
        billTotalField.setRequiredIndicatorVisible(false);


        billDeadLineField.setId("deadLine");
        billDeadLineField.setLabel("Dead Line");
        billDeadLineField.setWidthFull();
        billDeadLineField.setStepButtonsVisible(true);
        billDeadLineField.setMin(1);

        isCreditCard.setLabel("Credit Card");
        isCreditCard.setId("creditCard");
        isCreditCard.addSingleClickListener(this::creditCardListener);

        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }
        billDay.setItems(days);
        billDay.setWidthFull();
        billDay.setId("billDate");
        billDay.setLabel("Bill Date");

        buttonSave.setText("Save");
        buttonSave.setId("buttonSave");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSave.addClickListener(this::listenerButtonSave);

        buttonCancel.setText("Cancel");
        buttonCancel.setId("buttonCancel");
        buttonCancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        h3.setText("Bill Information");
        setDefaultValueField();
        createBillGrid();
    }

    private void initPrincipalLayoutRow() {
        getContent().add(layoutRowUp,layoutRowBottom);
        layoutRowBottom.setId("layoutRowBottom");
        layoutRowBottom.setWidthFull();
        layoutRowUp.setFlexGrow(1.0, billGrid);
        layoutRowBottom.add(billGrid);

        layoutRowUp.setId("layoutRowUp");
        layoutRowUp.setWidthFull();
        layoutRowUp.setFlexGrow(1.0, leftColumn);
        layoutRowUp.add(leftColumn);
        layoutRowUp.setFlexGrow(1.0, centralColumn);
        layoutRowUp.add(centralColumn);
        layoutRowUp.setFlexGrow(1.0, rightColumn);
        layoutRowUp.add(rightColumn);
    }

    private void initPrincipalColumns() {
        //leftColumn
        leftColumn.setId("leftColumn");
        leftColumn.setWidth(null);

        //centralColumn
        centralColumn.setWidth(null);
        centralColumn.setId("centralColumn");
        centralColumn.add(h3);
        centralColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        centralColumn.add(row1InCentralColumn);
        centralColumn.add(row2InCentralColumn);

        //rightColumn
        rightColumn.setWidth(null);
        rightColumn.setId("rightColumn");
    }

    private void initCentralColumn() {
        //row 1 in central column
        row1InCentralColumn.setWidthFull();
        row1InCentralColumn.addClassName(Gap.LARGE);
        row1InCentralColumn.setFlexGrow(1.0, column1InRow1InCentralColumn);
        row1InCentralColumn.setId("row1InCentralColumn");
        row1InCentralColumn.add(column1InRow1InCentralColumn);
        row1InCentralColumn.setFlexGrow(1.0, column2InRow1InCentralColumn);
        row1InCentralColumn.add(column2InRow1InCentralColumn);

        //column 1 in row 1 in central column
        column1InRow1InCentralColumn.setWidth(null);
        column1InRow1InCentralColumn.setId("column1InRow1InCentralColumn");
        column1InRow1InCentralColumn.add(billNameField);
        column1InRow1InCentralColumn.add(billDay);
        column1InRow1InCentralColumn.add(isCreditCard);

        //column 2 in row 1 in central column
        column2InRow1InCentralColumn.setWidth(null);
        column2InRow1InCentralColumn.add(billTotalField);
        column2InRow1InCentralColumn.add(billDeadLineField);
        column2InRow1InCentralColumn.setId("column2InInternalCentralRow");


        //row 2 in central column
        row2InCentralColumn.add(buttonSave);
        row2InCentralColumn.add(buttonCancel);
        row2InCentralColumn.addClassName(Gap.MEDIUM);
        row2InCentralColumn.setId("row2InCentralColumn");
    }

    private void listenerButtonSave(ClickEvent<Button> listener) {
        if (isEmptyFields()) {
            return;
        }
        String name = billNameField.getValue();
        BigDecimal total = billTotalField.getValue();
        Integer day = billDay.getValue();
        Integer deadLine = billDeadLineField.getValue();
        Boolean creditCard = isCreditCard.getValue();
        Bill bill = new Bill();
        bill.setName(name);
        bill.setTotal(total);
        bill.setBillingDay(day);
        bill.setDeadLine(deadLine);
        bill.setIsCreditCard(creditCard);
        service.save(bill);
        showNotificationSuccess("Bill saved");
        setDefaultValueField();
        billGrid.setItems(service.findAll());
    }

    private void creditCardListener(ClickEvent<Checkbox> listener) {
        if(listener.getSource().getValue()){
            billTotalField.setValue(BigDecimal.ZERO);
            billTotalField.setEnabled(false);
        } else {
            billTotalField.setEnabled(true);
        }

    }

    private void setDefaultValueField(){
        billNameField.setValue("");
        billTotalField.setValue(BigDecimal.ZERO);
        billDeadLineField.setValue(1);
        isCreditCard.setValue(false);
        billDay.setValue(1);
    }

    private boolean isEmptyFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("Name", billNameField.getValue());
        fields.put("Total", billTotalField.getValue());
        fields.put("Bill Date", billDay.getValue());
        fields.put("Dead Line", billDeadLineField.getValue());
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (entry.getValue() == null) {
                String message = "Please fill the " + entry.getKey() + " field.";
                showNotificationError(message);
                return true;
            }
            if (entry.getValue() instanceof String value && value.isEmpty()) {
                String message = "Please fill the " + entry.getKey() + " field.";
                showNotificationError(message);
                return true;
            }
        }
        return false;
    }

    private void showNotificationError(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    private void showNotificationSuccess(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

}
