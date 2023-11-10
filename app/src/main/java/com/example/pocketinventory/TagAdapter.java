package com.example.pocketinventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

/**
 * An adapter for managing tags in a RecyclerView.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.tagViewHolder> {

    private ArrayList<String> tags;
    private Context context;
    private int mode;

    /**
     * Constructor for the TagAdapter class.
     *
     * @param context The context of the TagAdapter.
     * @param tags    The list of tags to be displayed.
     * @param mode    The mode of the TagAdapter. Use 0 when used in homepage, 1 in filter.
     */
    public TagAdapter(Context context, ArrayList<String> tags, int mode) {
        this.tags = tags;
        this.context = context;
        this.mode = mode;
    }

    @NonNull
    @Override
    public tagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list_fragment, parent, false);
        return new tagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tagViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.chip.setText(tag); // Set the text of the chip.
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }


    /**
     * This class is the view holder for the TagAdapter class.
     */
    public class tagViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        /**
         * Constructor for the tagViewHolder class.
         *
         * @param itemView The view that the tagViewHolder will be constructed from.
         */
        public tagViewHolder(View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.tagChip);
            // Set the onclick listener if used in filter.
            if (mode == 1) {
                chip.setOnClickListener(v -> {
                    tags.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                });
            }
        }
    }
}
