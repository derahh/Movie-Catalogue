package id.co.derahh.moviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import id.co.derahh.moviecatalogue.database.model.movie.Movie;
import id.co.derahh.moviecatalogue.R;
import id.co.derahh.moviecatalogue.activity.DetailActivity;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private Movie movie;

    public List<Movie> getListData() {
        return listData;
    }

    public void setListData(List<Movie> listData) {
        this.listData.clear();
        this.listData = listData;
        notifyDataSetChanged();
    }

    private List<Movie> listData = new ArrayList<>();

    public MovieAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvTitle.setText(getListData().get(i).getTitle());
        viewHolder.tvDescription.setText(getListData().get(i).getDescription());
        Glide.with(mContext).load(getListData().get(i).getPhoto()).into(viewHolder.imgPhoto);
        Log.i("list photo", getListData().get(i).getPhoto());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie = getListData().get(i);
                showSelectedMovie(movie);
                Log.i("extra_poster", movie.getPosterPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListData().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle, tvDescription;
        final ImageView imgPhoto;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }

    private void showSelectedMovie(Movie movie){
        Intent detailMovieIntent = new Intent(mContext, DetailActivity.class);
        detailMovieIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        mContext.startActivity(detailMovieIntent);
    }
}
