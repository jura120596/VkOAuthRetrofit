package com.example.vkoauth;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VkServer {
    public static String OAUTH_SERVER_URL = "https://oauth.vk.com/";
    public static String API_SERVER_URL = "https://api.vk.com/";
    public static Retrofit oauth() {
        return new Retrofit.Builder()
                .baseUrl(OAUTH_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit api() {
        return new Retrofit.Builder()
                .baseUrl(API_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
