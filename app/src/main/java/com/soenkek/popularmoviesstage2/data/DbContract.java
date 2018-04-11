package com.soenkek.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sönke on 31.03.2018.
 */

public class DbContract {

    public static final String AUTHORITY = "com.soenkek.popularmoviesstage2";
    public static final String PATH_FAVORITES = "favorites";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Favorites implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TIME = "timeAdded";
    }
}
