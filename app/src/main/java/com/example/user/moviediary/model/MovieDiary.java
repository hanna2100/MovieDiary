package com.example.user.moviediary.model;

import java.io.Serializable;

public class MovieDiary {
    private int mv_id;
    private String detailImage;
    private String detailTitle;
    private float detailRatingBar;
    private String detailDate;
    private String detailReview;

    public MovieDiary(int mv_id, String detailImage, String detailTitle) {
        this.mv_id = mv_id;
        this.detailImage = detailImage;
        this.detailTitle = detailTitle;
    }

    public MovieDiary(String detailImage, String detailTitle, float detailRatingBar, String detailDate, String detailReview) {
        this.detailImage = detailImage;
        this.detailTitle = detailTitle;
        this.detailRatingBar = detailRatingBar;
        this.detailDate = detailDate;
        this.detailReview = detailReview;
    }

    public MovieDiary(int mv_id, String detailImage, String detailTitle, float detailRatingBar, String detailDate, String detailReview) {
        this.mv_id = mv_id;
        this.detailImage = detailImage;
        this.detailTitle = detailTitle;
        this.detailRatingBar = detailRatingBar;
        this.detailDate = detailDate;
        this.detailReview = detailReview;
    }

    public int getMv_id() {
        return mv_id;
    }

    public void setMv_id(int mv_id) {
        this.mv_id = mv_id;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public float getDetailRatingBar() {
        return detailRatingBar;
    }

    public void setDetailRatingBar(float detailRatingBar) {
        this.detailRatingBar = detailRatingBar;
    }

    public String getDetailDate() {
        return detailDate;
    }

    public void setDetailDate(String detailDate) {
        this.detailDate = detailDate;
    }

    public String getDetailReview() {
        return detailReview;
    }

    public void setDetailReview(String detailReview) {
        this.detailReview = detailReview;
    }
}
