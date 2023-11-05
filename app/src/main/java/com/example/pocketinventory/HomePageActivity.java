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
        Item item_1 = new Item(new Date(),"apple","iphone","New 15 pro max", 1836,"Fav", "xxxxx");
        log_list = (RecyclerView) findViewById(R.id.log_list);
        dataList = new ArrayList<>();
        log_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(this, dataList);
        log_list.setAdapter(adapter);
        dataList.add(item_1);
        adapter.update();

        addfromtext(100);//Add some test items

        Button addItemButton = findViewById(R.id.add_item);

        // TEMPORARY CODE: Replace once we have a database
        // ActivityResultLauncher for the ItemAddActivity which returns an item object in the intent
        ActivityResultLauncher<Intent> addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result != null && result.getResultCode() == RESULT_OK) {
                        Log.d("MainListActivity", "Received result from ItemAddActivity");
                        Item item = result.getData().getParcelableExtra("item");
                        dataList.add(item);
                        adapter.update();
                        adapter.notifyDataSetChanged();
                        if (filtered) {
                            dataListCopy.add(item);
                        }
                    }
                }
        );
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ItemAddActivity.class);
            addItemLauncher.launch(intent);
        });

        //Sort button
        final ImageButton sorterButton = findViewById(R.id.sorterButton);
        sorterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ItemSorterFragment(dataList, adapter).show(getSupportFragmentManager(),"SORT_ITEM");
            }
        });

        //Filter button
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



    }

    /**
     * Filter after pressing OK. It's called by the AfterDate button's onClickListener
     */

    public void onClick1 (View view) {
        Button button = (Button) view;
        if (button.getText().toString().compareTo("(Optional)") != 0) {
            button.setText("(Optional)");
        } else {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                itemFilterFragment.setDate(myCalendar, "after");
            };
            new DatePickerDialog(HomePageActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }

    }
    /**
     * Filter after pressing OK. It's called by the Before Datebutton's onClickListener
     */
    public void onClick2 (View view) {
        Button button = (Button) view;
        if (button.getText().toString().compareTo("(Optional)") != 0) {
            button.setText("(Optional)");
        } else {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                itemFilterFragment.setDate(myCalendar, "before");
            };
            new DatePickerDialog(HomePageActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }
    /**
     * Update some fields after filtered
     * @param adapter: the new adapter after filtered
     * @param dataList: the new list after filtered
     */
    public void onFiltered(ItemAdapter adapter, ArrayList<Item> dataList) {
        this.filtered = true;
        this.adapter = adapter;
        this.dataList = dataList;
    }

    /**
     * Add some test items from text file. For test only. File under "app/src/main/assets"
     * @param count: number of items to add
     */
    public void addfromtext(int count) {
        //add Item from text
        BufferedReader reader;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("testItems.txt")));
            String line = reader.readLine();

            while (line != null && count > 0) {
                String[] parts = line.split(", ");
                Item item = new Item(formatter.parse(parts[0]), parts[1], parts[2], parts[3], Double.parseDouble(parts[4]), parts[5], parts[6]);
                dataList.add(item);
                line = reader.readLine();
                count--;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            adapter.update();
        }
    }

}
