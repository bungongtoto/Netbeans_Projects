package moduledatabase.services;
import jakarta.ejb.Remote;
import java.util.List;
import moduledatabase.entities.User;

@Remote
public interface UserService {
    
    public  List<User> findAll();
}