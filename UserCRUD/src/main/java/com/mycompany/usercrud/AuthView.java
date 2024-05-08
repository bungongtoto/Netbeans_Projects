package com.mycompany.usercrud;

import entities.User;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import services.PasswordUtil;
import services.UserService;

@Named("authView")
@ViewScoped
public class AuthView implements Serializable {
     private static final long serialVersionUID = 1L;
     
     private User authUser;
     
     @Inject
     private UserService userService;
     
     private PasswordUtil pwdUtil = new PasswordUtil();

    
    public void openNew() {
        authUser = new User();
    }
     
    public void signUp(){
        String hashPwd = "" ;
        FacesContext context = FacesContext.getCurrentInstance();

        if (authUser.getUsername() == null || authUser.getUsername().isEmpty()) {
            context.addMessage("form:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username is required", null));
        }

        if (authUser.getPassword() == null || authUser.getPassword().isEmpty()) {
            context.addMessage("form:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));
        }
        
        if(!context.getMessageList().isEmpty()){
            return;
        }else {
           User appUser =  userService.findByusername(authUser.getUsername());
           if (appUser == null){
               hashPwd = pwdUtil.hashPassword(authUser.getPassword());
               authUser.setPassword(hashPwd);
               userService.createNewUser(authUser);
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sign up Successful, go to login", null));
           }else {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed. User name already in use", null));
           }
        }         
    }   
    
    public String login(){
        FacesContext context = FacesContext.getCurrentInstance();

        if (authUser.getUsername() == null || authUser.getUsername().isEmpty()) {
            context.addMessage("form:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username is required", null));
        }

        if (authUser.getPassword() == null || authUser.getPassword().isEmpty()) {
            context.addMessage("form:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));
        }
        
        if(!context.getMessageList().isEmpty()){
            return null;
        }else {
           User appUser =  userService.findByusername(authUser.getUsername());
           
           if(appUser == null) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Such User Name. Login Failed", null));
               return null;
           }else {
               System.out.println("App User: " + appUser.getPassword());
               if (pwdUtil.checkPassword(authUser.getPassword(), appUser.getPassword())){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "login Successful", null));
           
                    return "home.xhtml?faces-redirect=true";
               } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password. Login Failed", null));
                    return null;
               }
           }
        }         
    }
    
    public void changePassword(){
        String hashPwd = "" ;
        FacesContext context = FacesContext.getCurrentInstance();

        if (authUser.getUsername() == null || authUser.getUsername().isEmpty()) {
            context.addMessage("form:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username is required", null));
        }

        if (authUser.getPassword() == null || authUser.getPassword().isEmpty()) {
            context.addMessage("form:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));
        }
        
        if(!context.getMessageList().isEmpty()){
            return;
        }else {
           User appUser =  userService.findByusername(authUser.getUsername());
           if (appUser == null){
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No such user name", null));
           }else {
               hashPwd = pwdUtil.hashPassword(authUser.getPassword());
               appUser.setPassword(hashPwd);
               userService.changePassword(appUser);
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Reset Successful, go to login", null));
           }
        }   
    }

    /**
     * @return the authUser
     */
    public User getAuthUser() {
        return authUser;
    }

    /**
     * @param authUser the authUser to set
     */
    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }
    
}
