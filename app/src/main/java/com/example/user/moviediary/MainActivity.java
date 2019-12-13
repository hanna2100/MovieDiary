package com.example.user.moviediary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.user.moviediary.fragment.FrgMovieHome;
import com.example.user.moviediary.fragment.FrgMovieSearch;
import com.example.user.moviediary.fragment.FrgPosting;
import com.example.user.moviediary.fragment.FrgUser;
import com.example.user.moviediary.fragment.ThemeColors;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    ThemeColors themeColors;
    private boolean isThemeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        themeColors = new ThemeColors(MainActivity.this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
        FrameLayout mainFrame = findViewById(R.id.mainFrame);

        //테마색상변경
        mainFrame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                int red = new Random().nextInt(255);
                int green = new Random().nextInt(255);
                int blue = new Random().nextInt(255);
                themeColors.setNewThemeColor(MainActivity.this, red, green, blue);

                Toast.makeText(getApplicationContext(), "df", Toast.LENGTH_LONG).show();

                isThemeChanged = true;

                return true;
            }
        });


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
        //처음 띄울 프레그먼트
        if (isThemeChanged)
            setChangeFragment(FrgUser.newInstance());
        else
            setChangeFragment(FrgMovieHome.newInstance());
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
