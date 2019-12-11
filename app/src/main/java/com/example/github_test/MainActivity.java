package com.example.github_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

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

//        ################################################################################
//        ##    For making search Suggestion using the String array called SUGGESTION,  ##
//        ##    could be utilize into auto suggestion based on input?                   ##
//        ##    maybe additional feature if the main feature finish                     ##
//        ################################################################################

//        mMaterialSearchView.setSuggestions(SUGGESTION);

        final ListView listView = findViewById(R.id.listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , SUGGESTION);
        listView.setAdapter(arrayAdapter);

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
