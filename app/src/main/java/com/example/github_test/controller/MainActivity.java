package com.example.github_test.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.github_test.EndlessRecyclerViewScrollListener;
import com.example.github_test.ItemAdapter;
import com.example.github_test.R;
import com.example.github_test.api.Client;
import com.example.github_test.api.Service;
import com.example.github_test.model.Item;
import com.example.github_test.model.ItemResponse;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public String keyword ="";

    public int pageNumber;

    private RecyclerView recyclerView;
    TextView Disconnected;
    private Item item;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    LinearLayoutManager linearLayoutManager;
    List<Item> itemList = new ArrayList<Item>();
    RecyclerView.Adapter adapter = null;


    private EndlessRecyclerViewScrollListener scrollListener;


//
//    private static String[] SUGGESTION = new String[]{
//
//            "Curry", "Udon", "Karaage"
//
//    };
//
//    private MaterialSearchView mMaterialSearchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        initViews();
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager= new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(0);

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
                Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();

            }
        });

        initializeRecyclerView();

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadJSON();
            }
        });





//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
//
//        mMaterialSearchView = findViewById(R.id.searchView);

//        ################################################################################
//        ##    For making search Suggestion using the String array called SUGGESTION,  ##
//        ##    could be utilize into auto suggestion based on input?                   ##
//        ##    maybe additional feature if the main feature finish                     ##
//        ################################################################################

//        mMaterialSearchView.setSuggestions(SUGGESTION);

//        final ListView listView = findViewById(R.id.listView);
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , SUGGESTION);
//        listView.setAdapter(arrayAdapter);
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
//        });
    }

    private void initViews(){
        pd =  new ProgressDialog(this);
        pd.setMessage("Fetching users..");
        pd.setCancelable(false);
        pd.show();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
//        loadJSON();
    }

    private void initializeRecyclerView() {
        adapter = new ItemAdapter(itemList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        // recyclerView.smoothScrollToPosition(0);
        swipeContainer.setRefreshing(false);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    private void loadJSON(){
        Disconnected = findViewById(R.id.disconnected);
        try {
            Client client = new Client();
            Service apiService =
                    Client.getClient().create(Service.class);
            if(keyword == ""){
                keyword = "grace";
            }
            if(pageNumber == 0){
                pageNumber = 1;
            }
            Call<ItemResponse> call = apiService.getUserList(keyword, pageNumber);

            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    List<Item> items = response.body().getItems();
                    itemList.addAll(items);


                    adapter.notifyDataSetChanged();
//                    pd.hide();

                }

                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Disconnected.setVisibility(View.VISIBLE);
//                    pd.hide();
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextDataFromApi(int page) {
        pageNumber++;
        loadJSON();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem menuItem = menu.findItem(R.id.searchMenu);
//        mMaterialSearchView.setMenuItem(menuItem);
//        return super.onCreateOptionsMenu(menu);
//    }
}
