package com.example.user.moviediary.util;

import android.util.Log;

import com.example.user.moviediary.etc.MovieDetails;
import com.example.user.moviediary.etc.MovieRecommendations;
import com.example.user.moviediary.etc.MovieVideo;
import com.example.user.moviediary.etc.SearchResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    private final String TAG = "MovieDetails";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "ko-KR";
    private static String query = "";
    private static int page = 1;
    private static boolean include_adult = false;

    private static MoviesRepository repository;

    private TMDBApi api;

    private MoviesRepository(TMDBApi api) {
        this.api = api;
    }

    //싱글톤으로 작성. 검색결과를 가져오는 도구같은거니 도구는 하나만있으면 됨. 재사용하면 되니까.
    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())//파싱등록
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDBApi.class));
        }

        return repository;
    }

    /* 영화 검색 메소드 시작 */
    public void getSearchedMovieResult(final OnGetMoviesCallback callback) {

        Call<SearchResults> call = api.searchMovies(DeveloperKey.TMDB, LANGUAGE, query, page, include_adult);
        call.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                SearchResults results = response.body();

                if (results != null && results.getResults() != null)
                    callback.onSuccess(results.getResults());
                else
                    callback.onError();
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                callback.onError();
            }
        });
    }

    public static void setQuery(String QUERY) {
        MoviesRepository.query = QUERY;
    }

    public static void switchToTheNextPage() {
        MoviesRepository.page = ++page;
    }

    public static void setPage(int PAGE) {
        MoviesRepository.page = PAGE;
    }

    public static void setIncludeAdult(boolean includeAdult) {
        include_adult = includeAdult;
    }

    public static int getPage() {
        return page;
    }

    /* ~ 여기까지가 영화 검색관련 메소드

      영화 디테일정보얻기 메소드 시작 */
    public void getMovieDetailsResult(int movie_id, final OnGetDetailsCallback callback) {
        Call<MovieDetails> call = api.getMovieDetails(movie_id, DeveloperKey.TMDB, LANGUAGE);
        call.enqueue(new Callback<MovieDetails>() {

            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails results = response.body();
                if (results != null)
                    callback.onSuccess(results);
                else
                    callback.onError();
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                callback.onError();
            }
        });

    }

    /* ~ 여기까지가 영화 디테일정보얻기 메소드

      영화 유튜브영상 받기 메소드 시작 */
    public void getMovieVideoResult(int movie_id, final OnGetVideoCallback callback) {
        Call<MovieVideo> call = api.getMovieVideo(movie_id, DeveloperKey.TMDB, LANGUAGE);
        call.enqueue(new Callback<MovieVideo>() {

            @Override
            public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {
                MovieVideo results = response.body();
                if (results.getResults().size() != 0)
                    callback.onSuccess(results);
                else
                    callback.onError();
            }

            @Override
            public void onFailure(Call<MovieVideo> call, Throwable t) {
                callback.onError();
            }
        });

    }

    /* ~ 여기까지가 영화 유튜브영상 받기 메소드

      추천영화 받기 메소드 시작 */
    public void getMovieRecommendations(int movie_id, final OnGetRecommendationsCallback callback) {
        Call<MovieRecommendations> call = api.getMovieRecommendations(movie_id, DeveloperKey.TMDB, LANGUAGE, page);
        Log.d(TAG, "아이디"+movie_id);
        call.enqueue(new Callback<MovieRecommendations>() {

            @Override
            public void onResponse(Call<MovieRecommendations> call, Response<MovieRecommendations> response) {
                Log.d(TAG, "데이터 받기 성공");

                MovieRecommendations results = response.body();

                if (results != null&& results.getResults() != null) {
                    Log.d(TAG, "결과 널 아님");
                    callback.onSuccess(results.getResults());
                } else {
                    Log.d(TAG, "데이터받기 널");
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MovieRecommendations> call, Throwable t) {
                Log.d(TAG, "데이터받기에러");
                callback.onError();
            }
        });

    }
}