package com.code2ever.bot.views.bill;

import com.code2ever.bot.data.entity.Bill;
import com.code2ever.bot.data.service.bill.BillService;
import com.code2ever.bot.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@PageTitle("Bill")
@Route(value = "bot/bill", layout = MainLayout.class)
@Uses(Icon.class)
public class BillView extends Composite<VerticalLayout> {

    private final HorizontalLayout layoutRowUp = new HorizontalLayout();
    private final HorizontalLayout layoutRowBottom = new HorizontalLayout();
    private final VerticalLayout leftColumn = new VerticalLayout();
    private final VerticalLayout centralColumn = new VerticalLayout();
    private final VerticalLayout rightColumn = new VerticalLayout();
    private final HorizontalLayout row1InCentralColumn = new HorizontalLayout();
    private final VerticalLayout column1InRow1InCentralColumn = new VerticalLayout();
    private final VerticalLayout column2InRow1InCentralColumn = new VerticalLayout();
    private final HorizontalLayout row2InCentralColumn = new HorizontalLayout();
    private final H3 h3 = new H3();
    private final TextField billNameField = new TextField();
    private final BigDecimalField billTotalField = new BigDecimalField();
    private final IntegerField billDeadLineField = new IntegerField();
    private final Select<Integer> billDaySelect = new Select<>();
    private final Checkbox creditCardCheckbox = new Checkbox();
    private final Button buttonSave = new Button();
    private final Button buttonCancel = new Button();
    private final Grid<Bill> billGrid = new Grid<>(Bill.class, false);
    private final BillService service;
    private final List<Bill> billList = new ArrayList<>();

