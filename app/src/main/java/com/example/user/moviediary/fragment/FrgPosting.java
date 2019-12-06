package com.example.user.moviediary.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.example.user.moviediary.R;

import java.util.Calendar;

public class FrgPosting extends Fragment implements View.OnClickListener {
    Button btnAddMovie, btnCancel, btnSave;
    ImageView postingImage;
    RatingBar postingRatingBar;
    TextView postingTitle, postingDate;
    EditText edtReview;

    private Context mContext;
    private View view;

    public static FrgPosting newInstance() {
        FrgPosting fragment = new FrgPosting();
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
        view = inflater.inflate(R.layout.fragment_posting, container, false);

        btnAddMovie = view.findViewById(R.id.btnAddMovie);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);
        postingImage = view.findViewById(R.id.postingImage);
        postingRatingBar = view.findViewById(R.id.postingRatingBar);
        postingTitle = view.findViewById(R.id.postingTitle);
        postingDate = view.findViewById(R.id.postingDate);
        edtReview = view.findViewById(R.id.edtReview);

        btnAddMovie.setOnClickListener(this);
        postingDate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMovie:
                // 버튼 누르면 검색화면 뜨게 함!
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
                builder.setTitle("인사말").setMessage("반갑습니다");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                break;
        }
    }
}
