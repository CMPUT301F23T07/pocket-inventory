package com.example.pocketinventory;


import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the adapter for the recycler view. It takes in a list of items and displays them
 * in the recycler view.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> data;
    private Context context;
    private SelectionViewModel selectionViewModel;
    private ConstraintLayout constraintSelectionLayout;
    private boolean isEnable = false;
    private boolean isSelectAll = false;
    private ArrayList<Item> selectItems = new ArrayList<>();

    /**
     * Constructor for the adapter
     * @param context
     * @param data
     * @param constraintSelectionLayout
     */
    public ItemAdapter(Context context, List<Item> data, ConstraintLayout constraintSelectionLayout) {
        this.constraintSelectionLayout = constraintSelectionLayout;
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
        ViewModelProvider viewModelProvider = new ViewModelProvider((FragmentActivity) context);
        selectionViewModel = viewModelProvider.get(SelectionViewModel.class);
        return new ViewHolder(view);
    }

    /**
     * This method binds the data to the view holder
     *
     * This method is called by the RecyclerView to display data at the specified position.
     * It binds the data item to the provided ViewHolder, sets up long click and click listeners,
     * and manages the selection state for items when the user interacts with the view.
     *
     * Citation: https://youtu.be/Uld0N4ofgWQ?si=5ZYiswWMMzLF1FcL
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Obtain the data item for the specified position
        Item item = data.get(position);
        // Bind the data item to the ViewHolder, updating the view
        holder.bind(item);
        // Set up a long click listener to initiate ActionMode for item selection
        // Reference: https://youtu.be/Uld0N4ofgWQ?si=5ZYiswWMMzLF1FcL
        // Used the above resource's code structure to compelete the onLongClick functionality below:
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // ActionMode is not enabled yet because we haven't created one
                if (!isEnable){
                    // Create an ActionMode for item selection
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        // Implement ActionMode callback methods
                        @Override
                        // onCreateActionMode: Setup ActionMode UI and behavior
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            // Make the selection options visible
                            constraintSelectionLayout.setVisibility(View.VISIBLE);

                            //Initiate the buttons and Textviews presented within the selection layout
                            FloatingActionButton removeSelectionButton = constraintSelectionLayout.findViewById(R.id.removeSelectionButton);
                            TextView selectedItemNumberTextView = constraintSelectionLayout.findViewById(R.id.selectedItemNumberTextView);
                            FloatingActionButton tagSelectedButton = constraintSelectionLayout.findViewById(R.id.tagSelectedButton);
                            FloatingActionButton deleteSelectedButton = constraintSelectionLayout.findViewById(R.id.deleteSelectedButton);
                            FloatingActionButton selectAllButton = constraintSelectionLayout.findViewById(R.id.selectAllButton);
                            /*
                            mode.setCustomView(constraintSelectionLayout);
                            */

                            // Set a clickListerner for removeSelectionButton
                            removeSelectionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mode.finish(); // End ActionMode
                                }
                            });
                            // Set a clickListener for deleteSelectedButton
                            deleteSelectedButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Remove the selected item (stored in selectItems) from data
                                    for (Item item : selectItems){
                                        data.remove(item);
                                    }
                                    mode.finish(); // End ActionMode
                                }
                            });
                            // Set a clickListener for selectAllButton
                            selectAllButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // If the selectItems already has all of the items
                                    if (selectItems.size() == data.size()){
                                        isSelectAll = false;
                                        // Clear all the items (stored in selectItems)
                                        selectItems.clear();
                                    }
                                    else{ // If the selectItems has less items in it compared to data
                                        // SelectAll has a function to do here
                                        isSelectAll = true;
                                        // Update the selectItems with having all of the items from data while avoiding duplication
                                        selectItems.clear();
                                        selectItems.addAll(data);
                                    }
                                    // Update the number of selected items
                                    selectionViewModel.setText(String.valueOf(selectItems.size()));
                                    // Notify the context
                                    notifyDataSetChanged();
                                }
                            });

                            // Need to implement the add_tag_icon in the near future
                            /*
                            tagSelectedButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                             */
                            return true;
                        }



                        // onPrepareActionMode: Handle ActionMode preparation
                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // To prepare Action Mode:
                            // ActionMode is Enabled
                            isEnable = true;
                            // Make the checkbox appear for the holder
                            ClickItem(holder);

                            // Initiate the Textview where number of selected items is displayed
                            TextView selectedItemNumberTextView = constraintSelectionLayout.findViewById(R.id.selectedItemNumberTextView);

                            // Observe changes in the 'selectionViewModel.getText()' LiveData.
                            // When the data in the LiveData changes, the 'onChanged' callback is invoked.
                            selectionViewModel.getText().observe((LifecycleOwner) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    // Update the 'selectedItemNumberTextView' with the count of selected items.
                                    // The count is represented by the 's' parameter, and it's displayed as "X Selected".
                                    selectedItemNumberTextView.setText(String.format("%s Selected", s));
                                }
                            });
                            return true;
                        }
                        // onActionItemClicked: Handle ActionMode item clicks
                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            // Menu not used so nothing to put here
                            return false;
                        }

                        // onDestroyActionMode: Handle ActionMode cleanup
                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            isEnable = false; //ActionMode is not enabled
                            isSelectAll = false; //SelectAll items is false as well when go out of the ActionMode
                            selectItems.clear(); //Clear the list of selected items for fresh start upon next long-press on item
                            constraintSelectionLayout.setVisibility(View.GONE); //Make the selection layout disappear
                            notifyDataSetChanged();
                        }
                    };

                    // Start ActionMode using the callback
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);

                }

                else {
                    // Handle item selection when ActionMode is already enabled
                    ClickItem(holder);
                }
                return true;
            }
        });
        // Set up a click listener to toggle item selection when not in ActionMode
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable){
                    // Handle item selection when ActionMode is enabled
                    ClickItem(holder);
                }
            }
        });

        // Update the checkmark icon visibility based on selection state: isSelectAll or Not isSelectAll
        if (isSelectAll){
            holder.checkedBoxImageView.setVisibility(View.VISIBLE);
        }
        else{
            holder.checkedBoxImageView.setVisibility(View.GONE);
        }
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
     * This method handles the click event for an item in the RecyclerView.
     *
     * This method toggles the selection state of the item represented by the given ViewHolder.
     * When the item is selected, it displays a checkmark icon, and when deselected, it hides the icon.
     * The method also updates a count of selected items and communicates the count to a ViewModel.
     *
     * Citation: https://youtu.be/Uld0N4ofgWQ?si=5ZYiswWMMzLF1FcL
     *
     * @param holder The ViewHolder associated with the clicked item.
     */
    private void ClickItem(ViewHolder holder) {
        // Get the selected item from the data list based on the adapter position
        Item itemSelected = data.get(holder.getAdapterPosition());

        // Toggle the visibility of the checkmark icon based on the current state
        if (holder.checkedBoxImageView.getVisibility() == View.GONE) {
            holder.checkedBoxImageView.setVisibility(View.VISIBLE);
            selectItems.add(itemSelected);
        } else {
            holder.checkedBoxImageView.setVisibility(View.GONE);
            selectItems.remove(itemSelected);
        }

        // Update the count of selected items and communicate it to a ViewModel
        selectionViewModel.setText(String.valueOf(selectItems.size()));
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
        private ImageView checkedBoxImageView;

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
            checkedBoxImageView = itemView.findViewById(R.id.checkImageView);
        }

        /**
         * This method binds the data to the view holder
         * @param item
         */
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

