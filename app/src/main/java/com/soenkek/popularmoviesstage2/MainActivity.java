/**
 * Created by Sönke on 01.03.2018.
 */

package com.soenkek.popularmoviesstage2;

import android.content.Intent;
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
import com.soenkek.popularmoviesstage2.models.MovieObject;
import com.soenkek.popularmoviesstage2.utilities.JsonUtils;
import com.soenkek.popularmoviesstage2.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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

    @BindView(R.id.main_grid_view) GridView gridView;

    private String sortBy;
    private ArrayList<MovieObject> movieObjects;

    private static final String SAVED_INSTANCE_MOVIES_KEY = "movie-objects";
    public static final String INTENT_EXTRA_MOVIE_KEY = "movie-object";
    public static final String INTENT_EXTRA_LIST_INDEX = "list-index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        ButterKnife.bind(this);

        settingsPopIv = (ImageView) settingsItemPop.findViewById(R.id.sort_item_iv);
        settingsPopIv.setImageResource(R.drawable.set_ic_pop_white_24dp);
        settingsRatIv = (ImageView) settingsItemRat.findViewById(R.id.sort_item_iv);
        settingsRatIv.setImageResource(R.drawable.set_ic_rat_white_24dp);
        settingsFavIv = (ImageView) settingsItemFav.findViewById(R.id.sort_item_iv);
        settingsFavIv.setImageResource(R.drawable.set_ic_fav_white_24dp);
        settingsPopTv = (TextView) settingsItemPop.findViewById(R.id.sort_item_tv);
        settingsPopTv.setText(R.string.settings_pop_label);
        settingsRatTv = (TextView) settingsItemRat.findViewById(R.id.sort_item_tv);
        settingsRatTv.setText(R.string.settings_rat_label);
        settingsFavTv = (TextView) settingsItemFav.findViewById(R.id.sort_item_tv);
        settingsFavTv.setText(R.string.settings_fav_label);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_MOVIES_KEY)) {
            sortBy = savedInstanceState.getString("sortBy");
            movieObjects = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIES_KEY);
            settingsSwitched();
            populateGridLayout();
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
        if (NetworkUtils.isConnected(this)) {
            new FetchDataTask().execute(sortBy);
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void populateGridLayout() {
        MovieObjectAdapter movieObjectAdapter = new MovieObjectAdapter(this, movieObjects);
        gridView.setAdapter(movieObjectAdapter);
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
            } catch (ParseException e) {
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