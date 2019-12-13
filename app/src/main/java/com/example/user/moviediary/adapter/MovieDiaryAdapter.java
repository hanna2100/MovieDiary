package com.example.user.moviediary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.fragment.FrgMovieDiaryDetails;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.util.DbOpenHelper;
import com.example.user.moviediary.util.GlideApp;

import java.util.ArrayList;

public class MovieDiaryAdapter extends RecyclerView.Adapter<MovieDiaryAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<MovieDiary> list;
    private View view;
    private Context context;
    private DbOpenHelper dbOpenHelper;

    public MovieDiaryAdapter(int layout, ArrayList<MovieDiary> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        MovieDiaryAdapter.CustomViewHolder customViewHolder = new MovieDiaryAdapter.CustomViewHolder(view);
        context = viewGroup.getContext();

        dbOpenHelper = new DbOpenHelper(context);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int i) {

        MovieDiary movieDiary = list.get(i);

        GlideApp.with(context).load(movieDiary.getDetailImage()).into(customViewHolder.imageView);

        customViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이어리 상세정보 프래그먼트 띄움
                ((MainActivity) context)
                        .setChangeFragment(FrgMovieDiaryDetails.newInstance(list.get(i).getMv_id(),list.get(i).getDetailImage(),
                                list.get(i).getDetailTitle(),list.get(i).getDetailRatingBar(),
                                list.get(i).getDetailDate(),list.get(i).getDetailReview()));
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

            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = MainActivity.deviceWidth/3;
            layoutParams.height = (int)((MainActivity.deviceWidth/3)*1.4);
            imageView.setLayoutParams(layoutParams);

        }
    }
}
