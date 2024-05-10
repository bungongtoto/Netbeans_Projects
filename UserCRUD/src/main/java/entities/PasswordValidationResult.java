
package entities;

import jakarta.faces.bean.ManagedBean;

@ManagedBean(name="passwordValidationResult", eager=true)
public class PasswordValidationResult {
    private boolean isValid;
    private String errMessage;
    
    public PasswordValidationResult( boolean isValid ,String errMessage) {
        this.isValid = isValid;
        this.errMessage = errMessage;
    }
    
    public PasswordValidationResult() {
       
    }

    /**
     * @return the isValid
     */
    public boolean isIsValid() {
        return isValid;
    }

    /**
     * @param isValid the isValid to set
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * @return the errMessage
     */
    public String getErrMessage() {
        return errMessage;
    }

    /**
     * @param errMessage the errMessage to set
     */
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
    
    
}
