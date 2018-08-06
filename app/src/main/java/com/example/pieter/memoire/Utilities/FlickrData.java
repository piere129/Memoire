package com.example.pieter.memoire.Utilities;

/**
 * Necessary class for manipulating Flickr data. This is the upper class
 */
public class FlickrData {
    public Photos photos;
    public String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}