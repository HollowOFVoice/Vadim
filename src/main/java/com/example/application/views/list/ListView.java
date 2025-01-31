package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value="", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;
    CrmService service;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }


    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}


//package com.example.application.views.list;
//
//import com.example.application.data.Contact;
//import com.example.application.services.CrmService;
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.value.ValueChangeMode;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//
//import java.util.Collections;
//
//
//@Route(value = "")
//    @PageTitle("Contacts | Vaadin CRM")
//    public class  ListView extends VerticalLayout {
//        Grid<Contact> grid = new Grid<>(Contact.class);
//        TextField filterText = new TextField();
//    ContactForm form;
//    CrmService service;
//
//    public ListView(CrmService service) {
//        this.service = service;
//            addClassName("list-view");
//            setSizeFull();
//            configureGrid();
//            configureForm();
//
//            add(getToolbar(), getContent());
//        updateList();
//    }
//        private void configureGrid() {
//            grid.addClassNames("contact-grid");
//            grid.setSizeFull();
//            grid.setColumns("firstName", "lastName", "email");
//            grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
//            grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
//            grid.getColumns().forEach(col -> col.setAutoWidth(true));
//        }
//
//        private Component getContent() {
//            HorizontalLayout content = new HorizontalLayout(grid, form);
//            content.setFlexGrow(2, grid);
//            content.setFlexGrow(1, form);
//            content.addClassNames("content");
//            content.setSizeFull();
//            return content;
//        }
//
//
//        private void configureForm() {
//            form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
//            form.setWidth("25em");
//        }
//
//        private HorizontalLayout getToolbar() {
//            filterText.setPlaceholder("Filter by name...");
//            filterText.setClearButtonVisible(true);
//            filterText.setValueChangeMode(ValueChangeMode.LAZY);
//            filterText.addValueChangeListener(e -> updateList());
//            Button addContactButton = new Button("Add contact");
//
//            var toolbar = new HorizontalLayout(filterText, addContactButton);
//            toolbar.addClassName("toolbar");
//            return toolbar;
//        }
//
//    private void updateList() {
//        grid.setItems(service.findAllContacts(filterText.getValue()));
//    }
//
//
//
//    }
//
//
