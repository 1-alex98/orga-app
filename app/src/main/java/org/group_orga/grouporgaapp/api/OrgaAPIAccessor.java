package org.group_orga.grouporgaapp.api;

import java.io.IOException;

import java8.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrgaAPIAccessor {
    private static final OrgaAPIAccessor ourInstance = new OrgaAPIAccessor();
    private final OrgaAPITemplate service;

    private OrgaAPIAccessor() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.group-orga.org/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        service = retrofit.create(OrgaAPITemplate.class);
    }

    public static OrgaAPIAccessor getInstance() {
        return ourInstance;
    }

    public CompletableFuture<String> getVersion() {
        return CompletableFuture.supplyAsync(() -> {
            Call<String> version = service.getVersion();
            try {
                Response<String> stringResponse = version.execute();
                if (!stringResponse.isSuccessful())
                    throw new APIException(stringResponse);
                return stringResponse.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
