package com.quarkworks.appletopalbums.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quarkworks.appletopalbums.activities.R;
import com.quarkworks.appletopalbums.models.Genre;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenreViewHolder> {

    private List<Genre> genreList;

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public GenreViewHolder(View pView) {
            super(pView);
            name = pView.findViewById(R.id.genre);
        }
    }

    public GenresAdapter(List<Genre> pGenre) {
        genreList = pGenre;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View lItemView = LayoutInflater.from(pParent.getContext())
                .inflate(R.layout.genre_list_row, pParent, false);
        return new GenreViewHolder(lItemView);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder pHolder, int pPosition) {
        Genre lGenre = genreList.get(pPosition);
        pHolder.name.setText(lGenre.name);
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}
