package org.group_orga.grouporgaapp.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OrgaAPITemplate {

    @GET("version")
    Call<String> getVersion();
}
