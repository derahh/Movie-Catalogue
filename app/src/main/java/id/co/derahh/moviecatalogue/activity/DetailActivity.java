package id.co.derahh.moviecatalogue.activity;

import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import id.co.derahh.moviecatalogue.Util.SharedPreferencesUtils;
import id.co.derahh.moviecatalogue.model.tvShow.TvShow;
import id.co.derahh.moviecatalogue.model.movie.Movie;
import id.co.derahh.moviecatalogue.R;
import id.co.derahh.moviecatalogue.viewModel.FavoriteViewModel;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle, tvDescription, tvYear, tvUserScore;
    ImageView imgPhoto;

    private boolean isAlreadyLoved = false;
    private String id;

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TV_SHOW = "extra_tv_show";

    private Menu menu;
    private Movie movie;
    private TvShow tvShow;
    private Uri uri;
    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        imgPhoto = findViewById(R.id.img_photo);
        tvYear = findViewById(R.id.tv_year);
        tvUserScore = findViewById(R.id.user_score);

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        tvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);

        if (movie != null) {
            showMovieData();
        } else if (tvShow != null) {
            showTvShowData();
        }

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            Log.d("id: ", "Id: " + getIntent().getData());
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    if (movie != null) {
                        movie = new Movie(cursor);
                    } else if (tvShow != null) {
                        tvShow = new TvShow(cursor);
                    }
                }
                cursor.close();
            }
        }
    }

    private void showMovieData(){
        tvTitle.setText(movie.getTitle());
        tvDescription.setText(movie.getDescription());
        tvYear.setText(movie.getYear());
        tvUserScore.setText(String.format("%s", movie.getUserScore()));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this).load( movie.getPhoto()).apply(options).into(imgPhoto);
        Log.i("photo", movie.getPhoto());

        GradientDrawable gradientDrawable = (GradientDrawable) tvUserScore.getBackground();
        int userScoreColor = getUserScoreColor(movie.getUserScore());
        gradientDrawable.setColor(userScoreColor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail " + movie.getTitle());
        }
    }

    private void showTvShowData(){
        tvTitle.setText(tvShow.getTitle());
        tvDescription.setText(tvShow.getDescription());
        tvYear.setText(tvShow.getYear());
        tvUserScore.setText(String.format("%s", tvShow.getUserScore()));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this).load(tvShow.getPhoto()).apply(options).into(imgPhoto);

        GradientDrawable gradientDrawable = (GradientDrawable) tvUserScore.getBackground();
        int userScoreColor = getUserScoreColor(tvShow.getUserScore());
        gradientDrawable.setColor(userScoreColor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail " + tvShow.getTitle());
        }
    }

    private int getUserScoreColor(double userScore) {
        int userSCoreColorResourceId;
        if (userScore < 7.0) {
            userSCoreColorResourceId = R.color.user_score1;
        } else {
            userSCoreColorResourceId = R.color.user_score2;
        }
        return ContextCompat.getColor(this, userSCoreColorResourceId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        this.menu = menu;
        setFavorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            if (movie != null) {
                if (isAlreadyLoved) {
                    SharedPreferencesUtils.setInsertState(this, String.valueOf(movie.getId()), false);
                    unSaveFavoriteMovie();
                    setFavorite();
                } else {
                    SharedPreferencesUtils.setInsertState(this, String.valueOf(movie.getId()), true);
                    saveFavoriteMovie();
                    setFavorite();
                }
            } else if (tvShow != null) {
                if (isAlreadyLoved) {
                    SharedPreferencesUtils.setInsertState(this, String.valueOf(tvShow.getId()), false);
                    unSaveFavoriteTvShow();
                    setFavorite();
                } else {
                    SharedPreferencesUtils.setInsertState(this, String.valueOf(tvShow.getId()), true);
                    saveTvShowFavorite();
                    setFavorite();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFavoriteMovie(){
        favoriteViewModel.InsertFavorite(movie);
        Toast.makeText(this, "Favorited" + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void unSaveFavoriteMovie() {
        favoriteViewModel.DeleteFavorite(movie);
        Toast.makeText(this, "Unfavorited" + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void saveTvShowFavorite(){
        favoriteViewModel.InsertFavorite(tvShow);
        Toast.makeText(this, "Favorited" + tvShow.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void unSaveFavoriteTvShow() {
        favoriteViewModel.DeleteFavorite(tvShow);
        Toast.makeText(this, "Unfavorited" + tvShow.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void setFavorite() {
        MenuItem favorite = menu.findItem(R.id.action_favorite);
        if (movie != null) {
            isAlreadyLoved = SharedPreferencesUtils.getInsertState(this, String.valueOf(movie.getId()));
        } else if (tvShow != null) {
            isAlreadyLoved = SharedPreferencesUtils.getInsertState(this, String.valueOf(tvShow.getId()));
        }

        if (isAlreadyLoved) {
            favorite.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24dp));
        } else {
            favorite.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (movie != null) {
            isAlreadyLoved = SharedPreferencesUtils.getInsertState(this, String.valueOf(movie.getId()));
        } else if (tvShow != null) {
            isAlreadyLoved = SharedPreferencesUtils.getInsertState(this, String.valueOf(tvShow.getId()));
    }
        Log.d("IsAlreadyLove", String.valueOf(isAlreadyLoved));
    }
}
