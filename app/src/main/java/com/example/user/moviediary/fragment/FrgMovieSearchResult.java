package com.example.user.moviediary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.adapter.MovieSearchAdapter;
import com.example.user.moviediary.etc.SearchResults;
import com.example.user.moviediary.util.MoviesRepository;
import com.example.user.moviediary.util.OnGetMoviesCallback;

import java.util.ArrayList;
import java.util.List;

public class FrgMovieSearchResult extends Fragment {

    private final String TAG = "SearchingMovie";

    private View view;
    private Context mContext;

    private MoviesRepository moviesRepository;
    private MovieSearchAdapter adapter;

    private List<SearchResults.ResultsBean> list;

    public static FrgMovieSearchResult newInstance() {
        FrgMovieSearchResult fragment = new FrgMovieSearchResult();
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
        view = inflater.inflate(R.layout.fragment_movie_search_resuslt, container, false);

        final RecyclerView rcvSearch = view.findViewById(R.id.rcvSearch);
        final EditText edtSearch = view.findViewById(R.id.edtSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list = new ArrayList<>();

                String query = edtSearch.getText().toString().trim();
                query = query.replace(" ", "+");
                MoviesRepository.setQuery(query);
                MoviesRepository.setPage(1);
                moviesRepository = MoviesRepository.getInstance();
                moviesRepository.getSearchedMovieResult(new OnGetMoviesCallback() {
                    @Override
                    public void onSuccess(List<SearchResults.ResultsBean> resultsBeanList) {
                        list = resultsBeanList;
                        adapter = new MovieSearchAdapter(R.layout.item_movie_search, list,
                                new MovieSearchAdapter.OnItemSelectedInterface() {
                                    @Override
                                    public void onItemSelected(View v, int movieID) {
                                        ((MainActivity) getActivity()).setChangeFragment(FrgMovieDetails.newInstance(movieID));

                                    }
                                });
                        rcvSearch.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        rcvSearch.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void onError() {
                        if (edtSearch.getText().toString().equals("")) {
                            Toast.makeText(mContext, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        rcvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount();
                if (lastVisibleItemPosition == (itemTotalCount - 1) && itemTotalCount % 20 == 0) {

                    MoviesRepository.switchToTheNextPage();
                    moviesRepository.getSearchedMovieResult(new OnGetMoviesCallback() {
                        @Override
                        public void onSuccess(List<SearchResults.ResultsBean> resultsBeanList) {

                            list.addAll(resultsBeanList);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(mContext, "더 이상 검색결과가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return view;
    }

}
