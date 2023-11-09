package com.example.pocketinventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class tagAdapter extends RecyclerView.Adapter<tagAdapter.tagViewHolder> {

    private ArrayList<String> tags;
    private Context context;

    /*
     * Constructor for the tagAdapter class.
     * @param context The context of the tagAdapter.
     * @param tags The list of tags to be displayed.
     */
    public tagAdapter(Context context, ArrayList<String> tags) {
        this.tags = tags;
        this.context = context;
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


    /*
     * This class is the view holder for the tagAdapter class.
     */
    public class tagViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        /*
         * Constructor for the tagViewHolder class.
         * @param itemView The view that the tagViewHolder will be constructed from.
         */
        public tagViewHolder(View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.tagChip);
        }
    }
}
