package com.example.pocketinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFilterFragment extends DialogFragment {
    private ItemAdapter itemAdapter;
    private ArrayList<Item> list;
    private View view;
    private String current = "";
    RecyclerView recyclerView;
    HomePageActivity homePageActivity;
    private ArrayList<String> tags = new ArrayList<String>();
    private TagAdapter tagAdapter;

    public ItemFilterFragment(HomePageActivity homePageActivity) {
        this.homePageActivity = homePageActivity;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_layout, null);
        getData();

        //Set up tag autocomplete
        ArrayList<String> tagsUsed = new ArrayList<String>();
        for (Item item : list) {
            for (String tag : item.getTags()) {
                if (!tagsUsed.contains(tag)) {
                    tagsUsed.add(tag);
                }
            }
        }
        //Set up button listener
        Button buttonAdd = (Button) view.findViewById(R.id.buttonAddTag);
        ItemFilterFragment self = this;
        buttonAdd.setOnClickListener(v -> {
            ItemFilterFragment_TagFragment fragment = new ItemFilterFragment_TagFragment(self, tagsUsed);
            fragment.show(getFragmentManager(), "AddTag");
        });

        //Set up tag list
        RecyclerView tagView = (RecyclerView) view.findViewById(R.id.filterTagView);
        tagView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagAdapter = new TagAdapter(getContext(), tags, 1);
        tagView.setAdapter(tagAdapter);

        //Set up multiselect button
        MaterialButtonToggleGroup toggleGroup = (MaterialButtonToggleGroup) view.findViewById(R.id.filterButtonToggleGroup);
        view.findViewById(R.id.buttonAND).setBackgroundColor(getResources().getColor(R.color.md_theme_light_inversePrimary));
        ((Button)view.findViewById(R.id.buttonAND)).setTextColor(getResources().getColor(R.color.black));
        view.findViewById(R.id.buttonOR).setBackgroundColor(getResources().getColor(R.color.md_theme_dark_surfaceVariant));
        ((Button)view.findViewById(R.id.buttonOR)).setTextColor(getResources().getColor(R.color.white));
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                //Change color
                if (checkedId == R.id.buttonAND) {
                    view.findViewById(R.id.buttonAND).setBackgroundColor(getResources().getColor(R.color.md_theme_light_inversePrimary));
                    ((Button)view.findViewById(R.id.buttonAND)).setTextColor(getResources().getColor(R.color.black));
                    view.findViewById(R.id.buttonOR).setBackgroundColor(getResources().getColor(R.color.md_theme_dark_surfaceVariant));
                    ((Button)view.findViewById(R.id.buttonOR)).setTextColor(getResources().getColor(R.color.white));
                } else {
                    view.findViewById(R.id.buttonOR).setBackgroundColor(getResources().getColor(R.color.md_theme_light_inversePrimary));
                    ((Button)view.findViewById(R.id.buttonOR)).setTextColor(getResources().getColor(R.color.black));
                    view.findViewById(R.id.buttonAND).setBackgroundColor(getResources().getColor(R.color.md_theme_dark_surfaceVariant));
                    ((Button)view.findViewById(R.id.buttonAND)).setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = builder
                .setView(view)
                .setTitle("Advance Filters")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onOkPressed();
                    }
                });
        return builder.create();
    }

    /**
     * Get list and listadpater from the main activity
     */
    private void getData() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.log_list);
        itemAdapter = (ItemAdapter) recyclerView.getAdapter();
        list = (ArrayList<Item>) itemAdapter.getList();
    }

    /**
     * Filter after pressing OK. It's called by the button's onClickListener
     */
    public void onOkPressed() {
        boolean changed = false;
        //Get all the inputs
        String after = ((Button) view.findViewById(R.id.buttonAfter)).getText().toString();
        String before = ((Button) view.findViewById(R.id.buttonBefore)).getText().toString();
        Editable description = ((TextInputEditText) view.findViewById(R.id.descriptionInput)).getText();
        Editable make = ((TextInputEditText) view.findViewById(R.id.makeInput)).getText();
        //Editable tag = ((TextInputEditText) view.findViewById(R.id.tagInput)).getText(); //WIP
        //make a copy of the list
        ArrayList<Item> filteredList = new ArrayList<Item>(list);

        if (!after.isEmpty() && after.compareTo("(Optional)") != 0) {
            Date afterDate = parseDate(after);
            filteredList.removeIf(expense -> expense.getDate().compareTo(afterDate) < 0);
            changed = true;
        }
        if (!before.isEmpty() && before.compareTo("(Optional)") != 0) {
            Date beforeDate = parseDate(before);
            filteredList.removeIf(expense -> expense.getDate().compareTo(beforeDate) > 0);
            changed = true;
        }
        if (description != null && !description.toString().isEmpty()) {
            filteredList.removeIf(expense -> !expense.getDescription().toLowerCase().contains(description.toString().toLowerCase()));
            changed = true;
        }
        if (make != null && !make.toString().isEmpty()) {
            filteredList.removeIf(expense -> expense.getMake().toLowerCase().compareTo(make.toString().toLowerCase()) != 0);
            changed = true;
        }
        if (tags.size() > 0) {
            List<String> tagsLower = tags.stream().map(String::toLowerCase).collect(Collectors.toList());
            if (((MaterialButtonToggleGroup)view.findViewById(R.id.filterButtonToggleGroup)).getCheckedButtonId() == R.id.buttonAND) {
                //Remove if item does not contain all tags
                filteredList.removeIf(expense -> expense.getTags().size() == 0 ||
                        !tagsLower.stream().allMatch(tag -> expense.getTags().stream()
                                .map(String::toLowerCase).collect(Collectors.toList()).contains(tag)));
            } else {
                //Remove if item does not contain any of the tags
                filteredList.removeIf(expense -> expense.getTags().size() == 0 ||
                        !tagsLower.stream().anyMatch(tag -> expense.getTags().stream()
                                .map(String::toLowerCase).collect(Collectors.toList()).contains(tag)));
            }
            changed = true;
        }
        if (changed) {
            final ImageButton filterButton = getActivity().findViewById(R.id.filterButton);
            filterButton.setColorFilter(R.color.md_theme_dark_onError);
            itemAdapter = new ItemAdapter(getContext(), filteredList);
            recyclerView.setAdapter(itemAdapter);
            itemAdapter.update();
            homePageActivity.onFiltered(itemAdapter, filteredList);
        }
    }

    /**
     * Parse a string to a date object
     * @param date
     * @return Date Object
     */
    public static Date parseDate(String date) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set the date of the button
     * @param calendar
     * @param which
     */
    public void setDate(Calendar calendar, String which) {
        String date = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
        if (which == "after") {
            ((TextView)view.findViewById(R.id.buttonAfter)).setText(date);
        } else {
            ((TextView)view.findViewById(R.id.buttonBefore)).setText(date);
        }
    }

    /**
     * Add a tag to the list
     * @param tag
     */
    public void addTag(String tag) {
        tags.add(tag.trim());
        tagAdapter.notifyDataSetChanged();
    }

}
