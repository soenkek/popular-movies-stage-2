/**
 * Created by Sönke on 10.03.2018.
 */


package com.soenkek.popularmoviesstage2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieObject implements Parcelable{

    private final String id;
    private final String title;
    private final String originalTitle;
    private final String posterPath;
    private final String synopsis;
    private final float rating;
    private final String release;

    public MovieObject(String id, String title, String originalTitle, String posterPath, String synopsis, float rating, String release) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.release = release;
    }

    private MovieObject(Parcel in) {
        id = in.readString();
        title = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        synopsis = in.readString();
        rating = in.readFloat();
        release = in.readString();
    }

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };

    public String getId() {return id;}

    public String getTitle() {return title;}

    public String getOriginalTitle() {return originalTitle;}

    public String getPosterPath() {return posterPath;}

    public String getSynopsis() {return synopsis;}

    public float getRating() {return rating;}

    public String getRelease() {return release;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(synopsis);
        parcel.writeFloat(rating);
        parcel.writeString(release);
    }
}