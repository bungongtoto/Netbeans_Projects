
package services;

import entities.PayRoll;
import jakarta.ejb.Stateless;
import jakarta.faces.bean.ManagedBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.List;

@ManagedBean(name="payRollService", eager=true)
@Stateless
public class PayRollService {
    @PersistenceContext(name ="my_persistence_unit")
    private EntityManager entityManager;

   
    public List<PayRoll> findAll() {
        try {
            return entityManager.createNamedQuery("PayRoll.findAll", PayRoll.class).getResultList();
        } catch (NoResultException e) {
            // Handle case where no Employee are found
            return Collections.emptyList(); // Or throw an exception, depending on your requirements
        } catch (Exception e) {
            // Handle other exceptions, like database connection issues
            e.printStackTrace(); // Log the exception or handle it according to your application's error handling strategy
            return null; // Or throw a custom exception, depending on your requirements
        }
    }
    
    public PayRoll findByID(int id) {
        Query query = entityManager.createNamedQuery("PayRoll.findByID");
        return (PayRoll) query.setParameter("id", id).getSingleResult();
    }
    
    
    public void createNewPayRoll (PayRoll payRoll ) {
   
        try {
            
            entityManager.persist(payRoll);
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void updatePayRoll(PayRoll updatedPayRoll) {
         try {
            PayRoll existingPayRoll  = entityManager.find(PayRoll.class, updatedPayRoll.getId()); // Retrieve the entity
             if (existingPayRoll != null) {
                // Update the existing PayRoll entity with the new data
                existingPayRoll.setJob_title(updatedPayRoll.getJob_title());
                existingPayRoll.setSalary(updatedPayRoll.getSalary());
                // Merge the updated entity into the persistence context
                entityManager.merge(existingPayRoll);
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void deletePayRoll (int payRollId) {
       try {
            PayRoll payRoll = entityManager.find(PayRoll.class, payRollId); // Retrieve the entity
            if (payRoll != null) {
                entityManager.remove(payRoll); // Now remove the managed entity
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        } 
    }
}
