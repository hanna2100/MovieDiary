package com.example.user.moviediary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;

import java.util.ArrayList;

public class MovieDiaryAdapter2 extends RecyclerView.Adapter<MovieDiaryAdapter2.CustomViewHolder> {

    private int layout;
    private ArrayList<MovieDiary> list;
    private View view;
    private Context context;

    public MovieDiaryAdapter2(int layout, ArrayList<MovieDiary> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        MovieDiaryAdapter2.CustomViewHolder customViewHolder = new MovieDiaryAdapter2.CustomViewHolder(view);
        context = viewGroup.getContext();

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int i) {

        MovieDiary movieDiary = list.get(i);
        customViewHolder.imageView.setImageResource(movieDiary.getDetailImage());
        customViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, i+"번", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()  {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieImage);
        }
    }
}
