package com.example.pocketinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ItemFilterFragment_TagFragment extends DialogFragment {
    private View view;
    private ItemFilterFragment parent;
    String[] tags;
    private AutoCompleteTextView autoCompleteTextView;

    public ItemFilterFragment_TagFragment(ItemFilterFragment parent, ArrayList<String> tags) {
        this.parent = parent;
        this.tags = tags.toArray(new String[0]);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

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

    public void onOkPressed() {
        parent.addTag(autoCompleteTextView.getText().toString());
    }
}
