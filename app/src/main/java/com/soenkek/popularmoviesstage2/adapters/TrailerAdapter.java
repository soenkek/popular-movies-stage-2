package com.soenkek.popularmoviesstage2.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soenkek.popularmoviesstage2.R;
import com.soenkek.popularmoviesstage2.models.TrailerObject;
import com.soenkek.popularmoviesstage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sönke on 29.03.2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final Context mContext;

    final private TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Uri uri);
    }

    private ArrayList<TrailerObject> mTrailerObjects;

    public TrailerAdapter(@NonNull Context mContext, TrailerAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        TrailerObject trailerObject = mTrailerObjects.get(position);
        Picasso.with(mContext)
                .load(NetworkUtils.buildThumbnailUri(trailerObject.getKey()))
                .into(holder.thumbnailIv);
    }

    @Override
    public int getItemCount() {
        return mTrailerObjects.size();
    }

    public void setTrailerData(ArrayList<TrailerObject> mTrailerObjects) {
        this.mTrailerObjects = mTrailerObjects;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView thumbnailIv;
        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);

            thumbnailIv = (ImageView) itemView.findViewById(R.id.trailer_iv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TrailerObject trailerObject = mTrailerObjects.get(position);
            Uri uri = NetworkUtils.buildTrailerUri(trailerObject.getKey());
            mClickHandler.onClick(uri);
        }
    }
}
