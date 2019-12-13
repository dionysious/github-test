package com.example.github_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static String[] SUGGESTION = new String[]{

//            "Curry", "Udon", "Karaage"

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
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , SUGGESTION);
//        listView.setAdapter(arrayAdapter);

        Retrofit.Builder builder = new Retrofit.Builder().
                baseUrl("https://api.github.com/").
                addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GitHubClient client = retrofit.create(GitHubClient.class);

        Call<List<GitHubUser>> call = client.reposForUser("dionysious");

        call.enqueue(new Callback<List<GitHubUser>>() {
            @Override
            public void onResponse(Call<List<GitHubUser>> call, Response<List<GitHubUser>> response) {

                if(response.body() != null) {

                    //When you got a response
                    List<GitHubUser> repos = response.body();

                    //pass data to listview

                    listView.setAdapter(new GitHubUserAdapter(MainActivity.this, repos));
                }else{
                    Toast.makeText(MainActivity.this, "OnResponse tp null", Toast.LENGTH_SHORT).show();
                    Log.d("Enqueu OnResponse null", "OnResponse status return : " + response);
                    Log.d("Enqueue OnResponse null", "OnResponse status return body :" + response.body());
                    Log.d("Enqueu OnResponse null", "OnResponse status return code:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<GitHubUser>> call, Throwable t) {

                //Usually cause internet connection

                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

                Log.d("Enqueue OnFailure", "OnFailure check" + call );

            }
        });


//        ################################################################################
//        ##    For making search Suggestion using the String array called SUGGESTION,  ##
//        ##    could be utilize into auto suggestion based on input?                   ##
//        ##    maybe additional feature if the main feature finish                     ##
//        ################################################################################

//        mMaterialSearchView.setSuggestions(SUGGESTION); n

//
//        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1 , SUGGESTION);
//
//
//                listView.setAdapter(arrayAdapter);
//            }
//        });
//
//        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1 );
//
//                if(newText!= null && !newText.isEmpty()){
//
//                    for(String s : SUGGESTION){
//                        if(s.toLowerCase().contains(newText))
//                            arrayAdapter.add(s);
//                    }
//                }else{
//                    arrayAdapter.addAll(SUGGESTION);
//                }
//
//                listView.setAdapter(arrayAdapter);
//
//                return false;
//            }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        mMaterialSearchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }
}
