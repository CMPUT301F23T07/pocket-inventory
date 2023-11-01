package com.example.pocketinventory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pocketinventory.Expense;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainListActivity extends AppCompatActivity {
    private double total;
    private ArrayAdapter<Expense> expense_adapter;

    private ArrayList<Expense> dataList;
    private RecyclerView log_list;
    private TextView subtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        Expense expense_1 = new Expense("2023-09","apple","iphone","New 15 pro max", 1836,"Fav");
        log_list = (RecyclerView) findViewById(R.id.log_list);
        subtotal = (TextView) findViewById(R.id.Subtotal);
        dataList = new ArrayList<>();
        log_list.setLayoutManager(new LinearLayoutManager(this));
        ExpenseAdapter adapter = new ExpenseAdapter(this, dataList);
        log_list.setAdapter(adapter);
        total = total + expense_1.getValue();
        dataList.add(expense_1);

        subtotal.setText(String.format("$ %.2f", total));
        adapter.notifyDataSetChanged();

        Expense expense_2 = new Expense("2020-08","Asus","A15","RTX 2060", 4000,"NEw");
        total = total + expense_2.getValue();
        dataList.add(expense_2);

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
                        Expense expense = result.getData().getParcelableExtra("expense");
                        dataList.add(expense);
                        adapter.notifyDataSetChanged();
                    }
                }
        );
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainListActivity.this, ItemAddActivity.class);
            addItemLauncher.launch(intent);
        });

    }

}