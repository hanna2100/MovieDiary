package com.example.user.moviediary.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieChartAdapter;
import com.example.user.moviediary.etc.MovieChart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FrgMovieHome extends Fragment {

    private static final String TAG = "Parsing";

    private View view;
    private RecyclerView recyclerView;
    private LinearLayout layoutView;
    private ArrayList<MovieChart> list = new ArrayList<>();
    private MovieChartAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private Context mContext;

    public static FrgMovieHome newInstance() {
        FrgMovieHome fragment = new FrgMovieHome();
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
        view = inflater.inflate(R.layout.fragment_movie_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutView = view.findViewById(R.id.layoutView);

        new MyTask().execute();

        return view;

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //진행다일로그 시작
            layoutView.setVisibility(View.INVISIBLE);
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            try {
                //매니페스트에 인터넷허가, usesCleartextTraffic true추가해야함
                //implementation 'org.jsoup:jsoup:1.12.1'

                //크롤링 할 인터넷 사이트 접속
                doc = Jsoup.connect("http://www.cgv.co.kr/movies/").get();

                //셀렉터를 통해 얻어올 자료 특정화
                Elements titleElements = doc.select("div.box-contents strong.title");
                Elements posterElements = doc.select("div.box-image img[src]");
                Elements ageElements = doc.select("div.box-image span.thumb-image");
                Elements rsrvElements = doc.select("div.box-contents strong.percent");
                Elements greatElements = doc.select("div.box-contents span.percent");
                Elements releaseElements = doc.select("div.box-contents span.txt-info");

                //1위~7위까지의 각 영화 정보 얻어오기
                for (int i = 0; i < 7; i++) {
                    Element titleElement = titleElements.get(i);
                    Element posterElement = posterElements.get(i);
                    Element ageElement = ageElements.get(i);
                    Element rsrvElement = rsrvElements.get(i);
                    Element greatElement = greatElements.get(i);
                    Element releaseElement = releaseElements.get(i);

                    int rank = (i + 1);
                    //텍스트 추출
                    String title = titleElement.text();
                    String poster = posterElement.attr("src");
                    String age = ageElement.text();
                    String rsrv = rsrvElement.text();
                    String great = greatElement.text();
                    String rlsDate = releaseElement.text();

                    MovieChart movieChart = new MovieChart(rank, title, poster, age, rsrv, great, rlsDate);
                    //리스트에 넣기
                    list.add(movieChart);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            //레이아웃 매니저 설정
            linearLayoutManager = new LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);


            //레이아웃 어댑터 설정
            adapter = new MovieChartAdapter(R.layout.item_movie_chart, list);
            recyclerView.setAdapter(adapter);

            //프로그레스바 제거
            progressDialog.dismiss();
            layoutView.setVisibility(View.VISIBLE);

            super.onPostExecute(result);
        }



    }


}
