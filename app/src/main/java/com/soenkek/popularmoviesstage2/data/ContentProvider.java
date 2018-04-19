package com.soenkek.popularmoviesstage2.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Sönke on 02.04.2018.
 */

public class ContentProvider extends android.content.ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    Context mContext;
    DbHelper dbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_FAVORITES + "", FAVORITES);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        dbHelper = new DbHelper(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FAVORITES:
                cursor = database.query(DbContract.Favorites.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        DbContract.Favorites.COLUMN_TIMESTAMP);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                cursor = database.query(DbContract.Favorites.TABLE_NAME,
                        null,
                        DbContract.Favorites.COLUMN_MOVIE_ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        cursor.setNotificationUri(mContext.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                long id = database.insert(DbContract.Favorites.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    mContext.getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(DbContract.Favorites.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                int deletedRows = database.delete(DbContract.Favorites.TABLE_NAME, DbContract.Favorites.COLUMN_MOVIE_ID + "=?", new String[]{id});
                if (deletedRows > 0) {
                    mContext.getContentResolver().notifyChange(uri, null);
                }
                return deletedRows;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                break;
            case FAVORITES_WITH_ID:
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return 0;
    }
}
