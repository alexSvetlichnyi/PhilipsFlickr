package com.example.asvetlichniy.philipsflickr.service;

import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Task for getting photo info from server.
 */
public class GetPhotoInfoTask extends SpiceRequest<Photo> {

    private PhotosInterface photosInterface;
    private String id;
    private String secret;

    public GetPhotoInfoTask(PhotosInterface photosInterface, String id, String secret) {
        super(Photo.class);
        this.photosInterface = photosInterface;
        this.secret = secret;
        this.id = id;
    }

    @Override
    public Photo loadDataFromNetwork() throws Exception {
        return photosInterface.getInfo(id, secret);
    }
}