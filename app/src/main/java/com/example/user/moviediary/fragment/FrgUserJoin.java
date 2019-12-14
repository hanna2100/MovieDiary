package com.example.user.moviediary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;

public class FrgUserJoin extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View view;

    public static FrgUserJoin newInstance() {
        FrgUserJoin fragment = new FrgUserJoin();
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
        view = inflater.inflate(R.layout.fragment_user_join, container, false);

        ImageButton ibLoginDefault = view.findViewById(R.id.ibLoginDefault);
        ImageButton ibLoginKakao = view.findViewById(R.id.ibLoginKakao);
        BottomNavigationView bottomMenu = getActivity().findViewById(R.id.bottomMenu);
        bottomMenu.setVisibility(View.GONE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        ibLoginDefault.setOnClickListener(this);
        ibLoginKakao.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ibLoginDefault:
                ((MainActivity)mContext).setChangeFragment(FrgUserJoinDefault.newInstance());
                break;

            case R.id.ibLoginKakao:
                break;
        }
    }
}
