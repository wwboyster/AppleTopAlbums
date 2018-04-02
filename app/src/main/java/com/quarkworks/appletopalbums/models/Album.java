package com.quarkworks.appletopalbums.models;

import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

public class Album {
    public final String name;
    public final String artistName;
    public final String id;
    public final Calendar releaseDate;
    public final String copyright;
    public final String artistId;
    public final String artistUrl;
    public final String coverArtUrl;
    public final String appleUrl;
    public final Genre[] genres;
    private Bitmap coverArt;

    public static Album parseJSON(JSONObject pJson) throws JSONException, ParseException {
        String lArtistName, lId, lName, lCopyright, lArtistId, lArtistUrl, lCoverArtUrl, lAppleUrl;
        Genre[] lGenres;
        Calendar lReleaseDate;

        lArtistName = pJson.getString("artistName");
        lId = pJson.getString("id");
        lName = pJson.getString("name");
        lCopyright = pJson.getString("copyright");
        lArtistId = pJson.getString("artistId");
        lArtistUrl = pJson.getString("artistUrl");
        lCoverArtUrl = pJson.getString("artworkUrl100");
        lAppleUrl = pJson.getString("url");

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat lSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lDate = lSdf.parse(pJson.getString("releaseDate"));
        lReleaseDate = Calendar.getInstance();
        lReleaseDate.setTime(lDate);

        JSONArray lArr = pJson.getJSONArray("genres");
        lGenres = new Genre[lArr.length()];
        for (int i = 0; i < lGenres.length; i++) {
            String lGenreName = (lArr.getJSONObject(i)).getString("name");
            String lGenreUrl = (lArr.getJSONObject(i)).getString("url");
            lGenres[i] = new Genre(lGenreName, lGenreUrl);
        }

        return new Album(lArtistName, lId, lReleaseDate, lName,
                         lCopyright, lArtistId, lArtistUrl, lCoverArtUrl,
                         lGenres, lAppleUrl, null);
    }

    public Album(String pArtistName, String pId, Calendar pReleaseDate, String pName,
                 String pCopyright, String pArtistId, String pArtistUrl, String pCoverArtUrl,
                 Genre[] pGenres, String pAppleUrl, Bitmap pCoverArt) {
        artistName = pArtistName;
        id = pId;
        releaseDate = pReleaseDate;
        name = pName;
        copyright = pCopyright;
        artistId = pArtistId;
        artistUrl = pArtistUrl;
        coverArtUrl = pCoverArtUrl;
        genres = pGenres;
        appleUrl = pAppleUrl;
        coverArt = pCoverArt;
    }

    public void downloadBitmapComplete(Bitmap pResult) {
        setCoverArt(pResult);
    }

    private void setCoverArt(Bitmap pCoverArt) {
        if (coverArt == null) {
            coverArt = pCoverArt;
        }
    }

    public Bitmap getCoverArt() {
        return coverArt;
    }
}
