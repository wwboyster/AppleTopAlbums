package com.quarkworks.appletopalbums.activities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quarkworks.appletopalbums.adapters.AlbumsAdapter;
import com.quarkworks.appletopalbums.interfaces.ClickListener;
import com.quarkworks.appletopalbums.listeners.RecyclerTouchListener;
import com.quarkworks.appletopalbums.models.Album;
import com.quarkworks.appletopalbums.models.TopAlbumsSingleton;

public class AlbumList extends AppCompatActivity implements ClickListener {

    ProgressDialog cProgressDialog;
    private RecyclerView albumsRecyclerView;
    private AlbumsAdapter albumsAdapter;
    private List<Album> albumList = new ArrayList<Album>();

    private boolean checkNetworkConnection() {
        ConnectivityManager lManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo lNetwork = lManager.getActiveNetworkInfo();
        return lNetwork != null && lNetwork.isConnected();
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_album_list);

        albumsRecyclerView = findViewById(R.id.recycler_view);
        albumsRecyclerView.setHasFixedSize(true);
        albumsAdapter = new AlbumsAdapter(albumList);
        RecyclerView.LayoutManager albumLayoutManager = new LinearLayoutManager(getApplicationContext());
        albumsRecyclerView.setLayoutManager(albumLayoutManager);
        albumsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        albumsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        albumsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), albumsRecyclerView, this));
        albumsRecyclerView.setAdapter(albumsAdapter);

        if (checkNetworkConnection()) {
            cProgressDialog = new ProgressDialog(this);
            cProgressDialog.setMessage(getResources().getString(R.string.wait_message));
            cProgressDialog.setCancelable(false);
            cProgressDialog.show();

            startAlbumsDownload();
        } else if (TopAlbumsSingleton.getInstance().getAlbums() == null) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.no_connection_alert_title))
                    .setMessage(getResources().getString(R.string.no_connection_alert_message))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    private void startAlbumsDownload() {
        makeStringRequestWithOkHttp(getResources().getString(R.string.rss_feed_url));
    }

    private void makeStringRequestWithOkHttp(String pUrl) {
        OkHttpClient lClient = new OkHttpClient();
        okhttp3.Request lRequest = new okhttp3.Request.Builder().url(pUrl).build();

        lClient.newCall(lRequest).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call pCall, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call pCall, okhttp3.Response pResponse) throws IOException {
                final String lResult = pResponse.body().string();

                AlbumList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStringComplete(lResult);
                    }
                });
            }
        });
    }

    private void makeBitmapRequestWithOkHttp(Album pAlbum) {

        OkHttpClient lClient = new OkHttpClient();
        String pUrl = pAlbum.coverArtUrl;
        okhttp3.Request lRequest = new okhttp3.Request.Builder().url(pUrl).build();
        final Album ALBUM = pAlbum;

        lClient.newCall(lRequest).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call pCall, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call pCall, okhttp3.Response pResponse) throws IOException {
                InputStream lStream = pResponse.body().byteStream();
                final Bitmap IMAGE = BitmapFactory.decodeStream(lStream);
                AlbumList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ALBUM.downloadBitmapComplete(IMAGE);
                        setAlbumData(TopAlbumsSingleton.getInstance().getAlbums());
                    }
                });
            }
        });
    }

    public void downloadStringComplete(String pResult) {
        cProgressDialog.dismiss();
        JSONObject lObj;
        JSONArray lArr;
        try {
            lObj = new JSONObject(pResult);
            lObj = lObj.getJSONObject("feed");
            lArr = lObj.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        createAlbumList(lArr);
        setAlbumData(TopAlbumsSingleton.getInstance().getAlbums());
        for (Album a:albumList) {
            makeBitmapRequestWithOkHttp(a);
        }
    }

    private void createAlbumList(JSONArray pResults) {
        ArrayList<Album> lAlbums = new ArrayList<Album>();
        try {
            for (int i = 0; i < pResults.length(); i++) {
                lAlbums.add(Album.parseJSON(pResults.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        TopAlbumsSingleton.getInstance().setAlbums(lAlbums);
    }

    private void setAlbumData(List<Album> pAlbums) {
        albumList.clear();
        if (pAlbums != null) {
            albumList.addAll(pAlbums);
        }
        albumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View pView, int pPosition) {
        Intent lDetailIntent = new Intent(this, AlbumDetails.class);
        lDetailIntent.putExtra("albumPosition", pPosition);
        startActivity(lDetailIntent);
    }

    @Override
    public void onLongClick(View pView, int pPosition) {
        onClick(pView, pPosition);
    }
}
