package com.example.user.moviediary.util;

import com.example.user.moviediary.etc.MovieLatest;
import com.example.user.moviediary.etc.MoviePopular;

import java.util.List;

public interface OnGetLatestMoviesCallback {

    void onSuccess(List<MovieLatest.ResultsBean> resultsBeanList);

    void onError();

}
