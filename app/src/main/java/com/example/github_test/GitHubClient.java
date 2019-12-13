package com.example.github_test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubClient {
    @GET("/users/{user}/repos")
    Call<List<GitHubUser>> reposForUser(
            @Path("user") String user
    );
}
