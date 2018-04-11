/**
 * Created by Sönke on 02.03.2018.
 */

package com.soenkek.popularmoviesstage2.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3";

    private static final String PATH_MOVIE = "movie";
    private static final String PATH_SEARCH = "search";
    private static final String PATH_DISCOVER = "discover";
    private static final String PATH_FIND = "find";

    private static final String PATH_TRAILERS = "videos";
    private static final String PATH_REVIEWS = "reviews";

    public static final String SORT_BY_POP = "popular";
    public static final String SORT_BY_RAT = "top_rated";
    public static final String SORT_BY_FAV = "favorite";

    private static final String TMDB_API_KEY = "000";


    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch";

    private static final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi";
    private static final String PATH_THUMBNAIL_HQ = "hqdefault.jpg";
    private static final String PATH_THUMBNAIL_MAXRES = "maxresdefault.jpg";

    public static String httpRequest(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) return scanner.next();

        } finally {
            connection.disconnect();
        }
        return null;
    }

    public static URL fetchMovieList(String sortBy) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(PATH_MOVIE)
                .appendEncodedPath(sortBy)
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL fetchMovieDetails(String id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(PATH_MOVIE)
                .appendEncodedPath(id)
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL fetchMovieTrailers(String id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(PATH_MOVIE)
                .appendEncodedPath(id)
                .appendEncodedPath(PATH_TRAILERS)
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL fetchMovieReviews(String id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(PATH_MOVIE)
                .appendEncodedPath(id)
                .appendEncodedPath(PATH_REVIEWS)
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Uri buildImageUri(String imagePath, String imageSize) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(imagePath)
                .build();
    }

    public static Uri buildTrailerUri(String key) {
        return Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter("v", key)
                .build();
    }

    public static Uri buildThumbnailUri(String key) {
        return Uri.parse(THUMBNAIL_BASE_URL).buildUpon()
                .appendEncodedPath(key)
                .appendEncodedPath(PATH_THUMBNAIL_HQ)
                .build();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}