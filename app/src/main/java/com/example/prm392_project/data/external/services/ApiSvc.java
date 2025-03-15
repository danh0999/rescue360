package com.example.prm392_project.data.external.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiSvc {

    @GET("{endpoint}")
    Call<Object> get(@Path("endpoint") String endpoint);

    @POST("{endpoint}")
    Call<Object> post(@Path("endpoint") String endpoint, @Body Object body);

    @PUT("{endpoint}")
    Call<Object> put(@Path("endpoint") String endpoint, @Body Object body);

    @DELETE("{endpoint}")
    Call<Object> delete(@Path("endpoint") String endpoint);
}

