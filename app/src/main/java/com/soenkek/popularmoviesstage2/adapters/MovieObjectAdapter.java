/**
 * Created by Sönke on 11.03.2018.
 */

package com.soenkek.popularmoviesstage2.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.soenkek.popularmoviesstage2.R;
import com.soenkek.popularmoviesstage2.models.MovieObject;
import com.soenkek.popularmoviesstage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieObjectAdapter extends ArrayAdapter<MovieObject> {

    private final String imgSize;
    private final boolean sortedByFav;

    public MovieObjectAdapter(Activity context, List<MovieObject> movieObjects, String imgSize, boolean sortedByFav) {
        super(context, 0, movieObjects);
        this.imgSize = imgSize;
        this.sortedByFav = sortedByFav;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        MovieObject movieObject = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
        ImageView imageView = view.findViewById(R.id.item_movie_image);
        String imgPath;
        if (sortedByFav) {
            imgPath = movieObject.getDetailPosterPath();
        } else {
            imgPath = movieObject.getPosterPath();
        }
        Picasso.with(getContext())
                .load(NetworkUtils.buildImageUri(imgPath, imgSize))
                .into(imageView);
        return view;
    }
}
