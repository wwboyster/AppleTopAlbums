package com.quarkworks.appletopalbums.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quarkworks.appletopalbums.activities.R;
import com.quarkworks.appletopalbums.models.Album;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private List<Album> albumList;

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist;
        public ImageView coverArt;

        public AlbumViewHolder(View pView) {
            super(pView);
            title = pView.findViewById(R.id.title);
            artist = pView.findViewById(R.id.artist);
            coverArt = pView.findViewById(R.id.coverArt);;
        }
    }

    public AlbumsAdapter(List<Album> pAlbums) {
        albumList = pAlbums;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View lItemView = LayoutInflater.from(pParent.getContext())
                        .inflate(R.layout.album_list_row, pParent, false);
        return new AlbumViewHolder(lItemView);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder pHolder, int pPosition) {
        Album lAlbum = albumList.get(pPosition);
        pHolder.title.setText(lAlbum.name);
        pHolder.artist.setText(lAlbum.artistName);

        if (lAlbum.getCoverArt() != null) {
            pHolder.coverArt.setImageBitmap(lAlbum.getCoverArt());
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
