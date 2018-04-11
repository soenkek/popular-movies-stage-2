/**
 * Created by Sönke on 03.03.2018.
 */

package com.soenkek.popularmoviesstage2.utilities;

import com.soenkek.popularmoviesstage2.models.MovieObject;
import com.soenkek.popularmoviesstage2.models.ReviewObject;
import com.soenkek.popularmoviesstage2.models.TrailerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonUtils {

    public static ArrayList<MovieObject> parseListJson(String json) throws JSONException, ParseException {
        if (json == null || json.equals("")) throw new JSONException("Empty json");
        ArrayList<MovieObject> movieObjects = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            String id = result.getString("id");
            String poster_path = result.getString("poster_path");
            movieObjects.add(i, new MovieObject(id, null, null, poster_path, null, 0, null));
        }
        return movieObjects;
    }

    public static MovieObject parseDetailsJson(String json) throws JSONException, ParseException {
        if (json == null || json.equals("")) throw new JSONException("Empty json");
        JSONObject jsonObject = new JSONObject(json);
        String[] data = new String[6];
        String title = jsonObject.getString("title");
        String original_title = jsonObject.getString("original_title");
        String backdrop_path = jsonObject.getString("backdrop_path");
        String synopsis = jsonObject.getString("overview");
        float vote_average = Float.parseFloat(jsonObject.getString("vote_average"));
        String releaseDate = formatDate(jsonObject.getString("release_date"));
        return new MovieObject(null, title, original_title, backdrop_path, synopsis, vote_average, releaseDate);
    }

    public static ArrayList<TrailerObject> parseTrailersJson(String json) throws JSONException, ParseException {
        if (json == null || json.equals("")) throw new JSONException("Empty json");
        ArrayList<TrailerObject> trailerObjects = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            String name = result.getString("name");
            String type = result.getString("type");
            String key = result.getString("key");
            trailerObjects.add(i, new TrailerObject(name, type, key));
        }
        return trailerObjects;
    }

    public static ArrayList<ReviewObject> parseReviewsJson(String json) throws JSONException, ParseException {
        if (json == null || json.equals("")) throw new JSONException("Empty json");
        ArrayList<ReviewObject> reviewObjects = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            String author = result.getString("author");
            String content = result.getString("content");
            reviewObjects.add(i, new ReviewObject(author, content));
        }
        return reviewObjects;
    }

    private static String formatDate(String unformattedDate) throws ParseException {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd.mm.yyyy");
        Date date = oldFormat.parse(unformattedDate);
        return newFormat.format(date);
    }
}