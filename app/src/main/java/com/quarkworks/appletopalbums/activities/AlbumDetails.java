package com.quarkworks.appletopalbums.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quarkworks.appletopalbums.adapters.GenresAdapter;
import com.quarkworks.appletopalbums.interfaces.ClickListener;
import com.quarkworks.appletopalbums.listeners.RecyclerTouchListener;
import com.quarkworks.appletopalbums.models.Album;
import com.quarkworks.appletopalbums.models.Genre;
import com.quarkworks.appletopalbums.models.TopAlbumsSingleton;
import com.quarkworks.appletopalbums.views.LinkView;

public class AlbumDetails extends AppCompatActivity implements ClickListener {

    private Album focus;
    private RecyclerView genresRecyclerView;
    private GenresAdapter genresAdapter;
    private List<Genre> genreList = new ArrayList<Genre>();

    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        Intent startIntent = getIntent();
        int position = startIntent.getIntExtra("albumPosition", 0);
        focus = TopAlbumsSingleton.getInstance().getAlbums().get(position);
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_album_details);

        genresRecyclerView = findViewById(R.id.genre);
        genresRecyclerView.setHasFixedSize(true);
        genresAdapter = new GenresAdapter(genreList);
        RecyclerView.LayoutManager genreLayoutManager = new LinearLayoutManager(getApplicationContext());
        genresRecyclerView.setLayoutManager(genreLayoutManager);
        genresRecyclerView.setItemAnimator(new DefaultItemAnimator());
        genresRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), genresRecyclerView, this));
        genresRecyclerView.setAdapter(genresAdapter);

        setViews();
    }

    private void setViews() {
        View lView;
        if (focus.getCoverArt() != null) {
            lView = findViewById(R.id.coverArt);
            ((ImageView) lView).setImageBitmap(focus.getCoverArt());
        }
        lView = findViewById(R.id.title);
        ((LinkView) lView).setText(focus.name);
        ((LinkView) lView).setUrl(focus.appleUrl);
        lView = findViewById(R.id.artist);
        ((LinkView) lView).setText(focus.artistName);
        ((LinkView) lView).setUrl(focus.artistUrl);

        lView = findViewById(R.id.releaseDate);
        SimpleDateFormat lSdf = new SimpleDateFormat("MMMM dd, yyyy");
        ((TextView) lView).setText(lSdf.format(focus.releaseDate.getTime()));
        lView = findViewById(R.id.copyright);
        ((TextView) lView).setText(focus.copyright);

        genreList.clear();
        for (Genre g : focus.genres) {
            genreList.add(g);
        }
        genresAdapter.notifyDataSetChanged();
    }

    public void onLinkClick(View pView) {
        if (pView instanceof LinkView) {
            linkToBrowser(((LinkView) pView).getUrl());
        }
    }

    private void linkToBrowser(String pUrl) {
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pUrl));
        startActivity(linkIntent);
    }

    @Override
    public void onClick(View pView, int pPosition) {
        linkToBrowser(focus.genres[pPosition].url);
    }

    @Override
    public void onLongClick(View pView, int pPosition) {
        onClick(pView, pPosition);
    }
}
