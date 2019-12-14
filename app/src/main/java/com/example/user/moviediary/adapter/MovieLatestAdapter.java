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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.fragment.FrgMovieDetails;
import com.example.user.moviediary.fragment.FrgPosting;
import com.example.user.moviediary.model.MovieLatest;
import com.example.user.moviediary.util.DbOpenHelper;
import com.example.user.moviediary.util.GlideApp;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MovieLatestAdapter extends RecyclerView.Adapter<MovieLatestAdapter.LatestViewHolder>{

    private List<MovieLatest.ResultsBean> list;
    private int layout;
    private View view;
    private Context context;
    private DbOpenHelper dbOpenHelper;

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
        dbOpenHelper = new DbOpenHelper(context);

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
        customColor.setSpan(new ForegroundColorSpan(Color.parseColor("#0c085c")), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        customColor.setSpan(new StyleSpan(Typeface.BOLD), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvContent.setText(customColor);


        //본문제목설정
        viewHolder.tvLatestTitle.setText(title);

        //동그란 포스터이미지 설정
        if (movie.getPoster_path()!=null) {
            String url = "https://image.tmdb.org/t/p/w92" + movie.getPoster_path();

            GlideApp.with(viewHolder.itemView).load(url)
                    .centerCrop()
                    .into(viewHolder.ivLatestPoster);
        }
        //동그란 포스터이미지 누르면 상세보기로 이동
        viewHolder.ivLatestPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setChangeFragment(FrgMovieDetails.newInstance(movie.getId()));

            }
        });

        //상세보기버튼 클릭시 상세보기로 이동
        viewHolder.btnLatestMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setChangeFragment(FrgMovieDetails.newInstance(movie.getId()));

            }
        });

        //본문내용에 들어갈 영화 섬네일 설정
        if (movie.getPoster_path()!=null) {
            String url = "https://image.tmdb.org/t/p/w1280" + movie.getBackdrop_path();

            GlideApp.with(context).load(url)
                    .centerCrop()
                    .into(viewHolder.ivBackdrop);
        }

        //해당영화가 찜하기 목록에 있는지 여부 검사해서 보여질 하트모양 설정

        viewHolder.likeButtonVisibleSetting(movie.getId());

        //빈 하트 누르면->찜목록추가
        viewHolder.ibUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOpenHelper.openLike();
                dbOpenHelper.createLikeHelper();

                //컬럼추가
                dbOpenHelper.insertLikeColumn(movie.getId(), movie.getTitle(), movie.getPoster_path());
                dbOpenHelper.close();

                viewHolder.ibUnlike.setVisibility(View.GONE);
                viewHolder.ibLike.setVisibility(View.VISIBLE);
            }
        });

        //찐 하트 누르면->찜목록삭제
        viewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOpenHelper.openLike();
                dbOpenHelper.createLikeHelper();

                //컬럼삭제
                dbOpenHelper.deleteLikeColumns(movie.getId());
                dbOpenHelper.close();

                viewHolder.ibLike.setVisibility(View.GONE);
                viewHolder.ibUnlike.setVisibility(View.VISIBLE);
            }
        });

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
        ImageButton ibMore, ibLess, ibUnlike, ibLike;
        Button btnLatestMore;

        public LatestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvContent = itemView.findViewById(R.id.tvContent);
            this.tvLatestTitle = itemView.findViewById(R.id.tvLatestTitle);
            this.ivLatestPoster = itemView.findViewById(R.id.ivLatestPoster);
            this.ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            this.ibMore = itemView.findViewById(R.id.ibMore);
            this.ibLess = itemView.findViewById(R.id.ibLess);
            this.btnLatestMore = itemView.findViewById(R.id.btnLatestMore);
            this.ibUnlike = itemView.findViewById(R.id.ibUnlike);
            this.ibLike = itemView.findViewById(R.id.ibLike);

            ViewGroup.LayoutParams layoutParams = ivBackdrop.getLayoutParams();
            layoutParams.width = MainActivity.deviceWidth;
            layoutParams.height = MainActivity.deviceWidth;
            ivBackdrop.setLayoutParams(layoutParams);

        }

        private void likeButtonVisibleSetting(int movie_id) {
            dbOpenHelper.openLike();
            dbOpenHelper.createLikeHelper();
            //찜하기 목록에 있는지 여부 검사
            boolean isExistLike = dbOpenHelper.isExistLikeColumn(movie_id);
            dbOpenHelper.close();

            if(isExistLike) {
                ibLike.setVisibility(View.VISIBLE);
                ibUnlike.setVisibility(View.GONE);
            }else{
                ibLike.setVisibility(View.GONE);
                ibUnlike.setVisibility(View.VISIBLE);
            }
        }
    }



}
