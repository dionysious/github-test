package com.example.github_test.api;

import com.example.github_test.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("/search/users")
    Call<ItemResponse> getUserList(@Query("q") String filter, @Query("page") Integer pageNumber);
}
