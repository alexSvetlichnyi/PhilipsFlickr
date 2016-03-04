package com.example.asvetlichniy.philipsflickr.service;

import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;
import com.octo.android.robospice.request.SpiceRequest;

public class GetPhotosTask extends SpiceRequest<PhotoList> {
    public static final int PHOTOS_PER_PAGE = 40;

    private PhotosInterface photosInterface;
    private String keyword;
    private int page;

    public GetPhotosTask(PhotosInterface photosInterface, int page, String keyword) {
        super(PhotoList.class);
        this.photosInterface = photosInterface;
        this.page = page;
        this.keyword = keyword;
    }

    @Override
    public PhotoList loadDataFromNetwork() throws Exception {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setText(keyword);
        return photosInterface.search(searchParameters, PHOTOS_PER_PAGE, page);
    }
}