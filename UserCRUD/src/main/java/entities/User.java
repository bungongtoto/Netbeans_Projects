
package entities;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="USER")
@NamedQueries({
    @NamedQuery(name="User.findAll", query="SELECT u FROM User u"), 
    @NamedQuery(name="User.findByID", query="SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name="User.findByUsername", query="SELECT u FROM User u WHERE u.username = :username")
})
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String first;
    private String last;
    private int age;
    private String password;
    private String username;
    
    public User(int id, String first, String last, int age, String password, String username){
        this.id = id;
        this.first = first;
        this.last = last;
        this.age = age;
        this.password = password;
        this.username = username;
    }
    
    public User( String first, String last, int age, String password, String username){
        this.first = first;
        this.last = last;
        this.age = age;
        this.password = password;
        this.username = username;
    }
    
    public User(  String password, String username){
        this.password = password;
        this.username = username;
    }
    
    public User(){}

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
     * @return the first
     */
    public String getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * @return the last
     */
    public String getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(String last) {
        this.last = last;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    
      
}

