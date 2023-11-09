package com.example.pocketinventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class represents a dialog fragment for adding tags to items in the inventory
 */
public class ItemAddTagsFragment extends DialogFragment {
    private ArrayList<Item> selectItems;
    private ActionMode mode;
    private Context context;

    /**
     * Constructor for the Add Tags dialog fragment
     * @param selectItems  The list of items for which tags need to be added
     * @param mode The mode running in the background of this fragment
     * @param context The context (HomePageActivity)
     */
    public ItemAddTagsFragment(ArrayList<Item> selectItems, ActionMode mode, Context context) {
        this.selectItems = selectItems;
        this.mode = mode;
        this.context = context;
    }


    TextView addTagDescriptionTextView;
    TextInputLayout tagSelectedInput;

    /**
     * This method is called to create the dialog fragment, and returns the dialog after adding tags to selected items
     * @param   savedInstanceState  Saved instance state
     * @return  Dialog fragment
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the add_item_tags_fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_item_tags_fragment, null);

        // Initialize layouts of the add_item_tags_fragment that needs to be modified
        addTagDescriptionTextView = view.findViewById(R.id.addTagDescriptionTextView);
        tagSelectedInput = view.findViewById(R.id.tagSelectedInput);

        // Get the number of selected items
        int numberSelectedItems = selectItems.size();

        // Use the number of selected items to the fragment's description TextView
        addTagDescriptionTextView.setText("Select tags to add to the "+String.valueOf(numberSelectedItems)+" selected items");

        // Create a new builder for the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Return the created dialog
        return builder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // When cancelled is clicked : Action Mode is finished, exiting the selection action bar
                        mode.finish();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> tags;

                        // Get the tags input (String) from the EditText
                        String tagsString = tagSelectedInput.getEditText().getText().toString().trim();

                        // Create a new ArrayList to store tags.
                        tags = new ArrayList<>();

                        // Check if the String derived from the EditText is not empty.
                        if (!tagsString.isEmpty()) {

                            // Split the String derived from the EditText into an array using commas as separators.
                            String[] tagArray = tagsString.split(",");

                            // Iterate through the elements in the 'tagArray'.
                            for (String tag : tagArray) {
                                // Check if the tag is not empty
                                if (!tag.isEmpty()) {
                                    // Add the non-empty tag to the 'tags' ArrayList
                                    tags.add(tag);
                                }
                            }
                        }

                        // For each item in selected items
                        for (Item item : selectItems) {

                            // For each tag in the Array of tags
                            for (String t : tags){

                                // Add each tag to the item's tags (ArrayList<String>)
                                item.addTags(t);
                            }

                            // log the id
                            Log.d("TagAddActivity", "onClick: " + item.getId());
                            ItemDB.getInstance().updateItem(item);
                        }

                        // Update Item Data in HomePageActivity as well
                        ((HomePageActivity)context).updateItemData();

                        // When confirm is clicked : Action Mode is finished, exiting the selection action
                        // bar with the appropriate tags of the items updated
                        mode.finish();
                    }
                })
                .create();

    }
}
