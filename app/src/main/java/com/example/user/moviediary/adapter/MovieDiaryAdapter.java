package com.example.user.moviediary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.fragment.FrgMovieDiaryDetails;
import com.example.user.moviediary.util.GlideApp;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieDiaryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<MovieDiary> list;
    private LayoutInflater layoutInflater;

    private String posterPath;

    private MovieDiaryDetailAdapter movieDiaryDetailAdapter;

    public MovieDiaryAdapter(Context context, int layout, ArrayList<MovieDiary> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, null);
        }
        ImageView movieImage = convertView.findViewById(R.id.movieImage);
        final MovieDiary movieDiary = list.get(i);

        posterPath = movieDiary.getDetailImage();

        GlideApp.with(context).load(posterPath).fitCenter().into(movieImage);

        movieDiaryDetailAdapter = new MovieDiaryDetailAdapter(R.layout.user_detail, list);

        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View viewDialog = View.inflate(context, R.layout.user_detail, null);
//
//                CircleImageView detailProfileImage = viewDialog.findViewById(R.id.detailProfileImage);
//                TextView detailNickname = viewDialog.findViewById(R.id.detailNickname);
//                ImageButton ibOption = viewDialog.findViewById(R.id.ibOption);
//                ImageView detailPosterImage = viewDialog.findViewById(R.id.detailPosterImage);
//                RatingBar detailRatingBar = viewDialog.findViewById(R.id.detailRatingBar);
//                TextView detailDate = viewDialog.findViewById(R.id.detailDate);
//                TextView detailContent = viewDialog.findViewById(R.id.detailContent);
//
//                GlideApp.with(context).load(list.get(i).getDetailImage()).into(detailPosterImage);
//                detailRatingBar.setRating(list.get(i).getDetailRatingBar());
//                detailDate.setText(list.get(i).getDetailDate());
//                detailContent.setText(list.get(i).getDetailTitle()+"  "+list.get(i).getDetailReview());
//
//                setTags(detailContent,detailContent.getText().toString());
//
//                ibOption.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 게시물 수정 삭제 메뉴 만들어줘야함
//                        Toast.makeText(context,"왜안띄워줘",Toast.LENGTH_SHORT).show();
//                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//                        bottomSheetDialog.setContentView(R.layout.user_diary_dialog);
//                        Button btnDiaryEdit = bottomSheetDialog.findViewById(R.id.btnDiaryEdit);
//                        Button btnDiaryDelete = bottomSheetDialog.findViewById(R.id.btnDiaryDelete);
//
//                        btnDiaryEdit.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // 수정버튼 이벤트
//                            }
//                        });
//                        btnDiaryDelete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // 수정버튼 이벤트
//                            }
//                        });
//                        bottomSheetDialog.show();
//                    }
//                });
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                dialog.setView(viewDialog);
//                dialog.show();

                // 다이어리 상세정보 프래그먼트 띄움
                ((MainActivity) context).setChangeFragment(FrgMovieDiaryDetails.newInstance());
            }
        });
        return convertView;
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
