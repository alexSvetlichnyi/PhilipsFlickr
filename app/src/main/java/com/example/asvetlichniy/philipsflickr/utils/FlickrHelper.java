package com.example.asvetlichniy.philipsflickr.utils;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.photos.PhotosInterface;

import javax.xml.parsers.ParserConfigurationException;

public final class FlickrHelper {
	private static FlickrHelper instance = null;
	private static final String API_KEY = "7588f05414020ec877df51afe62244d4";
	private static final String API_SEC = "72b288b003b1ebef";

	private FlickrHelper() {
	}

	public static FlickrHelper getInstance() {
		if (instance == null) {
			instance = new FlickrHelper();
		}
		return instance;
	}

	public Flickr getFlickr() {
		try {
			Flickr f = new Flickr(API_KEY, API_SEC, new REST());
			return f;
		} catch (ParserConfigurationException e) {
			return null;
		}
	}

	public PhotosInterface getPhotosInterface() {
		Flickr f = getFlickr();
		if (f != null) {
			return f.getPhotosInterface();
		} else {
			return null;
		}
	}
}
