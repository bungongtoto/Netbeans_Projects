
package services;

import entities.Employee;
import jakarta.ejb.Stateless;
import jakarta.faces.bean.ManagedBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.List;

@ManagedBean(name="employeeService", eager=true)
@Stateless
public class EmployeeService {
    @PersistenceContext(name ="my_persistence_unit")
    private EntityManager entityManager;

   
    public List<Employee> findAll() {
        try {
            return entityManager.createNamedQuery("Employee.findAll", Employee.class).getResultList();
        } catch (NoResultException e) {
            // Handle case where no Employees are found
            return Collections.emptyList(); // Or throw an exception, depending on your requirements
        } catch (Exception e) {
            // Handle other exceptions, like database connection issues
            e.printStackTrace(); // Log the exception or handle it according to your application's error handling strategy
            return null; // Or throw a custom exception, depending on your requirements
        }
    }
    
    public Employee findByID(int id) {
        Query query = entityManager.createNamedQuery("Employee.findByID");
        return (Employee) query.setParameter("id", id).getSingleResult();
    }
    
    
    public void createNewPayRoll (Employee payRoll ) {
   
        try {
            
            entityManager.persist(payRoll);
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void updateEmployee(Employee updatedEmployee) {
         try {
            Employee existingEmployee  = entityManager.find(Employee.class, updatedEmployee.getId()); // Retrieve the entity
             if (existingEmployee != null) {
                // Update the existing PayRoll entity with the new data
                existingEmployee.setName(updatedEmployee.getName());
                existingEmployee.setDepartment(updatedEmployee.getDepartment());
                existingEmployee.setPayRoll(updatedEmployee.getPayRoll());
                
                // Merge the updated entity into the persistence context
                entityManager.merge(existingEmployee);
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void deletePayRoll (int employeeId) {
       try {
            Employee employee = entityManager.find(Employee.class, employeeId); // Retrieve the entity
            if (employee != null) {
                entityManager.remove(employee); // Now remove the managed entity
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        } 
    }
}
