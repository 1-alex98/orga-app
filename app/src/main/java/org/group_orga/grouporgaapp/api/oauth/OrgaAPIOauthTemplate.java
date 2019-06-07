package org.group_orga.grouporgaapp.api.oauth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrgaAPIOauthTemplate {
    @FormUrlEncoded
    @POST("oauth/token")
    Call<TokenResponse> getToken(@Field("grant_type")String token_type,@Field("username") String email,@Field("password")String password, @Field("client_id")String clientId,@Field("client_secret")String clientSecret);
}