    public BillView(BillService service) {
        this.service = service;
        billList.addAll(service.findAll());
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


    private void setUpConfirmDialog(Bill bill, Consumer<Bill> consumer) {
        ConfirmDialog dialog = new ConfirmDialog();
        String header = String.format("Delete \"%s\"?", bill.getName());
        dialog.setHeader(header);
        dialog.setText("Are you sure you want to permanently delete this item?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> consumer.accept(bill));
        dialog.open();
    }

    private void createBillGrid() {
        billGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        Grid.Column<Bill> billNameColumn = billGrid.addColumn(Bill::getName).setAutoWidth(true).setHeader("Name");
        Grid.Column<Bill> billTotalColumn = billGrid.addColumn(Bill::getTotal).setAutoWidth(true).setHeader("Total");
        Grid.Column<Bill> billingDayColumn = billGrid.addColumn(Bill::getBillingDay).setAutoWidth(true).setHeader("Billing Day");
        Grid.Column<Bill> billDeadLineColumn = billGrid.addColumn(Bill::getDeadLine).setAutoWidth(true).setHeader("DeadLine");
        Grid.Column<Bill> creditCardColumn = billGrid.addColumn(bill -> bill.getIsCreditCard() ? "True" : "False").setAutoWidth(true).setHeader("Credit Card");

        Editor<Bill> editor = billGrid.getEditor();
        Grid.Column<Bill> editColumn = billGrid.addComponentColumn(bill -> setUpComponentColumn(bill, editor));

        Binder<Bill> billBinder = new Binder<>(Bill.class);
        editor.setBinder(billBinder);
        editor.setBuffered(true);

        TextField billNameField = new TextField();
        billNameField.setWidthFull();
        billBinder.forField(billNameField).asRequired("Name must not be empty").bind(Bill::getName, Bill::setName);
        billNameColumn.setEditorComponent(billNameField);

        BigDecimalField billTotalField = new BigDecimalField();
        billTotalField.setWidthFull();
        billBinder.forField(billTotalField).asRequired("Total must not be empty").bind(Bill::getTotal, Bill::setTotal);
        billTotalColumn.setEditorComponent(billTotalField);

        Select<Integer> billDayField = new Select<>();
        billDayField.setWidthFull();
        billDayField.setItems(getBillingDays());
        billBinder.forField(billDayField).asRequired("Bill Day must not be empty").bind(Bill::getBillingDay, Bill::setBillingDay);
        billingDayColumn.setEditorComponent(billDayField);

        IntegerField billDeadLineField = new IntegerField();
        billDeadLineField.setWidthFull();
        billDeadLineField.setStepButtonsVisible(true);
        billDeadLineField.setMin(1);
        billBinder.forField(billDeadLineField).asRequired("DeadLine must not be empty").bind(Bill::getDeadLine, Bill::setDeadLine);
        billDeadLineColumn.setEditorComponent(billDeadLineField);

        Button saveButton = new Button("Save", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
        editor.addSaveListener(event -> {
            service.save(event.getItem());
            event.getGrid().setItems(billList);
        });
        billGrid.setItems(billList);
    }

    private HorizontalLayout setUpComponentColumn(Bill bill, Editor<Bill> editor) {
        Button editButton = new Button("Edit", event -> {
            if (editor.isOpen()) {
                editor.cancel();
            }
            billGrid.getEditor().editItem(bill);
        });
        Button deleteButton = new Button("Delete", event -> {
            Consumer<Bill> consumer = b -> {
                service.delete(b);
                billList.remove(b);
                billGrid.setItems(billList);
            };
            setUpConfirmDialog(bill, consumer);
        });
        HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
        actions.setPadding(false);
        return actions;
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

        creditCardCheckbox.setLabel("Credit Card");
        creditCardCheckbox.setId("creditCard");
        creditCardCheckbox.addSingleClickListener(this::creditCardListener);

        billDaySelect.setItems(getBillingDays());
        billDaySelect.setWidthFull();
        billDaySelect.setId("billDate");
        billDaySelect.setLabel("Bill Date");

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

    private List<Integer> getBillingDays() {
        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }
        return days;
    }

    private void initPrincipalLayoutRow() {
        getContent().add(layoutRowUp, layoutRowBottom);
        layoutRowUp.setId("layoutRowUp");
        layoutRowUp.setWidthFull();
        layoutRowUp.setFlexGrow(1.0, leftColumn);
        layoutRowUp.add(leftColumn);
        layoutRowUp.setFlexGrow(1.0, centralColumn);
        layoutRowUp.add(centralColumn);
        layoutRowUp.setFlexGrow(1.0, rightColumn);
        layoutRowUp.add(rightColumn);

        layoutRowBottom.setId("layoutRowBottom");
        layoutRowBottom.setWidthFull();
        layoutRowBottom.setFlexGrow(1.0, billGrid);
        layoutRowBottom.add(billGrid);
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
        column1InRow1InCentralColumn.add(billDaySelect);
        column1InRow1InCentralColumn.add(creditCardCheckbox);

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
        Bill bill = getNewBill();
        service.save(bill);
        showNotificationSuccess("Bill saved");
        setDefaultValueField();
        billGrid.setItems(service.findAll());
        billList.add(bill);
    }

    private Bill getNewBill() {
        String name = billNameField.getValue();
        BigDecimal total = billTotalField.getValue();
        Integer day = billDaySelect.getValue();
        Integer deadLine = billDeadLineField.getValue();
        Boolean creditCard = creditCardCheckbox.getValue();
        Bill bill = new Bill();
        bill.setName(name);
        bill.setTotal(total);
        bill.setBillingDay(day);
        bill.setDeadLine(deadLine);
        bill.setIsCreditCard(creditCard);
        return bill;
    }

    private void creditCardListener(ClickEvent<Checkbox> listener) {
        if (listener.getSource().getValue()) {
            billTotalField.setValue(BigDecimal.ZERO);
            billTotalField.setEnabled(false);
        } else {
            billTotalField.setEnabled(true);
        }

    }

    private void setDefaultValueField() {
        billNameField.setValue("");
        billTotalField.setValue(BigDecimal.ZERO);
        billDeadLineField.setValue(1);
        creditCardCheckbox.setValue(false);
        billDaySelect.setValue(1);
    }

    private boolean isEmptyFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("Name", billNameField.getValue());
        fields.put("Total", billTotalField.getValue());
        fields.put("Bill Date", billDaySelect.getValue());
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
