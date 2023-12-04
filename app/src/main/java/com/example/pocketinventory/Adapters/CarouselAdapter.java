package com.example.pocketinventory.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.pocketinventory.R;

import java.util.List;

/**
 * This class is the adapter for the image carousel. It is responsible for loading the images
 * from the URLs into the carousel.
 */
public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    private List<String> imageUrls; // List of image URLs
    private OnItemDeleteListener deleteListener;

    /**
     * Constructor for the adapter
     * @param imageUrls
     * @param deleteListener
     */
    public CarouselAdapter(List<String> imageUrls, OnItemDeleteListener deleteListener) {
        this.imageUrls = imageUrls;
        this.deleteListener = deleteListener;
    }

    /**
     * This method is called when the adapter is created. It inflates the image_carousel layout
     * and returns a ViewHolder object.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_carousel, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method is called when the adapter is bound to the RecyclerView. It loads the image
     * from the URL into the image view and sets the click listener for the delete button.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Use Glide to load the image from the URL
        Glide.with(holder.imageView.getContext())
                .load(imageUrls.get(position))
                .into(holder.imageView);

        // Handle delete button click
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    deleteListener.onDelete(holder.getAdapterPosition());
                }
            }
        });
    }

    /**
     * This method returns the number of images in the carousel
     * @return
     */
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    /**
     * This class represents a single image in the carousel. It contains a reference to the image
     * view and the delete button.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView deleteButton; // Reference to the delete button

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.carousel_image_view);
            deleteButton = view.findViewById(R.id.delete_image_button); // Assuming delete_icon is the ID of your delete button
        }
    }

    /**
     * Interface for the delete button click listener
     */
    public interface OnItemDeleteListener {
        void onDelete(int position);
    }
}
