package com.mycompany.usercrud;

import entities.Department;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.primefaces.PrimeFaces;
import services.DepartmentService;

@Named("departmentView")
@ViewScoped
public class DepartmentView implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Department> departments;

    private Department selectedDepartment;

    private List<Department> selectedDepartments;

    private Timer timer;

    @Inject
    private DepartmentService departmentService;

    @PostConstruct
    public void init() {
        // Load initial data
        loadData();
        this.selectedDepartments = new ArrayList<Department>();

        // Schedule a task to reload data every 30 seconds
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadData();
            }
        }, 30000, 30000); // 30 seconds delay, 30 seconds interval

    }

    public void loadData() {
        this.departments = this.departmentService.findAll();
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public List<Department> getSelectedDepartments() {
        return selectedDepartments;
    }

    public void setSelectedDepartments(List<Department> selectedDepartments) {
        this.selectedDepartments = selectedDepartments;
    }

    public void openNew() {
        this.selectedDepartment = new Department();
    }

    public void saveDepartment() {
        if (this.selectedDepartment.getId() == 0) {
            departmentService.createNewDepartment(selectedDepartment);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Department Added"));
        } else {
            departmentService.updateDepartment(selectedDepartment);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Department Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageDepartmentDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-departments");
    }

    public void deleteDepartment() {
        departmentService.deleteDepartment(selectedDepartment.getId());
        this.departments.remove(this.selectedDepartment);
        this.selectedDepartments.remove(this.selectedDepartment);
        this.selectedDepartment = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Department Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-departments");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedDepartments()) {
            int size = this.selectedDepartments.size();
            return size > 1 ? size + " departments selected" : "1 department selected";
        }

        return "Delete";
    }

    public boolean hasSelectedDepartments() {
        return this.selectedDepartments != null && !this.selectedDepartments.isEmpty();
    }

    public void deleteSelectedDepartments() {
        for (Department department : this.selectedDepartments) {
            departmentService.deleteDepartment(department.getId());
        }
        this.departments.removeAll(this.selectedDepartments);
        this.selectedDepartments = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Departments Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-departments");
        PrimeFaces.current().executeScript("PF('dtDepartments').clearFilters()");
    }

    @PreDestroy
    public void cleanUp() {
        // Cancel the timer
        if (timer != null) {
            timer.cancel();
        }
    }
}
