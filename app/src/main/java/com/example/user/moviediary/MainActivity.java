package com.example.user.moviediary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.user.moviediary.fragment.FrgMovieHome;
import com.example.user.moviediary.fragment.FrgMovieSearch;
import com.example.user.moviediary.fragment.FrgPosting;
import com.example.user.moviediary.fragment.FrgUser;
import com.example.user.moviediary.fragment.ThemeColors;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "ThemeColors", KEY = "color";
    public static int mainColor;

    ThemeColors themeColors;
    public static boolean isChangedTheme;

    private long backbtnTime = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeColors = new ThemeColors(MainActivity.this);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
        FrameLayout mainFrame = findViewById(R.id.mainFrame);

        SharedPreferences sharedPreferences = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString(KEY, "004bff");
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

        //첫 화면을 시작화면으로 설정
        if (!isChangedTheme)
            setChangeFragment(FrgMovieHome.newInstance());

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
}
