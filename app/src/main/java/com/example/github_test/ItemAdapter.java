package com.example.github_test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.github_test.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;

    public ItemAdapter(List<Item> itemArrayList, Context applicationContext) {
        this.items = itemArrayList;
        this.context = applicationContext;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        viewHolder.gitUsername.setText(items.get(i).getLogin());
        viewHolder.avatarUrl = items.get(i).getAvatarUrl();

        //To convert the avatarUrl into image
        Picasso.get()
                .load(items.get(i).getAvatarUrl())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView gitUsername;
        private ImageView imageView;
        String avatarUrl = "";



    public ViewHolder(View view) {
        super(view);
        gitUsername = view.findViewById(R.id.title);
        imageView = view.findViewById(R.id.cover);

        ///on item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }

        });
    }
    }
}
