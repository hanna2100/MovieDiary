package com.example.user.moviediary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.moviediary.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgUser extends Fragment implements View.OnClickListener {
    private DrawerLayout mainLayout;
    private LinearLayout drawerLayout;

    private ImageButton ibSetting, ibDiary, ibWish;
    private TextView userID, diaryCount, wishCount, userName, userMySelf;
    private Button btnEditProfile;
    private CircleImageView userImage;
    private GridView gridView;

    private Context mContext;
    private View view;

    public static FrgUser newInstance() {
        FrgUser fragment = new FrgUser();
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
        view = inflater.inflate(R.layout.fragment_user, container, false);

        mainLayout = view.findViewById(R.id.mainLayout);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        ibSetting = view.findViewById(R.id.ibSetting);
        ibDiary = view.findViewById(R.id.ibDiary);
        ibWish = view.findViewById(R.id.ibWish);

        userID = view.findViewById(R.id.userID);
        diaryCount = view.findViewById(R.id.diaryCount);
        wishCount = view.findViewById(R.id.wishCount);
        userName = view.findViewById(R.id.userName);
        userMySelf = view.findViewById(R.id.userMySelf);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        userImage = view.findViewById(R.id.userImage);
        gridView = view.findViewById(R.id.gridView);

        ibSetting.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        ibDiary.setOnClickListener(this);
        ibWish.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSetting:
                mainLayout.openDrawer(drawerLayout);
                break;
            case R.id.btnEditProfile:
                // 프로필 수정 창 뜨게 하기
                break;
            case R.id.ibDiary:
                // 그리드 뷰에 내가 리뷰 쓴 영화 나오게 하기!
                break;
            case R.id.ibWish:
                // 그리드 뷰에 내가 보고싶은 영화 나오게 하기!
                break;

        }
    }
}
