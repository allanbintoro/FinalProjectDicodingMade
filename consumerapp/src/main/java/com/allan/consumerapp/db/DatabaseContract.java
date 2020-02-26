package com.allan.consumerapp.db;

import android.net.Uri;

public class DatabaseContract {

    private static final String AUTHORITY = "com.allan.moviecatalogueuiux.provider";
    private static final String SCHEME = "content";

    public static final class AppDatabase {
        static final String TABLE_NAME = "movie";

        static final String MOVIE_TITLE = "original_title";
        static final String MOVIE_ORIGINAL_NAME = "original_name";
        static final String MOVIE_BACKDROP_IMAGE = "backdrop_path";
        static final String MOVIE_TYPE = "type";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
