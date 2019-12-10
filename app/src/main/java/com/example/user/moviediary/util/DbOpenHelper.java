package com.example.user.moviediary.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {

    private static final String DB_NAME = "Diary.db";
    private static final int VERSION = 1;
    public static SQLiteDatabase mDB;
    private UserTBLHelper userHelper;
    private PostingTBLHelper PostingHelper;
    private Context mContext;

    public DbOpenHelper(Context context){
        this.mContext = context;
    }

    public void create(){
        userHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public DbOpenHelper openUser() throws SQLException {
        userHelper = new UserTBLHelper(mContext, DB_NAME, null, VERSION);
        mDB = userHelper.getWritableDatabase();
        return this;
    }

    public DbOpenHelper openPosting() throws SQLException {
        PostingHelper = new PostingTBLHelper(mContext, DB_NAME, null, VERSION);
        mDB = PostingHelper.getWritableDatabase();
        return this;
    }

    //유저 테이블 CRUD 모음

    public long insertUserColumn(String nickname, String photo, String slf_int){
        ContentValues values = new ContentValues();
        values.put(DiaryDB.CreateUser.NICKNAME, nickname);
        values.put(DiaryDB.CreateUser.PHOTO, photo);
        values.put(DiaryDB.CreateUser.SLF_INT, slf_int);
        return mDB.insert(DiaryDB.CreateUser.CREATE_USR, null, values);
    }

    public Cursor selectUserColumns(){
        return mDB.query(DiaryDB.CreateUser.USER_TBL, null, null, null, null, null, null);
    }

    public boolean updateUserColumn(String nickname, String photo, String slf_int){
        ContentValues values = new ContentValues();
        values.put(DiaryDB.CreateUser.NICKNAME, nickname);
        values.put(DiaryDB.CreateUser.PHOTO, photo);
        values.put(DiaryDB.CreateUser.SLF_INT, slf_int);
        return mDB.update(DiaryDB.CreateUser.USER_TBL, values, "NICKNAME=" + nickname, null) > 0;

    }

    public void deleteUserColumns(String nickname) {
        mDB.delete(DiaryDB.CreateUser.USER_TBL, "NICKNAME="+nickname, null);

    }

    ///////////////////////////////////////////////////
    //포스팅 테이블 CRUD 모음

    public long insertPostingColumn(String title, String mv_date, String post_date
            , float star, String content){
        ContentValues values = new ContentValues();
        values.put(DiaryDB.CreatePosting.TITLE, title);
        values.put(DiaryDB.CreatePosting.MV_DATE, mv_date);
        values.put(DiaryDB.CreatePosting.POST_DATE, post_date);
        values.put(DiaryDB.CreatePosting.STAR, star);
        values.put(DiaryDB.CreatePosting.CONTENT, content);
        return mDB.insert(DiaryDB.CreatePosting.CREATE_POSTING, null, values);
    }

    public Cursor selectPostingColumns(){
        return mDB.query(DiaryDB.CreatePosting.POSTING_TBL, null, null, null, null, null, null);
    }

    public boolean updatePostingColumn(int post_no, String title, String mv_date, String post_date
            , float star, String content){
        ContentValues values = new ContentValues();
        values.put(DiaryDB.CreatePosting.TITLE, title);
        values.put(DiaryDB.CreatePosting.MV_DATE, mv_date);
        values.put(DiaryDB.CreatePosting.POST_DATE, post_date);
        values.put(DiaryDB.CreatePosting.STAR, star);
        values.put(DiaryDB.CreatePosting.CONTENT, content);
        return mDB.update(DiaryDB.CreatePosting.POSTING_TBL, values, "POST_NO=" + post_no, null) > 0;

    }

    public void deletePostingColumns(int post_no) {
        mDB.delete(DiaryDB.CreatePosting.POSTING_TBL, "POST_NO=" + post_no, null);

    }

    ///////////////////////////////////////////////////
    //포스팅 테이블 CRUD 모음

    public long insertLikeColumn(String title){
        ContentValues values = new ContentValues();
        values.put(DiaryDB.CreatePosting.TITLE, title);
        return mDB.insert(DiaryDB.CreatePosting.CREATE_POSTING, null, values);
    }

    public Cursor selectLikeColumns(){
        return mDB.query(DiaryDB.CreatePosting.POSTING_TBL, null, null, null, null, null, null);
    }

    public boolean updateLikeColumn(int post_no, String title){
        ContentValues values = new ContentValues();
        values.put(DiaryDB.CreatePosting.TITLE, title);
        return mDB.update(DiaryDB.CreatePosting.POSTING_TBL, values, "POST_NO=" + post_no, null) > 0;

    }

    public void deleteLikeColumns(int post_no) {
        mDB.delete(DiaryDB.CreatePosting.POSTING_TBL, "POST_NO=" + post_no, null);

    }

    ///////////////////////////////////////////////////

    //유저테이블
    private class UserTBLHelper extends SQLiteOpenHelper {

        public UserTBLHelper(Context context, String dbName, Object o, int version) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DiaryDB.CreateUser.CREATE_USR);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DiaryDB.CreateUser.USER_TBL);
            onCreate(db);
        }

    }

    //포스팅테이블
    private class PostingTBLHelper extends SQLiteOpenHelper {

        public PostingTBLHelper(Context context, String dbName, Object o, int version) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DiaryDB.CreatePosting.CREATE_POSTING);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DiaryDB.CreatePosting.POSTING_TBL);
            onCreate(db);
        }

    }

    //라이크 테이블
    private class LikeTBLHelper extends SQLiteOpenHelper {

        public LikeTBLHelper(Context context, String dbName, Object o, int version) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DiaryDB.CreateLike.CREATE_LIKE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DiaryDB.CreateLike.LIKE_TBL);
            onCreate(db);
        }

    }

}
