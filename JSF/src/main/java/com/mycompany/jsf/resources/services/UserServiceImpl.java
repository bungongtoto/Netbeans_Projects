package com.mycompany.jsf.resources.services;

import com.mycompany.jsf.resources.entities.User;
import jakarta.ejb.Stateless;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;


@ManagedBean(name="userServiceImpl", eager=true)
@SessionScoped
@Stateless
public class UserServiceImpl implements UserService {
    
    @PersistenceContext(name ="my_persistence_unit")
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u").getResultList();
    }
    
}
