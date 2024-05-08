package services;

import jakarta.ejb.Stateless;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import org.mindrot.jbcrypt.BCrypt;

@ManagedBean(name="passwordUtil", eager=true)
public class PasswordUtil {
     public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }
}
