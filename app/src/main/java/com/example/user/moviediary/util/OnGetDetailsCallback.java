package com.example.user.moviediary.util;

import com.example.user.moviediary.etc.MovieDetails;

public interface OnGetDetailsCallback {

    void onSuccess(MovieDetails movieDetails);

    void onError();
}
