package com.example.user.moviediary.util;

import com.example.user.moviediary.etc.MovieDetails;
import com.example.user.moviediary.etc.MovieRecommendations;
import com.example.user.moviediary.etc.MovieVideo;
import com.example.user.moviediary.etc.SearchResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {

    @GET("search/movie")
    Call<SearchResults> searchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") boolean include_adult
    );

    @GET("movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<MovieVideo> getMovieVideo(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/similar")
    Call<MovieRecommendations> getMovieRecommendations(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

}
