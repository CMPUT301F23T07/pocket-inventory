package com.example.pocketinventory.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.DialogFragment;

import com.example.pocketinventory.R;

import java.util.ArrayList;
/**
 * A dialog fragment allowing the filtering by tags within an ItemFilterFragment context.
 */
public class ItemFilterFragment_TagFragment extends DialogFragment {
    private View view;
    private ItemFilterFragment parent;
    String[] tags;
    private AutoCompleteTextView autoCompleteTextView;

    /**
     * Constructs a TagFragment associated with an ItemFilterFragment and a list of tags.
     *
     * @param parent The ItemFilterFragment parent to associate with this TagFragment.
     * @param tags   The list of tags to be displayed and used within the fragment.
     */
    public ItemFilterFragment_TagFragment(ItemFilterFragment parent, ArrayList<String> tags) {
        this.parent = parent;
        this.tags = tags.toArray(new String[0]);
    }

    /**
     * Called when the fragment is attached to its context.
     *
     * @param context The context to which the fragment is attached.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Create a new Dialog instance to be displayed by the fragment.
     *
     * @param savedInstanceState The last saved state of the fragment, or null if there is no saved state.
     * @return The Dialog to be displayed.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_add_tag_fragment, null);
        autoCompleteTextView = view.findViewById(R.id.filterTagAutoComplete);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, tags));


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = builder
                .setView(view)
                .setTitle("Add One Tag")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> onOkPressed());
        return builder.create();
    }
    /**
     * Handle the action upon pressing the "OK" button in the dialog.
     * It adds the entered tag to the parent ItemFilterFragment.
     */
    public void onOkPressed() {
        parent.addTag(autoCompleteTextView.getText().toString());
    }
}
