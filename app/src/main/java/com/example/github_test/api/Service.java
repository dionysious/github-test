package com.example.github_test.api;

import com.example.github_test.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    //get method from the api https://api.github.com/search/users?q={filter}&page={pageNumber}
    //can be used by calling the getUserList method

    @GET("/search/users")
    Call<ItemResponse> getUserList(@Query("q") String filter, @Query("page") Integer pageNumber);
}
