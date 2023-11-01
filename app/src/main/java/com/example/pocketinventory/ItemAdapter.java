package com.example.pocketinventory;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> data;
    private Context context;

    public ItemAdapter(Context context, List<Item> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView makeTextView;
        private TextView modelTextView;
        private TextView descriptionTextView;
        private TextView valueTextView;
        private TextView commentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            makeTextView = itemView.findViewById(R.id.makeTextView);
            modelTextView = itemView.findViewById(R.id.modelTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }

        public void bind(Item item) {
            dateTextView.setText("Date: " + item.getDate());
            makeTextView.setText("Make: " + item.getMake());
            modelTextView.setText("Model: " + item.getModel());
            descriptionTextView.setText("Description: " + item.getDescription());
            valueTextView.setText("Value: $" + item.getValue());
            commentTextView.setText("Comment: " + item.getComment());
        }
    }
}

