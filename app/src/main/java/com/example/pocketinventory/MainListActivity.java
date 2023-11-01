package com.example.pocketinventory;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class  MainListActivity extends AppCompatActivity{
        private double total;
        private ArrayAdapter<expense> expense_adapter;

        private ArrayList<expense> dataList;
        private ListView log_list;
        private TextView subtotal;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_list);
            expense expense_1 = new expense("2023-09","apple","iphone","New 15 pro max", 1836,"Fav");
            log_list = (ListView) findViewById(R.id.log_list);
            subtotal = (TextView) findViewById(R.id.Subtotal);
            dataList = new ArrayList<>();
            expense_adapter = new ArrayAdapter<>(this, R.layout.expense, R.id.expenseTextView, dataList);
            log_list.setAdapter(expense_adapter);
            total = total + expense_1.getValue();
            dataList.add(expense_1);

            subtotal.setText(String.format("$ %.2f", total));
            expense_adapter.notifyDataSetChanged();

            expense expense_2 = new expense("2020-08","Asus","A15","RTX 2060", 4000,"NEw");
            total = total + expense_2.getValue();
            dataList.add(expense_2);

            subtotal.setText(String.format("$ %.2f", total));
            expense_adapter.notifyDataSetChanged();





        }

    }

