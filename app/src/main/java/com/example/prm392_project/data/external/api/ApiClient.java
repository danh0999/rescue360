package com.example.prm392_project.data.external.api;

import static com.example.prm392_project.constants.ApiConst.BASE_URL;

import android.content.Context;

import com.example.prm392_project.utils.TokenManager;

import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            TokenManager tokenManager = new TokenManager(context);
            String token = tokenManager.getToken();
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            // Add Authorization header if token exists
            clientBuilder.addInterceptor(chain -> {
                Request.Builder requestBuilder = chain.request().newBuilder();
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer " + token);
                }
                return chain.proceed(requestBuilder.build());
            });

            clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }
        return retrofit;
    }
}
