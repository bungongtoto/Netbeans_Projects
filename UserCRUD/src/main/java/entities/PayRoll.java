package entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PayRoll")
@NamedQueries({
    @NamedQuery(name = "PayRoll.findAll", query = "SELECT p FROM PayRoll p"),
    @NamedQuery(name = "PayRoll.findByID", query = "SELECT p FROM PayRoll p WHERE p.id = :id"),})
public class PayRoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String job_title;
    private int salary;

    public PayRoll() {
    }

    public PayRoll(int id, String job_title, int salary) {
        this.id = id;
        this.job_title = job_title;
        this.salary = salary;
    }

    public PayRoll(String job_title, int salary) {
        this.job_title = job_title;
        this.salary = salary;
    }

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
     * @return the job_title
     */
    public String getJob_title() {
        return job_title;
    }

    /**
     * @param job_title the job_title to set
     */
    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    /**
     * @return the salary
     */
    public int getSalary() {
        return salary;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PayRoll payRoll = (PayRoll) o;
        return Objects.equals(id, payRoll.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
