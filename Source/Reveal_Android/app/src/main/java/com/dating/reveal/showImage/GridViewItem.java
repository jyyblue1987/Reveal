package com.dating.reveal.showImage;

import android.graphics.Bitmap;

/**
 * Created by JonIC on 2016-11-19.
 */
public class GridViewItem {

    private String path;
    private String filecount;
    private boolean isDirectory;
    private Bitmap image;


    public GridViewItem(String path, boolean isDirectory, Bitmap image) {
        this.path = path;
        this.isDirectory = isDirectory;
        this.image = image;
    }


    public String getPath() {
        return path;
    }


    public boolean isDirectory() {
        return isDirectory;
    }


    public Bitmap getImage() {
        return image;
    }

    public String getFilecount(){return filecount;}
}
