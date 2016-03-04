package com.example.asvetlichniy.philipsflickr.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asvetlichniy.philipsflickr.R;
import com.example.asvetlichniy.philipsflickr.utils.Utils;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.squareup.picasso.Picasso;

/**
 * Photos list recycle view adapter
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
    private PhotoList photos;
    private Context context;
    private OnPhotoSelectedListener listener;

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        CardView photoCardView;
        ImageView photoImage;
        TextView title;

        PhotoViewHolder(View itemView) {
            super(itemView);
            photoCardView = (CardView)itemView.findViewById(R.id.photo_card_view);
            photoImage = (ImageView)itemView.findViewById(R.id.photo_image);
            title = (TextView) itemView.findViewById(R.id.photo_title);
        }
    }

    public PhotosAdapter(Context context, PhotoList photos) {
        this.photos = photos;
        this.context = context;
    }

    public void setPhotoClickedListener(OnPhotoSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public PhotosAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_view, parent, false);
        PhotoViewHolder vh = new PhotoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder,final int position) {
        final Photo photo = photos.get(position);
        Picasso.with(context).load(Utils.createPhotoUrl(photos.get(position)))
                .placeholder(R.drawable.flickr_placeholder)
                .into(holder.photoImage);
        holder.title.setText(photo.getTitle());
        holder.photoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPhotoSelected(photo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
