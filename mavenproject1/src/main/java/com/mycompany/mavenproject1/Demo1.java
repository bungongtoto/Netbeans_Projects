package com.mycompany.mavenproject1;

import javax.naming.Context;
import moduledatabase.entities.User;
import moduledatabase.main.ClientUtility;
import moduledatabase.main.ClientUtility;
import moduledatabase.services.UserService;

public class Demo1 {
    public static void main (String [] args) {
        try{
            Context context = ClientUtility.getInitialContext();
            UserService userService = (UserService) context.lookup("java:global/ModuleDatabase-1.0-SNAPSHOT/UserServiceImpl!moduledatabase.services.UserService");
            System.out.println("User List");
            for (User user: userService.findAll() ){
                System.out.println("ID " + user.getId());
                System.out.println("First " + user.getFirst());
                System.out.println("Last " + user.getLast());
                System.out.println("Age " + user.getAge());
                System.out.println("===================================");
            }
        } catch( Exception e){
            System.err.println(e.getMessage());
        }
    }
}
