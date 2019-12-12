package com.example.user.moviediary.model;

public class MovieDiary {
    private int detailImage;
    private String detailTitle;
    private float detailRatingBar;
    private String detailDate;
    private String detailReview;

    public MovieDiary(int detailImage){
        this.detailImage = detailImage;
    }

    public MovieDiary(int detailImage, String detailTitle, float detailRatingBar, String detailDate, String detailReview) {
        this.detailImage = detailImage;
        this.detailTitle = detailTitle;
        this.detailRatingBar = detailRatingBar;
        this.detailDate = detailDate;
        this.detailReview = detailReview;
    }

    public int getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(int detailImage) {
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
