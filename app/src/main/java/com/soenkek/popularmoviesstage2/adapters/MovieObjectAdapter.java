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
import android.widget.TextView;

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
    public View getView(int position, View rootView, @NonNull ViewGroup parent) {
        MovieObject movieObject = getItem(position);
        if (rootView == null) {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
        ImageView imageView = rootView.findViewById(R.id.item_movie_image);
        TextView textView = rootView.findViewById(R.id.item_movie_text);
        String imgPath;
        if (sortedByFav) {
            imgPath = movieObject.getDetailPosterPath();
            textView.setVisibility(View.VISIBLE);
            textView.setText(movieObject.getTitle());
            imageView.setAlpha(0.6F);
        } else {
            imgPath = movieObject.getPosterPath();
            textView.setVisibility(View.GONE);
            imageView.setAlpha(1.0F);
        }
        Picasso.with(getContext())
                .load(NetworkUtils.buildImageUri(imgPath, imgSize))
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_photo_250dp))
                .error(getContext().getResources().getDrawable(R.drawable.ic_photo_250dp))
                .into(imageView);
        return rootView;
    }
}
