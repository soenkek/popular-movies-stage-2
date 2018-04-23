/**
 * Created by Sönke on 01.03.2018.
 */

package com.soenkek.popularmoviesstage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.soenkek.popularmoviesstage2.adapters.ReviewAdapter;
import com.soenkek.popularmoviesstage2.adapters.TrailerAdapter;
import com.soenkek.popularmoviesstage2.data.DbContract;
import com.soenkek.popularmoviesstage2.models.MovieObject;
import com.soenkek.popularmoviesstage2.models.ReviewObject;
import com.soenkek.popularmoviesstage2.models.TrailerObject;
import com.soenkek.popularmoviesstage2.utilities.JsonUtils;
import com.soenkek.popularmoviesstage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.detail_fav_iv) ImageView btnFav;
    @BindView(R.id.detail_poster_iv) ImageView posterView;
    @BindView(R.id.detail_rating_rb) RatingBar ratingImageView;
    @BindView(R.id.detail_rating_tv) TextView ratingTextView;
    @BindView(R.id.detail_title_tv) TextView titleView;
    @BindView(R.id.detail_original_title_label_tv) TextView originalTitleLabelView;
    @BindView(R.id.detail_original_title_tv) TextView originalTitleView;
    @BindView(R.id.detail_release_tv) TextView releaseView;
    @BindView(R.id.detail_synopsis_tv) TextView synopsisView;

    private MovieObject mMovieObject;
    private ArrayList<TrailerObject> mTrailerObjects;
    private ArrayList<ReviewObject> mReviewObjects;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    @BindView(R.id.detail_recyclerview_trailers) RecyclerView mRecyclerviewTrailers;
    @BindView(R.id.detail_recyclerview_reviews) RecyclerView mRecyclerviewReviews;

    private boolean isFav = false;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        LinearLayoutManager layoutManagerTrailer =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerviewTrailers.setLayoutManager(layoutManagerTrailer);
        mRecyclerviewTrailers.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this, this);

        LinearLayoutManager layoutManagerReview =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerviewReviews.setLayoutManager(layoutManagerReview);
        mRecyclerviewReviews.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(this);

        Intent callingIntent = getIntent();
        if (callingIntent == null || !callingIntent.hasExtra(MainActivity.INTENT_EXTRA_LIST_INDEX)) {
            Toast.makeText(this, "Error Loading Movie Details", Toast.LENGTH_LONG).show();
            finish();
        }
        mId = callingIntent.getStringExtra(MainActivity.INTENT_EXTRA_LIST_INDEX);
        new FetchMovieTask().execute(mId);
    }

    private void populateViews() {
        if (mMovieObject == null) {
            return;
        }
        collapsingToolbarLayout.setTitle(mMovieObject.getTitle());
        Picasso.with(this)
                .load(NetworkUtils.buildImageUri(mMovieObject.getDetailPosterPath(), getString(R.string.image_size_detail)))
                .into(posterView);
        ratingImageView.setRating(mMovieObject.getRating()/2);
        String rating = Math.round(mMovieObject.getRating()*10) + "%";
        ratingTextView.setText(rating);
        titleView.setText(mMovieObject.getTitle());
        if (mMovieObject.getTitle().equals(mMovieObject.getOriginalTitle())) {
            originalTitleLabelView.setVisibility(View.INVISIBLE);
            originalTitleLabelView.setHeight(0);
            originalTitleView.setVisibility(View.INVISIBLE);
            originalTitleView.setHeight(0);
        } else {
            originalTitleView.setText(mMovieObject.getOriginalTitle());
        }
        releaseView.setText(mMovieObject.getRelease());
        synopsisView.setText(mMovieObject.getSynopsis());
        setFavButton();
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFav) {
                    ContentValues contentValues = new ContentValues();
                    String id = mMovieObject.getId();
                    String title = mMovieObject.getTitle();
                    String originalTitle = mMovieObject.getOriginalTitle();
                    String posterPath = mMovieObject.getPosterPath();
                    String detailPosterPath = mMovieObject.getDetailPosterPath();
                    String synopsis = mMovieObject.getSynopsis();
                    Float rating = mMovieObject.getRating();
                    String release = mMovieObject.getRelease();
                    Log.d("++++++++++++++++", "Save to db: " + id + title + originalTitle + posterPath + detailPosterPath + synopsis + rating + release);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_ID, id);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_TITLE, title);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_ORIGINAL_TITLE, originalTitle);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_MAIN_POSTER_PATH, posterPath);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_DETAIL_POSTER_PATH, detailPosterPath);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_SYNOPSIS, synopsis);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_RATING, rating);
                    contentValues.put(DbContract.Favorites.COLUMN_MOVIE_RELEASE, release);
                    getContentResolver().insert(DbContract.Favorites.CONTENT_URI, contentValues);
                    isFav = true;
                } else {
                    Uri uri = DbContract.Favorites.CONTENT_URI.buildUpon()
                            .appendPath(mMovieObject.getId()).build();
                    getContentResolver().delete(uri, null, null);
                    isFav = false;
                }
                setFavButton();
            }
        });
        if (mTrailerObjects != null) {
            mTrailerAdapter.setTrailerData(mTrailerObjects);
            mRecyclerviewTrailers.setAdapter(mTrailerAdapter);
        }
        if (mReviewObjects != null) {
            mReviewAdapter.setReviewData(mReviewObjects);
            mRecyclerviewReviews.setAdapter(mReviewAdapter);
        }
    }

    private void setFavButton() {
        if (isFav)btnFav.setImageResource(R.drawable.ic_favorite_white_36dp);
        else btnFav.setImageResource(R.drawable.ic_favorite_border_white_36dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    class FetchMovieTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... id) {
            URL detailsUrl = NetworkUtils.fetchMovieDetails(id[0]);
            URL trailersUrl = NetworkUtils.fetchMovieTrailers(id[0]);
            URL reviewsURL = NetworkUtils.fetchMovieReviews(id[0]);
            String jsonDetails = null;
            String jsonTrailers = null;
            String jsonReviews = null;
            Cursor cursor = getContentResolver().query(
                    DbContract.Favorites.CONTENT_URI.buildUpon().appendPath(id[0]).build(),
                    null,
                    null,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                isFav = true;
                mMovieObject = new MovieObject(
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_MAIN_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_DETAIL_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_SYNOPSIS)),
                        cursor.getFloat(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_RATING)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Favorites.COLUMN_MOVIE_RELEASE))
                );
                cursor.close();
                Log.d("++++++++++++++++", "Load from db: " +
                        mMovieObject.getId() +
                        mMovieObject.getTitle() +
                        mMovieObject.getOriginalTitle() +
                        mMovieObject.getPosterPath() +
                        mMovieObject.getDetailPosterPath() +
                        mMovieObject.getSynopsis() +
                        mMovieObject.getRating() +
                        mMovieObject.getRelease()
                );
            } else {
                isFav = false;
                try {
                    jsonDetails = NetworkUtils.httpRequest(detailsUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mMovieObject = JsonUtils.parseDetailsJson(jsonDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            try {
                jsonTrailers = NetworkUtils.httpRequest(trailersUrl);
                jsonReviews = NetworkUtils.httpRequest(reviewsURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mTrailerObjects = JsonUtils.parseTrailersJson(jsonTrailers);
                mReviewObjects = JsonUtils.parseReviewsJson(jsonReviews);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            populateViews();
        }
    }
}