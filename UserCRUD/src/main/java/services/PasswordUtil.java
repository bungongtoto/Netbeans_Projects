package services;

import entities.PasswordValidationResult;
import jakarta.faces.bean.ManagedBean;
import org.mindrot.jbcrypt.BCrypt;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

@ManagedBean(name="passwordUtil", eager=true)
public class PasswordUtil {
    
    private PasswordValidator validator = new PasswordValidator(
        // length between 8 and 16 characters
        new LengthRule(8, 16),

        // at least one upper-case character
        new CharacterRule(EnglishCharacterData.UpperCase, 1),

        // at least one lower-case character
        new CharacterRule(EnglishCharacterData.LowerCase, 1),

        // at least one digit character
        new CharacterRule(EnglishCharacterData.Digit, 1),

        // at least one symbol (special character)
        new CharacterRule(EnglishCharacterData.Special, 1),

        // define some illegal sequences that will fail when >= 5 chars long
        // alphabetical is of the form 'abcde', numerical is '34567', qwery is 'asdfg'
        // the false parameter indicates that wrapped sequences are allowed; e.g. 'xyzabc'
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
        new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),

        // no whitespace
        new WhitespaceRule()
    );
    
     public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }
    
    public PasswordValidationResult isValidPassword(String candidate) {
        PasswordValidationResult resultObj = new PasswordValidationResult();
        RuleResult result = validator.validate(new PasswordData(candidate));

        if (result.isValid()) {
          resultObj.setIsValid(true);
          resultObj.setErrMessage(null);
        } else {
         String errMessage = "";
          for (String msg : validator.getMessages(result)) {
            errMessage = errMessage + ", " + msg;
          }
          
          resultObj.setIsValid(false);
          resultObj.setErrMessage(errMessage);
        }
        
        return resultObj;
    }
}
