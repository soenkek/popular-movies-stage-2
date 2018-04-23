package com.soenkek.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soenkek.popularmoviesstage2.data.DbContract.Favorites;

/**
 * Created by Sönke on 31.03.2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popmovies.db";
    private static final int DATABASE_VERSION = 7;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                Favorites._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Favorites.COLUMN_MOVIE_ID   + " TEXT NOT NULL UNIQUE, " +
                Favorites.COLUMN_MOVIE_TITLE + " TEXT, " +
                Favorites.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT, " +
                Favorites.COLUMN_MOVIE_MAIN_POSTER_PATH + " TEXT NOT NULL, " +
                Favorites.COLUMN_MOVIE_DETAIL_POSTER_PATH + " TEXT, " +
                Favorites.COLUMN_MOVIE_SYNOPSIS + " TEXT, " +
                Favorites.COLUMN_MOVIE_RATING + " TEXT, " +
                Favorites.COLUMN_MOVIE_RELEASE + " TEXT, " +
                Favorites.COLUMN_TIMESTAMP + " COLUMN_TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Favorites.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
