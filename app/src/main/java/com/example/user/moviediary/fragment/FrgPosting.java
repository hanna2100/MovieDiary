package com.example.user.moviediary.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.util.GlideApp;

import java.util.Calendar;

public class FrgPosting extends Fragment implements View.OnClickListener {

    private static final String MOVIE_ID = "MOVIE_ID";
    private static final String MOVIE_TITLE = "MOVIE_TITLE";
    private static final String POSTER_PATH = "POSTER_PATH";

    Button btnAddMovie, btnCancel, btnSave;
    ImageView postingImage;
    RatingBar postingRatingBar;
    TextView postingTitle, postingDate;
    EditText edtReview;

    private int movie_id;

    private Context mContext;
    private Activity mActivity;
    private View view;

    public static FrgPosting newInstance(int movie_id, String title, String poster_path) {
        FrgPosting fragment = new FrgPosting();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movie_id);
        args.putString(MOVIE_TITLE, title);
        args.putString(POSTER_PATH, poster_path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity) {
            this.mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posting, container, false);

        btnAddMovie = view.findViewById(R.id.btnAddMovie);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);
        postingImage = view.findViewById(R.id.postingImage);
        postingRatingBar = view.findViewById(R.id.postingRatingBar);
        postingTitle = view.findViewById(R.id.postingTitle);
        postingDate = view.findViewById(R.id.postingDate);
        edtReview = view.findViewById(R.id.edtReview);

        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //영화제목, 포스터 자동세팅
        autoSettingTitleAndPoster();

        btnAddMovie.setOnClickListener(this);
        postingDate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        return view;
    }

    private void autoSettingTitleAndPoster() {
        //번들에서 무비아이디, 무비타이틀, 포스터패스 받아오기
        movie_id = getArguments().getInt(MOVIE_ID);
        String movie_title = getArguments().getString(MOVIE_TITLE);
        String poster_path = getArguments().getString(POSTER_PATH);

        //포스터설정
        if (poster_path != null) {
            String posterPath = "https://image.tmdb.org/t/p/w500" + poster_path;
            GlideApp.with(view).load(posterPath)
                    .fitCenter()
                    .into(postingImage);
        }

        //제목설정
        postingTitle.setText(movie_title);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMovie:
                // 버튼 누르면 검색화면 뜨게 함!
                ((MainActivity) mActivity).setChangeFragment(FrgMovieSearch.newInstance());
                break;
            case R.id.postingDate:
                // 현재 날짜 가져오기
                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                // 날짜 선택 다이얼로그 뜨게 함!
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        postingDate.setText(year + "." + (month + 1) + "." + dayOfMonth);
                    }
                }, y, m, d); // 기본값 연월일
                dpd.show();
                break;
            case R.id.btnCancel:
                // 입력한거 싹다 지워지게? 취소하시겠습니까? 라는 안내 창 띄워??
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("취소").setMessage("입력하신 내용이 지워집니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // 영화 포스터 자리 디폴트 이미지 필요,,
                        // postingImage.setImageResource();
                        postingRatingBar.setRating(3.0f);
                        postingTitle.setText("");
                        postingDate.setText("이 곳을 눌러 날짜를 선택하세요.");
                        edtReview.setText("");
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.btnSave:
                float a = postingRatingBar.getRating();
                Toast.makeText(getContext(), a + " ", Toast.LENGTH_SHORT).show();
                // DB로 보내야함!
                // 저장 버튼 누르고 게시물 작성 완료하면 마이페이지로 넘어가서 새 게시물 등록된거 보여줄가??
                break;
        }
    }
}
