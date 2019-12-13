package com.example.user.moviediary.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.util.DbOpenHelper;
import com.example.user.moviediary.util.GlideApp;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgMovieDiaryDetails extends Fragment {
    private CircleImageView detailProfileImage;
    private TextView detailNickname;
    private ImageButton ibOption;
    private ImageView detailPosterImage;
    private RatingBar detailRatingBar;
    private TextView detailDate;
    private TextView detailContent;

    private ArrayList<MovieDiary> list;

    private Context mContext;
    private View view;
    private DbOpenHelper dbOpenHelper;

    private int mv_id;
    private String imageSource, title, date, content;
    private float star;

    public static FrgMovieDiaryDetails newInstance(int mv_id, String imageSource, String title, float star, String date, String content) {
        FrgMovieDiaryDetails fragment = new FrgMovieDiaryDetails();
        Bundle args = new Bundle();
        args.putInt("mv_id", mv_id);
        args.putString("imageSource", imageSource);
        args.putString("title", title);
        args.putFloat("star", star);
        args.putString("date", date);
        args.putString("content", content);
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

        mv_id = getArguments().getInt("mv_id");
        imageSource = getArguments().getString("imageSource");
        title = getArguments().getString("title");
        star = getArguments().getFloat("star");
        date = getArguments().getString("date");
        content = getArguments().getString("content");

        GlideApp.with(mContext).load(imageSource).fitCenter().into(detailPosterImage);
        detailRatingBar.setRating(star);
        detailDate.setText(date);
        detailContent.setText(title + "  " + content);

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
                        // 수정 버튼 이벤트
                        bottomSheetDialog.dismiss();
                        // 다이어리 수정 프래그먼트 콜해줌
                        ((MainActivity) mContext).setChangeFragment(FrgMovieDiaryEdit.newInstance(mv_id, imageSource, title, star, date, content));

                    }
                });
                btnDiaryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 식제 버튼 이벤트
                        dbOpenHelper.openPosting();
                        // 취소 재확인 다이얼로그 띄움
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("다이어리 삭제").setMessage("다이어리를 삭제하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dbOpenHelper.deletePostingColumns(mv_id);
                                dbOpenHelper.close();
                                Toast.makeText(getContext(), "다이어리가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                                ((MainActivity) mContext).setChangeFragment(FrgUser.newInstance());
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "취소되었습니다..", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
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


}
