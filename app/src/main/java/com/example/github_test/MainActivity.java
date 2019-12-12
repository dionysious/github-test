package com.example.github_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.github_test.GitHubRepoAdapter;

public class MainActivity extends AppCompatActivity {

    private static String[] SUGGESTION = new String[]{

            "Curry", "Udon", "Karaage"

    };

    private MaterialSearchView mMaterialSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        mMaterialSearchView = findViewById(R.id.searchView);

        final ListView listView = findViewById(R.id.listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , SUGGESTION);
        listView.setAdapter(arrayAdapter);

        Retrofit.Builder builder = new Retrofit.Builder().
                baseUrl("https://api.github.com/").
                addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GitHubClient client = retrofit.create(GitHubClient.class);

        Call<List<GitHubRepo>> call = client.reposForUser("fs-opensource");

        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {

                //When you got a response
                List<GitHubRepo> repos = response.body();

                //pass data to listview

                listView.setAdapter(new GitHubRepoAdapter(MainActivity.this, repos));

            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {

                //Usually cause internet connection

                Toast.makeText(MainActivity.this, "onfailure enqueue", Toast.LENGTH_SHORT).show();

            }
        });


//        ################################################################################
//        ##    For making search Suggestion using the String array called SUGGESTION,  ##
//        ##    could be utilize into auto suggestion based on input?                   ##
//        ##    maybe additional feature if the main feature finish                     ##
//        ################################################################################

//        mMaterialSearchView.setSuggestions(SUGGESTION);


        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1 , SUGGESTION);


                listView.setAdapter(arrayAdapter);
            }
        });

        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1 );

                if(newText!= null && !newText.isEmpty()){

                    for(String s : SUGGESTION){
                        if(s.toLowerCase().contains(newText))
                            arrayAdapter.add(s);
                    }
                }else{
                    arrayAdapter.addAll(SUGGESTION);
                }

                listView.setAdapter(arrayAdapter);

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        mMaterialSearchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }
}
