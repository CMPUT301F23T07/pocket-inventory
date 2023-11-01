package com.example.pocketinventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class ItemAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        Button cancelButton = findViewById(R.id.cancel_button);
        Button addButton = findViewById(R.id.add_button);

        TextInputLayout makeInput = findViewById(R.id.make_text);
        TextInputLayout modelInput = findViewById(R.id.model_text);
        TextInputLayout serialInput = findViewById(R.id.serial_number_text);
        TextInputLayout estimatedValueInput = findViewById(R.id.estimated_value_text);
        TextInputLayout dateOfPurchaseInput = findViewById(R.id.date_of_purchase_text);
        TextInputLayout descriptionInput = findViewById(R.id.description_text);
        TextInputLayout commentInput = findViewById(R.id.comment_text);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Read in all of the text fields (some may be empty) and create an expense object
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String make = makeInput.getEditText().getText().toString();
                String model = modelInput.getEditText().getText().toString();
                String serialNumber = serialInput.getEditText().getText().toString();
                String estimatedValue = estimatedValueInput.getEditText().getText().toString();
                String dateOfPurchase = dateOfPurchaseInput.getEditText().getText().toString();
                String description = descriptionInput.getEditText().getText().toString();
                String comment = commentInput.getEditText().getText().toString();

                Log.d("ItemAddActivity", "onClick: " + make + " " + model + " " + serialNumber + " " + estimatedValue + " " + dateOfPurchase + " " + description + " " + comment);

                Item item = new Item(dateOfPurchase, make, model, description, Double.parseDouble(estimatedValue), comment, serialNumber);
                Intent intent = new Intent();
                intent.putExtra("expense", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}