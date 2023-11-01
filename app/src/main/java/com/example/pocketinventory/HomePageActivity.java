package com.example.pocketinventory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    private double total;
    private ArrayAdapter<Item> expense_adapter;

    private ArrayList<Item> dataList;
    private RecyclerView log_list;
    private TextView subtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Item item_1 = new Item("2023-09","apple","iphone","New 15 pro max", 1836,"Fav", "xxxxx");
        log_list = (RecyclerView) findViewById(R.id.log_list);
        subtotal = (TextView) findViewById(R.id.Subtotal);
        dataList = new ArrayList<>();
        log_list.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter adapter = new ItemAdapter(this, dataList);
        log_list.setAdapter(adapter);
        total = total + item_1.getValue();
        dataList.add(item_1);

        subtotal.setText(String.format("$ %.2f", total));
        adapter.notifyDataSetChanged();

        Item item_2 = new Item("2020-08","Asus","A15","RTX 2060", 4000,"NEw", "xxxxxx");
        total = total + item_2.getValue();
        dataList.add(item_2);

        subtotal.setText(String.format("$ %.2f", total));
        adapter.notifyDataSetChanged();

        Button addItemButton = findViewById(R.id.add_expense);

        // TEMPORARY CODE: Replace once we have a database
        // ActivityResultLauncher for the ItemAddActivity which returns an expense object in the intent
        ActivityResultLauncher<Intent> addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result != null) {
                        Log.d("MainListActivity", "Received result from ItemAddActivity");
                        Item item = result.getData().getParcelableExtra("expense");
                        dataList.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }
        );
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ItemAddActivity.class);
            addItemLauncher.launch(intent);
        });

    }

}