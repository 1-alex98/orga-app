package org.group_orga.grouporgaapp.service;

public class UserService {
    private static UserService instance;

    public static synchronized UserService getInstance(){
        if(instance==null){
            instance = new UserService();
        }
        return instance;
    }

    public void login(String email, String password) {
        //TODO:login
    }
}
