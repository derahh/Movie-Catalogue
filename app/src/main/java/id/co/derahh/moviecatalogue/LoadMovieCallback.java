package id.co.derahh.moviecatalogue;

import android.database.Cursor;

public interface LoadMovieCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}

