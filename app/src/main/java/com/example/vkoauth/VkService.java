package com.example.vkoauth;

import com.example.vkoauth.responses.AccessTokenResponse;
import com.example.vkoauth.responses.VkUsersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VkService {
    @POST("access_token/")
    //{"access_token":"533b6a3", "expires_in":43200, "user_id":66748}
    public Call<AccessTokenResponse> getAccessToken(@Query("code") String code,
                                                    @Query("client_id") String clientID,
                                                    @Query("client_secret") String clientSecret,
                                                    @Query("redirect_uri") String redirectUri);
    @POST("method/users.get")
    public Call<VkUsersResponse> getUserProfile(@Query("v") String version, @Query("fields") String fields,
                                                @Query("access_token") String token);
}
