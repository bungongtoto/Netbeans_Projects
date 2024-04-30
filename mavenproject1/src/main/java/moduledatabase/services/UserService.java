package moduledatabase.services;
import java.util.List;
import moduledatabase.entities.User;


public interface UserService {
    
    public  List<User> findAll();
}