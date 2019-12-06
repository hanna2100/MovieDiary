package com.example.user.moviediary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.moviediary.R;
import com.example.user.moviediary.etc.MovieChart;
import com.example.user.moviediary.util.GlideApp;

import java.util.List;

public class MovieChartAdapter extends RecyclerView.Adapter<MovieChartAdapter.CustomViewHolder> {

    private final String TAG = "Parsing";
    private int layout;
    private List<MovieChart> list;
    private Context context;
    private View view;

    public MovieChartAdapter(int layout, List<MovieChart> list) {
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @NonNull
    @Override
    public MovieChartAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        context = viewGroup.getContext();
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {

        MovieChart movieChart = list.get(i);
        Log.d(TAG, movieChart.toString());
        GlideApp.with(customViewHolder.itemView).load(movieChart.getPoster())
                .override(185,260)
                .into(customViewHolder.poster);

        customViewHolder.rank.setText(movieChart.getRank() + "");
        customViewHolder.title.setText(movieChart.getTitle());
        customViewHolder.age.setText(movieChart.getAge());
        customViewHolder.reservationRate.setText(movieChart.getRsrvRate());
        customViewHolder.great.setText(movieChart.getGreat());
        customViewHolder.releaseDate.setText(movieChart.getRlsDate());

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView rank;
        TextView title;
        TextView reservationRate;
        TextView great;
        TextView releaseDate;
        TextView age;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            rank = itemView.findViewById(R.id.rank);
            age = itemView.findViewById(R.id.age);
            title = itemView.findViewById(R.id.title);
            reservationRate = itemView.findViewById(R.id.reservationRate);
            great = itemView.findViewById(R.id.great);
            releaseDate = itemView.findViewById(R.id.releaseDate);
        }
    }



}
