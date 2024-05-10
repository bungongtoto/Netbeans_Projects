package services;

import entities.User;
import jakarta.ejb.Stateless;
import jakarta.faces.bean.ManagedBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.List;

@ManagedBean(name="userService", eager=true)
@Stateless
public class UserService {
    @PersistenceContext(name ="my_persistence_unit")
    private EntityManager entityManager;

   
    public List<User> findAll() {
        try {
            return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
        } catch (NoResultException e) {
            // Handle case where no users are found
            return Collections.emptyList(); // Or throw an exception, depending on your requirements
        } catch (Exception e) {
            // Handle other exceptions, like database connection issues
            e.printStackTrace(); // Log the exception or handle it according to your application's error handling strategy
            return null; // Or throw a custom exception, depending on your requirements
        }
    }
    
    public User findByID(int id) {
        Query query = entityManager.createNamedQuery("User.findByID");
        return (User) query.setParameter("id", id).getSingleResult();
    }
    
    public User findByusername(String username) {
        try {
            Query query = entityManager.createNamedQuery("User.findByUsername");
            return (User) query.setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            // Handle the case where no user with the given username was found
            return null; // or throw a custom exception, log a message, etc.
        }
    }
    
    public void createNewUser(User user ) {
   
        try {
            
            entityManager.persist(user);
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void updateUser(User updatedUser) {
         try {
            User existingUser  = entityManager.find(User.class, updatedUser.getId()); // Retrieve the entity
             if (existingUser != null) {
                // Update the existing user entity with the new data
                existingUser.setFirst(updatedUser.getFirst());
                existingUser.setLast(updatedUser.getLast());
                existingUser.setAge(updatedUser.getAge());

                // Merge the updated entity into the persistence context
                entityManager.merge(existingUser);
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
     public void changePassword(User updatedUser) {
         try {
            User existingUser  = entityManager.find(User.class, updatedUser.getId()); // Retrieve the entity
             if (existingUser != null) {
                // Update the existing user entity with the new data
                existingUser.setPassword(updatedUser.getPassword());
                // Merge the updated entity into the persistence context
                entityManager.merge(existingUser);
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void deleteUser(int userId) {
       try {
            User user = entityManager.find(User.class, userId); // Retrieve the entity
            if (user != null) {
                entityManager.remove(user); // Now remove the managed entity
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
       
        
    }
}
