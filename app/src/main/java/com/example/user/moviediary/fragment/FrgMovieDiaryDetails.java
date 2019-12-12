package com.example.user.moviediary.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.util.DbOpenHelper;
import com.example.user.moviediary.util.GlideApp;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgMovieDiaryDetails extends Fragment {
    private CircleImageView detailProfileImage;
    private TextView detailNickname;
    private ImageButton ibOption;
    private ImageView detailPosterImage;
    private RatingBar detailRatingBar;
    private TextView detailDate;
    private TextView detailContent;

    private Context mContext;
    private View view;

    private ArrayList<MovieDiary> list;

    private DbOpenHelper dbOpenHelper;

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
        detailProfileImage = view.findViewById(R.id.detailProfileImage);
        detailNickname = view.findViewById(R.id.detailNickname);
        ibOption = view.findViewById(R.id.ibOption);
        detailPosterImage = view.findViewById(R.id.detailPosterImage);
        detailRatingBar = view.findViewById(R.id.detailRatingBar);
        detailDate = view.findViewById(R.id.detailDate);
        detailContent = view.findViewById(R.id.detailContent);

        dbOpenHelper = new DbOpenHelper(mContext);

        insertList();

        for (int i=0;i<list.size();i++) {

            GlideApp.with(mContext).load(list.get(i).getDetailImage()).into(detailPosterImage);
            detailRatingBar.setRating(list.get(i).getDetailRatingBar());
            detailDate.setText(list.get(i).getDetailDate());
            detailContent.setText(list.get(i).getDetailTitle() + "  " + list.get(i).getDetailReview());
        }

        setTags(detailContent, detailContent.getText().toString());

        ibOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시물 수정 삭제 메뉴 만들어줘야함
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                bottomSheetDialog.setContentView(R.layout.user_diary_dialog);
                Button btnDiaryEdit = bottomSheetDialog.findViewById(R.id.btnDiaryEdit);
                Button btnDiaryDelete = bottomSheetDialog.findViewById(R.id.btnDiaryDelete);

                btnDiaryEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 수정버튼 이벤트
                    }
                });
                btnDiaryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 수정버튼 이벤트
                    }
                });
                bottomSheetDialog.show();
            }
        });
        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return view;
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


    private void insertList() {
        dbOpenHelper.openPosting();
        dbOpenHelper.createPostingHelper();
        Cursor cursor = dbOpenHelper.selectPostingColumns();

        while (cursor.moveToNext()) {
            int tempMvId = cursor.getInt(cursor.getColumnIndex("mv_id"));
            String tempTitle = cursor.getString(cursor.getColumnIndex("title"));
            String tempPoster = cursor.getString(cursor.getColumnIndex("mv_poster"));
            String tempMovieDate = cursor.getString(cursor.getColumnIndex("mv_date"));
            String tempPostingDate = cursor.getString(cursor.getColumnIndex("post_date"));
            float tempStar = cursor.getFloat(cursor.getColumnIndex("star"));
            String tempContent = cursor.getString(cursor.getColumnIndex("content"));

            list.add(new MovieDiary(tempPoster, tempTitle, tempStar, tempMovieDate, tempContent));
        }
    }
}
