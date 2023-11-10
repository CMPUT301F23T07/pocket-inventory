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

import androidx.annotation.Nullable;
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

/**
 * A fragment for filtering items within the context of the HomePageActivity.
 */
public class ItemFilterFragment extends DialogFragment {
    private ItemAdapter itemAdapter;
    private ArrayList<Item> list;
    private View view;
    private String current = "";
    RecyclerView recyclerView;
    HomePageActivity homePageActivity;
    private ArrayList<String> tags = new ArrayList<String>();
    private TagAdapter tagAdapter;

    /**
     * Constructs an ItemFilterFragment with a reference to the HomePageActivity.
     *
     * @param homePageActivity
     */
    public ItemFilterFragment(HomePageActivity homePageActivity) {
        this.homePageActivity = homePageActivity;
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
        String logic;
        if (((MaterialButtonToggleGroup)view.findViewById(R.id.filterButtonToggleGroup)).getCheckedButtonId() == R.id.buttonAND) {
            logic = "and";
        } else {
            logic = "or";
        }
        FilterContext fc = new FilterContext(after, before, description.toString(), make.toString(), tags, logic, homePageActivity);
        filter(fc);
    }

    /**
     * Parse a string to a date object
     * @param date
     * @return Date Object
     */
    public static Date parseDate(@Nullable String date) {
        if (date == null || date.isEmpty() || date.compareTo("(Optional)") == 0) {
            return null;
        }
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

    /**
    *filter and update to homepage
    * @param fc: contains all the filter information
     */
    public static boolean filter(FilterContext fc) {
        boolean changed = false;
        RecyclerView recyclerView1 = (RecyclerView) fc.homePageActivity.findViewById(R.id.log_list);
        ItemAdapter itemAdapter1 = (ItemAdapter) recyclerView1.getAdapter();
        ArrayList<Item> list1 = (ArrayList<Item>) itemAdapter1.getList();
        ArrayList<Item> filteredList = new ArrayList<Item>(list1);

        //Switch date range if inversed
        Date afterDate = parseDate(fc.after);
        Date beforeDate = parseDate(fc.before);
        if ( afterDate != null && beforeDate != null && fc.after.compareTo(fc.before) > 0) {
            //Swtich
            Date temp = afterDate;
            afterDate = beforeDate;
            beforeDate = temp;
        }

        if (!fc.after.isEmpty() && fc.after.compareTo("(Optional)") != 0) {
            final Date finalAfterDate = afterDate;
            filteredList.removeIf(expense -> expense.getDate().compareTo(finalAfterDate) < 0);
            changed = true;
        }
        if (!fc.before.isEmpty() && fc.before.compareTo("(Optional)") != 0) {
            final Date finalBeforeDate = beforeDate;
            filteredList.removeIf(expense -> expense.getDate().compareTo(finalBeforeDate) > 0);
            changed = true;
        }
        if (fc.description != null && !fc.description.isEmpty()) {
            filteredList.removeIf(expense -> !expense.getDescription().toLowerCase().contains(fc.description.toLowerCase()));
            changed = true;
        }
        if (fc.make != null && !fc.make.isEmpty()) {
            filteredList.removeIf(expense -> expense.getMake().toLowerCase().compareTo(fc.make.toLowerCase()) != 0);
            changed = true;
        }
        if (fc.tags.size() > 0) {
            List<String> tagsLower = fc.tags.stream().map(String::toLowerCase).collect(Collectors.toList());
            if (fc.logic.toLowerCase().compareTo("and") == 0) {
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
            final ImageButton filterButton = fc.homePageActivity.findViewById(R.id.filterButton);
            filterButton.setColorFilter(R.color.md_theme_dark_onError);
            itemAdapter1 = new ItemAdapter(fc.homePageActivity, filteredList);
            recyclerView1.setAdapter(itemAdapter1);
            itemAdapter1.update();
            fc.homePageActivity.onFiltered(itemAdapter1, filteredList, fc);
        }
        return changed;
    }

    /**
    * A simple class to record filter settings
     */
    public class FilterContext {
        private final String after;
        private final String before;
        private final String description;
        private final String make;
        private final ArrayList<String> tags;
        private final String logic;
        private final HomePageActivity homePageActivity;
        public FilterContext(String after, String before, String description, String make,ArrayList<String> tags,String logic, HomePageActivity homePageActivity) {
            this.after = after;
            this.before = before;
            this.description = description;
            this.make = make;
            this.tags = tags;
            this.logic = logic;
            this.homePageActivity = homePageActivity;
        }
    }

}
