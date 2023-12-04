package com.example.pocketinventory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * This class is the main activity of the app. It displays a list of items and allows the user to
 * add new items to the list.
 */
public class HomePageActivity extends AppCompatActivity {
    private ItemAdapter adapter;
    private ArrayList<Item> dataList;
    private ArrayList<Item> dataListCopy; // Copy of the original list to restore filtering.
    private RecyclerView log_list;
    private ItemFilterFragment itemFilterFragment;
    private boolean filtered = false;
    private ItemFilterFragment.FilterContext fc;
    private ItemDB itemDB = ItemDB.getInstance();

    /**
     * This method is called when the activity is created. It sets up the recycler view and
     * initializes the list of items.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Update the list of items
        updateItemData();



        // Initialize the recycler view
        log_list = (RecyclerView) findViewById(R.id.log_list);
        dataList = new ArrayList<>();
        log_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(this, dataList);
        log_list.setAdapter(adapter);
        adapter.update();

        FloatingActionButton addItemButton = findViewById(R.id.add_item);
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ItemAddActivity.class);
            startActivity(intent);
        });

        // Sort button
        final ImageButton sorterButton = findViewById(R.id.sorterButton);
        sorterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ItemSorterFragment(dataList, adapter).show(getSupportFragmentManager(),"SORT_ITEM");
            }
        });

        // Filter button
        final ImageButton filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> {
            if (!filtered) {
                dataListCopy = new ArrayList<Item>(dataList);
                itemFilterFragment = new ItemFilterFragment(this);
                itemFilterFragment.show(getSupportFragmentManager(), "ADD_EXPENSE");

            } else {
                //restore the original list
                dataList = new ArrayList<Item>(dataListCopy);
                adapter = new ItemAdapter(this, dataList);
                log_list.setAdapter(adapter);
                adapter.update();
                dataListCopy = null;
                filterButton.setColorFilter(null);
                filtered = false;
            }

        });

        // For the navigation panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // This controls the navigation between the different activities
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(HomePageActivity.this, UserProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    /**
     * This method is called when the activity is resumed. It updates the list of items.
     */
    @Override
    protected void onResume() {
        super.onResume();
        ((BottomNavigationView)findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_home);
        updateItemData();
    }

    /**
     * Filter after pressing OK. It's called by the AfterDate button's onClickListener
     */
    public void onClick1 (View view) {
        Button button = (Button) view;
        if (button.getText().toString().compareTo("(From)") != 0) {
            button.setText("(From)");
        } else {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                itemFilterFragment.setDate(myCalendar, "after");
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(HomePageActivity.this,
                    date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }

    /**
     * Filter after pressing OK. It's called by the Before Datebutton's onClickListener
     */
    public void onClick2 (View view) {
        Button button = (Button) view;
        if (button.getText().toString().compareTo("(To)") != 0) {
            button.setText("(To)");
        } else {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                itemFilterFragment.setDate(myCalendar, "before");
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(HomePageActivity.this,
                    date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }
    /**
     * Update some fields after filtered
     * @param adapter: the new adapter after filtered
     * @param dataList: the new list after filtered
     */
    public void onFiltered(ItemAdapter adapter, ArrayList<Item> dataList, ItemFilterFragment.FilterContext fc) {
        this.filtered = true;
        this.adapter = adapter;
        this.dataList = dataList;
        this.fc = fc;
    }
    /**
     * Reapply filter based on previous filter settings. Normally, filter setting is deleted. So,
     * this method is used to reapply filter after filter is lost after sync data with database
     */
    public void reapplyFilter() {
        if (filtered) {
            dataListCopy = new ArrayList<Item>(dataList);
            ItemFilterFragment.filter(fc);
        }
    }

    /**
     * Update the list of items from database
     */
    public void updateItemData() {
        itemDB.getAllItems(task -> {
            if (task.isSuccessful()) {
                List<Item> items = task.getResult().toObjects(Item.class);
                //:( so sad almost every feature needs to take filter into account
                //Because filtered view used a seperate list and adapter.

                dataList.clear();
                dataList.addAll(items);
                if (filtered) {
                    //filter again after update, based on previous filter settings
                    dataListCopy = new ArrayList<Item>(dataList);
                    ItemFilterFragment.filter(fc);
                } else {
                    adapter.update();
                }


            } else {
                Log.d("HomePageActivity", "Error getting documents: ", task.getException());
            }
        });
    }

}