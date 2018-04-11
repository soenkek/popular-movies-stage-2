package com.soenkek.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soenkek.popularmoviesstage2.R;
import com.soenkek.popularmoviesstage2.models.ReviewObject;

import java.util.ArrayList;

/**
 * Created by Sönke on 30.03.2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final Context mContext;

    private ArrayList<ReviewObject> mReviewObjects;

    public ReviewAdapter(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        ReviewObject reviewObject = mReviewObjects.get(position);
        holder.authorTv.setText(reviewObject.getAuthor());
        holder.contentTv.setText(reviewObject.getContent());

    }

    @Override
    public int getItemCount() {
        return mReviewObjects.size();
    }

    public void setReviewData(ArrayList<ReviewObject> mReviewObjects) {
        this.mReviewObjects = mReviewObjects;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTv;
        final TextView contentTv;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            authorTv = (TextView) itemView.findViewById(R.id.review_author_tv);
            contentTv = (TextView) itemView.findViewById(R.id.review_content_tv);
        }
    }
}