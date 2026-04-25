package com.kingdew.internconnect.api;

import com.kingdew.internconnect.models.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user")
    Call<User> registerUser(@Body User user);

    @GET("user")
    Call<List<User>> getAllUsers();
}