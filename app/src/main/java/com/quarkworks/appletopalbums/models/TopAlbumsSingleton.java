package com.quarkworks.appletopalbums.models;

import java.util.List;

public class TopAlbumsSingleton {
    private static final TopAlbumsSingleton ourInstance;
    private List<Album> albums;

    static { ourInstance = new TopAlbumsSingleton(); }
    private TopAlbumsSingleton() {
        albums = null;
    }

    public void setAlbums(List<Album> pAlbums){
        albums = pAlbums;
    }
    public List<Album> getAlbums(){
        return albums;
    }

    public static TopAlbumsSingleton getInstance() {
        return ourInstance;
    }

}
