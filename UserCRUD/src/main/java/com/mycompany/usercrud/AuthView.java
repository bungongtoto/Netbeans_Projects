package com.mycompany.usercrud;

import entities.PasswordValidationResult;
import entities.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import services.PasswordUtil;
import services.UserService;

@Named("authView")
@SessionScoped
public class AuthView implements Serializable {
     private static final long serialVersionUID = 1L;
     
     private User authUser;
     private boolean loggedIn;
     
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
               PasswordValidationResult resultObj = pwdUtil.isValidPassword(authUser.getPassword());
               if (resultObj.isIsValid()){
                    hashPwd = pwdUtil.hashPassword(authUser.getPassword());
                    authUser.setPassword(hashPwd);
                    userService.createNewUser(authUser);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sign up Successful, go to login", null));
               } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resultObj.getErrMessage(), null));
                   return;
               }
               
           }else {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed. User name already in use", null));
           }
        }         
    }   
    
    public void login() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);

        if (authUser.getUsername() == null || authUser.getUsername().isEmpty()) {
            context.addMessage("form:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username is required", null));
        }

        if (authUser.getPassword() == null || authUser.getPassword().isEmpty()) {
            context.addMessage("form:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));
        }
        
        if(!context.getMessageList().isEmpty()){
            setLoggedIn(false);
            return;
        }else {
           User appUser =  userService.findByusername(authUser.getUsername());
           
           if(appUser == null) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Such User Name. Login Failed", null));
                setLoggedIn(false);
               return;
           }else {
//               System.out.println("App User: " + appUser.getPassword());
               if (pwdUtil.checkPassword(authUser.getPassword(), appUser.getPassword())){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "login Successful", null));
                    setLoggedIn(true);
                    session.setAttribute("authenticatedUser",appUser.getUsername() );
                    // Redirect to a secured page
                    externalContext.redirect("home.xhtml");
               } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password. Login Failed", null));
                    setLoggedIn(false);
                    return;
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
               PasswordValidationResult resultObj = pwdUtil.isValidPassword(authUser.getPassword());
               if (resultObj.isIsValid()){
                    hashPwd = pwdUtil.hashPassword(authUser.getPassword());
                    appUser.setPassword(hashPwd);
                    userService.changePassword(appUser);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Reset Successful, go to login", null));
               } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resultObj.getErrMessage(), null));
                   return;
               }
      
           }
        }   
    }
    
//    public void checkLoggedIn() throws IOException{
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        HttpSession session = (HttpSession) externalContext.getSession(false);
//
//        if (session == null || session.getAttribute("authenticatedUser") == null) {
//            externalContext.redirect("index.xhtml?redirect=home.xhtml");
//        }
//    }
    
     public void logout() throws IOException {
        // Invalidate session
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.invalidate();

        // Redirect to login page
        externalContext.redirect("index.xhtml");
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

    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
}
