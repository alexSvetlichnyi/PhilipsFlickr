package com.example.asvetlichniy.philipsflickr.utils;

import android.support.annotation.NonNull;

import com.googlecode.flickrjandroid.photos.Photo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {
	public static final String EXTRA_PHOTO_ID = "photo_id";
	public static final String EXTRA_PHOTO_TITLE = "photo_title";
	public static final String EXTRA_PHOTO_SECRET = "secret";
	public static final String YYYY_MMM_DD_T_HH_MM_A = "yyyy-MM-dd HH:mm";

	public final class PhotoSizes {

		public static final String THUMBNAIL = "t";
		public static final String SMALL = "h";
		public static final String MEDIUM = "m";
		public static final String LARGE = "z";
		public static final String EXTRA_LARGE = "k";

		private PhotoSizes() { }
	}

	private Utils() {
	}

    public static final String createPhotoUrl(@NonNull Photo photo) {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", photo.getFarm(),
                photo.getServer(), photo.getId(), photo.getSecret());
    }

	public static final String createPhotoUrl(@NonNull Photo photo, String size) {
		return String.format("https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg", photo.getFarm(),
				photo.getServer(), photo.getId(), photo.getSecret(), size);
	}

	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		return date != null ? sdf.format(date) : "";
	}
}
