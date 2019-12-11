package com.example.user.moviediary.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieDiaryDetailAdapter extends RecyclerView.Adapter<MovieDiaryDetailAdapter.DetailViewHolder> {
    private ArrayList<MovieDiary> list;
    private int layout;
    private View view;
    private Context context;

    public MovieDiaryDetailAdapter(int layout, ArrayList<MovieDiary> list) {
        this.list = list;
        this.layout = layout;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        DetailViewHolder detailViewHolder = new DetailViewHolder(view);
        context = viewGroup.getContext();
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder detailViewHolder, int i) {
        // 유저 정보에서 따와야함!
        // detailViewHolder.detailProfileImage.setImageResource();
        // detailViewHolder.detailNickname.setText();
        detailViewHolder.detailPosterImage.setImageResource(list.get(i).getDetailImage());
        detailViewHolder.detailRatingBar.setRating(list.get(i).getDetailRatingBar());
        detailViewHolder.detailDate.setText(list.get(i).getDetailDate());

        int titleLength = list.get(i).getDetailTitle().length();

        String detailTitle = list.get(i).getDetailTitle();
        String detailContent = list.get(i).getDetailReview();
        String str = detailTitle+"  "+detailContent;

        str = str.replace(" ", "\u00A0");

        SpannableStringBuilder customColor = new SpannableStringBuilder(str);
        customColor.setSpan(new ForegroundColorSpan(Color.parseColor("#22243d")), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        customColor.setSpan(new StyleSpan(Typeface.BOLD), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        detailViewHolder.detailContent.setText(customColor);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView detailProfileImage;
        public TextView detailNickname;
        public ImageButton ibOption;
        public ImageView detailPosterImage;
        public RatingBar detailRatingBar;
        public TextView detailDate;
        public TextView  detailContent;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            detailProfileImage = view.findViewById(R.id.detailProfileImage);
            detailNickname = view.findViewById(R.id.detailNickname);
            ibOption = view.findViewById(R.id.ibOption);
            detailPosterImage = view.findViewById(R.id.detailPosterImage);
            detailRatingBar = view.findViewById(R.id.detailRatingBar);
            detailDate = view.findViewById(R.id.detailDate);
            detailContent = view.findViewById(R.id.detailContent);
        }
    }
}
