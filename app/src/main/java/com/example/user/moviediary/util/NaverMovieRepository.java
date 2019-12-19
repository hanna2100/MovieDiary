package com.example.user.moviediary.util;

import android.content.Context;
import android.util.Log;

import com.example.user.moviediary.model.NaverMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaverMovieRepository {

    private static final String BASE_URL = "https://openapi.naver.com/v1/";
    private static String query = "";
    private static String yearfrom = "";
    private static String yearto = "";

    private static NaverMovieRepository repository;
    private NaverMovieApi api;

    private NaverMovieRepository(NaverMovieApi api) {
        this.api = api;
    }

    public static NaverMovieRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())//파싱등록
                    .build();

            repository = new NaverMovieRepository(retrofit.create(NaverMovieApi.class));
        }

        return repository;
    }

    // 영화 검색 메소드 시작
    public void getMovieResult(Context context, final String query, String yearfrom, String yearto,
                               final OnGetNaverMovieCallback callback) {

        Call<NaverMovie> call = api.searchNaverMovie(query, 10, yearfrom, yearto);
        call.enqueue(new Callback<NaverMovie>() {

            @Override
            public void onResponse(Call<NaverMovie> call, Response<NaverMovie> response) {
                NaverMovie results = response.body();

                //callback 할 영화아이템 객체
                NaverMovie.ItemsBean item = null;

                //검색결과가 2개 이상인경우 > 찾고자 하는 정확한 영화를 다시 찾음
                if (results.getItems().size() > 1) {
                    item = findTheExactRequestMovie(results, query);
                    if(item==null){
                        callback.onError();
                    }
                    callback.onSuccess(item);
                }//검색결과가 딱 1개인 경우 > 해당영화를 콜백
                else if (results.getItems().size()==1) {
                    item = results.getItems().get(0);
                    callback.onSuccess(item);
                } else {//검색결과가 없는경우
                    Log.d("네이버", "onResponse else="+ results.toString());
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<NaverMovie> call, Throwable t) {
                Log.d("네이버", "onFailure, t= " + t.toString() + "call =" + call.toString());
                callback.onError();
            }
        });
    }

    private NaverMovie.ItemsBean findTheExactRequestMovie(NaverMovie results, String query) {

        //리턴할 영화객체
        NaverMovie.ItemsBean item = null;
        //검색쿼리의 특수문자 제거
        query = spCharRid(query);

        List<NaverMovie.ItemsBean> list = results.getItems();

        for (NaverMovie.ItemsBean movie : list) {
            String tempTitle = movie.getTitle();
            tempTitle = tempTitle.replaceAll("<b>", "");
            tempTitle = tempTitle.replaceAll("</b>", "");

            String title = tempTitle;//검색된 결과들의 영화제목

            //검색된 영화제목중에 특수문자 제거
            title = spCharRid(title);

            if (title.equals(query)) {
                return movie;
            }

        }
        return item;
    }

    public String spCharRid(String strInput) {
        String strWork = strInput;

        String spChars[] = new String[]{ "`", "-", "=", ";", "'", "/", "~", "!", "@",
                "#", "$", "%", "^", "&", "|", ":", "<", ">",
                ".","\\*", "\\+","\\{","}","\\?","★","☆","♥","♡"};


        int spCharLen = spChars.length;

        for (int i = 0; i < spCharLen; i++) {
            strWork = strWork.replaceAll(spChars[i], "");

        }

        return strWork;
    }
}
