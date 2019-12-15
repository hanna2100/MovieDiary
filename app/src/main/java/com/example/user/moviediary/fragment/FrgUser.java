package com.example.user.moviediary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieDiaryAdapter;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.model.UserData;
import com.example.user.moviediary.util.DbOpenHelper;
import com.example.user.moviediary.util.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgUser extends Fragment implements View.OnClickListener, View.OnTouchListener {

    //Shared Preference 키값
    private static final String USER = "User", INIT = "init";

    private DrawerLayout mainLayout;
    private LinearLayout drawerLayout;
    private Button btnTheme, btnMail, btnLogout;
    private Switch switchAdult;

    private ImageButton ibSetting;
    private TextView diaryCount, wishCount, userName, diaryDesc;
    private Button btnEditProfile;
    private CircleImageView userImage;

    private MovieDiaryAdapter movieDiaryAdapter;
    private ArrayList<MovieDiary> diaryList = new ArrayList<>();
    private ArrayList<MovieDiary> wishList = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private View view;

    private FrameLayout flTop;

    private WrapContentHeightViewPager viewPager;
    private Adapter adapter;

    private DbOpenHelper dbOpenHelper;

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
        if (context instanceof Activity) {
            this.mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user, container, false);
        new ThemeColors(mContext);

        mainLayout = view.findViewById(R.id.mainLayout);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        ibSetting = view.findViewById(R.id.ibSetting);

        btnTheme = view.findViewById(R.id.btnTheme);
        btnMail = view.findViewById(R.id.btnMail);
        switchAdult = view.findViewById(R.id.switchAdult);
        btnLogout = view.findViewById(R.id.btnLogout);

        diaryCount = view.findViewById(R.id.diaryCount);
        wishCount = view.findViewById(R.id.wishCount);
        userName = view.findViewById(R.id.userName);
        diaryDesc = view.findViewById(R.id.diaryDesc);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        userImage = view.findViewById(R.id.userImage);

        flTop = view.findViewById(R.id.flTop);
        viewPager = view.findViewById(R.id.viewPager);

        // 게시물 수 세팅
        diaryCountSetting();
        // 찜 수 세팅
        wishCountSetting();

        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //탭레이아웃 세팅
        setupTabLayout();

        //프로필세팅
        setupProfile();

        ibSetting.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);

        //drawer
        btnTheme.setOnClickListener(this);
        btnMail.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        drawerLayout.setOnTouchListener(this);
        mainLayout.setDrawerListener(listener);

        return view;
    }



    private void diaryCountSetting() {
        diaryList.clear();
        dbOpenHelper = new DbOpenHelper(getContext());
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

            diaryList.add(new MovieDiary(tempMvId,tempPoster,tempTitle,tempStar,tempMovieDate,tempContent));
        }
        diaryCount.setText(String.valueOf(diaryList.size()));
        dbOpenHelper.close();
    }

    private void wishCountSetting() {
        wishList.clear();
        dbOpenHelper = new DbOpenHelper(getContext());
        dbOpenHelper.openLike();
        dbOpenHelper.createLikeHelper();
        Cursor cursor = dbOpenHelper.selectLikeColumns();

        while (cursor.moveToNext()) {
            int tempMvId = cursor.getInt(cursor.getColumnIndex("mv_id"));
            String tempPoster = cursor.getString(cursor.getColumnIndex("mv_poster"));
            String tempTitle = cursor.getString(cursor.getColumnIndex("title"));

            wishList.add(new MovieDiary(tempMvId,tempPoster,tempTitle));
        }
        wishCount.setText(String.valueOf(wishList.size()));

        dbOpenHelper.close();
    }

    private void setupProfile() {

        userName.setText(UserData.userName);
        diaryDesc.setText(UserData.diaryDescription);
        if (UserData.profileImgPath != null)
            userImage.setImageURI(Uri.parse(UserData.profileImgPath));
        else {
            userImage.setImageResource(R.drawable.user_default_image);
            userImage.setColorFilter(MainActivity.mainColor);
        }
        if (UserData.kakaoLogin == 0)
            switchAdult.setEnabled(false);
        Log.d("유저데이터", "프레그="+UserData.userName+UserData.profileImgPath+UserData.diaryDescription+UserData.kakaoLogin);

    }

    private void setupTabLayout() {
        if (viewPager != null) {
            Log.d("ViewPager", "setupViewPager");
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.user_ic_posting);
        tabLayout.getTabAt(1).setIcon(R.drawable.user_ic_like);

        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flTop.setBackgroundColor(MainActivity.mainColor);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new FrgUserPostingList(), null);
        adapter.addFragment(new FrgUserLikeList(), null);
        viewPager.setAdapter(adapter);

    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {

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

            //Drawer menu
            case R.id.btnTheme:
                MainActivity.isPressedTheme = true;
                int red = new Random().nextInt(255);
                int green = new Random().nextInt(255);
                int blue = new Random().nextInt(255);
                ThemeColors.setNewThemeColor((MainActivity) mContext, red, green, blue);
                break;

            case R.id.btnMail:

                mainLayout.closeDrawer(drawerLayout);

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                // email setting 배열로 해놔서 복수 발송 가능
                String[] address = {"space@kokoboa.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "문의 사항");
                email.putExtra(Intent.EXTRA_TEXT, "개발자님께 문의 및 의견 사항이 있어 메일을 보냅니다.\n");
                startActivity(email);
                break;

            case R.id.btnLogout:
                // 로그아웃 재확인 다이얼로그
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Logout").setMessage("로그아웃 하시겠습니까?\n모든 다이어리 정보가 삭제됩니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //db테이블 모두 드롭후 재생성
                        DbOpenHelper dbOpenHelper = new DbOpenHelper(mContext);
                        dbOpenHelper.openUser();
                        dbOpenHelper.upgradeUserHelper();
                        dbOpenHelper.openLike();
                        dbOpenHelper.upgradeLikeHelper();
                        dbOpenHelper.openPosting();
                        dbOpenHelper.upgradePostingHelper();
                        dbOpenHelper.close();

                        //프로필이 설정되지 않음을 저장
                        SharedPreferences.Editor editor = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                        editor.putBoolean(INIT, false);
                        editor.apply();

                        //액티비티 recreate
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            mActivity.recreate();

                        else {
                            Intent i = mActivity.getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mActivity.startActivity(i);
                        }
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE; // To make notifyDataSetChanged() do something
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
