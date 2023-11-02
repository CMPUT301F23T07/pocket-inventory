package com.example.pocketinventory;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the adapter for the recycler view. It takes in a list of items and displays them
 * in the recycler view.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> data;
    private Context context;

    /**
     * Constructor for the adapter
     * @param context
     * @param data
     */
    public ItemAdapter(Context context, List<Item> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * This method inflates the view and returns a ViewHolder object
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return ViewHolder object
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_fragment, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method binds the data to the view holder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = data.get(position);
        holder.bind(item);
    }

    /**
     * This method returns the number of items in the data set held by the adapter.
     * @return number of items in the data set
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
    /**
     * This method returns the list of items
     * @return list of items
     */
    public List<Item> getList() {
        return data;
    }

    /**
     * This method updates the total value text view, and call notifyDataSetChanged()
     */
    public void update(){
        double total = 0;
        for (Item item : data) {
            total += item.getValue();
        }
        TextView totalValueText = ((HomePageActivity)context).findViewById(R.id.total_value_text);
        totalValueText.setText(String.format("$ %.2f", total));
        notifyDataSetChanged();
    }

    /**
     * This class is the view holder for the recycler view. It holds the views that will be
     * displayed in the recycler view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView makeTextView;
        private TextView modelTextView;
        private TextView descriptionTextView;
        private TextView valueTextView;
        private TextView commentTextView;

        /**
         * Constructor for the view holder
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            makeTextView = itemView.findViewById(R.id.makeTextView);
            modelTextView = itemView.findViewById(R.id.modelTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }

        /**
         * This method binds the data to the view holder
         * @param item
         */
        public void bind(Item item) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateTextView.setText("Date: " + dateFormat.format(item.getDate()));
            makeTextView.setText("Make: " + item.getMake());
            modelTextView.setText("Model: " + item.getModel());
            descriptionTextView.setText("Description: " + item.getDescription());
            valueTextView.setText("Value: $" + item.getValue());
            commentTextView.setText("Comment: " + item.getComment());
        }


    }
}

