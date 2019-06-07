package org.group_orga.grouporgaapp.service;

import android.util.Log;

import org.group_orga.grouporgaapp.api.OrgaAPIAccessor;
import org.group_orga.grouporgaapp.api.data.Account;

import java8.util.concurrent.CompletableFuture;


public class UserService {
    private static UserService instance;
    private Account currentAccount;

    public static synchronized UserService getInstance(){
        if(instance==null){
            instance = new UserService();
        }
        return instance;
    }

    public CompletableFuture<Account> login(String email, String password) {
        return OrgaAPIAccessor.getInstance().login(email,password)
                .thenCompose(tokenResponse -> OrgaAPIAccessor.getInstance().getMe())
                .thenApply(account -> currentAccount=account);
    }

    public void register(String email, String password, String username) {
        Log.i("Login", "Register request with " + email + " " + password + " " + username);
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }
}
