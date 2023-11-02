package com.example.pocketinventory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.DateFormat;
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

        Item item_2 = new Item(new Date(new Long("1664590319000")),"Asus","A15","RTX 2060", 4000,"NEw", "xxxxxx");
        dataList.add(item_2);
        adapter.update();

        Item item_3 = new Item(new Date(),"Samsung","Neo QLED 4k","TV", 4400,"Expensive", "xxxxx");
        dataList.add(item_3);
        adapter.update();

        Item item_4 = new Item(new Date(),"Nokia","X30","Cellphone", 1500,"Durable", "xxxxx");
        dataList.add(item_4);
        adapter.update();



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
            if (filterButton.getColorFilter() == null) {
                dataListCopy = new ArrayList<Item>(dataList);
                itemFilterFragment = new ItemFilterFragment();
                itemFilterFragment.show(getSupportFragmentManager(), "ADD_EXPENSE");

            } else {
                //restore the original list
                dataList = new ArrayList<Item>(dataListCopy);
                adapter = new ItemAdapter(this, dataList);
                log_list.setAdapter(adapter);
                adapter.update();
                dataListCopy = null;
                filterButton.setColorFilter(null);
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
}