package id.co.derahh.moviecatalogue.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.derahh.moviecatalogue.adapter.MovieAdapter;
import id.co.derahh.moviecatalogue.database.model.movie.Movie;
import id.co.derahh.moviecatalogue.R;
import id.co.derahh.moviecatalogue.viewModel.FavoriteViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoriteFragment extends Fragment /*implements LoadMovieCallback*/ {

    private static final String TAG = MovieFavoriteFragment.class.getSimpleName();
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private ProgressBar progressBar;
    private TextView tvNoData;

    private MovieAdapter adapter;

    private FavoriteViewModel favoriteViewModel;

    public MovieFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_favorite,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar);
        tvNoData = view.findViewById(R.id.tv_no_data);
        RecyclerView recyclerView = view.findViewById(R.id.list_favorite);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getAllFavoritMovie().observe(this, getFavoriteMovie);

        adapter = new MovieAdapter(getContext());
        recyclerView.setAdapter(adapter);

    }

    private Observer<List<Movie>> getFavoriteMovie = new Observer<List<Movie>>() {
        @Override
        public void onChanged(List<Movie> movies) {
            if (movies.size() > 0) {
                adapter.setListData(movies);
                progressBar.setVisibility(View.GONE);
            }else {
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, (ArrayList<? extends Parcelable>) adapter.getListData());
    }
}
