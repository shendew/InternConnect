package com.kingdew.internconnect.api;

import com.kingdew.internconnect.models.Job;
import com.kingdew.internconnect.models.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("user")
    Call<User> registerUser(@Body User user);

    @GET("user")
    Call<List<User>> getAllUsers();

    @GET("user")
    Call<List<User>> searchUser(@Query("email") String email);


    @POST("jobs")
    Call<Job> addJob(@Body Job job);

    @GET("jobs")
    Call<List<Job>> getAllJobs();

    @PUT("jobs/{id}")
    Call<Job> updateJob(@Path("id") String id, @Body Job job);

    @GET("jobs")
    Call<List<Job>> searchJobs(@Query("title") String query);


}