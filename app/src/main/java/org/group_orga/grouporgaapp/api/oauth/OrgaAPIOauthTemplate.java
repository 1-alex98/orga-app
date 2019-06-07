package org.group_orga.grouporgaapp.api.oauth;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrgaAPIOauthTemplate {
    @POST("oauth/token")
    Call<TokenResponse> getToken(@Query("grant_type") String token_type, @Query("username") String email, @Query("password")String password, @Query("client_id")String clientId, @Query("client_secret")String clientSecret);
}
