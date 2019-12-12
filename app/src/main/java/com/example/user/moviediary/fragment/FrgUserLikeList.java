package com.example.user.moviediary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieDiaryAdapter2;
import com.example.user.moviediary.model.MovieDiary;

import java.util.ArrayList;

public class FrgUserLikeList extends Fragment {

    private View view;
    private Context mContext;
    private MovieDiaryAdapter2 adapter;
    private ArrayList<MovieDiary> list = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_likelist, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcvLike);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(gridLayoutManager);

        list.add(new MovieDiary(R.drawable.movie_no_poster));
        list.add(new MovieDiary(R.drawable.movie_no_poster));
        list.add(new MovieDiary(R.drawable.movie_no_poster));
        list.add(new MovieDiary(R.drawable.movie_no_poster));
        list.add(new MovieDiary(R.drawable.movie_no_poster));
        list.add(new MovieDiary(R.drawable.movie_no_poster));
        list.add(new MovieDiary(R.drawable.movie_no_poster));

        adapter = new MovieDiaryAdapter2(R.layout.item_user, list);
        recyclerView.setAdapter(adapter);
        Log.d("ViewPager", "likelist size = "+list.size());
        return view;

    }
}
