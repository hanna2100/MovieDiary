package com.example.user.moviediary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.fragment.FrgMovieDetails;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.util.GlideApp;

import java.util.ArrayList;

public class MovieLikeAdapter extends RecyclerView.Adapter<MovieLikeAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<MovieDiary> list;
    private View view;
    private Context context;

    public MovieLikeAdapter(int layout, ArrayList<MovieDiary> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        MovieLikeAdapter.CustomViewHolder customViewHolder = new MovieLikeAdapter.CustomViewHolder(view);
        context = viewGroup.getContext();
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int i) {

        final MovieDiary movieDiary = list.get(i);

        Log.d("TAG",movieDiary.getDetailTitle());

        GlideApp.with(context).load("https://image.tmdb.org/t/p/w500"+movieDiary.getDetailImage()).into(customViewHolder.imageView);

        customViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).setChangeFragment(FrgMovieDetails.newInstance(movieDiary.getMv_id()));                 }
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
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = MainActivity.deviceWidth/3;
            layoutParams.height = (int)((MainActivity.deviceWidth/3)*1.4);
            imageView.setLayoutParams(layoutParams);
        }
    }
}
