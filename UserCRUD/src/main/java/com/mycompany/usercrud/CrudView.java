package com.mycompany.usercrud;

import entities.User;
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
import services.UserService;

@Named("crudView")
@ViewScoped
public class CrudView implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<User> users;

    private User selectedUser;

    private List<User> selectedUsers;

    private Timer timer;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        // Load initial data
        loadData();
        this.selectedUsers = new ArrayList<User>();

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
        this.users = this.userService.findAll();
    }

    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void openNew() {
        this.selectedUser = new User();
    }

    public void saveUser() {
        if (this.selectedUser.getId() == 0) {
            userService.createNewUser(selectedUser);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Added"));
        } else {
            userService.updateUser(selectedUser);
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }

    public void deleteUser() {
        userService.deleteUser(selectedUser.getId());
        this.users.remove(this.selectedUser);
        this.selectedUsers.remove(this.selectedUser);
        this.selectedUser = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedUsers()) {
            int size = this.selectedUsers.size();
            return size > 1 ? size + " users selected" : "1 user selected";
        }

        return "Delete";
    }

    public boolean hasSelectedUsers() {
        return this.selectedUsers != null && !this.selectedUsers.isEmpty();
    }

    public void deleteSelectedUsers() {
        for (User user : this.selectedUsers) {
            userService.deleteUser(user.getId());
        }
        this.users.removeAll(this.selectedUsers);
        this.selectedUsers = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Users Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
        PrimeFaces.current().executeScript("PF('dtUsers').clearFilters()");
    }

    @PreDestroy
    public void cleanUp() {
        // Cancel the timer
        if (timer != null) {
            timer.cancel();
        }
    }
}
