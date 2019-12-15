package com.example.github_test.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public String keyword ="";
    public int pageNumber;

    private EndlessRecyclerViewScrollListener scrollListener;
    private MaterialSearchView mMaterialSearchView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    TextView Disconnected;
    LinearLayoutManager linearLayoutManager;
    PublishSubject<String> querySearchSubject;

    List<Item> itemList = new ArrayList<Item>();
    RecyclerView.Adapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager= new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(0);

        initViews();

        initRecyclerView();

    }

    private void initSwipeContainer(){
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
                Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    private void initMaterialSearchView(){
        mMaterialSearchView = findViewById(R.id.searchView);
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                querySearchSubject.onNext(newText);
                return false;
            }
        });
    }

    private void initQuerySearchSubject(){
        querySearchSubject = PublishSubject.create();
        querySearchSubject.debounce(1, TimeUnit.SECONDS).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(String s) {
                itemList.clear();
                keyword = s;
                pageNumber = 1;
                loadJSON();
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    private void initViews(){

        initSwipeContainer();

        initToolbar();

        initMaterialSearchView();

        initQuerySearchSubject();


    }

    private void initRecyclerView() {
        adapter = new ItemAdapter(itemList, getApplicationContext());
        recyclerView.setAdapter(adapter);
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
            Service apiService = Client.getClient().create(Service.class);
            //Default search keyword and pagenumber,, just in case. to avoid null object reference when get from api
            if(keyword == ""){ keyword = "grace"; }
            if(pageNumber == 0){ pageNumber = 1; }

            //getting the itemResponse using getUserList method from the service
            Call<ItemResponse> call = apiService.getUserList(keyword, pageNumber);
            call.enqueue(new Callback<ItemResponse>() {
                //Got Response
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    //Response success code == 200
                    if (response.code() == 200) {
                        List<Item> items = response.body().getItems();
                        itemList.addAll(items);
                        adapter.notifyDataSetChanged();
                    }
                    //Response failed because of rate limit,, response code == 403
                    else if(response.code() == 403){
                        Toast.makeText(MainActivity.this, "Rate limit reached, please wait another minute to make another search", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                }
                //Got no response,, usually because of the api or the network connection
                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Disconnected.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        mMaterialSearchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }
}