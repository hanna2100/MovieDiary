package com.example.user.moviediary.adapter;

import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.user.moviediary.R;
import com.example.user.moviediary.etc.MovieLatest;
import com.example.user.moviediary.util.GlideApp;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MovieLatestAdapter extends RecyclerView.Adapter<MovieLatestAdapter.LatestViewHolder>{

    private List<MovieLatest.ResultsBean> list;
    private int layout;
    private View view;
    private Context context;

    public MovieLatestAdapter(int layout, List<MovieLatest.ResultsBean> list) {
        this.list = list;
        this.layout = layout;
    }

    @NonNull
    @Override
    public LatestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        LatestViewHolder searchViewHolder = new LatestViewHolder(view);
        context = viewGroup.getContext();
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LatestViewHolder viewHolder, int i) {

        final MovieLatest.ResultsBean movie = list.get(i);

        //본문내용에 아이디+줄거리를 넣는데 아이디에만 색깔넣고 굵게하기
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String titleOverview = title+ "  " + overview;
        titleOverview = titleOverview.replace(" ", "\u00A0");
        int titleLength = title.length();
        SpannableStringBuilder customColor = new SpannableStringBuilder(titleOverview);
        customColor.setSpan(new ForegroundColorSpan(Color.parseColor("#22243d")), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        customColor.setSpan(new StyleSpan(Typeface.BOLD), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvContent.setText(customColor);


        //본문제목설정
        viewHolder.tvLatestTitle.setText(title);

        //포스터설정
        if (movie.getPoster_path()!=null) {
            String url = "https://image.tmdb.org/t/p/w92" + movie.getPoster_path();

            GlideApp.with(viewHolder.itemView).load(url)
                    .centerCrop()
                    .into(viewHolder.ivLatestPoster);
        }

        //본문내용에 들어갈 영화 섬네일 설정
        if (movie.getPoster_path()!=null) {
            String url = "https://image.tmdb.org/t/p/w1280" + movie.getBackdrop_path();

            Glide.with(context).load(url)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3))
                            .override(Target.SIZE_ORIGINAL, 1000).centerCrop())
                    .into(viewHolder.ivBackdrop);
        }

        //줄거리 더보기 설정 - 5줄 넘을때만 더보기 버튼 활성화
        viewHolder.tvContent.setMaxLines(Integer.MAX_VALUE);
        viewHolder.tvContent.post(new Runnable() {
            @Override
            public void run() {
                int lineCnt = viewHolder.tvContent.getLineCount();
                Log.d("cntTest", "movie = "+movie.getTitle()+", 카운트 = "+lineCnt);
                if(lineCnt<6){
                    viewHolder.ibMore.setVisibility(View.GONE);
                }else{
                    viewHolder.tvContent.setMaxLines(5);
                    viewHolder.ibMore.setVisibility(View.VISIBLE);
                }
            }
        });

        viewHolder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tvContent.setMaxLines(Integer.MAX_VALUE);
                viewHolder.ibLess.setVisibility(View.VISIBLE);
                viewHolder.ibMore.setVisibility(View.INVISIBLE);
            }
        });

        viewHolder.ibLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tvContent.setMaxLines(5);
                viewHolder.ibMore.setVisibility(View.VISIBLE);
                viewHolder.ibLess.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }



    public class LatestViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvLatestTitle;
        ImageView ivLatestPoster, ivBackdrop;
        ImageButton ibMore, ibLess;

        public LatestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvContent = itemView.findViewById(R.id.tvContent);
            this.tvLatestTitle = itemView.findViewById(R.id.tvLatestTitle);
            this.ivLatestPoster = itemView.findViewById(R.id.ivLatestPoster);
            this.ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            this.ibMore = itemView.findViewById(R.id.ibMore);
            this.ibLess = itemView.findViewById(R.id.ibLess);

        }
    }



}
