package org.group_orga.grouporgaapp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.retrofit.JSONAPIConverterFactory;

import org.group_orga.grouporgaapp.api.data.Account;
import org.group_orga.grouporgaapp.api.data.GroupOfUsers;
import org.group_orga.grouporgaapp.api.oauth.OrgaAPIOauthTemplate;
import org.group_orga.grouporgaapp.api.oauth.TokenResponse;

import java.io.IOException;
import java.util.List;

import java8.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrgaAPIAccessor {
    private static final OrgaAPIAccessor ourInstance = new OrgaAPIAccessor();
    private static final String CLIENT_SECRETE="3c0c001e-8899-11e9-bc42-526af7764f64";
    private static final String CLIENT_ID="6ee8261e-770c-11e9-8f9e-2a86e4085a59";
    private final OrgaAPIOauthTemplate oauthService;
    private OrgaAPITemplate service;

    private OrgaAPIAccessor() {
        Retrofit oauthRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.group-orga.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        oauthService = oauthRetrofit.create(OrgaAPIOauthTemplate.class);
    }

    public static OrgaAPIAccessor getInstance() {
        return ourInstance;
    }

    public CompletableFuture<TokenResponse> login(String email, String password){
        Call<TokenResponse> call = oauthService.getToken("password", email, password, CLIENT_ID, CLIENT_SECRETE);
        return executeQueryAsync(call)
                .thenApply(tokenResponse -> {
                    createServiceAfterLogin(tokenResponse);
                    return tokenResponse;
                });
    }

    private <T> CompletableFuture<T> executeQueryAsync(Call<T> call) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Response<T> response = call.execute();
                if(!response.isSuccessful()){
                    throw new APIException(response);
                }
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createServiceAfterLogin(TokenResponse body) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+body.getAccessToken()).build();
            return chain.proceed(request);
        });

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        //TODO: find a way to scan the package for classes instead of adding them manually
        ResourceConverter converter = new ResourceConverter(mapper,Account.class,GroupOfUsers.class);
        JSONAPIConverterFactory factory = new JSONAPIConverterFactory(converter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.group-orga.org/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(factory)
                .client(httpClient.build())
                .build();

        service = retrofit.create(OrgaAPITemplate.class);
    }

    private void assertLoggedIn(){
        if(service==null){
            throw new IllegalStateException("This operation needs you to be logged in but you are not.");
        }
    }

    public CompletableFuture<List<GroupOfUsers>> getGroupsOfUsersWithFilter(String filter) {
        assertLoggedIn();
        return executeQueryAsync(service.getGroupsOfUsers(filter));
    }

    public CompletableFuture<String> getVersion() {
        return executeQueryAsync(service.getVersion());
    }

    public CompletableFuture<Account> getMe() {
        return executeQueryAsync(service.getMe());
    }

    public CompletableFuture<GroupOfUsers> createGroup(String name, String password) {
        GroupOfUsers groupOfUsers = new GroupOfUsers();
        groupOfUsers.setName(name);
        groupOfUsers.setPassword(password);
        return executeQueryAsync(service.postNewGroup(groupOfUsers));
    }

    public static class APIException extends RuntimeException {
        private final Response response;

        public APIException(Response response) {
            this.response = response;
        }

        public Response getResponse() {
            return response;
        }
    }
}
