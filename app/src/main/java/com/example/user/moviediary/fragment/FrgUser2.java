package com.example.user.moviediary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieDiaryAdapter;
import com.example.user.moviediary.model.MovieDiary;
import com.example.user.moviediary.util.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgUser2 extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private DrawerLayout mainLayout;
    private LinearLayout drawerLayout;
    private Button btnTheme, btnMail;

    private ImageButton ibSetting;
    private TextView userID, diaryCount, wishCount, userName, userMySelf;
    private Button btnEditProfile;
    private CircleImageView userImage;

    private MovieDiaryAdapter movieDiaryAdapter;
    private ArrayList<MovieDiary> list = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private View view;

    private LinearLayout hideLayout;

    private WrapContentHeightViewPager viewPager;
    private Adapter adapter;

    public static FrgUser2 newInstance() {
        FrgUser2 fragment = new FrgUser2();
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

        view = inflater.inflate(R.layout.fragment_user2, container, false);
        new ThemeColors(mContext);

        mainLayout = view.findViewById(R.id.mainLayout);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        ibSetting = view.findViewById(R.id.ibSetting);

        btnTheme = view.findViewById(R.id.btnTheme);
        btnMail = view.findViewById(R.id.btnMail);

        userID = view.findViewById(R.id.userID);
        diaryCount = view.findViewById(R.id.diaryCount);
        wishCount = view.findViewById(R.id.wishCount);
        userName = view.findViewById(R.id.userName);
        userMySelf = view.findViewById(R.id.userMySelf);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        userImage = view.findViewById(R.id.userImage);

        hideLayout = view.findViewById(R.id.hideLayout);

        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        //추가
        viewPager = view.findViewById(R.id.viewPager);
        if (viewPager != null) {
            Log.d("ViewPager", "setupViewPager");
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        ibSetting.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);

        //drawer
        btnTheme.setOnClickListener(this);
        btnMail.setOnClickListener(this);
        drawerLayout.setOnTouchListener(this);
        mainLayout.setDrawerListener(listener);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new Adapter(getChildFragmentManager());
        Log.d("ViewPager", "adapter=" + adapter);
        adapter.addFragment(new FrgUserPostingList(), "Posting");
        adapter.addFragment(new FrgUserLikeList(), "Like");
        Log.d("ViewPager", "adapterCnt=" + adapter.getCount());
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

                int red = new Random().nextInt(255);
                int green = new Random().nextInt(255);
                int blue = new Random().nextInt(255);
                //ThemeColors.setNewThemeColor((MainActivity) mContext, red, green, blue);
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
