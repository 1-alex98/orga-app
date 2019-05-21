package org.group_orga.grouporgaapp.service;

import android.util.Log;

public class UserService {
    private static UserService instance;

    public static synchronized UserService getInstance(){
        if(instance==null){
            instance = new UserService();
        }
        return instance;
    }

    public void login(String email, String password) {
        Log.i("Login", "Login request with " + email + " " + password);
    }

    public void register(String email, String password, String username) {
        Log.i("Login", "Register request with " + email + " " + password + " " + username);
    }
}
