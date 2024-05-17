
package com.mycompany.usercrud;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.polar.PolarAreaChartDataSet;
import org.primefaces.model.charts.polar.PolarAreaChartModel;
import services.DepartmentService;
import services.EmployeeService;
import services.PayRollService;
import services.UserService;

@Named("chartJSView")
@ViewScoped
public class ChartJSView implements Serializable {
     private static final long serialVersionUID = 1L;
     
     private PolarAreaChartModel polarAreaModel;
     
    @Inject
    private UserService userService;
    
    @Inject
    private DepartmentService departmentService;
    
    @Inject
    private PayRollService payRollService;
    
    @Inject
    private EmployeeService employeeService;
     
    @PostConstruct
    public void init() {
        createPolarAreaModel();
    }
     
    private void createPolarAreaModel() {
        setPolarAreaModel(new PolarAreaChartModel());
        ChartData data = new ChartData();

        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(userService.findAll().size());
        values.add(employeeService.findAll().size());
        values.add(departmentService.findAll().size());
        values.add(payRollService.findAll().size());
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(75, 192, 192)");
        bgColors.add("rgb(255, 205, 86)");
        bgColors.add("rgb(54, 162, 235)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Users");
        labels.add("Employees");
        labels.add("Departments");
        labels.add("PayRolls");
        data.setLabels(labels);

        getPolarAreaModel().setData(data);
    }

    /**
     * @return the polarAreaModel
     */
    public PolarAreaChartModel getPolarAreaModel() {
        return polarAreaModel;
    }

    /**
     * @param polarAreaModel the polarAreaModel to set
     */
    public void setPolarAreaModel(PolarAreaChartModel polarAreaModel) {
        this.polarAreaModel = polarAreaModel;
    }
    
}
