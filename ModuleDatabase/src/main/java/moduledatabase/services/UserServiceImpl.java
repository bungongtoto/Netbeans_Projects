package moduledatabase.services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import moduledatabase.entities.User;

@Stateless
public class UserServiceImpl implements UserService {
    
    @PersistenceContext(name ="ModuleDatabase-1.0-SNAPSHOT")
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u").getResultList();
    }
    
}
