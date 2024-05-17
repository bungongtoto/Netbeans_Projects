package services;

import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.RequestScoped;
import jakarta.inject.Inject;

@ManagedBean(name="servicesBean", eager=true)
@RequestScoped
public class ServicesBean {
    
    @Inject 
    private DepartmentService departmentService;
    
    @Inject
    private PayRollService payRollService;

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
