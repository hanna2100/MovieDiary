package com.example.user.moviediary.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.user.moviediary.util.DbOpenHelper;
import com.example.user.moviediary.util.GlideApp;

import java.util.Calendar;

public class FrgMovieDiaryEdit extends Fragment implements View.OnClickListener  {

    private Button btnEditCancel;
    private Button btnEditSave;
    private ImageView detailPosterImage;
    private RatingBar detailRatingBar;
    private TextView detailDate;
    private EditText detailContent;

    private Context mContext;
    private View view;
    private DbOpenHelper dbOpenHelper;

    private int mv_id;
    private String imageSource, title, date, content;
    private float star;

    public static FrgMovieDiaryEdit newInstance(int mv_id, String imageSource, String title, float star, String date, String content) {
        FrgMovieDiaryEdit fragment = new FrgMovieDiaryEdit();
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
        View view = View.inflate(mContext, R.layout.fragment_user_diary_edit, null);
        btnEditCancel = view.findViewById(R.id.btnEditCancel);
        btnEditSave = view.findViewById(R.id.btnEditSave);

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
        detailContent.setText(content);

        detailContent.setEnabled(true);

        detailDate.setOnClickListener(this);
        btnEditCancel.setOnClickListener(this);
        btnEditSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        switch (v.getId()) {
            case R.id.detailDate:
                // 날짜 선택 다이얼로그 뜨게 함!
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        detailDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, y, m, d); // 기본값 연월일
                dpd.show();
                break;
            case R.id.btnEditCancel:
                Toast.makeText(mContext,"취소되었습니다.",Toast.LENGTH_SHORT).show();

                ((MainActivity) mContext)
                        .setChangeFragment(FrgMovieDiaryDetails.newInstance(mv_id, imageSource, title, star, date, content));
                break;
            case R.id.btnEditSave:
                String editDate = detailDate.getText().toString();
                String editPostDate = y+"-"+m+"-"+d;
                float editStar = detailRatingBar.getRating();
                String editContent = detailContent.getText().toString();

                dbOpenHelper.openPosting();
                dbOpenHelper.updatePostingColumn(mv_id,title,imageSource,editDate,editPostDate,editStar,editContent);
                dbOpenHelper.close();

                Toast.makeText(mContext,"다이어리가 수정되었습니다.",Toast.LENGTH_SHORT).show();

                ((MainActivity) mContext)
                        .setChangeFragment(FrgMovieDiaryDetails.newInstance(mv_id, imageSource, title, editStar, editDate, editContent));

                break;
        }
    }

}

