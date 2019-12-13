package com.example.user.moviediary.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieDiaryAdapter;
import com.example.user.moviediary.model.MovieDiary;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgUser extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private DrawerLayout mainLayout;
    private LinearLayout drawerLayout;
    private Button btnTheme, btnMail;


    //탭 레이아웃 선택시 효과 주기위한 레이아웃 변수
    private LinearLayout diaryTabSelect;
    private LinearLayout wishTabSelect;

    private ImageButton ibSetting, ibDiary, ibWish;
    private TextView userID, diaryCount, wishCount, userName, userMySelf;
    private Button btnEditProfile;
    private CircleImageView userImage;
    private GridView gridView;

    private MovieDiaryAdapter movieDiaryAdapter;
    private ArrayList<MovieDiary> list = new ArrayList<>();
    private Context mContext;
    private View view;

    // 그리드뷰 이벤트 사용 변수
    int myLastVisiblePos;
    private LinearLayout hideLayout;

    public static FrgUser newInstance() {
        FrgUser fragment = new FrgUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity ){
            Log.d("FrgUser","Context=MainActivity");
        }

        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user, container, false);

        new ThemeColors(mContext); /** 테마 클래스 */

        mainLayout = view.findViewById(R.id.mainLayout);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        ibSetting = view.findViewById(R.id.ibSetting);
        ibDiary = view.findViewById(R.id.ibDiary);
        ibWish = view.findViewById(R.id.ibWish);

        btnTheme = view.findViewById(R.id.btnTheme); // 테마변경 버튼
        btnMail = view.findViewById(R.id.btnMail); // 관리자에게 메일로 의견 및 문의사항 보내기 버튼

        userID = view.findViewById(R.id.userID);
        diaryCount = view.findViewById(R.id.diaryCount);
        wishCount = view.findViewById(R.id.wishCount);
        userName = view.findViewById(R.id.userName);
        userMySelf = view.findViewById(R.id.userMySelf);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        userImage = view.findViewById(R.id.userImage);
        gridView = view.findViewById(R.id.gridView);

        diaryTabSelect = view.findViewById(R.id.diaryTabSelect);
        wishTabSelect = view.findViewById(R.id.wishTabSelect);

        hideLayout = view.findViewById(R.id.hideLayout);
        // 처음 유저 프레그먼트에 들어왔을때는 다이어리 탭이 디폴트
        diaryTabSelect.setVisibility(View.VISIBLE);
        wishTabSelect.setVisibility(View.INVISIBLE);

        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        movieDiaryAdapter = new MovieDiaryAdapter(getContext(), R.layout.item_user, list);
        diaryCount.setText(String.valueOf(list.size()));
        gridView.setAdapter(movieDiaryAdapter);
        // 그리드 뷰 이벤트 사용 변수
        myLastVisiblePos = gridView.getFirstVisiblePosition();

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {

                int currentFirstVisPos = view.getFirstVisiblePosition();

                if (currentFirstVisPos > myLastVisiblePos) {
                    //scroll down
                    Toast.makeText(mContext, "스크롤 다운", Toast.LENGTH_SHORT).show();

                    Animation animHide = AnimationUtils.loadAnimation(mContext, R.anim.view_hide);

                    hideLayout.startAnimation(animHide);
                    hideLayout.setVisibility(View.GONE);
                }
                if (currentFirstVisPos < myLastVisiblePos) {
                    //scroll up
                    Toast.makeText(mContext, "스크롤 업", Toast.LENGTH_SHORT).show();

                    Animation animShow = AnimationUtils.loadAnimation(mContext, R.anim.view_show);

                    hideLayout.startAnimation(animShow);
                    hideLayout.setVisibility(View.VISIBLE);
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });
        ibSetting.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        ibDiary.setOnClickListener(this);
        ibWish.setOnClickListener(this);
        btnTheme.setOnClickListener(this);
        btnMail.setOnClickListener(this);
        drawerLayout.setOnTouchListener(this);
        mainLayout.setDrawerListener(listener);

        return view;
    }

    DrawerLayout.DrawerListener listener=new DrawerLayout.DrawerListener(){

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }

    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSetting:
                mainLayout.openDrawer(drawerLayout);
                break;
            case R.id.btnEditProfile:
                // 프로필 수정 창 뜨게 하기
                ((MainActivity) mContext).setChangeFragment(FrgUserProfileEdit.newInstance());
                break;
            case R.id.ibDiary:
                // 그리드 뷰에 내가 리뷰 쓴 영화 나오게 하기!
                diaryTabSelect.setVisibility(View.VISIBLE);
                wishTabSelect.setVisibility(View.INVISIBLE);
                break;
            case R.id.ibWish:
                // 그리드 뷰에 내가 보고싶은 영화 나오게 하기!
                diaryTabSelect.setVisibility(View.INVISIBLE);
                wishTabSelect.setVisibility(View.VISIBLE);
                break;

            case R.id.btnTheme:
                // 색상 변경
                int red= new Random().nextInt(255);
                int green= new Random().nextInt(255);
                int blue= new Random().nextInt(255);
                //ThemeColors.setNewThemeColor((MainActivity) mContext, red, green, blue);
                ThemeColors.setNewThemeColor((MainActivity)mContext, red, green, blue);
                break;

            case R.id.btnMail:

                mainLayout.closeDrawer(drawerLayout); // 버튼 클릭했을 때 닫아주기

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                // email setting 배열로 해놔서 복수 발송 가능
                String[] address = {"space@kokoboa.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT,"문의 사항");
                email.putExtra(Intent.EXTRA_TEXT,"개발자님께 문의 및 의견 사항이 있어 메일을 보냅니다.\n");
                startActivity(email);

                break;


        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return true;

    }
}
