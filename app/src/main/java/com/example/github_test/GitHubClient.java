package com.example.github_test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubClient {
    @GET("https://api.github.com/search/users?q={user}+type:user")
    Call<List<GitHubRepo>> reposForUser(
            @Path("user") String user
    );
}
