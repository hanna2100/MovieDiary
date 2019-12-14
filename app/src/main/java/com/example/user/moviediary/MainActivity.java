package com.example.user.moviediary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.user.moviediary.fragment.FrgMovieHome;
import com.example.user.moviediary.fragment.FrgMovieSearch;
import com.example.user.moviediary.fragment.FrgPosting;
import com.example.user.moviediary.fragment.FrgUser;
import com.example.user.moviediary.fragment.FrgUserJoin;
import com.example.user.moviediary.fragment.ThemeColors;
import com.example.user.moviediary.model.UserData;
import com.example.user.moviediary.util.DbOpenHelper;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "ThemeColors", KEY = "color";
    private static final String USER = "User", INIT = "init";
    public static int mainColor;
    public static int deviceWidth;
    public static boolean isPressedTheme;

    private long backbtnTime = 0l;
    private DbOpenHelper dbOpenHelper;

    ThemeColors themeColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeColors = new ThemeColors(MainActivity.this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
        FrameLayout mainFrame = findViewById(R.id.mainFrame);
        dbOpenHelper = new DbOpenHelper(this);

        //바텀메뉴 테마색상설정
        SharedPreferences prefTheme = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String stringColor = prefTheme.getString(KEY, "252525");
        mainColor = Color.parseColor("#" + stringColor);
        ColorStateList colorList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked}, // checked
                        new int[]{-android.R.attr.state_checked} // unchecked
                },
                new int[]{
                        mainColor,
                        Color.GRAY
                }
        );
        bottomMenu.setItemIconTintList(colorList);
        bottomMenu.setItemTextColor(colorList);

        //최초로그인 확인하기 & 최초로그인 아닐 시 첫 화면을 홈화면으로 설정
        SharedPreferences prefUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        boolean isInitialized = prefUser.getBoolean(INIT, false);
        if (isInitialized == false) {
            setChangeFragment(FrgUserJoin.newInstance());
        }else if(isInitialized ==true && isPressedTheme==false){
            setChangeFragment(FrgMovieHome.newInstance());
            setupUserProfile();
        }

        //디바이스 크기구하기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;


        //메뉴를 변경했을 때 해당된 프레그먼트를 세팅한다
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.actionHome:
                        setChangeFragment(FrgMovieHome.newInstance());
                        break;
                    case R.id.actionSearch:
                        setChangeFragment(FrgMovieSearch.newInstance(false));
                        break;
                    case R.id.actionPosting:
                        setChangeFragment(FrgPosting.newInstance(0, null, null));
                        break;
                    case R.id.actionUser:
                        setChangeFragment(FrgUser.newInstance());
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        // This will get you total fragment in the backStack

        long curentTime = System.currentTimeMillis();
        long getTime = curentTime - backbtnTime;

        if (getTime >= 0 && getTime < 500) {
            finish();
        } else {
            backbtnTime = curentTime;
            super.onBackPressed();
        }
    }

    public void setChangeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();

        return;
    }

    public void setupUserProfile() {
        dbOpenHelper.openUser();
        dbOpenHelper.createUserHelper();
        Cursor cursor = dbOpenHelper.selectUserColumns();

        if (cursor.moveToNext()) {
            UserData.userName = cursor.getString(cursor.getColumnIndex("name"));
            UserData.profileImgPath = cursor.getString(cursor.getColumnIndex("profile_img"));
            UserData.diaryDescription = cursor.getString(cursor.getColumnIndex("diary_desc"));
            UserData.kakaoLogin = cursor.getInt(cursor.getColumnIndex("kakao_login"));
        }
        Log.d("유저데이터", UserData.userName+UserData.profileImgPath+UserData.diaryDescription+UserData.kakaoLogin);
        dbOpenHelper.close();
    }

}
