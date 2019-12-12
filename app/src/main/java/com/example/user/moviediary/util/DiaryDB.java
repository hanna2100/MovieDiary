package com.example.user.moviediary.util;

import android.provider.BaseColumns;

public final class DiaryDB {

    public static final class CreateUser implements BaseColumns{

        public static final String NICKNAME = "nickname";
        public static final String PHOTO = "photo";
        public static final String SLF_INT = "slf_int";
        public static final String USER_TBL = "user_tbl";
        public static final String CREATE_USR = "create table if not exists "
                +USER_TBL+"("
                +NICKNAME+" text primary key, "
                +PHOTO+" text not null , "
                +SLF_INT+" text );";
    }

    public static final class CreatePosting implements BaseColumns{

        public static final String POSTING_TBL = "posting_tbl";
        public static final String MV_ID = "mv_id";
        public static final String TITLE = "title";
        public static final String MV_POSTER = "mv_poster";
        public static final String MV_DATE = "mv_date"; //영화본날짜
        public static final String POST_DATE = "post_date"; //포스팅한날짜
        public static final String STAR = "star";
        public static final String CONTENT = "content";
        public static final String CREATE_POSTING = "create table if not exists "
                +POSTING_TBL+" ("
                +MV_ID+" integer primary key, "
                +MV_POSTER+" text not null, "
                +TITLE+" text not null , "
                +MV_DATE+" text not null , "
                +POST_DATE+" text not null , "
                +STAR+" real not null , "
                +CONTENT+" text not null);";
    }

    public static final class CreateLike implements BaseColumns{

        public static final String LIKE_TBL = "like_tbl";
        public static final String TITLE = "title";
        public static final String MV_ID = "mv_id";
        public static final String MV_POSTER = "mv_poster";
        public static final String CREATE_LIKE = "create table if not exists "
                +LIKE_TBL+"("
                +MV_ID+" integer primary key, "
                +TITLE+" text not null, "
                +MV_POSTER+" text not null);";
    }
}
