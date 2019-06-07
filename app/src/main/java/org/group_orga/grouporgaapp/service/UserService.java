package org.group_orga.grouporgaapp.service;

import android.util.Log;

import org.group_orga.grouporgaapp.api.OrgaAPIAccessor;
import org.group_orga.grouporgaapp.api.oauth.TokenResponse;

import java8.util.concurrent.CompletableFuture;


public class UserService {
    private static UserService instance;

    public static synchronized UserService getInstance(){
        if(instance==null){
            instance = new UserService();
        }
        return instance;
    }

    public CompletableFuture<TokenResponse> login(String email, String password) {
        return OrgaAPIAccessor.getInstance().login(email,password);
    }

    public void register(String email, String password, String username) {
        Log.i("Login", "Register request with " + email + " " + password + " " + username);
    }
}
