package com.code2ever.bot.views.about;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.SamplePerson;
import com.code2ever.bot.data.service.BatchService;
import com.code2ever.bot.data.service.BillService;
import com.code2ever.bot.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("About")
@Route(value = "about/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class AboutView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "about/%s/edit";

    private final Grid<BatchPayment> grid = new Grid<>(BatchPayment.class, false);

    private TextField name;
    private TextField price;
    private TextField date;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private  BeanValidationBinder<BatchPayment> binder;

    private BatchPayment bill;

    private final BatchService batchService;

    public AboutView(BatchService batchService) {
        this.batchService = batchService;
        addClassNames("about-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
//        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("batchDate").setAutoWidth(true);
        grid.addColumn("isBatchPaid").setAutoWidth(true);

//        LitRenderer<BatchPayment> importantRenderer = LitRenderer.<BatchPayment>of(
//                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
//                .withProperty("icon", important -> important.isImportant() ? "check" : "minus").withProperty("color",
//                        important -> important.isImportant()
//                                ? "var(--lumo-primary-text-color)"
//                                : "var(--lumo-disabled-text-color)");
//
//        grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);

//        grid.setItems(query -> batchService.findAll(
//                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
//                clearForm();
                UI.getCurrent().navigate(AboutView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(BatchPayment.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
//            clearForm();
//            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.bill == null) {
//                    this.bill = new SamplePerson();
                }
                binder.writeBean(this.bill);
//                batchService.update(this.bill);
//                clearForm();
//                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(AboutView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//        Optional<Long> samplePersonId = event.getRouteParameters().get(SAMPLEPERSON_ID).map(Long::parseLong);
//        if (samplePersonId.isPresent()) {
//            Optional<SamplePerson> samplePersonFromBackend = billService.get(samplePersonId.get());
//            if (samplePersonFromBackend.isPresent()) {
//                populateForm(samplePersonFromBackend.get());
//            } else {
//                Notification.show(
//                        String.format("The requested samplePerson was not found, ID = %s", samplePersonId.get()), 3000,
//                        Notification.Position.BOTTOM_START);
//                // when a row is selected but the data is no longer available,
//                // refresh grid
//                refreshGrid();
//                event.forwardTo(AboutView.class);
//            }
//        }
    }

//    private void createEditorLayout(SplitLayout splitLayout) {
//        Div editorLayoutDiv = new Div();
//        editorLayoutDiv.setClassName("editor-layout");
//
//        Div editorDiv = new Div();
//        editorDiv.setClassName("editor");
//        editorLayoutDiv.add(editorDiv);
//
//        FormLayout formLayout = new FormLayout();
//        name = new TextField("First Name");
//        price = new TextField("Last Name");
//        date = new TextField("Email");
//        formLayout.add(name, price, date);
//        editorDiv.add(formLayout);
//        createButtonLayout(editorLayoutDiv);
//
//        splitLayout.addToSecondary(editorLayoutDiv);
//    }
//
//    private void createButtonLayout(Div editorLayoutDiv) {
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setClassName("button-layout");
//        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        buttonLayout.add(save, cancel);
//        editorLayoutDiv.add(buttonLayout);
//    }
//
    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();

        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }
//
//    private void refreshGrid() {
//        grid.select(null);
//        grid.getDataProvider().refreshAll();
//    }
//
//    private void clearForm() {
//        populateForm(null);
//    }
//
//    private void populateForm(SamplePerson value) {
//        this.bill = value;
//        binder.readBean(this.bill);
//    }
}
