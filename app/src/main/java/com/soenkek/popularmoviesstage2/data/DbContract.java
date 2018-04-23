package com.soenkek.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sönke on 31.03.2018.
 */

public class DbContract {

    public static final String AUTHORITY = "com.soenkek.popularmoviesstage2";
    public static final String PATH_FAVORITES = "favorites";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Favorites implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_MAIN_POSTER_PATH = "posterPath";
        public static final String COLUMN_MOVIE_DETAIL_POSTER_PATH = "detailPosterPath";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE = "release";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
