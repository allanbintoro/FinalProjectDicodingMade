package com.allan.moviecatalogueuiux.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.allan.moviecatalogueuiux.data.db.RoomClient;

public class MovieProvider extends ContentProvider {
    private static final int KEY_CODE_MOVIE = 1;

    private static final String AUTHORITY = "com.allan.moviecatalogueuiux.provider";
    private static final String TABLE_NAME = "movie";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME, KEY_CODE_MOVIE);
    }

    public MovieProvider() {
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return RoomClient.getInstance(getContext())
                .getMovieDatabase()
                .getMovieDao()
                .getMovieCursor();
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
