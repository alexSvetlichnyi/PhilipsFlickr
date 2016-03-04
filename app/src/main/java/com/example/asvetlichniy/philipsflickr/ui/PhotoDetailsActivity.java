package com.example.asvetlichniy.philipsflickr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asvetlichniy.philipsflickr.R;
import com.example.asvetlichniy.philipsflickr.service.GetPhotoInfoTask;
import com.example.asvetlichniy.philipsflickr.utils.FlickrHelper;
import com.example.asvetlichniy.philipsflickr.utils.Utils;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.Photo;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;

public class PhotoDetailsActivity extends AppCompatActivity {
    @NonNull
    private SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private ImageView photoImage;
    private TextView photoDescription;
    private TextView posted;
    private TextView lastUpdated;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        initViews();
        Intent intent = getIntent();
        if (intent != null) {
            getPhotoDetails(intent.getStringExtra(Utils.EXTRA_PHOTO_ID), intent.getStringExtra
                    (Utils.EXTRA_PHOTO_SECRET));
            getSupportActionBar().setTitle(intent.getStringExtra(Utils.EXTRA_PHOTO_TITLE));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private void initViews() {
        photoImage = (ImageView) findViewById(R.id.photo_details_image);
        posted = (TextView) findViewById(R.id.date_posted);
        photoDescription = (TextView) findViewById(R.id.photo_description);
        lastUpdated = (TextView) findViewById(R.id.last_updated);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getPhotoDetails(String id, String secret) {
        GetPhotoInfoTask request = new GetPhotoInfoTask(FlickrHelper.getInstance().getPhotosInterface()
                , id, secret);
        spiceManager.execute(request, new GetPhotoInfoListener());
    }

    private class GetPhotoInfoListener implements RequestListener<Photo> {
        @Override
        public void onRequestFailure(SpiceException e) {
            // TODO Show error
        }

        @Override
        public void onRequestSuccess(Photo photo) {
            update(photo);
        }
    }

    private void update(Photo photo) {
        updateImage(photo);
        updatePostedDate(photo);
        updateLastUpdated(photo);
    }

    private void updateLastUpdated(Photo photo) {
        if (photo.getLastUpdate() != null) {
            String datePosted = Utils.format(photo.getDatePosted(), Utils.YYYY_MMM_DD_T_HH_MM_A);
            lastUpdated.setText(String.format(getString(R.string.photo_last_update), datePosted));
        } else {
            lastUpdated.setVisibility(GONE);
        }
    }

    private void updatePostedDate(Photo photo) {
        if (photo.getDatePosted() != null) {
            String postedText;
            String datePosted = Utils.format(photo.getDatePosted(), Utils.YYYY_MMM_DD_T_HH_MM_A);
            postedText = String.format(getString(R.string.photo_posted), datePosted);
            User owner = photo.getOwner();
            if (owner != null && !TextUtils.isEmpty(owner.getUsername())) {
                postedText = postedText + String.format(getString(R.string.photo_user), owner.getUsername());
            }
            posted.setText(postedText);
        } else {
            posted.setVisibility(GONE);
        }
    }

    private void updateImage(Photo photo) {
        Picasso.with(getBaseContext()).load(Utils.createPhotoUrl(photo, Utils.PhotoSizes.LARGE))
                .placeholder(R.drawable.flickr_placeholder)
                .into(photoImage);
        if (!TextUtils.isEmpty(photo.getDescription())) {
            photoDescription.setText(String.format(getString(R.string.photo_description), photo.getDescription()));
        } else {
            photoDescription.setVisibility(GONE);
        }
    }
}
