/**
 * Created by Sönke on 01.03.2018.
 */

package com.soenkek.popularmoviesstage2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soenkek.popularmoviesstage2.adapters.MovieObjectAdapter;
import com.soenkek.popularmoviesstage2.data.DbContract;
import com.soenkek.popularmoviesstage2.models.MovieObject;
import com.soenkek.popularmoviesstage2.utilities.JsonUtils;
import com.soenkek.popularmoviesstage2.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.settings_bar) View settings_bar;
    @BindView(R.id.settings_item_pop) View settingsItemPop;
    private ImageView settingsPopIv;
    private TextView settingsPopTv;
    @BindView(R.id.settings_item_rat) View settingsItemRat;
    private ImageView settingsRatIv;
    private TextView settingsRatTv;
    @BindView(R.id.settings_item_fav) View settingsItemFav;
    private ImageView settingsFavIv;
    private TextView settingsFavTv;

    @BindView(R.id.root_layout) View rootLayout;
    @BindView(R.id.main_grid_view) GridView gridView;
    @BindView(R.id.refresh_view) View refreshView;

    private String sortBy;
    private ArrayList<MovieObject> movieObjects;

    private static final String SAVED_INSTANCE_MOVIES_KEY = "movie-objects";
    public static final String INTENT_EXTRA_LIST_INDEX = "list-index";

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        mToast = Toast.makeText(this, R.string.toast_connection_failed, Toast.LENGTH_LONG);

        ButterKnife.bind(this);

        settingsPopIv = settingsItemPop.findViewById(R.id.sort_item_iv);
        settingsPopIv.setImageResource(R.drawable.set_ic_pop_white_24dp);
        settingsRatIv = settingsItemRat.findViewById(R.id.sort_item_iv);
        settingsRatIv.setImageResource(R.drawable.set_ic_rat_white_24dp);
        settingsFavIv = settingsItemFav.findViewById(R.id.sort_item_iv);
        settingsFavIv.setImageResource(R.drawable.set_ic_fav_white_24dp);
        settingsPopTv = settingsItemPop.findViewById(R.id.sort_item_tv);
        settingsPopTv.setText(R.string.settings_pop_label);
        settingsRatTv = settingsItemRat.findViewById(R.id.sort_item_tv);
        settingsRatTv.setText(R.string.settings_rat_label);
        settingsFavTv = settingsItemFav.findViewById(R.id.sort_item_tv);
        settingsFavTv.setText(R.string.settings_fav_label);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_MOVIES_KEY)) {
            sortBy = savedInstanceState.getString("sortBy");
            movieObjects = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIES_KEY);
            settingsSwitched();
            if (movieObjects != null) {
            populateGridLayout();
            } else {
                updateData();
            }
        } else {
            sortBy = NetworkUtils.SORT_BY_POP;
            settingsSwitched();
            updateData();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra(INTENT_EXTRA_LIST_INDEX, movieObjects.get(i).getId());
                startActivity(intent);
            }
        });

        settingsItemPop.setOnClickListener(this);
        settingsItemRat.setOnClickListener(this);
        settingsItemFav.setOnClickListener(this);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == settingsItemRat) {
            sortBy = NetworkUtils.SORT_BY_RAT;
        } else if (view == settingsItemFav) {
            sortBy = NetworkUtils.SORT_BY_FAV;
        } else {
            sortBy = NetworkUtils.SORT_BY_POP;
        }
        settingsSwitched();
        updateData();
    }

    private void settingsSwitched() {
        if (NetworkUtils.SORT_BY_RAT.equals(sortBy)) {
            setSettingsAttributes(settingsItemPop, settingsPopIv, settingsPopTv, false);
            setSettingsAttributes(settingsItemRat, settingsRatIv, settingsRatTv, true);
            setSettingsAttributes(settingsItemFav, settingsFavIv, settingsFavTv, false);
            gridView.setNumColumns(GridView.AUTO_FIT);
        } else if (NetworkUtils.SORT_BY_FAV.equals(sortBy)) {
            setSettingsAttributes(settingsItemPop, settingsPopIv, settingsPopTv, false);
            setSettingsAttributes(settingsItemRat, settingsRatIv, settingsRatTv, false);
            setSettingsAttributes(settingsItemFav, settingsFavIv, settingsFavTv, true);
            gridView.setNumColumns(1);
        } else {
            sortBy = NetworkUtils.SORT_BY_POP;
            setSettingsAttributes(settingsItemPop, settingsPopIv, settingsPopTv, true);
            setSettingsAttributes(settingsItemRat, settingsRatIv, settingsRatTv, false);
            setSettingsAttributes(settingsItemFav, settingsFavIv, settingsFavTv, false);
            gridView.setNumColumns(GridView.AUTO_FIT);
        }
    }

    private void setSettingsAttributes(View v, ImageView iv, TextView tv, boolean isSelected) {
        if (isSelected) {
            v.setPadding(12, 10, 12, 0);
            iv.setColorFilter(getResources().getColor(R.color.colorAccent));
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        } else {
            v.setPadding(12, 12, 12, 0);
            iv.setColorFilter(getResources().getColor(R.color.white));
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }
    }

    private void updateData() {
        if (!sortBy.equals(NetworkUtils.SORT_BY_FAV)) {
            rootLayout.setBackgroundColor(getResources().getColor(R.color.white));
            if (NetworkUtils.isConnected(this)) {
                new FetchDataTask().execute(sortBy);
                if (mToast != null) {
                    mToast.cancel();
                }
            } else {
                gridView.setVisibility(View.GONE);
                refreshView.setVisibility(View.VISIBLE);
                movieObjects = null;
                mToast.show();
            }
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            rootLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            Uri uri = DbContract.Favorites.CONTENT_URI;
            Cursor cursor = getContentResolver().query(uri, null, null, null, DbContract.Favorites.COLUMN_TIMESTAMP + " DESC");
            movieObjects = new ArrayList<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                MovieObject movieObject = new MovieObject(
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_MAIN_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_DETAIL_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_SYNOPSIS)),
                        cursor.getFloat(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_RATING)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_RELEASE))
                );
                movieObjects.add(movieObject);
                cursor.moveToNext();
            }
            populateGridLayout();
        }
    }

    private void populateGridLayout() {
        refreshView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        String imgSize;
        boolean sortByFav;
        if (sortBy.equals(NetworkUtils.SORT_BY_FAV)) {
            imgSize = getString(R.string.image_size_detail);
            sortByFav = true;
        }
        else {
            imgSize = getString(R.string.image_size_grid);
            sortByFav = false;
        }
        if (movieObjects != null) {
            MovieObjectAdapter movieObjectAdapter = new MovieObjectAdapter(this, movieObjects, imgSize, sortByFav);
            gridView.setAdapter(movieObjectAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sortBy", sortBy);
        outState.putParcelableArrayList(SAVED_INSTANCE_MOVIES_KEY, movieObjects);
        super.onSaveInstanceState(outState);
    }

    class FetchDataTask extends AsyncTask<String, Void, ArrayList<MovieObject>> {
        @Override
        protected ArrayList<MovieObject> doInBackground(String... sortBy) {
            URL url = NetworkUtils.fetchMovieList(sortBy[0]);
            String json = null;
            try {
                json = NetworkUtils.httpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<MovieObject> movieObjectArrayList = null;
            try {
                movieObjectArrayList = JsonUtils.parseListJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
                cancel(true);
            }
            return movieObjectArrayList;
        }
        @Override
        protected void onPostExecute(ArrayList<MovieObject> data) {
            movieObjects = data;
            populateGridLayout();
        }
    }
}