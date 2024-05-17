
package services;

import entities.Department;
import jakarta.ejb.Stateless;
import jakarta.faces.bean.ManagedBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.List;

@ManagedBean(name="departmentService", eager=true)
@Stateless
public class DepartmentService {
    @PersistenceContext(name ="my_persistence_unit")
    private EntityManager entityManager;

   
    public List<Department> findAll() {
        try {
            return entityManager.createNamedQuery("Department.findAll", Department.class).getResultList();
        } catch (NoResultException e) {
            // Handle case where no users are found
            return Collections.emptyList(); // Or throw an exception, depending on your requirements
        } catch (Exception e) {
            // Handle other exceptions, like database connection issues
            e.printStackTrace(); // Log the exception or handle it according to your application's error handling strategy
            return null; // Or throw a custom exception, depending on your requirements
        }
    }
    
    public Department findByID(int id) {
        Query query = entityManager.createNamedQuery("Department.findByID", Department.class);
        return (Department) query.setParameter("id", id).getSingleResult();
    }
    
    
    public void createNewDepartment (Department department ) {
   
        try {
            
            entityManager.persist(department);
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void updateDepartment(Department updatedDepartment) {
         try {
            Department existingDepartment  = entityManager.find(Department.class, updatedDepartment.getId()); // Retrieve the entity
             if (existingDepartment != null) {
                // Update the existing Department entity with the new data
                existingDepartment.setName(updatedDepartment.getName());
                // Merge the updated entity into the persistence context
                entityManager.merge(existingDepartment);
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        }
    }
    
    public void deleteDepartment (int departmentId) {
       try {
            Department department = entityManager.find(Department.class, departmentId); // Retrieve the entity
            if (department != null) {
                entityManager.remove(department); // Now remove the managed entity
            }
            
        } catch (RuntimeException e) {
           
            throw e; // Re-throw the exception for caller to handle
        } 
    }
}
