package com.example.user.moviediary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.fragment.FrgMovieDiaryDetails;

import java.util.ArrayList;

public class MovieDiaryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<MovieDiary> list;
    private LayoutInflater layoutInflater;

    public MovieDiaryAdapter(Context context, int layout, ArrayList<MovieDiary> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, null);
        }
        ImageView movieImage = convertView.findViewById(R.id.movieImage);
        final MovieDiary movieDiary = list.get(i);
        movieImage.setImageResource(movieDiary.getDetailImage());
        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이어리 상세정보 프래그먼트 띄움
                ((MainActivity) context).setChangeFragment(FrgMovieDiaryDetails.newInstance());
            }
        });
        return convertView;
    }


}
