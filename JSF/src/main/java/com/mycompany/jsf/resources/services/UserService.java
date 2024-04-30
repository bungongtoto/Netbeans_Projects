package com.mycompany.jsf.resources.services;
import com.mycompany.jsf.resources.entities.User;
import jakarta.ejb.Remote;
import java.util.List;


@Remote
public interface UserService {
    
    public  List<User> findAll();
}