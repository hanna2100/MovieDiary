package com.example.user.moviediary.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;

import java.util.ArrayList;

public class FrgMovieDiaryDetails extends Fragment implements View.OnClickListener {
    private ImageView detailImage;
    private TextView detailTitle;
    private RatingBar detailRatingBar;
    private TextView detailDate;
    private TextView detailReview;
    private Button btnEdit;
    private Button btnDelete;


    private Context mContext;
    private View view;

    private ArrayList<MovieDiary> list;

    public static FrgMovieDiaryDetails newInstance() {
        FrgMovieDiaryDetails fragment = new FrgMovieDiaryDetails();
        Bundle args = new Bundle();
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
        View view = View.inflate(mContext, R.layout.user_detail, null);
        detailImage = view.findViewById(R.id.detailImage);
        detailTitle = view.findViewById(R.id.detailTitle);
        detailRatingBar = view.findViewById(R.id.detailRatingBar);
        detailDate = view.findViewById(R.id.detailDate);
        detailReview = view.findViewById(R.id.detailReview);
//        btnEdit = view.findViewById(R.id.btnEdit);
//        btnDelete = view.findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnEdit : break;
//            case R.id.btnDelete : break;
        }

    }
    // 목록에서 한 게시물을 클릭했을때 #이 붙은 해시태그가 있으면 색깔 변하게 해주고, 그 해시태그 클릭이벤트 해줘!
    private void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '#') {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            // 해시태그한거 클릭 이벤트 여기다가~~!
                            // 태그 기반으로 검색?할 수 있게??? 만들어야하나?
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // 해시태그 된거 컬러!!
                            ds.setColor(Color.parseColor("#33b5e5"));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }
}