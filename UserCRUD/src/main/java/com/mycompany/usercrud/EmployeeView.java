package com.mycompany.usercrud;

import entities.Department;
import entities.Employee;
import entities.PayRoll;
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
import services.EmployeeService;
import services.PayRollService;

@Named("employeeView")
@ViewScoped
public class EmployeeView implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Employee> employees;

    private List<Department> departments;

    private List<PayRoll> payRolls;

    private Employee selectedEmployee;

    private List<Employee> selectedEmployees;

    private Timer timer;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private DepartmentService departmentService;

    @Inject
    private PayRollService payRollService;

    @PostConstruct
    public void init() {
        // Load initial data
        loadData();
        this.selectedEmployees = new ArrayList<Employee>();
        this.departments = new ArrayList<Department>();
        this.payRolls = new ArrayList<PayRoll>();

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
        this.employees = this.employeeService.findAll();
    }

    public void loadDepartments() {
        this.departments = this.departmentService.findAll();
    }

    public void loadPayRolls() {
        this.payRolls = this.payRollService.findAll();
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public List<Employee> getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(List<Employee> selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public void openNew() {
        this.selectedEmployee = new Employee();
    }

    public void saveEmployee() {
        if (this.selectedEmployee.getId() == 0) {
            employeeService.createNewPayRoll(selectedEmployee);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Added"));
        } else {
            employeeService.updateEmployee(selectedEmployee);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Updated"));
        }
        System.out.println("Started the process to add");
        PrimeFaces.current().executeScript("PF('manageEmployeeDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-employees");
    }

    public void deleteEmployee() {
        employeeService.deletePayRoll(selectedEmployee.getId());
        this.employees.remove(this.selectedEmployee);
        this.selectedEmployees.remove(this.selectedEmployee);
        this.selectedEmployee = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-employees");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedEmployees()) {
            int size = this.selectedEmployees.size();
            return size > 1 ? size + " payRolls selected" : "1 payRoll selected";
        }

        return "Delete";
    }
    

    public boolean hasSelectedEmployees() {
        return this.selectedEmployees != null && !this.selectedEmployees.isEmpty();
    }

    public void deleteSelectedEmployees() {
        for (Employee payRollt : this.selectedEmployees) {
            employeeService.deletePayRoll(payRollt.getId());
        }
        this.employees.removeAll(this.selectedEmployees);
        this.selectedEmployees = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employees Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-employees");
        PrimeFaces.current().executeScript("PF('dtEmployees').clearFilters()");
    }

    @PreDestroy
    public void cleanUp() {
        // Cancel the timer
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * @return the departments
     */
    public List<Department> getDepartments() {
        if (departments.isEmpty()) {
            loadDepartments(); // Call method to fetch departments
        }
        return departments;
    }

    /**
     * @param departments the departments to set
     */
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    /**
     * @return the payRolls
     */
    public List<PayRoll> getPayRolls() {
        if (payRolls.isEmpty()) {
            loadPayRolls(); // Call method to fetch payrolls
        }
        return payRolls;
    }

    /**
     * @param payRolls the payRolls to set
     */
    public void setPayRolls(List<PayRoll> payRolls) {
        this.payRolls = payRolls;
    }

    /**
     * @return the departmentService
     */
    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    /**
     * @return the payRollService
     */
    public PayRollService getPayRollService() {
        return payRollService;
    }
}
