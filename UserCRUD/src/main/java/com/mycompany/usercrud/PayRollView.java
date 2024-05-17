
package com.mycompany.usercrud;

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
import services.PayRollService;

@Named("payRollView")
@ViewScoped
public class PayRollView implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<PayRoll> payRolls;

    private PayRoll selectedPayRoll;

    private List<PayRoll> selectedPayRolls;
    
    private Timer timer;

    @Inject
    private PayRollService payRollService;

    @PostConstruct
    public void init() {
        // Load initial data
        loadData();
        this.selectedPayRolls = new ArrayList<PayRoll>();
        
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
        this.payRolls = this.payRollService.findAll();
    }
    
    public List<PayRoll> getPayRolls() {
        return payRolls;
    }

    public PayRoll getSelectedPayRoll() {
        return selectedPayRoll;
    }

    public void setSelectedPayRoll(PayRoll selectedPayRoll) {
        this.selectedPayRoll = selectedPayRoll;
    }

    public List<PayRoll> getSelectedPayRolls() {
        return selectedPayRolls;
    }

    public void setSelectedPayRolls(List<PayRoll> selectedPayRolls) {
        this.selectedPayRolls = selectedPayRolls;
    }

    public void openNew() {
        this.selectedPayRoll = new PayRoll();
    }

    public void savePayRoll() {
        if ( this.selectedPayRoll.getId() == 0) {
            payRollService.createNewPayRoll(selectedPayRoll);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("PayRoll Added"));
        }
        else {
            payRollService.updatePayRoll(selectedPayRoll);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("PayRoll Updated"));
        }

        PrimeFaces.current().executeScript("PF('managePayRollDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-payRolls");
    }

    public void deletePayRoll() {
        payRollService.deletePayRoll(selectedPayRoll.getId());
        this.payRolls.remove(this.selectedPayRoll);
        this.selectedPayRolls.remove(this.selectedPayRoll);
        this.selectedPayRoll = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("PayRoll Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-payRolls");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedPayRolls()) {
            int size = this.selectedPayRolls.size();
            return size > 1 ? size + " payRolls selected" : "1 payRoll selected";
        }

        return "Delete";
    }

    public boolean hasSelectedPayRolls() {
        return this.selectedPayRolls != null && !this.selectedPayRolls.isEmpty();
    }
    
     public void deleteSelectedPayRolls() {
        for(PayRoll payRollt : this.selectedPayRolls){
            payRollService.deletePayRoll(payRollt.getId());
        }
        this.payRolls.removeAll(this.selectedPayRolls);
        this.selectedPayRolls = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("PayRolls Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-payRolls");
        PrimeFaces.current().executeScript("PF('dtPayRolls').clearFilters()");
    }

    @PreDestroy
    public void cleanUp() {
        // Cancel the timer
        if (timer != null) {
            timer.cancel();
        }
    }
}
