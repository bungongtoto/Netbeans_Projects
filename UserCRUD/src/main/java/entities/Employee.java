
package entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Employee")
@NamedQueries({
    @NamedQuery(name="Employee.findAll", query="SELECT e FROM Employee e"), 
    @NamedQuery(name="Employee.findByID", query="SELECT e FROM Employee e WHERE e.id = :id"),
})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "payroll_id")
    private PayRoll payRoll;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * @return the payRoll
     */
    public PayRoll getPayRoll() {
        return payRoll;
    }

    /**
     * @param payRoll the payRoll to set
     */
    public void setPayRoll(PayRoll payRoll) {
        this.payRoll = payRoll;
    }
    
    
}
