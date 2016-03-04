package com.example.asvetlichniy.philipsflickr.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asvetlichniy.philipsflickr.R;
import com.example.asvetlichniy.philipsflickr.service.GetPhotosTask;
import com.example.asvetlichniy.philipsflickr.utils.FlickrHelper;
import com.example.asvetlichniy.philipsflickr.utils.Utils;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends AppCompatActivity implements OnPhotoSelectedListener {
    @NonNull
    private SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private PhotosAdapter photosAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int pageNumber = 0;
    private String keyword;
    @NonNull
    private PhotoList photos = new PhotoList();
    private boolean inProgress;

    private SearchView searchView;
    private Toolbar toolbar;
    private LinearLayout emptyMessage;
    private TextView errorView;
    private RelativeLayout progress;
    private RecyclerView photosList;

    private SearchView.OnQueryTextListener onSearchTextListener = new SearchView
            .OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            pageNumber = 0;
            keyword = query;
            photos.clear();
            photosAdapter.notifyDataSetChanged();
            getPhotosFromServer(keyword, pageNumber);
            hideSearchView();
            showPhotosList();
            updateTitle(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //Do nothing for now
            return false;
        }
    };

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!recyclerView.canScrollVertically(1) && photos.getPages() > pageNumber &&
                    !inProgress) {
                pageNumber++;
                getPhotosFromServer(keyword, pageNumber);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPhotosList();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context
                .SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(onSearchTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideSearchView() {
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }

    private void showPhotosList() {
        emptyMessage.setVisibility(View.GONE);
        photosList.setVisibility(View.VISIBLE);
    }

    private void updateTitle(String title) {
        toolbar.setTitle(title);
        toolbar.invalidate();
    }

    private void getPhotosFromServer(String keyword, int page) {
        inProgress = true;
        GetPhotosTask request = new GetPhotosTask(FlickrHelper.getInstance().getPhotosInterface()
                , page, keyword);
        spiceManager.execute(request, new GetPhotosRequestListener());
        progress.setVisibility(View.VISIBLE);
        photosList.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void initPhotosList() {
        photosList.setHasFixedSize(true);
        photosList.setOnScrollListener(scrollListener);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        photosList.setLayoutManager(layoutManager);

        photosAdapter = new PhotosAdapter(getApplicationContext(), photos);
        photosAdapter.setPhotoClickedListener(this);
        photosList.setAdapter(photosAdapter);
    }

    private void initViews() {
        emptyMessage = (LinearLayout) findViewById(R.id.empty_screen_notification);
        photosList = (RecyclerView) findViewById(R.id.photos_recycler_view);
        progress = (RelativeLayout) findViewById(R.id.rellay_progress_display);
        errorView = (TextView) findViewById(R.id.error_text);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onPhotoSelected(Photo photo) {
        Intent intent = new Intent(this, PhotoDetailsActivity.class);
        intent.putExtra(Utils.EXTRA_PHOTO_ID, photo.getId());
        intent.putExtra(Utils.EXTRA_PHOTO_SECRET, photo.getSecret());
        intent.putExtra(Utils.EXTRA_PHOTO_TITLE, photo.getTitle());
        startActivity(intent);
    }

    private class GetPhotosRequestListener implements RequestListener<PhotoList> {
        @Override
        public void onRequestFailure(SpiceException e) {
            inProgress = false;
            if (photos.isEmpty()) {
                errorView.setVisibility(View.VISIBLE);
                photosList.setVisibility(View.INVISIBLE);
            }
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onRequestSuccess(PhotoList pagePhotos) {
            inProgress = false;
            if (pagePhotos.size() == 0) {
                errorView.setVisibility(View.VISIBLE);
                photosList.setVisibility(View.INVISIBLE);
            } else {
                photos.addAll(pagePhotos);
                photos.setPages(pagePhotos.getPages());
                photosAdapter.notifyDataSetChanged();
            }
            progress.setVisibility(View.GONE);
        }
    }
}
