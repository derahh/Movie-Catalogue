package id.co.derahh.moviecatalogue.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.util.Log;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.co.derahh.moviecatalogue.LoadMovieCallback;
import id.co.derahh.moviecatalogue.R;
import id.co.derahh.moviecatalogue.adapter.TvShowFavoriteAdapter;
import id.co.derahh.moviecatalogue.database.DatabaseContract;
import id.co.derahh.moviecatalogue.database.TvShowHelper;
import id.co.derahh.moviecatalogue.helper.MappingHelper;
import id.co.derahh.moviecatalogue.model.tvShow.TvShow;
import id.co.derahh.moviecatalogue.viewModel.FavoriteViewModel;

import static id.co.derahh.moviecatalogue.database.DatabaseContract.TvShowColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavoriteFragment extends Fragment /*implements LoadMovieCallback*/ {

    private static final String TAG = TvShowFavoriteFragment.class.getSimpleName();
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private ProgressBar progressBar;
    private TextView tvNoData;

    private TvShowFavoriteAdapter adapter;
    private TvShowHelper tvShowHelper;

    private FavoriteViewModel favoriteViewModel;


    public TvShowFavoriteFragment() {
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

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getAllFavoritTvShow().observe(this, getFavoritTvShow);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
//
//        tvShowHelper = TvShowHelper.getInstance(getContext());
//        tvShowHelper.open();
//
        adapter = new TvShowFavoriteAdapter(getContext());
        recyclerView.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        MovieFavoriteFragment.DataObserver myObserver = new MovieFavoriteFragment.DataObserver(handler, getContext());
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(DatabaseContract.MovieColumns.CONTENT_URI, true, myObserver);
//
//        if (savedInstanceState == null) {
//            new LoadTvShowAsync(getContext(), this).execute();
//        } else {
//            ArrayList<TvShow> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
//            if (list != null) {
//                adapter.setListData(list);
//            }
//        }
    }

    private Observer<List<TvShow>> getFavoritTvShow = new Observer<List<TvShow>>() {
        @Override
        public void onChanged(List<TvShow> tvShows) {
            if (tvShows.size() > 0) {
                adapter.setListData(tvShows);
                progressBar.setVisibility(View.GONE);
            }else {
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    };
//
//    @Override
//    public void preExecute() {
//        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.setVisibility(View.VISIBLE);
//                tvNoData.setVisibility(View.GONE);
//                Log.d(TAG, "run: PreExecute");
//            }
//        });
//    }
//
//    @Override
//    public void postExecute(Cursor cursor) {
//        progressBar.setVisibility(View.GONE);
//        ArrayList<TvShow> tvShows = MappingHelper.mapCursorTvShowToArrayList(cursor);
//        if (tvShows != null) {
//            if (tvShows.size() > 0) {
//                adapter.setListData(tvShows);
//                tvNoData.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                Log.d(TAG, " MOVIE TIDAK NULL");
//            } else {
//                tvNoData.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//                adapter.setListData(new ArrayList<TvShow>());
//                Log.d(TAG, " MOVIE NULL");
//            }
//        } else {
//            tvNoData.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//            adapter.setListData(new ArrayList<TvShow>());
//            Log.d(TAG, " MOVIE NULL");
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        tvShowHelper.close();
//    }
//
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, (ArrayList<? extends Parcelable>) adapter.getListData());
    }
//
//    private static class LoadTvShowAsync extends AsyncTask<Void, Void, Cursor> {
//
//        private final WeakReference<Context> weakContext;
//        private final WeakReference<LoadMovieCallback> weakCallback;
//
//        private LoadTvShowAsync(Context context, LoadMovieCallback callback) {
//            weakContext = new WeakReference<>(context);
//            weakCallback = new WeakReference<>(callback);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            weakCallback.get().preExecute();
//            Log.d(TAG, "onPreExecute: ");
//        }
//
//        @Override
//        protected Cursor doInBackground(Void... voids) {
//            Log.d(TAG, "doInBackground: ");
//            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
//        }
//
//        @Override
//        protected void onPostExecute(Cursor cursor) {
//            super.onPostExecute(cursor);
//            weakCallback.get().postExecute(cursor);
//            Log.d(TAG, "onPostExecute: ");
//        }
//    }
//
    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }
}
