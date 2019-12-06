package com.example.user.moviediary.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieRcmAdapter;
import com.example.user.moviediary.etc.MovieDetails;
import com.example.user.moviediary.etc.MovieRecommendations;
import com.example.user.moviediary.etc.MovieVideo;
import com.example.user.moviediary.util.GlideApp;
import com.example.user.moviediary.util.MoviesRepository;
import com.example.user.moviediary.util.OnGetDetailsCallback;
import com.example.user.moviediary.util.OnGetRecommendationsCallback;
import com.example.user.moviediary.util.OnGetVideoCallback;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;


public class FrgMovieDetails extends Fragment implements DiscreteScrollView.OnItemChangedListener {

    private final String TAG = "MovieDetails";
    private static final String MOVIE_ID = "MOVIE_ID";

    private ImageView ivBackdrop;
    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvOverview;
    private TextView tvRuntime;
    private TextView tvRelease;
    private TextView tvVoteAvg;
    private RatingBar ratingBar;
    private TextView tvOverviewRcm;

    private View view;
    private Context mContext;
    private MoviesRepository moviesRepository;
    private LinearLayout layoutView;
    private FrameLayout flYoutube;
    private TextView tvNotFoundVideo;
    private int movie_id;
    private List<MovieRecommendations.ResultsBean> list;
    private DiscreteScrollView discreteRcm;
    private InfiniteScrollAdapter infiniteAdapter;
    private boolean dataComplete;

    public static FrgMovieDetails newInstance(int movie_id) {
        FrgMovieDetails fragment = new FrgMovieDetails();

        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movie_id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        layoutView = view.findViewById(R.id.layoutView);

        ivBackdrop = view.findViewById(R.id.ivBackdrop);
        ivPoster = view.findViewById(R.id.ivPoster);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvOverview = view.findViewById(R.id.tvOverview);
        tvRuntime = view.findViewById(R.id.tvRuntime);
        tvRelease = view.findViewById(R.id.tvRelease);
        tvVoteAvg = view.findViewById(R.id.tvVoteAvg);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvOverviewRcm = view.findViewById(R.id.tvOverviewRcm);
        Button btnPosting = view.findViewById(R.id.btnPosting);

        moviesRepository = MoviesRepository.getInstance();

        new FrgMovieDetails.MyTask().execute();
        return view;
    }

    private void onMovieRcmChanged(MovieRecommendations.ResultsBean result) {
        tvOverviewRcm.setText(result.getOverview());
        //줄거리 데이터가 없을때
        if (tvOverviewRcm.getText().toString().equals("")) {
            tvOverviewRcm.setText("줄거리가 없습니다.");
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onMovieRcmChanged(list.get(positionInDataSet));
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //진행다이어로그 시작
            layoutView.setVisibility(View.INVISIBLE);
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다");
            progressDialog.show();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(Void... params) {

            tvNotFoundVideo = view.findViewById(R.id.tvNotFoundVideo);
            flYoutube = view.findViewById(R.id.flYoutube);
            ImageButton ibLike = view.findViewById(R.id.ibLike);

            //포스터 이미지뷰 모서리 둥글게
            GradientDrawable drawable =
                    (GradientDrawable) mContext.getDrawable(R.drawable.background_round);
            ivPoster.setBackground(drawable);
            ivPoster.setClipToOutline(true);

            movie_id = getArguments().getInt(MOVIE_ID);

            //영화상세정보 가져와서 뷰에 세팅
            getMovieDetailsFromTMDB();

            //영화정보를 모두 가져올때까지 기다림
            while (true) {
                try {
                    Thread.sleep(100);
                    if (dataComplete == true)
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        private void getMovieDetailsFromTMDB() {
            moviesRepository.getMovieDetailsResult(movie_id, new OnGetDetailsCallback() {
                @Override
                public void onSuccess(MovieDetails movieDetails) {

                    Log.d(TAG, "영상여부" + movieDetails.isVideo());

                    tvTitle.setText(movieDetails.getTitle());
                    tvRuntime.setText(movieDetails.getRuntime() + "min");
                    tvRelease.setText(movieDetails.getRelease_date());
                    tvVoteAvg.setText(String.valueOf(movieDetails.getVote_average()));
                    ratingBar.setRating((float) movieDetails.getVote_average() / 2);
                    tvOverview.setText(movieDetails.getOverview());

                    String posterPath = "https://image.tmdb.org/t/p/w500" + movieDetails.getPoster_path();
                    GlideApp.with(view).load(posterPath)
                            .override(185, 260)
                            .into(ivPoster);
                    String backdropPath = "https://image.tmdb.org/t/p/w1280" + movieDetails.getBackdrop_path();
                    GlideApp.with(view).load(backdropPath)
                            .centerCrop()
                            .into(ivBackdrop);

                    //비디오 가져오기
                    getMovieUrlFromTMDB();

                    //추천영화 가져오기
                    getMovieRecommendationsFromTMDB();

                }

                @Override
                public void onError() {

                }
            });


        }

        private void getMovieUrlFromTMDB() {
            moviesRepository.getMovieVideoResult(movie_id, new OnGetVideoCallback() {
                @Override
                public void onSuccess(final MovieVideo movieVideo) {

                    String site = movieVideo.getResults().get(0).getSite();
                    if (site.equals("YouTube")) {
                        final String videoUrl = movieVideo.getResults().get(0).getKey();

                        FrgYoutubePlayer youtubePlayer = FrgYoutubePlayer.newInstance(videoUrl);
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.flYoutube, youtubePlayer).commit();
                        tvNotFoundVideo.setVisibility(View.GONE);

                    } else {
                        onError();
                    }
                }

                @Override
                public void onError() {
                    flYoutube.setVisibility(View.GONE);
                    tvNotFoundVideo.setVisibility(View.VISIBLE);
                }
            });
        }

        private void getMovieRecommendationsFromTMDB() {
            MoviesRepository.setPage(1);
            moviesRepository.getMovieRecommendations(movie_id, new OnGetRecommendationsCallback() {
                @Override
                public void onSuccess(List<MovieRecommendations.ResultsBean> resultsBeanList) {
                    list = resultsBeanList;

                    discreteRcm = view.findViewById(R.id.discreteRcm);
                    discreteRcm.setAdapter(new MovieRcmAdapter(R.layout.item_movie_recommendations, list));

                    discreteRcm.setOrientation(DSVOrientation.HORIZONTAL);
                    discreteRcm.addOnItemChangedListener(FrgMovieDetails.this);
                    infiniteAdapter = InfiniteScrollAdapter.wrap(new MovieRcmAdapter(R.layout.item_movie_recommendations, list));
                    discreteRcm.setAdapter(infiniteAdapter);
                    discreteRcm.setItemTransformer(new ScaleTransformer.Builder()
                            .setMinScale(0.8f)
                            .build());

                    onMovieRcmChanged(resultsBeanList.get(0));
                    dataComplete = true;

                }

                @Override
                public void onError() {
                    dataComplete = true;

                }

            });
        }


        @Override
        protected void onPostExecute(Void result) {

            layoutView.setVisibility(View.VISIBLE);
            progressDialog.dismiss();

            dataComplete = false;
            super.onPostExecute(result);
        }


    }


}
